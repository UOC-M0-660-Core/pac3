package edu.uoc.pac3.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.takeFrom
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.utils.EmptyContent
import io.ktor.http.HttpStatusCode
import io.ktor.util.AttributeKey

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 04/08/2020
 */

class OAuthFeature(
    private val getToken: suspend () -> String?,
    private val refreshToken: suspend () -> Unit
) {
    class Config {
        lateinit var getToken: suspend () -> String?
        lateinit var refreshToken: suspend () -> Unit
    }

    companion object Feature : HttpClientFeature<Config, OAuthFeature> {
        override val key: AttributeKey<OAuthFeature> = AttributeKey("OAuth")

        override fun prepare(block: Config.() -> Unit): OAuthFeature {
            val config = Config().apply(block)
            return OAuthFeature(config.getToken, config.refreshToken)
        }

        private val RefreshKey = "Ktor-OAuth-Refresh"

        override fun install(feature: OAuthFeature, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                // Add Refresh Header for handling infinite loop on 401s
                context.headers[RefreshKey] = context.headers.contains("Authorization").toString()

                // Add Authorization Header
                if (feature.getToken() != null) {
                    context.headers["Authorization"] = "Bearer ${feature.getToken()}"
                }

                proceed()
            }
            scope.receivePipeline.intercept(HttpReceivePipeline.After) {
                // Request is unauthorized
                if (subject.status == HttpStatusCode.Unauthorized && context.request.headers[RefreshKey] != true.toString()) {
                    try {
                        // Refresh the Token
                        feature.refreshToken()

                        // Retry the request
                        val call = scope.requestPipeline.execute(
                            HttpRequestBuilder().takeFrom(context.request),
                            EmptyContent
                        ) as HttpClientCall

                        // Proceed with the new request
                        proceedWith(call.response)

                        return@intercept
                    } catch (exception: Exception) {
                        // If refresh fails, proceed as 401
                    }
                }
                // Proceed as normal request
                proceedWith(subject)
            }
        }
    }
}
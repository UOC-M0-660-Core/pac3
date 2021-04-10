package edu.uoc.pac3.data.streams


/**
 * Created by alex on 07/09/2020.
 */

// TODO: Fill with missing parameters
// TODO: Serialize from JSON
// Remember that Kotlin variables must use camelCase.
// Use the appropriate Serialization annotation to convert snake_case from the JSON to camelCase in Kotlin.

data class Stream(
    val userName: String? = null,
    val title: String? = null,
)

data class StreamsResponse(
    val data: List<Stream>? = null,
    val pagination: Pagination? = null,
)

data class Pagination(
    val cursor: String? = null,
)
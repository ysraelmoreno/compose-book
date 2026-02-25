package com.ysraelmorenopkg.composebook.sdui.model

/**
 * Raw payload representing an SDUI component before mapping.
 *
 * Models what arrives from a BFF response: a type string identifying
 * the component kind, a flat parameter map, and optional nested children.
 *
 * This is intentionally dependency-free — it uses [Map] instead of
 * framework-specific JSON types (JsonObject, Gson JsonElement, etc.)
 * so that any serialization library can produce it.
 *
 * @property type Component type identifier (e.g., "button", "richText", "spacer").
 *               Used to resolve which [SduiComponentMapper] handles this payload.
 * @property parameters Flat key-value map of component parameters.
 *                      Values may be String, Number, Boolean, List, or nested Maps.
 * @property children Nested child payloads for composite components.
 * @property id Optional component identifier from the BFF response.
 */
data class SduiPayload(
    val type: String,
    val parameters: Map<String, Any?> = emptyMap(),
    val children: List<SduiPayload> = emptyList(),
    val id: String = "",
) {

    /**
     * Retrieves a parameter value by key, casting to [T].
     *
     * @return The value cast to [T], or null if the key is missing or the cast fails.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> param(key: String): T? = parameters[key] as? T

    /**
     * Retrieves a String parameter, defaulting to empty string.
     */
    fun stringParam(key: String, default: String = ""): String =
        parameters[key]?.toString() ?: default

    /**
     * Retrieves a Boolean parameter, defaulting to false.
     */
    fun boolParam(key: String, default: Boolean = false): Boolean =
        (parameters[key] as? Boolean) ?: default

    /**
     * Creates a copy with an updated parameter.
     */
    fun withParam(key: String, value: Any?): SduiPayload =
        copy(parameters = parameters + (key to value))
}

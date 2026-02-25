package com.ysraelmorenopkg.composebook.sdui.parser

import com.ysraelmorenopkg.composebook.sdui.model.SduiPayload
import org.json.JSONArray
import org.json.JSONObject

/**
 * Parses raw JSON into [SduiPayload] instances.
 *
 * Uses Android's built-in [org.json] package — no external serialization
 * library required. Consumers with their own JSON library can skip this
 * and build [SduiPayload] directly.
 *
 * Expected JSON structure per component:
 * ```json
 * {
 *   "type": "button",
 *   "id": "add-to-cart",
 *   "parameters": { "value": "Click", "enabled": true },
 *   "components": [ ... ]
 * }
 * ```
 */
object SduiPayloadParser {

    /**
     * Parses a single component JSON object into an [SduiPayload].
     */
    fun parseComponent(json: JSONObject): SduiPayload {
        val type = json.getString("type")
        val id = json.optString("id", "")
        val parameters = json.optJSONObject("parameters")?.toMap() ?: emptyMap()
        val children = json.optJSONArray("components")?.let { parseComponentArray(it) } ?: emptyList()

        return SduiPayload(
            type = type,
            parameters = parameters,
            children = children,
            id = id,
        )
    }

    /**
     * Parses a JSON array of components into a list of [SduiPayload].
     */
    fun parseComponentArray(jsonArray: JSONArray): List<SduiPayload> {
        return (0 until jsonArray.length()).map { i ->
            parseComponent(jsonArray.getJSONObject(i))
        }
    }

    /**
     * Parses a section-level JSON that wraps a `"components"` array.
     *
     * ```json
     * {
     *   "section": "product_detail",
     *   "components": [ ... ]
     * }
     * ```
     *
     * @return List of [SduiPayload] from the top-level components array.
     */
    fun parseSection(json: JSONObject): List<SduiPayload> {
        val components = json.optJSONArray("components") ?: return emptyList()
        return parseComponentArray(components)
    }

    /**
     * Parses a raw JSON string into a section's list of [SduiPayload].
     */
    fun parseSection(rawJson: String): List<SduiPayload> {
        return parseSection(JSONObject(rawJson))
    }

    /**
     * Parses a raw JSON string into a single [SduiPayload].
     */
    fun parseComponent(rawJson: String): SduiPayload {
        return parseComponent(JSONObject(rawJson))
    }
}

/**
 * Converts a [JSONObject] into a [Map] with native Kotlin types.
 *
 * - JSONObject → Map<String, Any?>
 * - JSONArray → List<Any?>
 * - JSONObject.NULL → null
 * - Primitives pass through (String, Int, Long, Double, Boolean)
 */
private fun JSONObject.toMap(): Map<String, Any?> {
    val map = mutableMapOf<String, Any?>()
    val keys = this.keys()
    while (keys.hasNext()) {
        val key = keys.next()
        map[key] = this.get(key).toKotlinValue()
    }
    return map
}

private fun JSONArray.toList(): List<Any?> {
    return (0 until this.length()).map { this.get(it).toKotlinValue() }
}

private fun Any.toKotlinValue(): Any? {
    return when (this) {
        JSONObject.NULL -> null
        is JSONObject -> this.toMap()
        is JSONArray -> this.toList()
        else -> this
    }
}

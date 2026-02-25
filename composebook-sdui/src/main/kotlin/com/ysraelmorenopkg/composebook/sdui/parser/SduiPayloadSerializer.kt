package com.ysraelmorenopkg.composebook.sdui.parser

import com.ysraelmorenopkg.composebook.sdui.model.SduiPayload
import org.json.JSONArray
import org.json.JSONObject

/**
 * Converts [SduiPayload] back into JSON strings and objects.
 *
 * Used by the PayloadEditor to display and edit the raw JSON representation.
 * Roundtrips with [SduiPayloadParser]: parse → edit → serialize → parse.
 */
object SduiPayloadSerializer {

    fun toJsonObject(payload: SduiPayload): JSONObject {
        val json = JSONObject()
        json.put("type", payload.type)

        if (payload.id.isNotEmpty()) {
            json.put("id", payload.id)
        }

        if (payload.parameters.isNotEmpty()) {
            json.put("parameters", payload.parameters.toJsonValue())
        }

        if (payload.children.isNotEmpty()) {
            val childrenArray = JSONArray()
            payload.children.forEach { child ->
                childrenArray.put(toJsonObject(child))
            }
            json.put("components", childrenArray)
        }

        return json
    }

    fun toJsonString(payload: SduiPayload, indent: Int = 2): String {
        return toJsonObject(payload).toString(indent)
    }
}

private fun Any?.toJsonValue(): Any? {
    return when (this) {
        null -> JSONObject.NULL
        is Map<*, *> -> {
            val obj = JSONObject()
            this.forEach { (k, v) -> obj.put(k.toString(), v.toJsonValue()) }
            obj
        }
        is List<*> -> {
            val arr = JSONArray()
            this.forEach { arr.put(it.toJsonValue()) }
            arr
        }
        is String, is Number, is Boolean -> this
        else -> this.toString()
    }
}

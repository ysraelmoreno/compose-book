package com.ysraelmorenopkg.composebook.sdui.mapper

import com.ysraelmorenopkg.composebook.sdui.model.SduiComponentType
import com.ysraelmorenopkg.composebook.sdui.model.SduiPayload
import com.ysraelmorenopkg.composebook.sdui.registry.SduiRegistry

/**
 * Orchestrates the payload → component mapping pipeline.
 *
 * Resolves the correct [SduiComponentMapper] from the [registry] based on
 * the payload's [type][SduiPayload.type] and invokes it. Supports recursive
 * mapping for nested children, mirroring browse-android's `BffModelMapper`.
 */
class SduiPayloadMapper(private val registry: SduiRegistry) {

    /**
     * Maps a single [payload] into a typed component.
     *
     * @return A [MappingResult.Success] with the mapped component,
     *         or a [MappingResult.Error] describing what went wrong.
     */
    suspend fun map(payload: SduiPayload): MappingResult {
        val type = SduiComponentType(payload.type)
        val mapper = registry.findMapper(type)
            ?: return MappingResult.Error(
                "No mapper registered for type '${payload.type}'"
            )

        return try {
            val component = mapper.map(payload) { children ->
                children.mapNotNull { child ->
                    when (val result = map(child)) {
                        is MappingResult.Success -> result.component
                        is MappingResult.Error -> null
                    }
                }
            }

            if (component != null) {
                MappingResult.Success(component)
            } else {
                MappingResult.Error(
                    "Mapper for type '${payload.type}' returned null"
                )
            }
        } catch (e: Exception) {
            MappingResult.Error(
                "Mapper for type '${payload.type}' threw: ${e.message}"
            )
        }
    }
}

sealed interface MappingResult {
    data class Success(val component: Any) : MappingResult
    data class Error(val message: String) : MappingResult
}

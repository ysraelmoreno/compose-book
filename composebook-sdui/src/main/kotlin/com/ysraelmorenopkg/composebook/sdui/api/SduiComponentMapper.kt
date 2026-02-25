package com.ysraelmorenopkg.composebook.sdui.api

import com.ysraelmorenopkg.composebook.sdui.model.SduiPayload

/**
 * Maps an [SduiPayload] into a typed component of type [T].
 *
 * Mirrors the browse-android `ComponentMapper` pattern: each mapper handles
 * one component type, transforming raw BFF parameters into a typed data class.
 *
 * Mappers are `suspend` to support async operations (e.g., conditional logic,
 * use case calls) that real-world mappers may require.
 *
 * @param T The concrete component type this mapper produces
 *
 * @see [com.ysraelmorenopkg.composebook.sdui.registry.SduiRegistry.registerMapper]
 */
fun interface SduiComponentMapper<T : Any> {

    /**
     * Maps a raw [payload] into a typed component.
     *
     * @param payload The raw payload with type, parameters, and children
     * @param mapChildren Recursive mapper — call this to map nested child payloads
     *                    into typed components. Returns a list of [Any] because
     *                    children may be of different concrete types.
     * @return The mapped component, or null if mapping fails
     */
    suspend fun map(
        payload: SduiPayload,
        mapChildren: suspend (List<SduiPayload>) -> List<Any>,
    ): T?
}

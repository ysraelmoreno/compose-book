package com.ysraelmorenopkg.composebook.sdui.registry

import com.ysraelmorenopkg.composebook.sdui.api.SduiComponentMapper
import com.ysraelmorenopkg.composebook.sdui.api.SduiComponentRenderer
import com.ysraelmorenopkg.composebook.sdui.model.SduiComponentType
import kotlin.reflect.KClass

/**
 * Central registry for SDUI component renderers and mappers.
 *
 * Consuming apps register their existing renderers and mappers here.
 * Renderers are resolved by component [KClass] (for rendering).
 * Mappers are resolved by [SduiComponentType] string (for payload-to-component mapping).
 */
interface SduiRegistry {

    // -- Renderers --

    /**
     * Registers a renderer for a specific component class.
     *
     * If a renderer for this class is already registered, it is replaced
     * and a warning is logged (last-write-wins semantics).
     *
     * @param T The concrete component type
     * @param componentClass The KClass used as the lookup key
     * @param renderer The renderer that knows how to draw [T]
     */
    fun <T : Any> registerRenderer(
        componentClass: KClass<T>,
        renderer: SduiComponentRenderer<T>,
    )

    /**
     * Finds the renderer registered for [componentClass], if any.
     *
     * @return The renderer cast to work with [Any], or null if no renderer is registered.
     */
    fun findRenderer(componentClass: KClass<*>): SduiComponentRenderer<Any>?

    /**
     * Returns all registered renderer component classes.
     */
    fun registeredTypes(): Set<KClass<*>>

    // -- Mappers --

    /**
     * Registers a mapper that converts [SduiPayload] with the given [type]
     * into components of [componentClass].
     *
     * @param T The concrete component type the mapper produces
     * @param type The payload type string this mapper handles (e.g., "button")
     * @param componentClass The KClass the mapper produces (used for renderer lookup after mapping)
     * @param mapper The mapper implementation
     */
    fun <T : Any> registerMapper(
        type: SduiComponentType,
        componentClass: KClass<T>,
        mapper: SduiComponentMapper<T>,
    )

    /**
     * Finds the mapper registered for [type], if any.
     *
     * @return The mapper cast to work with [Any], or null if no mapper is registered.
     */
    fun findMapper(type: SduiComponentType): SduiComponentMapper<Any>?
}

/**
 * Convenience extension to register a renderer using reified type parameter.
 */
inline fun <reified T : Any> SduiRegistry.registerRenderer(
    renderer: SduiComponentRenderer<T>,
) {
    registerRenderer(T::class, renderer)
}

/**
 * Convenience extension to register a mapper using reified type parameter.
 */
inline fun <reified T : Any> SduiRegistry.registerMapper(
    type: SduiComponentType,
    mapper: SduiComponentMapper<T>,
) {
    registerMapper(type, T::class, mapper)
}

/**
 * Convenience extension to register a mapper with a string type.
 */
inline fun <reified T : Any> SduiRegistry.registerMapper(
    type: String,
    mapper: SduiComponentMapper<T>,
) {
    registerMapper(SduiComponentType(type), T::class, mapper)
}

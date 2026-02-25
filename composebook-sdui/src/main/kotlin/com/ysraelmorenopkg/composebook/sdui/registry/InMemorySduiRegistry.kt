package com.ysraelmorenopkg.composebook.sdui.registry

import android.util.Log
import com.ysraelmorenopkg.composebook.sdui.api.SduiComponentMapper
import com.ysraelmorenopkg.composebook.sdui.api.SduiComponentRenderer
import com.ysraelmorenopkg.composebook.sdui.model.SduiComponentType
import kotlin.reflect.KClass

/**
 * In-memory implementation of [SduiRegistry].
 *
 * Stores renderers keyed by component [KClass] and mappers keyed by
 * [SduiComponentType], mirroring the browse-android `MainRenderMap`
 * and `BffModelMapper` patterns respectively.
 */
class InMemorySduiRegistry : SduiRegistry {

    private val renderers = HashMap<KClass<*>, SduiComponentRenderer<Any>>()
    private val mappers = HashMap<SduiComponentType, SduiComponentMapper<Any>>()

    // -- Renderers --

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> registerRenderer(
        componentClass: KClass<T>,
        renderer: SduiComponentRenderer<T>,
    ) {
        val existing = renderers.put(componentClass, renderer as SduiComponentRenderer<Any>)
        if (existing != null) {
            Log.w(
                TAG,
                "Renderer for ${componentClass.simpleName} was replaced. " +
                    "Two plugins may be conflicting.",
            )
        }
    }

    override fun findRenderer(componentClass: KClass<*>): SduiComponentRenderer<Any>? {
        return renderers[componentClass]
    }

    override fun registeredTypes(): Set<KClass<*>> {
        return renderers.keys.toSet()
    }

    // -- Mappers --

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> registerMapper(
        type: SduiComponentType,
        componentClass: KClass<T>,
        mapper: SduiComponentMapper<T>,
    ) {
        val existing = mappers.put(type, mapper as SduiComponentMapper<Any>)
        if (existing != null) {
            Log.w(
                TAG,
                "Mapper for type '${type.value}' was replaced. " +
                    "Two plugins may be conflicting.",
            )
        }
    }

    override fun findMapper(type: SduiComponentType): SduiComponentMapper<Any>? {
        return mappers[type]
    }

    private companion object {
        const val TAG = "SduiRegistry"
    }
}

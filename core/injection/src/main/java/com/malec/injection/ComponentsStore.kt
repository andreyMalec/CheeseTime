package com.malec.injection

internal class ComponentsStore {
    private val components = mutableMapOf<String, Any>()

    fun add(key: String, component: Any) {
        components[key] = component
    }

    operator fun get(key: String): Any =
        components[key] ?: throw IllegalStateException("Component of the $key type was not found")

    fun remove(key: String) {
        components.remove(key)
    }

    fun remove(componentClass: Class<*>): Boolean {
        var searchedComponent: Any? = null
        for (component in components.values) {
            if (component.isSameClass(componentClass)) {
                searchedComponent = component
                break
            }
        }
        searchedComponent?.let {
            return components.values.remove(it)
        }
        return false
    }

    fun isExist(key: String) = components.containsKey(key)

    @Suppress("UNCHECKED_CAST")
    fun <T> findComponent(
        searchedClass: Class<T>,
        componentBuilder: (() -> T)?
    ): T {
        components.values.forEach { component ->
            if (component.isSameClass(searchedClass)) {
                return component as T
            }
        }

        componentBuilder?.invoke()?.let { newComponent -> return newComponent }

        throw IllegalStateException("Component of the ${searchedClass.simpleName} type was not found")
    }

    private fun Any.isSameClass(otherClass: Class<*>): Boolean =
        otherClass.isAssignableFrom(javaClass) || javaClass.isAssignableFrom(otherClass)
}
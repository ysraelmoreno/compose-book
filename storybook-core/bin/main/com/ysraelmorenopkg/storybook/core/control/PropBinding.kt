package com.ysraelmorenopkg.storybook.core.control

/**
 * Binds a prop field to a control, enabling runtime editing without reflection.
 * 
 * PropBinding maintains immutability by using explicit getter/setter functions.
 * The setter always returns a new Props instance.
 * 
 * @param Props The props data class type
 * @param T The type of the specific field being controlled
 * @property key Unique identifier for this binding (typically the prop field name)
 * @property getter Function to extract the current value from Props
 * @property setter Function to create a new Props with updated value (immutable update)
 * @property control The control that defines how this prop is edited in the UI
 */
class PropBinding<Props : Any, T : Any>(
    val key: String,
    val getter: (Props) -> T,
    val setter: (Props, T) -> Props,
    val control: PropControl<T>
) {
    init {
        require(key.isNotBlank()) { "PropBinding key cannot be blank" }
    }
    
    /**
     * Gets the current value from the given props instance.
     */
    fun getValue(props: Props): T = getter(props)
    
    /**
     * Creates a new Props instance with the updated value.
     * This maintains immutability as required by the architecture.
     */
    fun updateValue(props: Props, newValue: T): Props = setter(props, newValue)
}

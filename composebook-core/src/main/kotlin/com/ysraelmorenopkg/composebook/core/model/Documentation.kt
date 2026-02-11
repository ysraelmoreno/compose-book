package com.ysraelmorenopkg.composebook.core.model

/**
 * Documentation for a story component.
 * 
 * Provides structured information about the component's purpose, usage, and implementation.
 * This is UI-independent and can be rendered in any format (Markdown, HTML, etc.).
 * 
 * @property description Main description of the component and its purpose
 * @property usage How to use the component with code examples
 * @property props Description of available props and their effects
 * @property notes Additional notes, warnings, or best practices
 */
data class Documentation(
    val description: String? = null,
    val usage: String? = null,
    val props: String? = null,
    val notes: String? = null
) {
    /**
     * Checks if the documentation has any content.
     */
    fun isEmpty(): Boolean {
        return description.isNullOrBlank() &&
               usage.isNullOrBlank() &&
               props.isNullOrBlank() &&
               notes.isNullOrBlank()
    }
    
    companion object {
        val Empty = Documentation()
    }
}

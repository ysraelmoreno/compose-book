package com.ysraelmorenopkg.composebook.ui.docs

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

/**
 * Colors used for Kotlin syntax highlighting.
 * Uses theme colors so highlighting adapts to light/dark mode.
 */
data class KotlinSyntaxColors(
    val default: Color,
    val keyword: Color,
    val string: Color,
    val comment: Color,
    val number: Color
)

/**
 * Highlights Kotlin source code for display in documentation usage blocks.
 * Default language for usage blocks is Kotlin.
 *
 * Token types: comments, strings, keywords, numbers; everything else uses default color.
 */
object KotlinSyntaxHighlighter {

    private val LINE_COMMENT = Regex("//[^\n]*")
    private val BLOCK_COMMENT = Regex("/\\*[\\s\\S]*?\\*/")
    private val TRIPLE_QUOTE_STRING = Regex("\"\"\"[\\s\\S]*?\"\"\"")
    private val DOUBLE_QUOTE_STRING = Regex("\"(?:[^\"\\\\]|\\\\.)*\"")
    private val KEYWORDS = Regex(
        "\\b(" +
            "val|var|fun|if|else|for|while|return|class|object|interface|when|try|catch|finally|" +
            "null|true|false|in|is|as|package|import|override|open|data|sealed|enum|const|" +
            "by|companion|init|constructor|abstract|internal|private|protected|public|" +
            "throw|typealias|typeof|where" +
            ")\\b"
    )
    private val NUMBER = Regex("\\b\\d+\\.?\\d*([fFL])?\\b")

    /**
     * Builds an [AnnotatedString] with span styles for Kotlin syntax.
     * Order of application: comments, strings, keywords, numbers (earlier types take precedence).
     */
    fun highlight(code: String, colors: KotlinSyntaxColors): AnnotatedString {
        val normalized = code.trimIndent()
        if (normalized.isEmpty()) return AnnotatedString("")

        // Collect (start, end, type). Priority: comment=4, string=3, keyword=2, number=1
        data class Span(val start: Int, val end: Int, val type: Int)

        val spans = mutableListOf<Span>()

        LINE_COMMENT.findAll(normalized).forEach { spans += Span(it.range.first, it.range.last + 1, 4) }
        BLOCK_COMMENT.findAll(normalized).forEach { spans += Span(it.range.first, it.range.last + 1, 4) }
        TRIPLE_QUOTE_STRING.findAll(normalized).forEach { spans += Span(it.range.first, it.range.last + 1, 3) }
        DOUBLE_QUOTE_STRING.findAll(normalized).forEach { spans += Span(it.range.first, it.range.last + 1, 3) }
        KEYWORDS.findAll(normalized).forEach { spans += Span(it.range.first, it.range.last + 1, 2) }
        NUMBER.findAll(normalized).forEach { spans += Span(it.range.first, it.range.last + 1, 1) }

        // Sort by start, then by -type (higher type first at same position)
        spans.sortWith(compareBy<Span> { it.start }.thenBy { -it.type })

        // Merge: for each index, use the span with highest type that covers it
        val maxType = IntArray(normalized.length) { 0 }
        for (s in spans) {
            for (i in s.start until s.end.coerceAtMost(normalized.length)) {
                if (s.type > maxType[i]) maxType[i] = s.type
            }
        }

        return buildAnnotatedString {
            var i = 0
            while (i < normalized.length) {
                val t = maxType[i]
                val spanColor = when (t) {
                    4 -> colors.comment
                    3 -> colors.string
                    2 -> colors.keyword
                    1 -> colors.number
                    else -> colors.default
                }
                var j = i
                while (j < normalized.length && maxType[j] == t) j++
                withStyle(SpanStyle(color = spanColor)) {
                    append(normalized.substring(i, j))
                }
                i = j
            }
        }
    }
}

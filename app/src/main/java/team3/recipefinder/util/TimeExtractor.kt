package team3.recipefinder.util

import java.util.regex.Pattern

private data class Extractor(val regex: String, val factor: Int) {
    fun applyTo(text: String): Int {
        val matcher = Pattern.compile(regex).matcher(text)

        return if (matcher.find()) {
            (matcher.group(1)?.toInt() ?: 0) * factor
        } else 0
    }
}

private val extractors = listOf(
    Extractor("^.*\\s+(\\d+)\\s*[sS]econd.*$", 1),
    Extractor("^.*\\s+(\\d+)\\s*[mM]inute.*$", 60),
    Extractor("^.*\\s+(\\d+)\\s*[hH]our.*$", 3600)
)

/**
 * Extract time (in seconds) from a String
 * For example: "wait for 5 minutes" should return 60 * 5 = 300
 * @return time in seconds
 */
fun String.extractTime(): Int {
    return extractors.map { it.applyTo(this) }.sum()
}

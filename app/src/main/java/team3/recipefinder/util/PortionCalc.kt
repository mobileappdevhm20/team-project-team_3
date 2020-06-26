package team3.recipefinder.util

import java.util.regex.Pattern

fun calculateAmount(value: String, basePortion: Int, portion: Int): String {
    val returnValue: String
    val regex = "(\\d*\\.\\d+)|(\\d+\\.?)"
    val m = Pattern.compile(regex).matcher(value)
    if (m.find()) {
        val baseValue = m.group()
        val baseFactor = java.lang.Float.valueOf(baseValue) / basePortion
        returnValue =
            value.replace(
                baseValue,
                (java.lang.Float.valueOf(baseValue) - (baseFactor * (basePortion - portion))).toString()
            )
    } else {
        returnValue = value
    }
    return returnValue
}

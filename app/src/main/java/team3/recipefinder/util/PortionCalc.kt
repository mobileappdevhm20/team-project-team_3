package team3.recipefinder.util

import java.util.regex.Pattern

fun calculateAmount(value: String, portion: Int): String {
    val returnValue: String
    val regex = "(\\d*\\.\\d+)|(\\d+\\.?)"
    val m = Pattern.compile(regex).matcher(value)
    if (m.find()) {
        val number = m.group()
        returnValue =
            value.replace(number, (java.lang.Float.valueOf(number) * portion).toString())
    } else {
        returnValue = value
    }
    return returnValue
}
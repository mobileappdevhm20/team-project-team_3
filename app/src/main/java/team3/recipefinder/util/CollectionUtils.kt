package team3.recipefinder.util

fun <T> ArrayList<T>.replace(data: Collection<T>) {
    clear()
    addAll(data)
}
package team3.recipefinder.util

fun <T> ArrayList<T>.replace(data: Collection<T>) {
    clear()
    addAll(data)
}

fun <K, V> HashMap<K, V>.getAndRemove(key: K): V? {
    return this[key]?.also { this.remove(key) }
}

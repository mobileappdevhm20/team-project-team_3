package team3.recipefinder.util

import org.junit.Assert
import org.junit.Test

class PortionTest {

    private val basePortion1 = 4
    private val basePortion2 = 2

    @Test
    fun extractAmount() {
        Assert.assertEquals("4.5 El", calculateAmount("6 El", basePortion1, 3))
    }

    @Test
    fun extractAmount1() {
        Assert.assertEquals("7.5 El", calculateAmount("5 El", basePortion1, 6))
    }

    @Test
    fun extractAmount3() {
        Assert.assertEquals("0.25 El", calculateAmount(".5 El", basePortion2, 1))
    }

    @Test
    fun extractAmount4() {
        Assert.assertEquals("3.0 El", calculateAmount("1.5 El", basePortion2, 4))
    }
}

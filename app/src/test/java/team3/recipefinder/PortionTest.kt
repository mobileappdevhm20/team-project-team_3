package team3.recipefinder

import org.junit.Assert
import org.junit.Test
import team3.recipefinder.util.calculateAmount
import team3.recipefinder.util.extractTime

class PortionTest {
    @Test
    fun extractAmount() {
        Assert.assertEquals("6.0 El", calculateAmount("3 El", 2))
    }

    @Test
    fun extractAmount1() {
        Assert.assertEquals("15.0 El", calculateAmount("5 El", 3))
    }

    @Test
    fun extractAmount3() {
        Assert.assertEquals("1.0 El", calculateAmount(".5 El", 2))
    }

    @Test
    fun extractAmount4() {
        Assert.assertEquals("4.5 El", calculateAmount("1.5 El", 3))
    }
}
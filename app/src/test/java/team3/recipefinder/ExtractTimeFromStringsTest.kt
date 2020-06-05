package team3.recipefinder

import org.junit.Assert
import org.junit.Test
import team3.recipefinder.util.extractTime

class ExtractTimeFromStringsTest {
    @Test
    fun extractSeconds() {
        Assert.assertEquals(45, "Cook for 45 Seconds".extractTime())
    }

    @Test
    fun extractSecondsBadFormat() {
        Assert.assertEquals(69, "Cook for 69seconds".extractTime())
    }

    @Test
    fun extractSecondsSingular() {
        Assert.assertEquals(1, "Cook for 1 second".extractTime())
    }

    @Test
    fun extractMinutes() {
        Assert.assertEquals(300, "Cook for 5 Minutes".extractTime())
    }

    @Test
    fun extractCombinedMinutesAndSeconds() {
        Assert.assertEquals(342, "Cook for 5 Minutes and 42 seconds".extractTime())
    }

    @Test
    fun extractHours() {
        Assert.assertEquals(2 * 3600, "Cook for 2 hours".extractTime())
    }

    @Test
    fun extractHoursMinutesAndSeconds() {
        Assert.assertEquals(4 * 3600 + 125, "Cook for 4hours, 2 minutes and 5 seconds".extractTime())
    }
}
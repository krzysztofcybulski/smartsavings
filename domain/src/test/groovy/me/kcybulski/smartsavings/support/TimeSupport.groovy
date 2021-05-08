package me.kcybulski.smartsavings.support


import java.time.Clock
import java.time.LocalDate

import static me.kcybulski.smartsavings.TestData.randomDay

trait TimeSupport {

    Clock clock = new TestClock()

    LocalDate today = randomDay()

    LocalDate yesterday = today.minusDays(1)
    LocalDate threeDaysAgo = today.minusDays(3)

    def setup() {
        clock.setTime(today)
    }

}

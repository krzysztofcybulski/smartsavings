package me.kcybulski.smartsavings.domain

import java.time.Clock

class SmartSavings(
    private val clock: Clock
) {

    fun howMuchWorthNow(investment: Investment): Earnings = TODO()

}

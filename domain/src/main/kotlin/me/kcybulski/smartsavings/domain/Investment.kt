package me.kcybulski.smartsavings.domain

import java.math.BigDecimal
import java.time.LocalDate

class Investment(
    val started: LocalDate,
    val amount: Money,
    val frequency: Frequency
)

data class Money(val value: BigDecimal, val currency: Currency)

data class Currency(val iso: String)

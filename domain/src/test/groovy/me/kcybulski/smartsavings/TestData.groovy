package me.kcybulski.smartsavings

import me.kcybulski.smartsavings.domain.Currency
import me.kcybulski.smartsavings.domain.Money

import java.time.LocalDate

import static java.time.LocalDate.ofEpochDay

class TestData {

    static LocalDate randomDay() {
        return ofEpochDay(20000)
    }

    static Currency USD = new Currency('USD')

    static Money USD_1 = Money.@Companion.of(1.00, USD)
    static Money USD_10 = Money.@Companion.of(10.00, USD)

}

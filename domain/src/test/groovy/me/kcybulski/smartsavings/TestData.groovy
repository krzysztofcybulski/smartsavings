package me.kcybulski.smartsavings

import me.kcybulski.smartsavings.domain.Currency
import me.kcybulski.smartsavings.domain.Money

import java.time.LocalDate

import static java.time.LocalDate.ofEpochDay

class TestData {

    static LocalDate randomDay() {
        return ofEpochDay(20000)
    }

    static Money usd(BigDecimal value) {
        return Money.@Companion.of(value, new Currency('USD'))
    }

    static Money USD_1 = usd(1.00)
    static Money USD_10 = usd(10.00)

}

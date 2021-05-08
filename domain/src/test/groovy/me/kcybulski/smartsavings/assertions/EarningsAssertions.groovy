package me.kcybulski.smartsavings.assertions

import me.kcybulski.smartsavings.domain.Earnings
import me.kcybulski.smartsavings.domain.Money

class EarningsAssertions {

    private Earnings earnings

    static EarningsAssertions assertThat(Earnings earnings) {
        return new EarningsAssertions(earnings: earnings)
    }

    EarningsAssertions isWorth(Money money) {
        assert earnings.worth == money
        return this
    }

}

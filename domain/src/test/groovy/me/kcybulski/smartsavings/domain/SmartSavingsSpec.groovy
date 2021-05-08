package me.kcybulski.smartsavings.domain


import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

import static me.kcybulski.smartsavings.TestData.randomDay

class SmartSavingsSpec extends Specification {

    @Subject
    SmartSavings smartSavings = new SmartSavings()

    def 'should calculate one day earnings'() {
        given:
            LocalDate today = randomDay()
            Investment investment = new Investment(
                    today.minusDays(1),
                    new Money(10.0, new Currency('PLN')),
                    EveryDay.INSTANCE
            )
        expect:
            smartSavings.howMuchWorthNow(investment).worth == new Money(10.0, new Currency('PLN'))
    }

}

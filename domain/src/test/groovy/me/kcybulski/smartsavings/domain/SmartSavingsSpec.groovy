package me.kcybulski.smartsavings.domain

import me.kcybulski.smartsavings.support.TimeSupport
import spock.lang.Specification
import spock.lang.Subject

import static me.kcybulski.smartsavings.TestData.USD_10

class SmartSavingsSpec extends Specification implements TimeSupport {

    CryptocurrenciesFactory cryptocurrencies = new CryptocurrenciesFactory()

    @Subject
    SmartSavings smartSavings = new SmartSavings(clock)

    def 'should calculate one day earnings'() {
        given:
            Investment investment = new Investment(
                    today,
                    EveryDay.INSTANCE,
                    [new Asset(cryptocurrencies.bitcoin(), USD_10)]
            )
        when:
            Earnings earnings = smartSavings.howMuchWorthNow(investment)
        then:
            earnings.worth == USD_10
    }

}

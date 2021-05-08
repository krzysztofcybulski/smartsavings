package me.kcybulski.smartsavings.domain

import me.kcybulski.smartsavings.support.TestCryptoPrices
import me.kcybulski.smartsavings.support.TimeSupport
import spock.lang.Specification
import spock.lang.Subject

import static me.kcybulski.smartsavings.TestData.USD_1
import static me.kcybulski.smartsavings.TestData.USD_10
import static me.kcybulski.smartsavings.TestData.usd
import static me.kcybulski.smartsavings.assertions.EarningsAssertions.assertThat

class SmartSavingsSpec extends Specification implements TimeSupport {

    CryptocurrenciesFactory cryptocurrencies = new CryptocurrenciesFactory()
    TestCryptoPrices cryptoPrices = new TestCryptoPrices()

    @Subject
    SmartSavings smartSavings = new SmartSavings(clock, cryptoPrices)

    def 'should calculate one day earnings from one crypto'() {
        given:
            cryptoPrices.setPrice('BTC', yesterday, USD_10)
        and:
            Investment investment = new Investment(
                    today,
                    EveryDay.INSTANCE,
                    [new Asset(cryptocurrencies.bitcoin(), USD_10)]
            )
        when:
            Earnings earnings = smartSavings.howMuchWorthNow(investment)
        then:
            assertThat(earnings)
                    .invested(usd(10.0))
                    .isWorth(usd(10.0))
    }

    def 'should calculate three days earnings from one crypt'() {
        given:
            cryptoPrices.setPrice('BTC', threeDaysAgo, USD_1)
            cryptoPrices.setPrice('BTC', yesterday, USD_10)
        and:
            Investment investment = new Investment(
                    threeDaysAgo,
                    EveryDay.INSTANCE,
                    [new Asset(cryptocurrencies.bitcoin(), USD_10)]
            )
        when:
            Earnings earnings = smartSavings.howMuchWorthNow(investment)
        then:
            assertThat(earnings)
                    .invested(usd(40.00))
                    .isWorth(usd(220.00))
    }

}

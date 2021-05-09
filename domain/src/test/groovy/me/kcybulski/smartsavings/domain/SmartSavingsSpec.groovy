package me.kcybulski.smartsavings.domain

import me.kcybulski.smartsavings.support.TestCryptoPrices
import me.kcybulski.smartsavings.support.TimeSupport
import spock.lang.Specification
import spock.lang.Subject

import static me.kcybulski.smartsavings.TestData.*
import static me.kcybulski.smartsavings.assertions.EarningsAssertions.assertThat

class SmartSavingsSpec extends Specification implements TimeSupport {

    TestCryptoPrices cryptoPrices = new TestCryptoPrices()

    CryptocurrenciesFactory cryptocurrencies = new CryptocurrenciesFactory()
    WalletCalculator walletCalculator = new WalletCalculator(cryptoPrices)

    @Subject
    SmartSavings smartSavings = new SmartSavings(clock, walletCalculator)

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
            Earnings earnings = smartSavings.howMuchWorthNow(investment).get()
        then:
            assertThat(earnings)
                    .invested(usd(10.0))
                    .isWorth(usd(10.0))
    }

    def 'should calculate three days earnings from one crypto'() {
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
            Earnings earnings = smartSavings.howMuchWorthNow(investment).get()
        then:
            assertThat(earnings)
                    .invested(usd(40.00))
                    .isWorth(usd(220.00))
    }

    def 'should calculate three days earnings from two cryptos'() {
        given:
            cryptoPrices.setPrice('BTC', threeDaysAgo, USD_1)
            cryptoPrices.setPrice('BTC', yesterday, USD_10)
            cryptoPrices.setPrice('ETH', threeDaysAgo, USD_5)
            cryptoPrices.setPrice('ETH', yesterday, USD_10)
        and:
            Investment investment = new Investment(
                    threeDaysAgo,
                    EveryDay.INSTANCE,
                    [
                            new Asset(cryptocurrencies.bitcoin(), USD_5),
                            new Asset(cryptocurrencies.ethereum(), USD_5)
                    ]
            )
        when:
            Earnings earnings = smartSavings.howMuchWorthNow(investment).get()
        then:
            assertThat(earnings)
                    .invested(usd(40.00))
                    .isWorth(usd(140.00))
    }

}

package me.kcybulski.smartsavings.domain

import me.kcybulski.smartsavings.support.TestCryptoPrices
import me.kcybulski.smartsavings.support.TimeSupport
import spock.lang.Specification
import spock.lang.Subject

import static me.kcybulski.smartsavings.TestData.*
import static me.kcybulski.smartsavings.assertions.EarningsAssertions.assertThat

class SmartSavingsSpec extends Specification implements TimeSupport {

    TestCryptoPrices cryptoPrices = new TestCryptoPrices()
    WalletCalculator walletCalculator = new WalletCalculator(cryptoPrices)

    @Subject
    SmartSavings smartSavings = new SmartSavings(clock, walletCalculator)

    def 'should calculate one day earnings from one crypto'() {
        given:
            cryptoPrices.setPrice(yesterday, bitcoinWorth(10.00))
        and:
            Investment investment = new Investment(
                    TETHER,
                    [new Asset(BITCOIN, 10.00)],
                    today,
                    EveryDay.INSTANCE
            )
        when:
            Earnings earnings = smartSavings.howMuchWorthNow(investment).block()
        then:
            assertThat(earnings)
                    .invested(10.00)
                    .isWorth(10.00)
    }
//
    def 'should calculate three days earnings from one crypto'() {
        given:
            cryptoPrices.setPrice(threeDaysAgo, bitcoinWorth(1.00))
            cryptoPrices.setPrice(yesterday, bitcoinWorth(10.00))
        and:
            Investment investment = new Investment(
                    TETHER,
                    [new Asset(BITCOIN, 10.00)],
                    threeDaysAgo,
                    EveryDay.INSTANCE
            )
        when:
            Earnings earnings = smartSavings.howMuchWorthNow(investment).block()
        then:
            assertThat(earnings)
                    .invested(40.00)
                    .isWorth(220.00)
    }
//
    def 'should calculate three days earnings from two cryptos'() {
        given:
            cryptoPrices.setPrice(threeDaysAgo, bitcoinWorth(1.00))
            cryptoPrices.setPrice(yesterday, bitcoinWorth(10.00))
            cryptoPrices.setPrice(threeDaysAgo, ethereumWorth(5.00))
            cryptoPrices.setPrice(yesterday, ethereumWorth(10.00))
        and:
            Investment investment = new Investment(
                    TETHER,
                    [
                            new Asset(BITCOIN, 5.00),
                            new Asset(ETHEREUM, 5.00)
                    ],
                    threeDaysAgo,
                    EveryDay.INSTANCE
            )
        when:
            Earnings earnings = smartSavings.howMuchWorthNow(investment).block()
        then:
            assertThat(earnings)
                    .invested(40.00)
                    .isWorth(140.00)
    }

}

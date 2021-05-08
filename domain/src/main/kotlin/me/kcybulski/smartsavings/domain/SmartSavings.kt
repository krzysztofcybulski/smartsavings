package me.kcybulski.smartsavings.domain

import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId.systemDefault

class SmartSavings(
    private val clock: Clock,
    private val cryptoPrices: CryptoPricesPort
) {

    fun howMuchWorthNow(investment: Investment): Earnings {
        val today = LocalDate.ofInstant(clock.instant(), systemDefault())
        val wallet = investment
            .getDailyInvestments(today)
            .map { dailyCryptoWallet(it) }
            .reduce { acc, wallet -> acc + wallet }

        return Earnings(
            investment = investment.totalInvestment(today),
            worth = walletWorth(wallet, today)
        )
    }

    private fun dailyCryptoWallet(dailyInvestment: DailyInvestment): Wallet = Wallet(
        dailyInvestment.assets.associate { howMuchCrypto(it, dailyInvestment.day) }
    )

    private fun howMuchCrypto(asset: Asset, day: LocalDate): Pair<Cryptocurrency, BigDecimal> {
        val amount = asset.money / cryptoPrices.getPriceAt(asset.crypto, day)
        return asset.crypto to amount
    }

    private fun walletWorth(wallet: Wallet, day: LocalDate): Money = wallet.entries
            .mapValues { (key, value) -> cryptoPrices.getPriceAt(key, day) * value }
            .values
            .reduce { a, b -> a + b }

}

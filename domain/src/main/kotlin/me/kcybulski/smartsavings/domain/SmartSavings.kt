package me.kcybulski.smartsavings.domain

import reactor.core.publisher.Mono
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId.systemDefault

class SmartSavings(
    private val clock: Clock,
    private val walletCalculator: WalletCalculator
) {

    fun howMuchWorthNow(investment: Investment): Mono<Earnings> {
        val today = today()
        return walletCalculator
            .fromInvestment(investment, today)
            .flatMap { walletCalculator.walletWorth(it, investment.coin, today) }
            .map {
                Earnings(
                    investment = investment.totalInvestment(today),
                    base = investment.coin,
                    worth = it
                )
            }
    }

    private fun today() = LocalDate.ofInstant(clock.instant(), systemDefault())

}

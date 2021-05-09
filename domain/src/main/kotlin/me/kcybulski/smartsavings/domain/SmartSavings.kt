package me.kcybulski.smartsavings.domain

import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId.systemDefault
import java.util.concurrent.CompletableFuture

class SmartSavings(
    private val clock: Clock,
    private val walletCalculator: WalletCalculator
) {

    fun howMuchWorthNow(investment: Investment): CompletableFuture<Earnings> {
        val today = today()
        return walletCalculator
            .fromInvestment(investment, today)
            .thenCompose { walletCalculator.walletWorth(it, today) }
            .thenApply {
                Earnings(
                    investment = investment.totalInvestment(today),
                    worth = it
                )
            }
    }

    private fun today() = LocalDate.ofInstant(clock.instant(), systemDefault())

}

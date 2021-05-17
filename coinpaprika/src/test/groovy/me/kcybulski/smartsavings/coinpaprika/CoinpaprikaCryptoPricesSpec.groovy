package me.kcybulski.smartsavings.coinpaprika.api

import kotlin.Pair
import me.kcybulski.smartsavings.coinpaprika.CoinpaprikaApiStub
import me.kcybulski.smartsavings.coinpaprika.CoinpaprikaClient
import me.kcybulski.smartsavings.coinpaprika.CoinpaprikaCryptoPricesAdapter
import me.kcybulski.smartsavings.coinpaprika.CoinpaprikaCryptoPricesFactory
import me.kcybulski.smartsavings.domain.Coin
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class CoinpaprikaCryptoPricesSpec extends Specification implements CoinpaprikaApiStub {

    LocalDate firstJanuaryOf2020 = LocalDate.of(2020, 1, 1)
    LocalDate firstJanuaryOf2021 = LocalDate.of(2021, 1, 1)

    CoinpaprikaClient client = new CoinpaprikaClient(
            CoinpaprikaCryptoPricesFactory.@INSTANCE.defaultHttpClient,
            CoinpaprikaCryptoPricesFactory.@INSTANCE.defaultObjectMapper,
            'localhost:8089'
    )

    @Subject
    CoinpaprikaCryptoPricesAdapter cryptoPrices = new CoinpaprikaCryptoPricesAdapter(client)

    def 'should return prices for two years'() {
        given:
            Coin doge = new Coin('DOGE', 'Doge')
            stubCoinpaprikaFirstOfJanuary(doge, 2020, 10.00)
            stubCoinpaprikaFirstOfJanuary(doge, 2021, 110.00)
        when:
            Flux<Pair<LocalDate, BigDecimal>> exchange = cryptoPrices.getExchange(
                    doge,
                    new Coin('USDT', 'Tether'),
                    [firstJanuaryOf2020, firstJanuaryOf2021]
            )
        then:
            StepVerifier
                    .create(exchange)
                    .expectNext(new Pair(firstJanuaryOf2020, 10.00))
                    .expectNext(new Pair(firstJanuaryOf2021, 110.00))
                    .expectComplete()
                    .verify()
    }
}

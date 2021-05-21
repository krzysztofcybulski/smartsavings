package me.kcybulski.smartsavings.coinpaprika

import com.github.tomakehurst.wiremock.WireMockServer
import me.kcybulski.smartsavings.domain.Coin

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options

trait CoinpaprikaApiStub {

    static WireMockServer wireMockServer = new WireMockServer(
            options()
                    .dynamicPort()
    )

    def setupSpec() {
        wireMockServer.start()
    }

    def stubCoinpaprikaFirstOfJanuary(Coin coin, int year, BigDecimal price) {
        String coinSymbol = "${coin.symbol}-${coin.name}".toLowerCase()
        wireMockServer.stubFor(
                get(urlEqualTo("/coins/$coinSymbol/ohlcv/historical?start=${year}-01-01&limit=366"))
                        .willReturn(aResponse().withBody("""
                        [
                            {
                                "time_open": "${year}-01-01T00:00:00Z",
                                "time_close": "${year}-01-01T23:59:59Z",
                                "open": ${price},
                                "high": ${price},
                                "low": ${price},
                                "close": ${price}
                            }
                        ]
                    """))
        )
    }

    def cleanupSpec() {
        wireMockServer.stop()
    }
}

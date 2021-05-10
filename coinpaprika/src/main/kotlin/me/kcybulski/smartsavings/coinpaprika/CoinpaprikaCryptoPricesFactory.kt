package me.kcybulski.smartsavings.coinpaprika

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import me.kcybulski.smartsavings.domain.ports.CryptoPricesPort
import reactor.netty.http.client.HttpClient

object CoinpaprikaCryptoPricesFactory {

    private val defaultHttpClient: HttpClient = HttpClient.create()
        .headers { headers ->
            headers.remove("Content-Length")
        }

    private val defaultObjectMapper: ObjectMapper = ObjectMapper()
        .registerModule(KotlinModule())
        .registerModule(JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun create(): CryptoPricesPort = CoinpaprikaCryptoPricesAdapter(CoinpaprikaClient(defaultHttpClient, defaultObjectMapper))

    fun create(
        httpClient: HttpClient,
        objectMapper: ObjectMapper
    ): CryptoPricesPort = CoinpaprikaCryptoPricesAdapter(CoinpaprikaClient(httpClient, objectMapper))

}

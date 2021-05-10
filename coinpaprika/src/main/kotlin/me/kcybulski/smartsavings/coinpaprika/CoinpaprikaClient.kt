package me.kcybulski.smartsavings.coinpaprika

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import reactor.core.publisher.Flux
import reactor.netty.http.client.HttpClient
import reactor.netty.http.client.HttpClientResponse
import java.math.BigDecimal
import java.time.LocalDate

internal class CoinpaprikaClient(
    private val httpClient: HttpClient,
    private val objectMapper: ObjectMapper
) {

    fun getDayPrices(coin: String, year: Int): Flux<Candlestick> = httpClient
        .baseUrl("https://api.coinpaprika.com/v1")
        .host("api.coinpaprika.com")
        .get()
        .uri("/coins/${coin}/ohlcv/historical?start=${year}-01-01&limit=366")
        .responseContent()
        .aggregate()
        .flatMapIterable(this::responseToCandlesticks)

    private fun responseToCandlesticks(response: ByteBuf) = readResponse(response)
        .map { Candlestick(it.time_open, it.open) }

    private fun readResponse(input: ByteBuf): List<Response> = objectMapper.readValue(ByteBufInputStream(input))

    class Response(
        val time_open: LocalDate,
        val open: BigDecimal
    )

    class Candlestick(
        val date: LocalDate,
        val startingPrice: BigDecimal
    )
}
fun ByteBuf.toByteArraySafe(): ByteArray {
    if (this.hasArray()) {
        return this.array()
    }

    val bytes = ByteArray(this.readableBytes())
    this.getBytes(this.readerIndex(), bytes)

    return bytes
}

package me.kcybulski.smartsavings.api

import com.fasterxml.jackson.databind.ObjectMapper
import me.kcybulski.smartsavings.api.ObjectMapperConfiguration.DEFAULT_OBJECT_MAPPER
import me.kcybulski.smartsavings.domain.SmartSavings
import ratpack.handling.RequestLogger.ncsa
import ratpack.server.RatpackServer

class Server(
    private val smartSavings: SmartSavings,
    private val objectMapper: ObjectMapper = DEFAULT_OBJECT_MAPPER
) {

    private val ratpackServer: RatpackServer = RatpackServer.of { server ->
        server
            .serverConfig { config ->
                config.threads(1)
            }
            .registryOf { registry ->
                registry
                    .add(objectMapper)
            }
            .handlers { chain ->
                chain
                    .all(ncsa())
                    .post("savings", SavingsHandler(smartSavings))
            }
    }

    fun start() = ratpackServer.start()
    fun stop() = ratpackServer.stop()

}

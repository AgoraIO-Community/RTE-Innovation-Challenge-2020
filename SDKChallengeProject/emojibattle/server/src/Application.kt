package om.qifan.emojibattle.server

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT) // Pretty Prints the JSON
        }
    }

    routing {
        post("/evaluate") {
            val post = call.receive<GameSessionResult>()
            gamerSessionResult.add(post)
            call.respond(post)
        }
        get("/gameresult") {
            val roomId = call.request.queryParameters["roomId"]
            val userId = call.request.queryParameters["userId"]

            val targetResult = gamerSessionResult.maxByOrNull { it.verifiedTimes }
            if (targetResult?.roomId == roomId && targetResult?.userId == userId) {
                call.respond(GameResult(true))
            } else {
                call.respond(GameResult(false))
            }
        }
    }
}

val gamerSessionResult = Collections.synchronizedList(mutableListOf<GameSessionResult>())


data class GameSessionResult(
    val roomId: String,
    val userId: String,
    val verifiedTimes: Int
)

data class GameResult(
    val isWinner: Boolean
)
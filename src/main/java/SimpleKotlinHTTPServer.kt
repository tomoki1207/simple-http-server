import kotlinx.coroutines.experimental.launch
import java.net.ServerSocket

private const val PORT = 8080

fun main(args: Array<String>) {
    val serverSocket = ServerSocket(PORT)
    val parser = RequestParser()

    println("HTTP Server Start! Listening at $PORT !")

    while (true) {
        val socket = serverSocket.accept()
        launch {
            val inputStream = socket.getInputStream()
            val outputStream = socket.getOutputStream()

            val request = parser.fromInputStream(inputStream)
            val response = RequestHandler.handleRequest(request)
            response.writeTo(outputStream)
        }
    }
}
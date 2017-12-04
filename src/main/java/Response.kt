import enums.Status
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class Response(private val status: Status, private val contentType: String, private val body: List<Byte>) {

    companion object {
        private val rfc1123Formatter = DateTimeFormatter.RFC_1123_DATE_TIME
    }

    fun writeTo(out: OutputStream) {
        val now = OffsetDateTime.now(ZoneOffset.UTC)
        val res =
                """HTTP/1.1 ${status.status}
                |Date: ${rfc1123Formatter.format(now)}
                |Server: SimpleScalaHttpServer
                |Content-Type: $contentType
                |Content-Length: ${body.count()}
                |Connection: Close
                |
                |""".trimMargin()
        with(out) {
            write(res.toByteArray(StandardCharsets.UTF_8))
            write(body.toByteArray())
            flush()
        }
    }
}

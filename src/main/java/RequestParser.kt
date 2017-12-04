import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class RequestParser {

    fun fromInputStream(input: InputStream): Request? {
        val requestLine = BufferedReader(InputStreamReader(input)).readLine() ?: return null
        val (method, path, version) = requestLine.split(Regex("\\s+"))
        return Request(method, path, version)
    }
}

data class Request(val method: String, val targetPath: String, val httpVersion: String)

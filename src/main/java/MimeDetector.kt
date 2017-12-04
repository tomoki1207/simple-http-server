import java.io.IOException
import java.nio.file.Path
import java.util.*

class MimeDetector(configFilePath: String) {
    private var prop: Properties

    fun getMime(path: Path): String {
        val ext = path.fileName.toString().substringAfterLast('.')
        return this.prop.getProperty(ext, "application/octet-stream")
    }

    init {
        val p = Properties()
        val input = this.javaClass.getResourceAsStream(configFilePath)
        try {
            p.load(input)
        } catch (ex: IOException) {
            println("Failed to load mime config. Caused by: \n ${ex.message}")
        }
        this.prop = p
    }
}
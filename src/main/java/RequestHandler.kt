import enums.Status
import java.nio.file.Files
import java.nio.file.Paths

object RequestHandler {
    private val BadRequestHtmlPath = Paths.get("public/400.html")
    private val ForbiddenHtmlPath = Paths.get("public/403.html")
    private val NotFoundHtmlPath = Paths.get("public/404.html")
    private val HtmlMime = "text/html;charset=utf8"
    private val mimeDetector = MimeDetector("mimes.properties")

    fun handleRequest(request: Request?): Response {
        if (request == null) {
            return Response(Status.BAD_REQUEST, HtmlMime, Files.readAllBytes(BadRequestHtmlPath).asList())
        }

        val normalizedPath = Paths.get("public", request.targetPath).normalize()
        val path = if (Files.isDirectory(normalizedPath)) normalizedPath.resolve("index.html") else normalizedPath

        if (!path.startsWith("public/")) { // ディレクトリトラバーサル
            return Response(Status.FORBIDDEN, HtmlMime, Files.readAllBytes(ForbiddenHtmlPath).asList())
        }

        if (Files.isRegularFile(normalizedPath)) {
            val mime = mimeDetector.getMime(normalizedPath)
            return Response(Status.OK, mime, Files.readAllBytes(path).asList())
        }

        val indexFilePath = path.resolve("index.html")
        if (Files.isDirectory(path) && Files.exists(indexFilePath)) {
            return Response(Status.OK, HtmlMime, Files.readAllBytes(indexFilePath).asList())
        }

        return Response(Status.NOT_FOUND, HtmlMime, Files.readAllBytes(NotFoundHtmlPath).asList())
    }
}
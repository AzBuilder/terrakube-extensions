@Grapes([
        @Grab('commons-io:commons-io:2.8.0'),
        @Grab('org.apache.commons:commons-lang3:3.12.0'),
        @Grab('org.apache.commons:commons-compress:1.21')
])
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.SystemUtils
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.apache.commons.compress.archivers.ArchiveEntry
import java.nio.file.Files

class Snyk {
    def loadTool(workingDirectory, toolsDirectory, version) {
        String os = checkOS()
        String snykFile = "snyk-${os}"
        String snykURL = "https://static.snyk.io/cli/v${version}/${snykFile}"

        println "Downloading ${snykURL}"
        File snykExecFile = new File("${toolsDirectory}/snyk/snyk")
        FileUtils.copyURLToFile(new URL(snykURL), snykExecFile)

        //MAKE SNYK AN EXECUTABLE FILE
        snykExecFile.setExecutable(true, true)
    }

    private String checkOS() {
        println "OS Type: " + SystemUtils.OS_NAME
        String os = "";

        if (SystemUtils.IS_OS_WINDOWS)
            os = "win.exe"

        if (SystemUtils.IS_OS_LINUX)
            os = "linux"

        if (SystemUtils.IS_OS_MAC)
            os = "macos"

        return os;
    }
}
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

class TerraTag {
    def loadTool(workingDirectory, toolsDirectory, version) {
        String os = checkOS()
        String arch = "amd64" //TerraTag only support amd64
        String terraTagFile = "terratag_${version}_${os}_${arch}.tar.gz"
        String terraTagURL = "https://github.com/env0/terratag/releases/download/v${version}/${terraTagFile}"

        println "Downloading ${terraTagURL}"
        File terratagTagGzFile = new File("${workingDirectory}/${terraTagFile}");
        FileUtils.copyURLToFile(new URL(terraTagURL), terratagTagGzFile)

        extractTarGz(terratagTagGzFile, workingDirectory, toolsDirectory)
    }

    private void extractTarGz(terratagTarGz, workingDirectory, toolsDirectory) {
        InputStream fi = new FileInputStream(terratagTarGz)
        InputStream bi = new BufferedInputStream(fi)
        InputStream gzi = new GzipCompressorInputStream(bi)
        ArchiveInputStream terraTagTarGz = new TarArchiveInputStream(gzi)

        File targetDir = new File(toolsDirectory);

        try {
            ArchiveInputStream i = terraTagTarGz
            ArchiveEntry entry = null
            while ((entry = i.getNextEntry()) != null) {
                if (!i.canReadEntryData(entry)) {
                    continue
                }
                String name = "${toolsDirectory}/terratag/${entry.getName()}"
                println "Extracting ${name}"
                File f = new File(name)
                if (entry.isDirectory()) {
                    if (!f.isDirectory() && !f.mkdirs()) {
                        throw new IOException("failed to create directory " + f)
                    }
                } else {
                    File parent = f.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("failed to create directory " + parent)
                    }
                    OutputStream o = Files.newOutputStream(f.toPath())
                    IOUtils.copy(i, o);
                    o.close()
                }
            }
            i.close()
        } catch (IOException exception) {
            println exception.getMessage()
        }

        //MAKE TERRATAG AN EXECUTABLE FILE
        File terraTag = new File("${toolsDirectory}/terratag/terratag")
        terraTag.setExecutable(true, true)
    }


    private String checkOS() {
        println "OS Type: " + SystemUtils.OS_NAME
        String os = "";

        if (SystemUtils.IS_OS_WINDOWS)
            os = "windows"

        if (SystemUtils.IS_OS_LINUX)
            os = "linux"

        if (SystemUtils.IS_OS_MAC)
            os = "darwin"

        return os;
    }
}
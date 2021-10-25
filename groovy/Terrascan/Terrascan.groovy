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

class Terrascan {
    def loadTool(workingDirectory, toolsDirectory, version) {
        String os = checkOS()
        String arch = "x86_64" // we only need x86_64
        String terrascanFile = "terrascan_${version}_${os}_${arch}.tar.gz"
        String terrascanURL = "https://github.com/accurics/terrascan/releases/download/v${version}/${terrascanFile}"

        println "Downloading ${terrascanURL}"
        File terrascanTagGzFile = new File("${workingDirectory}/${terrascanFile}");
        FileUtils.copyURLToFile(new URL(terrascanURL), terrascanTagGzFile)

        extractTarGz(terrascanTagGzFile, toolsDirectory)
    }

    private void extractTarGz(terratagTarGz, toolsDirectory) {
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
                String name = "${toolsDirectory}/terrascan/${entry.getName()}"
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

        //MAKE TERRASCAN AN EXECUTABLE FILE
        File terrascan = new File("${toolsDirectory}/terrascan/terrascan")
        terrascan.setExecutable(true, true)
    }


    private String checkOS() {
        println "OS Type: " + SystemUtils.OS_NAME
        String os = "";

        if (SystemUtils.IS_OS_WINDOWS)
            os = "Windows"

        if (SystemUtils.IS_OS_LINUX)
            os = "Linux"

        if (SystemUtils.IS_OS_MAC)
            os = "Darwin"

        return os;
    }
}
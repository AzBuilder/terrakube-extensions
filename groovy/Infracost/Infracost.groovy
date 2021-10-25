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
import java.nio.charset.Charset;
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class Infracost {
    def loadTool(workingDirectory, toolsDirectory, version, credentials) {
        String os = checkOS()
        String arch = "amd64" //Infracost only support amd64
        String infracostFile = "infracost-${os}-${arch}.tar.gz"
        String infracostURL = "https://github.com/infracost/infracost/releases/download/v${version}/${infracostFile}"

        println "Downloading ${infracostURL}"
        File infracostTarGz = new File("${workingDirectory}/${infracostFile}");
        FileUtils.copyURLToFile(new URL(infracostURL), infracostTarGz)

        FileUtils.writeStringToFile(new File(FileUtils.getUserDirectoryPath() + "/.config/infracost/credentials.yml"), credentials, Charset.defaultCharset());

        extractTarGz(infracostTarGz, toolsDirectory, os, arch)
    }

    private void extractTarGz(infracostTarGz, toolsDirectory, os, arch) {
        InputStream fi = new FileInputStream(infracostTarGz)
        InputStream bi = new BufferedInputStream(fi)
        InputStream gzi = new GzipCompressorInputStream(bi)
        ArchiveInputStream archiveInput = new TarArchiveInputStream(gzi)

        File targetDir = new File(toolsDirectory);

        try {
            ArchiveInputStream i = archiveInput
            ArchiveEntry entry = null
            while ((entry = i.getNextEntry()) != null) {
                if (!i.canReadEntryData(entry)) {
                    continue
                }
                String name = "${toolsDirectory}/infracost/${entry.getName()}"
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

        //RENAME infracost file
        Path source = Paths.get("${toolsDirectory}/infracost/infracost-${os}-${arch}");
        Files.move(source, source.resolveSibling("infracost"));

        //MAKE INFRACOST AN EXECUTABLE FILE
        File terraTag = new File("${toolsDirectory}/infracost/infracost")
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
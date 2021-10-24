@Grapes([
  @Grab('commons-io:commons-io:2.8.0'),
  @Grab('org.apache.commons:commons-compress:1.21')
])
import org.apache.commons.io.FileUtils
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
          
class TerraTag {
  def downloadTerraTag(workingDirectory, version, os, arch) {
    String terraTagFile = "terratag_${version}_${os}_${arch}.tar.gz"
    String terraTagURL = "https://github.com/env0/terratag/releases/download/v${version}/${terraTagFile}"
    println "Downloading $terraTagURL"
    FileUtils.copyURLToFile(new URL(terraTagURL), new File("${workingDirectory}/${terraTagFile}"))
  }
} 

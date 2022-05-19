@Grapes([
        @Grab('commons-io:commons-io:2.8.0'),
        @Grab('org.apache.commons:commons-lang3:3.12.0'),
])
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.SystemUtils

class Opa {
    def loadTool(workingDirectory, toolsDirectory, version) {
        String os = checkOS()
        String opaFile = ""
        switch(os) { 
           case "darwin":
              opaFile="opa_darwin_amd64" 
              break; 
           case "linux": 
              opaFile="opa_linux_amd64_static"
              break; 
           case "windows":
              opaFile="opa_windows_amd64.exe"
              break; 
        } 
        String opaURL = "https://openpolicyagent.org/downloads/v${version}/${opaFile}"

        println "Downloading ${opaURL}"
        File opaExecFile = new File("${toolsDirectory}/opa/opa")
        FileUtils.copyURLToFile(new URL(opaURL), opaExecFile)

        opaExecFile.setExecutable(true, true)
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
@Grapes([
        @Grab('commons-io:commons-io:2.8.0')
])
import org.apache.commons.io.IOUtils
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

class Context {
    String terrakubeApi
    String terrakubeToken
    String workingDirectory
    String jobId

    Context(terrakubeApi, terrakubeToken, jobId, workingDirectory){
        this.terrakubeApi = terrakubeApi
        this.terrakubeToken = terrakubeToken
        this.jobId = jobId
        this.workingDirectory = workingDirectory
    }

    def saveFile(propertyName, fileName) {
        def jsonSlurper = new JsonSlurper()
        def context = getContext(jobId)

        File file = new File("${workingDirectory}/${fileName}")
        String fileContent = file.text

        def body = jsonSlurper.parseText(fileContent)

        context.put(propertyName, body)

        String jsonOut = JsonOutput.toJson(context)

        def post = new URL("${terrakubeApi}/context/v1/${jobId}").openConnection();
        def message = jsonOut
        post.setRequestMethod("POST")
        post.setDoOutput(true)
        post.setRequestProperty("Content-Type", "application/json")
        post.setRequestProperty("Authorization", "Bearer ${terrakubeToken}")
        post.getOutputStream().write(message.getBytes("UTF-8"))
        def postRC = post.getResponseCode()
        println "Updating Context Response: ${postRC}"
        
    }

    def saveProperty(propertyName, data) {
        def context = getContext(jobId)

        context.put(propertyName, data)

        String bodyJson = JsonOutput.toJson(context)

        def post = new URL("${terrakubeApi}/context/v1/${jobId}").openConnection();
        def message = bodyJson
        post.setRequestMethod("POST")
        post.setDoOutput(true)
        post.setRequestProperty("Content-Type", "application/json")
        post.setRequestProperty("Authorization", "Bearer ${terrakubeToken}")
        post.getOutputStream().write(message.getBytes("UTF-8"))
        def postRC = post.getResponseCode()
        println "Updating Context Response: ${postRC}" 
    }

    def getContext(jobId) {
        def jsonSlurper = new JsonSlurper()

        def get = new URL("${terrakubeApi}/context/v1/${jobId}").openConnection();
        get.setRequestMethod("GET")
        get.setDoOutput(true)
        get.setRequestProperty("Authorization", "Bearer ${terrakubeToken}")
        def getRC = get.getResponseCode()
        def inputStream = get.getInputStream();
        
        String contextBody = IOUtils.toString(inputStream, "UTF-8");
        println "Response code: ${getRC}"
        println "Current context: ${contextBody}"

        jsonSlurper.parseText(contextBody)
    }

}
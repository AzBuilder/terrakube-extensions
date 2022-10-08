import groovy.json.JsonSlurper
import groovy.json.JsonOutput

class Context {
    String terrakubeApi
    String terrakubeToken
    String workspaceDirectory
    String jobId

    Context(terrakubeApi, terrakubeToken, jobId, workingDirectory){
        this.terrakubeApi = terrakubeApi
        this.terrakubeToken = terrakubeToken
        this.jobId = jobId
        this.workingDirectory = workingDirectory
    }

    def saveFile(propertyName, fileName) {
        def jsonSlurper = new JsonSlurper()
        def context = jsonSlurper.parseText("{}")

        File file = new File("${workspaceDirectory}/${fileName}")
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
        println "Saving Context Response: ${postRC}"
        
    }

    def get(name) {
        
    }

}
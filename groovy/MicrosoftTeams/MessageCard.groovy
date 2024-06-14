import groovy.transform.CompileStatic

/**
 * This class represents a MSTeams Message with a color, title, summary, and image.
 */
@CompileStatic
class MessageCard {

    private final String color
    // Terrakube's GitHub avatar
    private final String image = 'https://avatars.githubusercontent.com/u/80990539?s=200&v=4'
    private final String summary
    private final String title
    private String activityTitle
    private String activitySubtitle
    private List buttons = []
    private List facts = []

    /**
     * Constructs a new Message with the specified color, title, and summary.
     * @param color the color of the message
     * @param title the title of the message
     * @param summary the summary of the message
     */
    MessageCard(String color, String title, String summary) {
        println "Creating a new MessageCard with color: $color, title: $title, and summary: $summary"
        this.color = color
        this.title = title
        this.summary = summary
    }

    MessageCard setActivity(String activityTitle, String activitySubtitle) {
        println "Setting activity with title: $activityTitle and subtitle: $activitySubtitle"
        this.activityTitle = activityTitle
        this.activitySubtitle = activitySubtitle

        return this
    }

    MessageCard setButtons(Map buttons) {
        println "Setting buttons: $buttons"

        if (buttons != null && buttons) {
            println "Processing buttons: ${buttons}"
            buttons.each { key, value ->
                this.buttons << [
                    '@type': 'OpenUri',
                    name: key,
                    targets: [[
                        os: 'default',
                        uri: value
                    ]]
                ]
            }
        }

        return this
    }

    MessageCard setFacts(Map facts) {
        println "Setting facts: $facts"

        if (facts != null && facts) {
            println "Processing facts: ${facts}"
            facts.each { key, value ->
                this.facts << [
                    name: key,
                    value: value
                ]
            }
        }

        return this
    }

    /**
    * Returns the payload of the message.
    * @return the payload of the message
    */
    Map payload() {
        println 'Creating payload for MessageCard'

        return [
            '@type': 'MessageCard',
            '@context': 'http://schema.org/extensions',
            themeColor: this.color,
            summary: this.summary,
            title: this.title,
            potentialAction: this.buttons,
            sections: [[
                activityImage: this.image,
                activityTitle: this.activityTitle,
                activitySubtitle: this.activitySubtitle,
                facts: this.facts
            ]]
        ]
    }

}

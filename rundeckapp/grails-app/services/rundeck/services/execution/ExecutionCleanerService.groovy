package rundeck.services.execution

import grails.events.Event
import grails.events.annotation.Subscriber

class ExecutionCleanerService {

    @Subscriber("clean-executions")
    def cleanExecutions(Event eventData) {

        println "triggered by action: ${eventData.data.actionId}"
        println "cleaner params: ${eventData.data.payload}"

    }
}

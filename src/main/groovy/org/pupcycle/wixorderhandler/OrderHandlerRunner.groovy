package org.pupcycle.wixorderhandler

import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.model.Label
import com.google.api.services.gmail.model.ListLabelsResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class OrderHandlerRunner implements CommandLineRunner {

    @Autowired
    Gmail gmailService

    @Override
    void run(String... args) throws Exception {
        // Print the labels in the user's account.
        String user = "me"
        ListLabelsResponse listResponse =
                gmailService.users().labels().list(user).execute()
        List<Label> labels = listResponse.getLabels()
        if (labels.size() == 0) {
            System.out.println("No labels found.")
        } else {
            System.out.println("Labels:")
            for (Label label : labels) {
                System.out.printf("- %s\n", label.getName())
            }
        }
    }
}

package org.pupcycle.wixorderhandler.config

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@CompileStatic
class GmailConfig {

    @Value('${spring.application.name}')
    private String APPLICATION_NAME

    /** Directory to store user credentials for this application. */
    private static final File DATA_STORE_DIR = new File(System.getProperty("user.home"), ".credentials/gmail-java-quickstart")

    /** Global instance of the {@link com.google.api.client.util.store.FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance()

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/gmail-java-quickstart
     */
    private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_LABELS)

    @Value('${client.secret.path}')
    private String clientSecretPath

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR)
        } catch (Throwable t) {
            t.printStackTrace()
            System.exit(1)
        }
    }

    /**
     * Build and return an authorized Gmail client service.
     * @return an authorized Gmail client service
     * @throws IOException
     */
    @Bean
    Gmail getGmailService() throws IOException {
        Credential credential = authorize()
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build()
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    Credential authorize() throws IOException {
        // Load client secrets.
        InputStream inputStream = new FileInputStream(clientSecretPath)
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream))

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build()
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user")
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath())
        return credential
    }
}

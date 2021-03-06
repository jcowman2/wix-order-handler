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
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Contains all configuration necessary to build and return authorized Google API services.
 *
 * @author Joe Cowman
 */
@Configuration
@CompileStatic
class GoogleApiConfig {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleApiConfig.class)

    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance()

    private final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_READONLY, SheetsScopes.SPREADSHEETS) //If modified, delete previous credentials

    @Value('${spring.application.name}')
    private String applicationName

    @Value('${client.secret.path}')
    private String clientSecretPath

    @Value('${client.credential.path}')
    private String credentialPath

    @Value('${server.port}')
    private int port

    /**
     * Returns Google's HTTP Transport
     * @return HTTP Transport
     */
    @Bean
    HttpTransport httpTransport() {
        return GoogleNetHttpTransport.newTrustedTransport()
    }

    /**
     * Returns the credential file.
     * @return the credential file.
     */
    @Bean
    File dataStoreDir() {
        return new File(credentialPath)
    }

    /**
     * Creates a FileDataStoreFactory for the given data store directory.
     * @return the FileDataStoreFactory
     */
    @Bean
    FileDataStoreFactory fileDataStoreFactory() {
        return new FileDataStoreFactory(dataStoreDir())
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    @Bean
    Credential authorize() throws IOException {
        // Load client secrets.
        InputStream inputStream = new FileInputStream(clientSecretPath)
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream))

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport(), JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(fileDataStoreFactory())
                        .setAccessType("offline")
                        .build()

        // Retrieve access token.
        Credential credential = new AuthorizationCodeInstalledApp(flow,
                new LocalServerReceiver.Builder().setPort(port).build())
                .authorize("user")

        LOG.info("Credentials saved to " + dataStoreDir().getAbsolutePath())

        return credential
    }

    /**
     * Build and return an authorized Gmail client service.
     * @return an authorized Gmail client service
     */
    @Bean
    Gmail getGmailService() {
        Credential credential = authorize()
        return new Gmail.Builder(httpTransport(), JSON_FACTORY, credential)
                .setApplicationName(applicationName)
                .build()
    }

    /**
     * Build and return an authorized Sheets client service
     * @return an authorized Sheets client service
     */
    @Bean
    Sheets getSheetsService() {
        Credential credential = authorize()
        return new Sheets.Builder(httpTransport(), JSON_FACTORY, credential)
                .setApplicationName(applicationName)
                .build()
    }

}

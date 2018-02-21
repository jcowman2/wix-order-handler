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

    @Value('${client.secret.path}')
    private String clientSecretPath

    @Value('${client.credential.path')
    public final String credentialPath

    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance()

    private final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_LABELS) //If modified, delete previous credentials

    @Bean
    HttpTransport httpTransport() {
        return GoogleNetHttpTransport.newTrustedTransport()
    }

    @Bean
    File dataStoreDir() {
        return new File(credentialPath)
    }

    @Bean
    FileDataStoreFactory fileDataStoreFactory() {
        return new FileDataStoreFactory(dataStoreDir())
    }

    /**
     * Build and return an authorized Gmail client service.
     * @return an authorized Gmail client service
     * @throws IOException
     */
    @Bean
    Gmail getGmailService() throws IOException {
        Credential credential = authorize()
        return new Gmail.Builder(httpTransport(), JSON_FACTORY, credential)
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
                        httpTransport(), JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(fileDataStoreFactory())
                        .setAccessType("offline")
                        .build()
        Credential credential = new AuthorizationCodeInstalledApp(flow,
                new LocalServerReceiver.Builder().setPort(8091).build())
                .authorize("user")
        System.out.println("Credentials saved to " + dataStoreDir().getAbsolutePath())
        return credential
    }
}

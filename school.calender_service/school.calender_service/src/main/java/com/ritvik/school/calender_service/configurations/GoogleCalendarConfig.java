
package com.ritvik.school.calender_service.configurations;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class GoogleCalendarConfig {

    private static final String APPLICATION_NAME =
            "School Management Calendar";

    private static final JsonFactory JSON_FACTORY =
            GsonFactory.getDefaultInstance();

    private final String credentialsPath;
    private final String tokensPath;

    public GoogleCalendarConfig(
            @Value("${google.calendar.credentials-path}")
            String credentialsPath,

            @Value("${google.calendar.token-path}")
            String tokensPath) {

        this.credentialsPath = credentialsPath;
        this.tokensPath = tokensPath;
    }

    @Bean
    public Calendar googleCalendar()
            throws GeneralSecurityException, IOException {

        NetHttpTransport httpTransport =
                GoogleNetHttpTransport.newTrustedTransport();

        Credential credential = getCredentials(httpTransport);

        return new Calendar.Builder(
                httpTransport,
                JSON_FACTORY,
                credential
        )
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private Credential getCredentials(
            NetHttpTransport httpTransport) throws IOException {

        Path credentialsFile = Path.of(credentialsPath);

        if (!Files.exists(credentialsFile)) {
            throw new FileNotFoundException(
                    "Google credentials file not found: " + credentialsFile
            );
        }

        GoogleClientSecrets clientSecrets;

        try (InputStreamReader reader =
                     new InputStreamReader(
                             Files.newInputStream(credentialsFile))) {

            clientSecrets = GoogleClientSecrets.load(
                    JSON_FACTORY,
                    reader
            );
        }

        File tokenDirectory = new File(tokensPath);

        if (!tokenDirectory.exists() && !tokenDirectory.mkdirs()) {
            throw new IOException(
                    "Unable to create token directory: " + tokensPath
            );
        }

        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport,
                        JSON_FACTORY,
                        clientSecrets,
                        Collections.singletonList(
                                CalendarScopes.CALENDAR_READONLY
                        )
                )
                        .setDataStoreFactory(
                                new FileDataStoreFactory(tokenDirectory)
                        )
                        .setAccessType("offline")
                        .build();

        LocalServerReceiver receiver =
                new LocalServerReceiver.Builder()
                        .setPort(8888)
                        .build();

        return new AuthorizationCodeInstalledApp(
                flow,
                receiver
        ).authorize("user");
    }
}

package com.back.agenda.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GmailService {

    private static final String APPLICATION_NAME = "Agenda App";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/gmail.send");
    private static final String CREDENTIALS_FILE_PATH = "GOOGLE_CREDENTIALS";
    private static final String REDIRECT_URI = "http://localhost:8080/oauth2/callback";


    public static Gmail getGmailService() throws IOException {
        final NetHttpTransport httpTransport = new NetHttpTransport();
        Credential credential = authorize(httpTransport);
        return new Gmail.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static Credential authorize(final NetHttpTransport httpTransport) throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new FileReader(CREDENTIALS_FILE_PATH));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .build();


        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(9090)
                .setCallbackPath("/oauth2/callback")
                .build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

}

package com.moneyflow.config.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleConnection {
    @Value("${spring.application.name}")
    private String applicatinName;

    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = List.of(SheetsScopes.SPREADSHEETS); // Scoppe de Leitura e Escritaate
    private static final String CREDENTIALS_PATH = "/google/credentials.json"; //Local do projeto onde esta arquivo de credencial do Google
    private static final String TOKENS_DIR = System.getProperty("user.home") + "/.moneyflow/tokens"; //Local no dispositivo onde está o token de autenticação gerado pelo Google

    /**
     * Method for authorizing access to Google Sheet
     */
    private Credential autorize(NetHttpTransport transport) throws IOException {
        InputStream in = getClass().getResourceAsStream(CREDENTIALS_PATH);
        if (in == null)
            throw new FileNotFoundException("The credentials.json file could not be found");

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(transport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get(TOKENS_DIR).toFile()))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(8888)
                .build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private Sheets getSheetsService() throws  IOException, GeneralSecurityException {
        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        return new Sheets.Builder(transport, JSON_FACTORY, autorize(transport))
                .setApplicationName(applicatinName)
                .build();
    }

    public List<List<Object>> lerPlanilha(String spreadsheetId, String range) throws IOException, GeneralSecurityException {
        ValueRange response = getSheetsService()
                .spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        return values != null ? values : List.of();
    }
}

package vn.com.pn.service.googledrive;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.client.auth.oauth2.Credential;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import vn.com.pn.controller.UserController;

import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleDriveServiceImpl implements GoogleDriveService {
    private static Log logger = LogFactory.getLog(UserController.class);

    private static String folderID = "1o9HnobatZjkOJ3w2ukpcOOJ3IIYaaJMz";

    private static final String APPLICATION_NAME = "TownHouse";

    /**
     * Directory to store authorization tokens for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File("credentials");

    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CLIENT_SECRET_FILE_NAME = "client_secret.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        java.io.File clientSecretFilePath = new java.io.File(DATA_STORE_DIR, CLIENT_SECRET_FILE_NAME);

        if (!clientSecretFilePath.exists()) {
            throw new FileNotFoundException("Please copy " + CLIENT_SECRET_FILE_NAME //
                    + " to folder: " + DATA_STORE_DIR.getAbsolutePath());
        }

        // Load client secrets.
        InputStream in = new FileInputStream(clientSecretFilePath);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + DATA_STORE_DIR);
        }

        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        FileDataStoreFactory DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();

        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }


    public static Drive getDriveService() {
        Drive service = null;
        try {
            System.out.println("DATA_STORE_DIR: " + DATA_STORE_DIR.getAbsolutePath());
            // 1: Create DATA_STORE_DIR
            if (!DATA_STORE_DIR.exists()) {
                DATA_STORE_DIR.mkdirs();
                System.out.println("Created Folder: " + DATA_STORE_DIR.getAbsolutePath());
                throw new FileNotFoundException("Please copy " + CLIENT_SECRET_FILE_NAME
                        + " to folder: " + DATA_STORE_DIR.getAbsolutePath());
            }

            // 2: Build a new authorized API client service.
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            // 3: Read client_secret.json file & create Credential object.
            Credential credential = getCredentials(HTTP_TRANSPORT);

            // 4: Create Google Drive Service.
            service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return service;
    }

    private static File createGoogleFile(String contentType, String customFileName,
                                         AbstractInputStreamContent uploadStreamContent) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(customFileName);
        fileMetadata.setMimeType(contentType);
        fileMetadata.setParents(Collections.singletonList(folderID));

        Drive driveService = getDriveService();

        File file = driveService.files().create(fileMetadata, uploadStreamContent)
                .setFields("name,size,mimeType,webContentLink,webViewLink").execute();
        return file;
    }

    @Override
    public File uploadFile(String contentType, String customFileName, byte[] uploadData)
            throws IOException {
        AbstractInputStreamContent uploadStreamContent = new ByteArrayContent(contentType, uploadData);
        return createGoogleFile(contentType, customFileName, uploadStreamContent);
    }
}
package vn.com.pn.service.googledrive;

import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.io.InputStream;

public interface GoogleDriveService {
    File uploadFile(String contentType, String customFileName, byte[] uploadData) throws IOException;
}

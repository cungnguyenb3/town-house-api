package vn.com.pn.screen.f004GoogleDrive.service;

import com.google.api.services.drive.model.File;

import java.io.IOException;

public interface GoogleDriveService {
    File uploadFile(String contentType, String customFileName, byte[] uploadData) throws IOException;
}

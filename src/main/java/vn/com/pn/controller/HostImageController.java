package vn.com.pn.controller;

import com.google.api.client.http.HttpTransport;
import com.google.api.services.drive.model.File;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.com.pn.api.request.HostImageRequest;
import vn.com.pn.api.response.UploadFileResponse;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.dto.HostImageDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.HostImage;
import vn.com.pn.domain.ImageInfo;
import vn.com.pn.service.googledrive.GoogleDriveService;
import vn.com.pn.service.hostimage.HostImageService;
import vn.com.pn.utils.MapperUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
public class HostImageController {
    private static Log logger = LogFactory.getLog(HostImageController.class);

    @Autowired
    private HostImageService hostImageService;

    @Autowired
    private GoogleDriveService driveService;

    @ApiOperation(value = "View a list host images", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_IMAGE_GET_ALL_FILE, method = RequestMethod.GET)
    public BaseOutput getAll() {
        logger.info("========== HostImageController.getAll START ==========");
        BaseOutput response = hostImageService.getAllFile();
        logger.info(CommonFunction.convertToJSONStringResponse(response));
        logger.info("========== HostImageController.getAll END ==========");
        return response;
    }

    @ApiOperation(value = "Upload a host image", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_IMAGE_UPLOAD_FILE, method = RequestMethod.POST)
    public ResponseEntity<?> uploadFile(@RequestBody HostImageRequest request) {
        logger.info("========== HostImageController.uploadFile START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            byte[] imageByte = Base64.getDecoder().decode(request.getBase64Encoded()) ;
            File file = driveService.uploadFile(request.getMediaType(),
                    request.getFileName(), imageByte);
            HostImageDTO hostImageDTO = new HostImageDTO(file.getName(),
                    file.getSize().toString(), file.getMimeType(),file.getWebContentLink(),
                    file.getWebViewLink(), request.getHostId());
            BaseOutput response = hostImageService.storeFile(hostImageDTO);
            logger.info("========== HostImageController.uploadFile END ==========");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_IMAGE_UPLOAD_MULTIPLE_FILES, method = RequestMethod.POST)
    public ResponseEntity<?> uploadMultipleFiles(@RequestBody HostImageRequest[] requests) {
        logger.info("========== HostImageController.uploadFile START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(requests));
        try {
            List<HostImageDTO> hostImageDTOList = new ArrayList<>();
            for (HostImageRequest hostImageRequest: requests) {
                byte[] imageByte = Base64.getDecoder().decode(hostImageRequest.getBase64Encoded()) ;
                File file = driveService.uploadFile(hostImageRequest.getMediaType(),
                        hostImageRequest.getFileName(), imageByte);
                HostImageDTO hostImageDTO = new HostImageDTO(file.getName(),
                        file.getSize().toString(), file.getMimeType(),file.getWebContentLink(),
                        file.getWebViewLink(), hostImageRequest.getHostId());
                hostImageDTOList.add(hostImageDTO);
            }
            return ResponseEntity.ok().body(hostImageService.storeMultipleFile(hostImageDTOList));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @ApiOperation(value = "Get information from input file", response = BaseOutput.class)
//    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_IMAGE_DATA_INFO, method = RequestMethod.POST)
//    public ResponseEntity<?> getFileInfo(@RequestParam("file") MultipartFile inputFile) {
//        try {
//            logger.info("========== HostImageController.getFileInfo START ==========");
//            File file = driveService.uploadFile(inputFile.getContentType(),
//                    inputFile.getOriginalFilename(), inputFile.getBytes());
//            file.getOriginalFilename();
//            file.getSize();
//            file.getMimeType();
//            logger.info("========== HostImageController.getFileInfo END ==========");
//            return ResponseEntity.ok().body(file.toPrettyString());
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }



    @ApiOperation(value = "Get information from input file", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_IMAGE_DATA_INFO, method = RequestMethod.POST)
    public ResponseEntity<?> getFileInfo(@RequestParam("file") MultipartFile file) {
        logger.info("========== HostImageController.getFileInfo START ==========");
        ImageInfo imageInfo = hostImageService.getImageInfo(file);
        logger.info("========== HostImageController.getFileInfo END ==========");
        return ResponseEntity.ok(imageInfo);
    }

//    @ApiOperation(value = "Get image from image id", response = ResponseEntity.class)
//    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_IMAGE_DOWNLOAD_FILE_ID, method = RequestMethod.GET)
//    public ResponseEntity<Resource> downloadFile(@PathVariable String id) {
//        logger.info("========== HostImageController.downloadFile START ==========");
//        HostImage hostImage = hostImageService.getFile(id);
//        logger.info("========== HostImageController.downloadFile END ==========");
//        return ResponseEntity.ok().contentType(MediaType.parseMediaType(hostImage.getFileType()))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename\"" + hostImage.getFileName() + "\"")
//                .body(new ByteArrayResource(hostImage.getData()));
//    }

    @ApiOperation(value = "Delete a host image", response = ResponseEntity.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_IMAGE_FILE_ID, method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String id) {
        logger.info("========== HostImageController.delete START ==========");
        BaseOutput response = hostImageService.deleteFile(id);
        logger.info("========== HostImageController.delete END ==========");
        return ResponseEntity.ok(response);
    }
}

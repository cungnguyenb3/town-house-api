package vn.com.pn.screen.m005HostImage.controller;

import com.google.api.services.drive.model.File;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.exception.ResourceInvalidInputException;
import vn.com.pn.screen.m005HostImage.request.HostImageRequest;
import vn.com.pn.common.common.CommonConstants;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.screen.m005HostImage.dto.HostImageDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m005HostImage.entity.ImageInfo;
import vn.com.pn.screen.f004GoogleDrive.service.GoogleDriveService;
import vn.com.pn.screen.m005HostImage.service.HostImageService;

import javax.validation.Valid;
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
    public ResponseEntity<?> uploadFile(@Valid @RequestBody HostImageRequest request) {
        logger.info("========== HostImageController.uploadFile START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        try {
            byte[] imageByte = Base64.getDecoder().decode(request.getBase64Encoded());
            File file = driveService.uploadFile(request.getMediaType(),
                    request.getFileName(), imageByte);
            HostImageDTO hostImageDTO = new HostImageDTO(file.getName(),
                    file.getSize().toString(), file.getMimeType(), file.getWebContentLink(),
                    file.getWebViewLink(), request.getHostId());
            BaseOutput response = hostImageService.storeFile(hostImageDTO);
            logger.info("========== HostImageController.uploadFile END ==========");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResourceInvalidInputException(ScreenMessageConstants.INVALID_INPUT);
        }
    }

    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_IMAGE_UPLOAD_MULTIPLE_FILES, method = RequestMethod.POST)
    public ResponseEntity<?> uploadMultipleFiles(@Valid @RequestBody HostImageRequest[] requests) {
        logger.info("========== HostImageController.uploadFile START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(requests));
        try {
            List<HostImageDTO> hostImageDTOList = new ArrayList<>();
            for (HostImageRequest hostImageRequest : requests) {
                byte[] imageByte = Base64.getDecoder().decode(hostImageRequest.getBase64Encoded());
                File file = driveService.uploadFile(hostImageRequest.getMediaType(),
                        hostImageRequest.getFileName(), imageByte);
                HostImageDTO hostImageDTO = new HostImageDTO(file.getName(),
                        file.getSize().toString(), file.getMimeType(), file.getWebContentLink(),
                        file.getWebViewLink(), hostImageRequest.getHostId());
                hostImageDTOList.add(hostImageDTO);
            }
            return ResponseEntity.ok().body(hostImageService.storeMultipleFile(hostImageDTOList));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResourceInvalidInputException(ScreenMessageConstants.INVALID_INPUT);
        }
    }

    @ApiOperation(value = "Get information from input file", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_IMAGE_DATA_INFO, method = RequestMethod.POST)
    public ResponseEntity<?> getFileInfo(@Valid @RequestParam("file") MultipartFile file) {
        logger.info("========== HostImageController.getFileInfo START ==========");
        ImageInfo imageInfo = hostImageService.getImageInfo(file);
        logger.info("========== HostImageController.getFileInfo END ==========");
        return ResponseEntity.ok(imageInfo);
    }

    @ApiOperation(value = "Delete a host image", response = ResponseEntity.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_IMAGE_FILE_ID, method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@Valid @PathVariable String id) {
        logger.info("========== HostImageController.delete START ==========");
        BaseOutput response = hostImageService.deleteFile(id);
        logger.info("========== HostImageController.delete END ==========");
        return ResponseEntity.ok(response);
    }
}

package vn.com.pn.controller;

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
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.HostImageDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.HostImage;
import vn.com.pn.domain.ImageInfo;
import vn.com.pn.service.hostimage.HostImageService;
import vn.com.pn.utils.MapperUtil;

@RestController
@RequestMapping(CommonConstants.API_URL_CONST.ROOT)
public class HostImageController {
    private static Log logger = LogFactory.getLog(HostImageController.class);

    @Autowired
    private HostImageService hostImageService;

    @ApiOperation(value = "Upload a host image", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_IMAGE_UPLOAD_FILE, method = RequestMethod.POST)
    public ResponseEntity<UploadFileResponse> uploadFile(@RequestBody HostImageRequest request) {
        logger.info("========== HostImageController.uploadFile START ==========");
        logger.info("request: " + CommonFunction.convertToJSONString(request));
        HostImageDTO hostImageDTO = MapperUtil.mapper(request, HostImageDTO.class);
        HostImage hostImage = hostImageService.storeFile(hostImageDTO);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/host-images/download-file/")
                .path(String.valueOf(hostImage.getId()))
                .toUriString();
        UploadFileResponse uploadFileResponse = new UploadFileResponse(hostImage.getFileName(), fileDownloadUri,
                hostImageDTO.getMediaType(), hostImage.getHost());
        logger.info("========== HostImageController.uploadFile END ==========");
        return ResponseEntity.ok(uploadFileResponse);
    }

    @ApiOperation(value = "Get information from input file", response = BaseOutput.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_IMAGE_DATA_INFO, method = RequestMethod.POST)
    public ResponseEntity<?> getFileInfo(@RequestParam("file") MultipartFile file) {
        logger.info("========== HostImageController.getFileInfo START ==========");
        ImageInfo imageInfo = hostImageService.getImageInfo(file);
        logger.info("========== HostImageController.getFileInfo END ==========");
        return ResponseEntity.ok(imageInfo);
    }

//    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_IMAGE_UPLOAD_MULTIPLE_FILES, method = RequestMethod.POST)
//    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
//        return Arrays.asList(files).stream().map(file -> uploadFile(file)).collect(Collectors.toList());
//    }

    @ApiOperation(value = "Get image from image id", response = ResponseEntity.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_IMAGE_DOWNLOAD_FILE_ID, method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@PathVariable String id) {
        logger.info("========== HostImageController.downloadFile START ==========");
        HostImage hostImage = hostImageService.getFile(id);
        logger.info("========== HostImageController.downloadFile END ==========");
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(hostImage.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename\"" + hostImage.getFileName() + "\"")
                .body(new ByteArrayResource(hostImage.getData()));
    }

    @ApiOperation(value = "Delete a host image", response = ResponseEntity.class)
    @RequestMapping(value = CommonConstants.API_URL_CONST.HOST_IMAGE_FILE_ID, method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String id) {
        logger.info("========== HostImageController.delete START ==========");
        BaseOutput response = hostImageService.deleteFile(id);
        logger.info("========== HostImageController.delete END ==========");
        return ResponseEntity.ok(response);
    }

}

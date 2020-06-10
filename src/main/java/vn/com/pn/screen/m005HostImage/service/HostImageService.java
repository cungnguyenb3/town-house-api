package vn.com.pn.screen.m005HostImage.service;

import org.springframework.web.multipart.MultipartFile;
import vn.com.pn.screen.m005HostImage.dto.HostImageDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m005HostImage.entity.HostImage;
import vn.com.pn.screen.m005HostImage.entity.ImageInfo;

import java.util.List;

public interface HostImageService {
    BaseOutput storeFile(HostImageDTO file);
    HostImage getFile (String fileId);
    ImageInfo getImageInfo(MultipartFile file);
    BaseOutput deleteFile (String fileId);
    List<HostImage> storeMultipleFile (List<HostImageDTO> hostImageDTOs);
    BaseOutput getAllFile();
}

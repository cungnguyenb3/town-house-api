package vn.com.pn.service.hostimage;

import org.springframework.web.multipart.MultipartFile;
import vn.com.pn.common.dto.HostImageDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.HostImage;
import vn.com.pn.domain.ImageInfo;

import java.util.List;

public interface HostImageService {
    BaseOutput storeFile(HostImageDTO file);
    HostImage getFile (String fileId);
    ImageInfo getImageInfo(MultipartFile file);
    BaseOutput deleteFile (String fileId);
    List<HostImage> storeMultipleFile (List<HostImageDTO> hostImageDTOs);
    BaseOutput getAllFile();
}

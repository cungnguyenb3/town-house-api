package vn.com.pn.service.hostimage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.dto.HostImageDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.Host;
import vn.com.pn.domain.HostImage;
import vn.com.pn.domain.ImageInfo;
import vn.com.pn.exception.FileNotFoundException;
import vn.com.pn.exception.FileStorageException;
import vn.com.pn.repository.host.HostRepository;
import vn.com.pn.repository.hostimage.HostImageRepository;
import vn.com.pn.service.user.UserServiceImpl;

import java.io.IOException;
import java.util.Base64;

@Service
public class HostImageServiceImpl implements HostImageService {
    private static Log logger = LogFactory.getLog(UserServiceImpl.class);

    @Autowired
    private HostImageRepository hostImageRepository;

    @Autowired
    private HostRepository hostRepository;

    @Override
    public HostImage storeFile(HostImageDTO hostImageDTO) {
        logger.info("HostImageServiceImpl.storeFile");
        try {
            HostImage hostImage = new HostImage();
            hostImage.setFileName(hostImageDTO.getFileName());
            hostImage.setFileType(hostImageDTO.getMediaType());
            byte[] imageByte = Base64.getDecoder().decode(hostImageDTO.getBase64Encoded()) ;
            hostImage.setData(imageByte);

            Host host = hostRepository.findById(Integer.parseInt(hostImageDTO.getHostId())).orElse(null);
            if (host != null) {
                hostImage.setHost(host);
            }
            return hostImageRepository.save(hostImage);
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + hostImageDTO.getFileName() + ". Please try again!", e);
        }
    }

    public ImageInfo getImageInfo(MultipartFile file){
        logger.info("HostImageServiceImpl.getImageInfo");
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setFileName(fileName);
            imageInfo.setFileType(file.getContentType());
            byte[] imageByte = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageByte);
            imageInfo.setBase64Encode(base64Image);
            imageInfo.setFileSize(file.getSize());
            return imageInfo;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
        }
    }

    @Override
    public BaseOutput deleteFile(String fileId) {
        logger.info("HostImageServiceImpl.deleteFile");
        HostImage hostImage = hostImageRepository.findById(Integer.parseInt(fileId)).orElse(null);
        if (hostImage == null) {
            throw new FileNotFoundException("File not found with id " + fileId);
        }
        hostImageRepository.delete(hostImage);
        Object object = ResponseEntity.ok().build();
        return CommonFunction.successOutput(object);
    }

    @Override
    public HostImage getFile(String fileId) {
        logger.info("HostImageServiceImpl.getFile");
        HostImage hostImage = hostImageRepository.findById(Integer.parseInt(fileId)).orElse(null);
        if (hostImage != null) {
            return hostImage;
        }
        throw new FileNotFoundException("File not found with id " + fileId);
    }
}

package vn.com.pn.screen.m005HostImage.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.screen.m005HostImage.dto.HostImageDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m002Host.entity.Host;
import vn.com.pn.screen.m005HostImage.entity.HostImage;
import vn.com.pn.screen.m005HostImage.entity.ImageInfo;
import vn.com.pn.exception.FileNotFoundException;
import vn.com.pn.exception.FileStorageException;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.screen.m002Host.repository.HostRepository;
import vn.com.pn.screen.m005HostImage.repository.HostImageRepository;
import vn.com.pn.screen.m001User.service.UserServiceImpl;

import java.io.IOException;
import java.util.*;

@Service
public class HostImageServiceImpl implements HostImageService {
    private static Log logger = LogFactory.getLog(UserServiceImpl.class);

    @Autowired
    private HostImageRepository hostImageRepository;

    @Autowired
    private HostRepository hostRepository;

    @Override
    public BaseOutput getAllFile() {
        logger.info("HostImageServiceImpl.getAll");
        List<Object> listHostImage = new ArrayList<>(hostImageRepository.findAll());
        return CommonFunction.successOutput(listHostImage);
    }

    @Override
    public BaseOutput storeFile(HostImageDTO hostImageDTO) {
        logger.info("HostImageServiceImpl.storeFile");
        try {
            Host host = hostRepository.findById(Long.parseLong(hostImageDTO.getHostId())).orElseThrow(()
                    -> new ResourceNotFoundException("Host", "id", hostImageDTO.getHostId()));
            String fileSize = CommonFunction.humanReadableByteCountBin(Long.parseLong(hostImageDTO.getFileSize()));
            HostImage hostImage = new HostImage();
            hostImage.setHost(host);
            hostImage.setFileName(hostImageDTO.getFileName());
            hostImage.setFileSize(fileSize);
            hostImage.setFileType(hostImageDTO.getFileType());
            hostImage.setWebContentLink(hostImageDTO.getWebContentLink());
            hostImage.setWebViewLink(hostImageDTO.getWebViewLink());
            return CommonFunction.successOutput(hostImageRepository.save(hostImage));
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + hostImageDTO.getFileName() + ". Please try again!", e);
        }
    }

    @Override
    public List<HostImage> storeMultipleFile (List<HostImageDTO> hostImageDTOList) {
        try {
            List<HostImage> hostImages = new ArrayList<>();
            for (HostImageDTO hostImageDTO: hostImageDTOList) {
                Host host = hostRepository.findById(Long.parseLong(hostImageDTO.getHostId())).orElse(null);
                if (host != null) {
                    String fileSize = CommonFunction.humanReadableByteCountBin(Long.parseLong(hostImageDTO.getFileSize()));
                    HostImage hostImage = new HostImage();
                    hostImage.setHost(host);
                    hostImage.setFileName(hostImageDTO.getFileName());
                    hostImage.setFileSize(fileSize);
                    hostImage.setFileType(hostImageDTO.getFileType());
                    hostImage.setWebContentLink(hostImageDTO.getWebContentLink());
                    hostImage.setWebViewLink(hostImageDTO.getWebViewLink());

                    hostImages.add(hostImage);
                }
            }
            return hostImageRepository.saveAll(hostImages);
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            throw new FileStorageException("Could not store all file Please try again!", e);
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
        HostImage hostImage = hostImageRepository.findById(Long.parseLong(fileId)).orElse(null);
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
        HostImage hostImage = hostImageRepository.findById(Long.parseLong(fileId)).orElse(null);
        if (hostImage != null) {
            return hostImage;
        }
        throw new FileNotFoundException("File not found with id " + fileId);
    }
}

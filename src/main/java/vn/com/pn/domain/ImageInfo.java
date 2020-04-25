package vn.com.pn.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageInfo {
    private String fileName;

    private String fileType;

    private String base64Encode;

    private long fileSize;
}

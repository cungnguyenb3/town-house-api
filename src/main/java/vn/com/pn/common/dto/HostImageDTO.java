package vn.com.pn.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostImageDTO {
    private String base64Encoded;
    private String fileName;
    private String mediaType;
    private String hostId;
}

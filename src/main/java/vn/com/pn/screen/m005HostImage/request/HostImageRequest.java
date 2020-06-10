package vn.com.pn.screen.m005HostImage.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostImageRequest {
    private String base64Encoded;
    private String fileName;
    private String mediaType;
    private String hostId;
}

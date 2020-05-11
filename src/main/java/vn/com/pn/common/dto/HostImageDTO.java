package vn.com.pn.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HostImageDTO {
    private String fileName;
    private String fileSize;
    private String fileType;
    private String webContentLink;
    private String webViewLink;
    private String hostId;
}

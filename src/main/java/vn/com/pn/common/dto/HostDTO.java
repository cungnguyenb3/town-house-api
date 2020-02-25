package vn.com.pn.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostDTO extends BaseDTO{
    private String name;
    private String description;
    private String hostAgentId;
    private String hostCategoryId;
    private String hostRoomTypeId;
    private String hostCityId;
}

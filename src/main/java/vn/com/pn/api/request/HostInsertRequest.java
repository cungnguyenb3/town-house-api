package vn.com.pn.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostInsertRequest extends BaseRequest{
    private String name;
    private String description;
    private String hostAgentId;
    private String hostCategoryId;
    private String hostRoomTypeId;
    private String hostCityId;
}

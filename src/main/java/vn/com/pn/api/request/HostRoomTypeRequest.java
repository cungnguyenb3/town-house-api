package vn.com.pn.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostRoomTypeRequest extends BaseRequest{
    private String name;
    private String description;
}

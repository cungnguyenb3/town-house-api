package vn.com.pn.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostUpdateRequest {
    private String name;
    private String description;
    private String hostAgentId;
    private String hostCategoryId;
    private String hostRoomTypeId;
    private String hostCityId;
    private String address;
    private String latitude;
    private String longitude;
    private String bedroomCount;
    private String bed;
    private String bathroomCount;
    private String availabilityType;
    private String startDate;
    private String endDate;
    private String price;
    private String priceType;
    private String minimumStay;
    private String minimumStayType;
    private String refundType;
    private String status;
}

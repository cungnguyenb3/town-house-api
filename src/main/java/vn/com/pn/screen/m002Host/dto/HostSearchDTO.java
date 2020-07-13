package vn.com.pn.screen.m002Host.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.pn.screen.m005HostImage.entity.HostImage;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HostSearchDTO {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String hostAgentName;
    private String hostCategoryName;
    private String hostRoomTypeName;
    private Long standardPriceMondayToThursday;
    private Long standardPriceFridayToSunday;
    private Integer acreage;
    private String cityName;
    private Float stars;
    private Integer totalReview;
    private Set<HostImage> hostImages;
}

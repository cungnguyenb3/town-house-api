package vn.com.pn.screen.f006Notification.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FCMDataRequestDto {
    private String id;
    private String title = "Town house";
    private String body;
}

package vn.com.pn.screen.f006Notification.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class FCMRequestDto {
    private FCMDataRequestDto data = new FCMDataRequestDto(); ;
    private String to;
    private List<String> tos;
}

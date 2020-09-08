package vn.com.pn.screen.m013Momo.dto;

import lombok.Getter;
import lombok.Setter;
import springfox.documentation.spring.web.json.Json;

@Getter
@Setter
public class RequestPaymentDTO {
    private String partnerCode;
    private String partnerRefId;
    private String customerNumber;
    private String appData;
    private String hash;
    private Double version;
    private Integer payType;
    private String description;
    private Json extra_data;
}

package vn.com.pn.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostCancellationPolicyInsertRequest {
    private String name;
    private String description;
}
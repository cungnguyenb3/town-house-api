package vn.com.pn.common.output;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseOutput {
    private int status;
    private Object message;
    private Integer totalRecord;
    private Object data;
}

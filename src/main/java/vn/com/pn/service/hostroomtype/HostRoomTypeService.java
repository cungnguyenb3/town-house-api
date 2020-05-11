package vn.com.pn.service.hostroomtype;

import vn.com.pn.common.dto.HostRoomTypeDTO;
import vn.com.pn.common.dto.HostRoomTypeUpdateDTO;
import vn.com.pn.common.output.BaseOutput;

public interface HostRoomTypeService {
    BaseOutput getAll();
    BaseOutput insert(HostRoomTypeDTO hostRoomTypeDTO);
    BaseOutput update(HostRoomTypeUpdateDTO hostRoomTypeUpdateDTO);
    BaseOutput delete(String id);
}

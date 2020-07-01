package vn.com.pn.screen.m007HostRoomType.service;

import vn.com.pn.screen.m007HostRoomType.dto.HostRoomTypeDTO;
import vn.com.pn.screen.m007HostRoomType.dto.HostRoomTypeUpdateDTO;
import vn.com.pn.common.output.BaseOutput;

public interface HostRoomTypeService {
    BaseOutput getAll();

    BaseOutput insert(HostRoomTypeDTO hostRoomTypeDTO);

    BaseOutput update(HostRoomTypeUpdateDTO hostRoomTypeUpdateDTO);

    BaseOutput delete(String id);
}

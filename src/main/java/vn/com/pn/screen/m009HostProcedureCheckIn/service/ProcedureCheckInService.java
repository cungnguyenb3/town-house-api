package vn.com.pn.screen.m009HostProcedureCheckIn.service;

import vn.com.pn.screen.m009HostProcedureCheckIn.dto.ProcedureCheckInDTO;
import vn.com.pn.common.output.BaseOutput;

public interface ProcedureCheckInService {
    BaseOutput getAll();
    BaseOutput insert(ProcedureCheckInDTO procedureCheckInDTO);

}

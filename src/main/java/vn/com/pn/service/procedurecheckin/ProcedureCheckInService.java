package vn.com.pn.service.procedurecheckin;

import vn.com.pn.common.dto.CurrencyUnitDTO;
import vn.com.pn.common.dto.ProcedureCheckInDTO;
import vn.com.pn.common.output.BaseOutput;

public interface ProcedureCheckInService {
    BaseOutput getAll();
    BaseOutput insert(ProcedureCheckInDTO procedureCheckInDTO);

}

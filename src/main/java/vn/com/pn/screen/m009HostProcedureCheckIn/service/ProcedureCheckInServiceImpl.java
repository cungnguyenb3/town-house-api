package vn.com.pn.screen.m009HostProcedureCheckIn.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.screen.m009HostProcedureCheckIn.dto.ProcedureCheckInDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m009HostProcedureCheckIn.entity.ProcedureCheckIn;
import vn.com.pn.screen.m009HostProcedureCheckIn.repository.ProcedureCheckInRepository;
import vn.com.pn.screen.m002Host.service.HostServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcedureCheckInServiceImpl implements ProcedureCheckInService{
    private static Log logger = LogFactory.getLog(HostServiceImpl.class);

    @Autowired
    private ProcedureCheckInRepository procedureCheckInRepository;

    @Override
    public BaseOutput getAll() {
        logger.info("ProcedureCheckInServiceImpl.getAll");
        List<Object> listProcedureCheckIn = new ArrayList<Object>(procedureCheckInRepository.findAll());
        return CommonFunction.successOutput(listProcedureCheckIn);
    }

    @Override
    public BaseOutput insert(ProcedureCheckInDTO procedureCheckInDTO) {
        logger.info("ProcedureCheckInServiceImpl.insert");
        try {
            ProcedureCheckIn procedureCheckIn = new ProcedureCheckIn();
            if (procedureCheckInDTO.getName() != null && procedureCheckInDTO.getName() != ""){
                procedureCheckIn.setName(procedureCheckInDTO.getName());
            }
            return CommonFunction.successOutput(procedureCheckInRepository.save(procedureCheckIn));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}

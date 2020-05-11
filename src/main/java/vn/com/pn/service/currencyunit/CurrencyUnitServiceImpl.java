package vn.com.pn.service.currencyunit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.CurrencyUnitDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.CurrencyUnit;
import vn.com.pn.domain.Language;
import vn.com.pn.repository.currencyunit.CurrencyUnitRepository;
import vn.com.pn.service.host.HostServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyUnitServiceImpl implements CurrencyUnitService{
    private static Log logger = LogFactory.getLog(HostServiceImpl.class);

    @Autowired
    private CurrencyUnitRepository currencyUnitRepository;

    @Override
    public BaseOutput getAll() {
        logger.info("CurrencyUnitServiceImpl.getAll");
        List<Object> listCurrencyUnits = new ArrayList<Object>(currencyUnitRepository.findAll());
        return CommonFunction.successOutput(listCurrencyUnits);
    }

    @Override
    public BaseOutput insert(CurrencyUnitDTO currencyUnitDTO) {
        logger.info("CurrencyUnitServiceImpl.insert");
        try {
            CurrencyUnit currencyUnit = new CurrencyUnit();
            if (currencyUnitDTO.getName() != null && currencyUnitDTO.getName() != ""){
                currencyUnit.setName(currencyUnitDTO.getName());
            }
            return CommonFunction.successOutput(currencyUnitRepository.save(currencyUnit));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}

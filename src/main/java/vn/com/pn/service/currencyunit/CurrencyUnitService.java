package vn.com.pn.service.currencyunit;

import vn.com.pn.common.dto.CurrencyUnitDTO;
import vn.com.pn.common.output.BaseOutput;

public interface CurrencyUnitService {
    BaseOutput getAll();
    BaseOutput insert(CurrencyUnitDTO currencyUnitDTO);
}

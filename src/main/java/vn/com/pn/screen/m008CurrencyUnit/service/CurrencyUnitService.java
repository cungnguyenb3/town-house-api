package vn.com.pn.screen.m008CurrencyUnit.service;

import vn.com.pn.screen.m008CurrencyUnit.dto.CurrencyUnitDTO;
import vn.com.pn.common.output.BaseOutput;

public interface CurrencyUnitService {
    BaseOutput getAll();
    BaseOutput insert(CurrencyUnitDTO currencyUnitDTO);
}

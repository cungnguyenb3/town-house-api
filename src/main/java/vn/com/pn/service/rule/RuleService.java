package vn.com.pn.service.rule;

import vn.com.pn.common.dto.CurrencyUnitDTO;
import vn.com.pn.common.dto.RuleDTO;
import vn.com.pn.common.output.BaseOutput;

public interface RuleService {
    BaseOutput getAll();
    BaseOutput insert(RuleDTO ruleDTO);
}

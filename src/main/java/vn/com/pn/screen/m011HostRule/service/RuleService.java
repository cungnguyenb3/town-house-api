package vn.com.pn.screen.m011HostRule.service;

import vn.com.pn.screen.m011HostRule.dto.HostRuleDTO;
import vn.com.pn.common.output.BaseOutput;

public interface RuleService {
    BaseOutput getAll();

    BaseOutput insert(HostRuleDTO hostRuleDTO);
}

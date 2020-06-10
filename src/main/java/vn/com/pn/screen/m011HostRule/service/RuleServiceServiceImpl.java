package vn.com.pn.screen.m011HostRule.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.screen.m011HostRule.dto.HostRuleDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m011HostRule.entity.Rule;
import vn.com.pn.screen.m011HostRule.repository.RuleRepository;
import vn.com.pn.screen.m002Host.service.HostServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleServiceServiceImpl implements RuleService{
    private static Log logger = LogFactory.getLog(HostServiceImpl.class);

    @Autowired
    private RuleRepository ruleRepository;

    @Override
    public BaseOutput getAll() {
        logger.info("RuleServiceServiceImpl.getAll");
        List<Object> listRules = new ArrayList<Object>(ruleRepository.findAll());
        return CommonFunction.successOutput(listRules);
    }

    @Override
    public BaseOutput insert(HostRuleDTO hostRuleDTO) {
        logger.info("RuleServiceServiceImpl.insert");
        try {
            Rule rule = new Rule();
            if (hostRuleDTO.getName() != null && hostRuleDTO.getName() != ""){
                rule.setName(hostRuleDTO.getName());
            }
            return CommonFunction.successOutput(ruleRepository.save(rule));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}

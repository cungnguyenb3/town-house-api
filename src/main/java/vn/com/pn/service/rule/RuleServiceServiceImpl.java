package vn.com.pn.service.rule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.RuleDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.Language;
import vn.com.pn.domain.Rule;
import vn.com.pn.repository.rule.RuleRepository;
import vn.com.pn.service.host.HostServiceImpl;

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
    public BaseOutput insert(RuleDTO ruleDTO) {
        logger.info("RuleServiceServiceImpl.insert");
        try {
            Rule rule = new Rule();
            if (ruleDTO.getName() != null && ruleDTO.getName() != ""){
                rule.setName(ruleDTO.getName());
            }
            return CommonFunction.successOutput(ruleRepository.save(rule));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}

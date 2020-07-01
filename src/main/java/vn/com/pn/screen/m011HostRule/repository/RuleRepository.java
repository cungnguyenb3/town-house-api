package vn.com.pn.screen.m011HostRule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.pn.screen.m011HostRule.entity.Rule;

public interface RuleRepository extends JpaRepository<Rule, Long> {
    @Query(value = "SELECT 1 FROM rules LIMIT 1" , nativeQuery = true)
    Integer checkRuleIsEmpty();
}

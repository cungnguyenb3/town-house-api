package vn.com.pn.repository.rule;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.pn.domain.Rule;

public interface RuleRepository extends JpaRepository <Rule, Long> {
}

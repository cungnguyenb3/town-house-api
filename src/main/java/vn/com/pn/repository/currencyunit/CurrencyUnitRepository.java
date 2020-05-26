package vn.com.pn.repository.currencyunit;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.pn.domain.CurrencyUnit;

public interface CurrencyUnitRepository extends JpaRepository <CurrencyUnit, Long> {
}

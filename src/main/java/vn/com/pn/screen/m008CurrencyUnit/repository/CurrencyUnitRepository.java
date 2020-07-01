package vn.com.pn.screen.m008CurrencyUnit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.pn.screen.m008CurrencyUnit.entity.CurrencyUnit;

public interface CurrencyUnitRepository extends JpaRepository<CurrencyUnit, Long> {
    @Query(value = "SELECT 1 FROM currency_units LIMIT 1" , nativeQuery = true)
    Integer checkCurrencyUnitIsEmpty();
}

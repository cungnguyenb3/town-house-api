package vn.com.pn.screen.m012Language.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.pn.screen.m012Language.entity.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    @Query(value = "SELECT 1 FROM languages LIMIT 1" , nativeQuery = true)
    Integer checkLanguageIsEmpty();
}

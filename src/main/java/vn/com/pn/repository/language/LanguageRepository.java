package vn.com.pn.repository.language;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.pn.domain.Language;

public interface LanguageRepository extends JpaRepository <Language, Long> {
}

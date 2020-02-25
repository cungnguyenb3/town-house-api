package vn.com.pn.repository.hostcategory;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.pn.domain.HostCategory;

public interface HostCategoryRepository extends JpaRepository <HostCategory, Integer > {
}

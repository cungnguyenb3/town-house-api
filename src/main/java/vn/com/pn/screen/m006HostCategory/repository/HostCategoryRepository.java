package vn.com.pn.screen.m006HostCategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.pn.screen.m006HostCategory.entity.HostCategory;

public interface HostCategoryRepository extends JpaRepository<HostCategory, Long> {
    @Query(value = "SELECT 1 FROM host_categories LIMIT 1" , nativeQuery = true)
    Integer checkHostCategoryIsEmpty();
}

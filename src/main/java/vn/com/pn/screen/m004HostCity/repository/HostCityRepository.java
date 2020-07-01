package vn.com.pn.screen.m004HostCity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.pn.screen.m004HostCity.entity.HostCity;

public interface HostCityRepository extends JpaRepository<HostCity, Long> {
    @Query(value = "SELECT 1 FROM host_cities LIMIT 1" , nativeQuery = true)
    Integer checkHostCityIsEmpty();
}

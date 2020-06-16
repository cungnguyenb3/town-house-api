package vn.com.pn.screen.m002Host.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.pn.screen.m002Host.entity.Host;

import javax.transaction.Transactional;

@Repository
public interface HostRepository extends JpaRepository <Host, Long>,
    PagingAndSortingRepository<Host, Long> {

    @Query(value = "SELECT * FROM hosts WHERE city_id = :city_id",
            nativeQuery = true)
    Page<Host> findByHostCityId (@Param("city_id") Long hostCity, Pageable pageable);

    @Query(value = "SELECT * FROM hosts WHERE status = true",
            nativeQuery = true)
    Page<Host> getAllHost(Pageable pageable);
}

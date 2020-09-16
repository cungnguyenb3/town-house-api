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
import java.util.List;

@Repository
public interface HostRepository extends JpaRepository<Host, Long>,
        PagingAndSortingRepository<Host, Long> {

    @Query(value = "SELECT * FROM hosts WHERE city_id = :city_id",
            nativeQuery = true)
    Page<Host> findByHostCityId(@Param("city_id") Long hostCity, Pageable pageable);

    @Query(value = "SELECT * FROM hosts WHERE status = true ORDER BY created_at DESC",
            nativeQuery = true)
    Page<Host> getAllHost(Pageable pageable);

    @Query(value = "SELECT * FROM hosts WHERE status = true and agent_id = :id ORDER BY created_at DESC",
            nativeQuery = true)
    Page<Host> getHostByUser(@Param("id") Long id, Pageable pageable);

    @Query(value = "SELECT * FROM hosts where stars >= 4", nativeQuery = true)
    List<Host> getHostRecommendation();

    @Query(value = "SELECT * FROM hosts WHERE status = true and agent_id = :id ORDER BY created_at DESC",
            nativeQuery = true)
    List<Host> getHostByUser(@Param("id") Long id);

}



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

    @Modifying
    @Transactional
    @Query(value = "DELETE from hosts WHERE host_id = :host_id ", nativeQuery = true)
    void deleteHostByHostId(@Param("host_id") Long id);


//    @Modifying
//    @Transactional
//    @Query(value = "DELETE from languages_communications WHERE host_id = :host_id " +
//            "and language_id = :language_id", nativeQuery = true)
//    void deleteLanguageCommunicate(@Param("host_id") Long hostId,
//                                   @Param("language_id") Long languageId);
//
//    @Modifying
//    @Transactional
//    @Query(value = "DELETE from hosts_rules WHERE host_id = :host_id " +
//            "and rule_id = :rule_id", nativeQuery = true)
//    void deleteRules(@Param("host_id") Long hostId,
//                     @Param("rule_id") Long rule_id);

}

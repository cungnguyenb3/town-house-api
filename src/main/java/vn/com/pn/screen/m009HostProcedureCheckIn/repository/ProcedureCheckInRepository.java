package vn.com.pn.screen.m009HostProcedureCheckIn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.pn.screen.m009HostProcedureCheckIn.entity.ProcedureCheckIn;

public interface ProcedureCheckInRepository extends JpaRepository<ProcedureCheckIn, Long> {
    @Query(value = "SELECT 1 FROM procedures_check_in LIMIT 1" , nativeQuery = true)
    Integer checkProcedureCheckInIsEmpty();
}

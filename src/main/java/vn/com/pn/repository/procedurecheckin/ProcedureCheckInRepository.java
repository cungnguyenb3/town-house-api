package vn.com.pn.repository.procedurecheckin;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.pn.domain.ProcedureCheckIn;

public interface ProcedureCheckInRepository extends JpaRepository<ProcedureCheckIn, Integer> {
}

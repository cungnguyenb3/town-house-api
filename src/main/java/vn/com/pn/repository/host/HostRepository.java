package vn.com.pn.repository.host;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import vn.com.pn.domain.Host;

@Repository
public interface HostRepository extends JpaRepository <Host, Long>,
        PagingAndSortingRepository<Host, Long> {
}

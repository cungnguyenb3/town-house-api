package vn.com.pn.repository.host;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.pn.domain.Host;

public interface HostRepository extends JpaRepository <Host, Integer> {
}

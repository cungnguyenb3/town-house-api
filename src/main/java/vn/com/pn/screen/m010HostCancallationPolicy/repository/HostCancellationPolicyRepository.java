package vn.com.pn.screen.m010HostCancallationPolicy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.pn.screen.m010HostCancallationPolicy.entity.HostCancellationPolicy;

public interface HostCancellationPolicyRepository extends JpaRepository<HostCancellationPolicy, Long> {
    @Query(value = "SELECT 1 FROM host_cancellation_policies LIMIT 1" , nativeQuery = true)
    Integer checkHostCancellationPolicyIsEmpty();
}

package vn.com.pn.screen.m013Momo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.pn.screen.m013Momo.entity.MomoFirstResponse;

import java.util.Optional;

public interface MomoFirstResponseRepository extends JpaRepository<MomoFirstResponse, Long> {
    Optional<MomoFirstResponse> findByTransid(String transid);
}

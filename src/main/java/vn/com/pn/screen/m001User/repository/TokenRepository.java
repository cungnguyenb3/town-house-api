package vn.com.pn.screen.m001User.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.pn.screen.m001User.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
}

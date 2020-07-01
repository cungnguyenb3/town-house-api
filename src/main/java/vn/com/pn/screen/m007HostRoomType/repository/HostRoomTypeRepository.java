package vn.com.pn.screen.m007HostRoomType.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.com.pn.screen.m007HostRoomType.entity.HostRoomType;

public interface HostRoomTypeRepository extends JpaRepository<HostRoomType, Long> {
    @Query(value = "SELECT 1 FROM host_room_types LIMIT 1" , nativeQuery = true)
    Integer checkHostRoomTypeIsEmpty();
}

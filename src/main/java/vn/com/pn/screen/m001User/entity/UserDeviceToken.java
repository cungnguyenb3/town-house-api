package vn.com.pn.screen.m001User.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "device_token")
@NoArgsConstructor
public class UserDeviceToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceToken;

    public UserDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @JsonIgnore
    @ManyToOne (cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;
}

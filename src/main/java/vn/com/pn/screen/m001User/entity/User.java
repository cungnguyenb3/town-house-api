package vn.com.pn.screen.m001User.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.search.annotations.Indexed;
import vn.com.pn.screen.m002Host.entity.Host;
import vn.com.pn.screen.m003Role.entity.Role;

@SuppressWarnings("serial")
@Getter
@Setter
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String username;

    @Email
    @Column(nullable = false)
    private String email;

    @NotBlank
    private String phone;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    private String national;

    private String gender;

    @NotBlank
    @Column(nullable = false)
    private String password;

    private boolean isEnable;

    private boolean status;

    @OneToMany(mappedBy="user",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserDeviceToken> deviceTokens = new HashSet<UserDeviceToken>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "host_wishlists",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "host_id"))
    private Set<Host> hosts = new HashSet<>();


}

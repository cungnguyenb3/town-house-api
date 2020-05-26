package vn.com.pn.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@Component
@Getter
@Setter
@Entity
@Table(name = "forgot_password_code")
public class ForgotPasswordCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String code;
    private boolean isUsed;
    private Date createDate;
    private Date expirationDate;
}

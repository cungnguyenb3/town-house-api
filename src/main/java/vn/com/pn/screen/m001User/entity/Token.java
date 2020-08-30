package vn.com.pn.screen.m001User.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tokenId;

    private Date expiredDate;

    @JsonIgnore
    @ManyToOne (cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;
}

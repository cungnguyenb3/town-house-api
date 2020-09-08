package vn.com.pn.screen.m013Momo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class MomoBasicRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private String message;
    @Lob
    private String data;
    private String phoneNumber;
    private String bookingCode;
    private String amount;
}

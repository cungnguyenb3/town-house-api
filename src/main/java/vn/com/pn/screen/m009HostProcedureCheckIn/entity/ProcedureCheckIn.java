package vn.com.pn.screen.m009HostProcedureCheckIn.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "procedures_check_in")
@Entity
public class ProcedureCheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}

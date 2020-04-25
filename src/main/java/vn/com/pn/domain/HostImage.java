package vn.com.pn.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "host_images")
public class HostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fileName;

    private String fileType;

    @Lob
    private byte[] data;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "host_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Host host;
}

package vn.com.pn.screen.m005HostImage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import vn.com.pn.screen.m002Host.entity.Host;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "host_images")
public class HostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileSize;

    private String fileType;

    private String webContentLink;

    private String webViewLink;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "host_id", referencedColumnName = "id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Host host;
}

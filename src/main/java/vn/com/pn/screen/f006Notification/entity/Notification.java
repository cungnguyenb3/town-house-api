package vn.com.pn.screen.f006Notification.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import vn.com.pn.screen.m001User.entity.User;
import vn.com.pn.utils.DateAuditUtil;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notification extends DateAuditUtil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private boolean isRead;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Notification(String title, String content, boolean isRead) {
        this.title = title;
        this.content = content;
        this.isRead = isRead;
    }

    @PrePersist
    public void autoGenerateValueCreatedAt() {
        this.setCreatedAt(new Date());
    }
}

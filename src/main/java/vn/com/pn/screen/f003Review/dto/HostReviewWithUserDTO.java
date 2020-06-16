package vn.com.pn.screen.f003Review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HostReviewWithUserDTO {
    private Long id;
    private String fullName;
    private String content;
    private Integer startsRating;
    private Date createAt;
}

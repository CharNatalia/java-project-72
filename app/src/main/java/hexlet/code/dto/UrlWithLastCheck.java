package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UrlWithLastCheck {
    private Long urlId;
    private String name;
    private LocalDateTime createdAt;
    private Integer status;
}

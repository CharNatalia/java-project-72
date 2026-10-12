package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class LastUrlsChecks {
    private Long urlId;
    private String name;
    private LocalDateTime createdAt;
    private Integer status;
}

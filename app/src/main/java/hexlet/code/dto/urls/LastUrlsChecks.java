package hexlet.code.dto.urls;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class LastUrlsChecks {
    private Long urlId;
    private String url;
    private LocalDateTime createdAt;
    private int status;
}

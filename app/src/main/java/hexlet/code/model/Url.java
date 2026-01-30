package hexlet.code.model;

import java.time.LocalDateTime;

public class Url {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public Url(String name) {
        this.name = name;
    }
}

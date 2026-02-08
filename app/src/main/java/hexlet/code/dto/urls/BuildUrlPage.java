package hexlet.code.dto.urls;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BuildUrlPage {
    private Map<String, String> errors;
}

package hexlet.code.dto.urls;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class BuildUrlsPage extends BasePage {
    private List<LastUrlsChecks> urls;
}

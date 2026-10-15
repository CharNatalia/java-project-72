package hexlet.code.dto.urls;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.UrlWithLastCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UrlsPage extends BasePage {
    private List<UrlWithLastCheck> urlWithLastUrlsChecks;
}

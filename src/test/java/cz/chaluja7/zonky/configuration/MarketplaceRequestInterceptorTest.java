package cz.chaluja7.zonky.configuration;

import feign.RequestTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ResultOfMethodCallIgnored")
@RunWith(MockitoJUnitRunner.class)
public class MarketplaceRequestInterceptorTest {

    private static final String SOME_USER_AGENT_HEADER_VALUE = "XXX";

    private RequestTemplate template;

    private final MarketplaceRequestInterceptor marketplaceRequestInterceptor
            = new MarketplaceRequestInterceptor(SOME_USER_AGENT_HEADER_VALUE);

    @Before
    public void before() {
        template = new RequestTemplate();
    }

    @Test
    public void shouldSetNotPresentedHeaders() {
        assertThat(template.headers())
                .doesNotContainKey(HttpHeaders.USER_AGENT);

        assertThat(template.headers())
                .doesNotContainKey(HttpHeaders.ACCEPT);

        assertThat(template.headers())
                .doesNotContainKey(HttpHeaders.CONTENT_TYPE);

        marketplaceRequestInterceptor.apply(template);

        assertThat(template.headers())
                .containsKey(HttpHeaders.USER_AGENT);

        assertThat(Objects.requireNonNull(template.headers().get(HttpHeaders.USER_AGENT)).iterator().next())
                .isEqualTo(SOME_USER_AGENT_HEADER_VALUE);

        assertThat(template.headers())
                .containsKey(HttpHeaders.ACCEPT);

        assertThat(Objects.requireNonNull(template.headers().get(HttpHeaders.ACCEPT)).iterator().next())
                .isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        assertThat(template.headers())
                .containsKey(HttpHeaders.CONTENT_TYPE);

        assertThat(Objects.requireNonNull(template.headers().get(HttpHeaders.CONTENT_TYPE)).iterator().next())
                .isEqualTo(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    public void shouldNotSetAlreadyPresentedHeaders() {
        template.header(HttpHeaders.USER_AGENT, Collections.singletonList("A"));
        template.header(HttpHeaders.ACCEPT, Collections.singletonList("B"));
        template.header(HttpHeaders.CONTENT_TYPE, Collections.singletonList("C"));

        marketplaceRequestInterceptor.apply(template);

        assertThat(Objects.requireNonNull(template.headers().get(HttpHeaders.USER_AGENT)).iterator().next())
                .isEqualTo("A");

        assertThat(Objects.requireNonNull(template.headers().get(HttpHeaders.ACCEPT)).iterator().next())
                .isEqualTo("B");

        assertThat(Objects.requireNonNull(template.headers().get(HttpHeaders.CONTENT_TYPE)).iterator().next())
                .isEqualTo("C");
    }

}

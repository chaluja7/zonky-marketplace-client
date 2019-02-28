package cz.chaluja7.zonky;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import cz.chaluja7.zonky.configuration.properties.ZonkyApiProperties;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public abstract class IntegrationBase {

    private static final String HEADER_PAGE = "X-Page";
    private static final String HEADER_SIZE = "X-Size";
    private static final String HEADER_ORDER = "X-Order";
    protected static final String HEADER_TOTAL = "X-Total";

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(wireMockConfig().port(5051));

    @Autowired
    private ZonkyApiProperties zonkyApiProperties;

    protected void prepareStubForMarketplaceLoans(int page, int size, ZonedDateTime fromDateTime, int total, String file) {
        MappingBuilder mappingBuilder = get(urlPathMatching("/loans/marketplace"));
        mappingBuilder = mappingBuilder.withHeader(HttpHeaders.ACCEPT, equalTo(MediaType.APPLICATION_JSON_VALUE));
        mappingBuilder = mappingBuilder.withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE));
        mappingBuilder = mappingBuilder.withHeader(HttpHeaders.USER_AGENT, equalTo(zonkyApiProperties.getUserAgent()));
        mappingBuilder = mappingBuilder.withHeader(HEADER_PAGE, equalTo(String.valueOf(page)));
        mappingBuilder = mappingBuilder.withHeader(HEADER_SIZE, equalTo(String.valueOf(size)));
        mappingBuilder = mappingBuilder.withHeader(HEADER_ORDER, equalTo("datePublished"));
        if (fromDateTime != null) {
            mappingBuilder = mappingBuilder.withQueryParam("datePublished__gt", equalTo(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(fromDateTime)));
        }

        stubFor(mappingBuilder
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withHeader(HEADER_TOTAL, String.valueOf(total))
                        .withBodyFile(file)));
    }

}

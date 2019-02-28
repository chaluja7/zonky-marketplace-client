package cz.chaluja7.zonky.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class MarketplaceRequestInterceptor implements RequestInterceptor {

    private final String userAgentHeaderValue;

    MarketplaceRequestInterceptor(String userAgentHeaderValue) {
        this.userAgentHeaderValue = userAgentHeaderValue;
    }

    @Override
    public void apply(RequestTemplate template) {
        addHeaderIfNotSetAlready(template, HttpHeaders.USER_AGENT, userAgentHeaderValue);
        addHeaderIfNotSetAlready(template, HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        addHeaderIfNotSetAlready(template, HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        handleQueries(template);
    }

    private void addHeaderIfNotSetAlready(RequestTemplate template, String name, String value) {
        if (!template.headers().containsKey(name)) {
            template.header(name, value);
        }
    }

    // TODO there is currently a bug within feign client implementation, see https://github.com/spring-cloud/spring-cloud-openfeign/issues/128
    // TODO '+'s are not url encoded within query parameters, this fix is therefore needed (but ugly - remove it after feign is working again)
    private void handleQueries(RequestTemplate template) {
        final Map<String, Collection<String>> queries = new HashMap<>();
        template.queries().forEach((key, value) -> {
            List<String> list = value.stream()
                    .map(val -> val.replaceAll("\\+", "%2B"))
                    .collect(Collectors.toCollection(LinkedList::new));
            queries.put(key, list);
        });

        template.queries(Collections.emptyMap());
        template.queries(queries);
    }

}

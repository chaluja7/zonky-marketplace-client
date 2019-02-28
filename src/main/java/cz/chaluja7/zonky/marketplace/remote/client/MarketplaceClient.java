package cz.chaluja7.zonky.marketplace.remote.client;

import cz.chaluja7.zonky.marketplace.remote.data.LoanDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.OffsetDateTime;
import java.util.List;

@FeignClient(value = "marketplace", url = "${zonky.api.base-url}/loans")
public interface MarketplaceClient {

    @GetMapping("/marketplace")
    ResponseEntity<List<LoanDto>> getLoans(
            @RequestHeader("X-Page") int page, @RequestHeader("X-Size") int pageSize, @RequestHeader("X-Order") String order);

    @GetMapping("/marketplace")
    ResponseEntity<List<LoanDto>> getLoansAfter(
            @RequestHeader("X-Page") int page, @RequestHeader("X-Size") int pageSize, @RequestHeader("X-Order") String order,
            @RequestParam("datePublished__gt") OffsetDateTime publishedAfter);

}

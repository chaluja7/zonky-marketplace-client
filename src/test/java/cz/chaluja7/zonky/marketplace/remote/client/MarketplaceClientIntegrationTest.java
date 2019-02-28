package cz.chaluja7.zonky.marketplace.remote.client;

import cz.chaluja7.zonky.IntegrationBase;
import cz.chaluja7.zonky.marketplace.remote.data.LoanDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class MarketplaceClientIntegrationTest extends IntegrationBase {

    @Autowired
    private MarketplaceClient marketplaceClient;

    @Test
    public void shouldRetrieveEmptyLoanResponse() {
        prepareStubForMarketplaceLoans(0, 1, null, 0, "marketplace/emptyLoanData.json");

        ResponseEntity<List<LoanDto>> responseEntity = marketplaceClient.getLoans(0, 1, "datePublished");

        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(responseEntity.getHeaders().get(HEADER_TOTAL)).get(0))
                .isEqualTo("0");
        assertThat(responseEntity.getBody())
                .isEmpty();
    }

    @Test
    public void shouldRetrieveSingleLoanResponse() {
        prepareStubForMarketplaceLoans(0, 1, null, 1, "marketplace/singleLoanData.json");

        ResponseEntity<List<LoanDto>> responseEntity = marketplaceClient.getLoans(0, 1, "datePublished");

        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(responseEntity.getHeaders().get(HEADER_TOTAL)).get(0))
                .isEqualTo("1");
        assertThat(responseEntity.getBody())
                .isNotNull();
        assertThat(responseEntity.getBody().size())
                .isEqualTo(1);


        LoanDto loanDto = responseEntity.getBody().get(0);
        assertThat(loanDto.getId())
                .isEqualTo(1L);
        assertThat(loanDto.getUrl())
                .isEqualTo("https://app.zonky.cz/loan/1");
        assertThat(loanDto.getName())
                .isEqualTo("Loan refinancing");
        assertThat(loanDto.getStory())
                .isEqualTo("Dear investors, ...");
        assertThat(loanDto.getPurpose())
                .isEqualTo("6");
        assertThat(loanDto.getTermInMonths())
                .isEqualTo(42);
        assertThat(loanDto.getInterestRate())
                .isEqualByComparingTo(BigDecimal.valueOf(0.0599));
        assertThat(loanDto.getRevenueRate())
                .isEqualByComparingTo(BigDecimal.valueOf(0.0499));
        assertThat(loanDto.getAnnuityWithInsurance())
                .isEqualByComparingTo(BigDecimal.valueOf(2543));
        assertThat(loanDto.getRating())
                .isEqualTo("AAA");
        assertThat(loanDto.getTopped())
                .isNull();
        assertThat(loanDto.getAmount())
                .isEqualByComparingTo(BigDecimal.valueOf(200000));
        assertThat(loanDto.getRemainingInvestment())
                .isEqualByComparingTo(BigDecimal.valueOf(152600));
        assertThat(loanDto.getReservedAmount())
                .isEqualByComparingTo(BigDecimal.valueOf(1000));
        assertThat(loanDto.getInvestmentRate())
                .isEqualByComparingTo(BigDecimal.valueOf(0.237));
        assertThat(loanDto.getCovered())
                .isFalse();
        assertThat(loanDto.getDatePublished())
                .isEqualTo(ZonedDateTime.parse("2016-04-19T18:25:41.208+02:00"));
        assertThat(loanDto.getPublished())
                .isTrue();
        assertThat(loanDto.getDeadline())
                .isEqualTo(ZonedDateTime.parse("2016-04-26T18:23:53.101+02:00"));
        assertThat(loanDto.getInvestmentsCount())
                .isEqualTo(72);
        assertThat(loanDto.getQuestionsCount())
                .isEqualTo(3);
        assertThat(loanDto.getRegion())
                .isEqualTo("6");
        assertThat(loanDto.getMainIncomeType())
                .isEqualTo("EMPLOYMENT");
        assertThat(loanDto.getInsuranceActive())
                .isTrue();

        assertThat(loanDto.getPhotos())
                .isNotEmpty();
        assertThat(loanDto.getPhotos().size())
                .isEqualTo(1);
        assertThat(loanDto.getPhotos().get(0).getName())
                .isEqualTo("6");
        assertThat(loanDto.getPhotos().get(0).getUrl())
                .isEqualTo("/loans/31959/photos/1987");

        assertThat(loanDto.getInsuranceHistory())
                .isNotEmpty();
        assertThat(loanDto.getInsuranceHistory().size())
                .isEqualTo(1);
        assertThat(loanDto.getInsuranceHistory().get(0).getPolicyPeriodFrom())
                .isEqualTo(LocalDate.parse("2016-04-18"));
        assertThat(loanDto.getInsuranceHistory().get(0).getPolicyPeriodTo())
                .isEqualTo(LocalDate.parse("2016-04-19"));
    }

    @Test
    public void shouldRetrieveMultipleLoansResponse() {
        prepareStubForMarketplaceLoans(0, 2, null, 2, "marketplace/multipleLoansData.json");

        ResponseEntity<List<LoanDto>> responseEntity = marketplaceClient.getLoans(0, 2, "datePublished");

        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(responseEntity.getHeaders().get(HEADER_TOTAL)).get(0))
                .isEqualTo("2");
        assertThat(responseEntity.getBody())
                .isNotNull();
        assertThat(responseEntity.getBody().size())
                .isEqualTo(2);

        assertThat(responseEntity.getBody().get(0).getId())
                .isEqualTo(1L);
        assertThat(responseEntity.getBody().get(1).getId())
                .isEqualTo(2L);
    }

    @Test
    public void shouldRetrieveMultipleLoansResponseFromDateTime() {
        final ZonedDateTime now = ZonedDateTime.now();
        prepareStubForMarketplaceLoans(0, 2, now, 2, "marketplace/multipleLoansData.json");


        ResponseEntity<List<LoanDto>> responseEntity = marketplaceClient.getLoansAfter(0, 2,
                "datePublished", now.toOffsetDateTime());

        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(responseEntity.getHeaders().get(HEADER_TOTAL)).get(0))
                .isEqualTo("2");
        assertThat(responseEntity.getBody())
                .isNotNull();
        assertThat(responseEntity.getBody().size())
                .isEqualTo(2);

        assertThat(responseEntity.getBody().get(0).getId())
                .isEqualTo(1L);
        assertThat(responseEntity.getBody().get(1).getId())
                .isEqualTo(2L);
    }

}
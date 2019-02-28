package cz.chaluja7.zonky.marketplace.remote;

import cz.chaluja7.zonky.IntegrationBase;
import cz.chaluja7.zonky.marketplace.job.JobKey;
import cz.chaluja7.zonky.marketplace.processor.LoanProcessor;
import cz.chaluja7.zonky.marketplace.remote.data.LoanDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class MarketPlaceServiceIntegrationTest extends IntegrationBase {

    private static List<LoanDto> processedTestingLoansList;

    @Autowired
    private MarketplaceService marketplaceService;

    @Autowired
    private MarketplaceInfoService marketplaceInfoService;

    @Before
    public void before() {
        processedTestingLoansList = new ArrayList<>();
    }

    @After
    public void after() {
        processedTestingLoansList.clear();
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldProcessTwoBatchRuns() {
        // first run - process all
        prepareStubForMarketplaceLoans(0, 1, null, 2, "marketplace/singleLoanData.json");
        prepareStubForMarketplaceLoans(1, 1, null, 2, "marketplace/singleLoanOtherData.json");

        assertThat(marketplaceInfoService.getLastProcessedRecordTime(JobKey.PERIODIC))
                .isNotPresent();

        marketplaceService.processNotYetProcessedMarketplaceLoans(JobKey.PERIODIC);

        assertThat(processedTestingLoansList.size())
                .isEqualTo(2);
        assertThat(processedTestingLoansList.get(0).getId())
                .isEqualTo(1L);
        assertThat(processedTestingLoansList.get(1).getId())
                .isEqualTo(2L);

        Optional<ZonedDateTime> lastProcessedRecordTime = marketplaceInfoService.getLastProcessedRecordTime(JobKey.PERIODIC);
        assertThat(lastProcessedRecordTime)
                .isPresent();

        processedTestingLoansList.clear();

        // second run - process after some dateTime
        prepareStubForMarketplaceLoans(0, 1, lastProcessedRecordTime.get(), 1, "marketplace/singleLoanData.json");

        marketplaceService.processNotYetProcessedMarketplaceLoans(JobKey.PERIODIC);

        assertThat(processedTestingLoansList.size())
                .isEqualTo(1);
        assertThat(processedTestingLoansList.get(0).getId())
                .isEqualTo(1L);

        assertThat(marketplaceInfoService.getLastProcessedRecordTime(JobKey.PERIODIC))
                .isPresent();
        assertThat(marketplaceInfoService.getLastProcessedRecordTime(JobKey.PERIODIC))
                .isNotEqualTo(lastProcessedRecordTime);
    }

    @TestConfiguration
    static class MarketPlaceServiceIntegrationTestConfig {

        @Bean
        public LoanProcessor testingLoanProcessor() {
            return loans -> processedTestingLoansList.addAll(loans);
        }

    }

}

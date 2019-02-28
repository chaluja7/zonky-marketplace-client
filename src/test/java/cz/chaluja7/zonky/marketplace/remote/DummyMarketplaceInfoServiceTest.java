package cz.chaluja7.zonky.marketplace.remote;

import cz.chaluja7.zonky.marketplace.job.JobKey;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DummyMarketplaceInfoServiceTest {

    private DummyMarketplaceInfoService dummyMarketplaceInfoService = new DummyMarketplaceInfoService();

    @Before
    public void before() {
        DummyMarketplaceInfoService.LAST_PROCESSED_RECORDS_MAP.clear();
    }

    @Test
    public void shouldNotRetrieveNonExisting() {
        assertThat(dummyMarketplaceInfoService.getLastProcessedRecordTime(JobKey.PERIODIC))
                .isNotPresent();
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldStoreAndRetrieve() {
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime inTheFuture = ZonedDateTime.now().plusMinutes(1);

        assertThat(dummyMarketplaceInfoService.getLastProcessedRecordTime(JobKey.PERIODIC))
                .isNotPresent();

        dummyMarketplaceInfoService.putLastProcessedRecordTime(JobKey.PERIODIC, now);

        assertThat(dummyMarketplaceInfoService.getLastProcessedRecordTime(JobKey.PERIODIC))
                .isPresent();

        assertThat(dummyMarketplaceInfoService.getLastProcessedRecordTime(JobKey.PERIODIC).get())
                .isEqualTo(now);

        dummyMarketplaceInfoService.putLastProcessedRecordTime(JobKey.PERIODIC, inTheFuture);

        assertThat(dummyMarketplaceInfoService.getLastProcessedRecordTime(JobKey.PERIODIC))
                .isPresent();

        assertThat(dummyMarketplaceInfoService.getLastProcessedRecordTime(JobKey.PERIODIC).get())
                .isEqualTo(inTheFuture);
    }

}

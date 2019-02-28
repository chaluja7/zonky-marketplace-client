package cz.chaluja7.zonky.marketplace.job;

import cz.chaluja7.zonky.marketplace.remote.MarketplaceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class MarketplaceScheduledTrackerTest {

    @Mock
    private MarketplaceService marketplaceServiceMock;

    @InjectMocks
    private MarketplaceScheduledTracker marketplaceScheduledTracker;

    @Test
    public void shouldProcessNewMarketplaceLoans() {
        doNothing().when(marketplaceServiceMock)
                .processNotYetProcessedMarketplaceLoans(JobKey.PERIODIC);

        marketplaceScheduledTracker.processNewMarketplaceLoans();

        verify(marketplaceServiceMock, times(1))
                .processNotYetProcessedMarketplaceLoans(JobKey.PERIODIC);

        verifyNoMoreInteractions(marketplaceServiceMock);
    }

}

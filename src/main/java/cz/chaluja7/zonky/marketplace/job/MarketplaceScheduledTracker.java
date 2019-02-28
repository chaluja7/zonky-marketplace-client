package cz.chaluja7.zonky.marketplace.job;

import cz.chaluja7.zonky.marketplace.remote.MarketplaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MarketplaceScheduledTracker {

    private static final Logger logger = LoggerFactory.getLogger(MarketplaceScheduledTracker.class);

    private final MarketplaceService marketplaceService;

    public MarketplaceScheduledTracker(MarketplaceService marketplaceService) {
        this.marketplaceService = marketplaceService;
    }

    @Scheduled(fixedRate = 5 * 60 * 1000, initialDelay = 10 * 1000)
    public void processNewMarketplaceLoans() {
        logger.info("start processing not yet processed marketplace loans by job {}", JobKey.PERIODIC);
        marketplaceService.processNotYetProcessedMarketplaceLoans(JobKey.PERIODIC);
        logger.info("end processing not yet processed marketplace loans by job {}", JobKey.PERIODIC);
    }

}

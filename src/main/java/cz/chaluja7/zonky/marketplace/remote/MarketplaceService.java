package cz.chaluja7.zonky.marketplace.remote;

import cz.chaluja7.zonky.marketplace.job.JobKey;
import org.springframework.lang.NonNull;

public interface MarketplaceService {

    void processNotYetProcessedMarketplaceLoans(@NonNull JobKey jobKey);

}

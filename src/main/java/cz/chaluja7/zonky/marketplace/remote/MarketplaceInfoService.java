package cz.chaluja7.zonky.marketplace.remote;

import cz.chaluja7.zonky.marketplace.job.JobKey;
import org.springframework.lang.NonNull;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface MarketplaceInfoService {

    void putLastProcessedRecordTime(@NonNull JobKey key, @NonNull ZonedDateTime zonedDateTime);

    Optional<ZonedDateTime> getLastProcessedRecordTime(@NonNull JobKey key);

}

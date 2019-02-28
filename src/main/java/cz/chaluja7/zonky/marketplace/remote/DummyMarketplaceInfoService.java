package cz.chaluja7.zonky.marketplace.remote;

import cz.chaluja7.zonky.marketplace.job.JobKey;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dummy implementation of MarketplaceInfoService. It enables to store & retrieve time of last successfully retrieved
 * loan from marketplace, so other retrieval can continue just right there. Without this information it can not be guaranteed
 * that some loans are not processed repeatedly or skipped at all.
 *
 * In the real environment the logic should be different - data should be stored in DB & file etc and concurrency should be handled more carefully.
 * For now this implementation will be acceptable for one instance only and is valid for one application lifecycle only.
 * After the app is restarted, everything is forbidden and in the first batch run, all marketplace loans are processed.
 */
@Service
class DummyMarketplaceInfoService implements MarketplaceInfoService {

    static final Map<JobKey, ZonedDateTime> LAST_PROCESSED_RECORDS_MAP = new ConcurrentHashMap<>();

    public void putLastProcessedRecordTime(@NonNull JobKey key, @NonNull ZonedDateTime zonedDateTime) {
        LAST_PROCESSED_RECORDS_MAP.put(key, zonedDateTime);
    }

    public Optional<ZonedDateTime> getLastProcessedRecordTime(@NonNull JobKey key) {
        return Optional.ofNullable(LAST_PROCESSED_RECORDS_MAP.get(key));
    }

}

package cz.chaluja7.zonky.marketplace.remote;

import cz.chaluja7.zonky.configuration.properties.ZonkyApiProperties;
import cz.chaluja7.zonky.marketplace.job.JobKey;
import cz.chaluja7.zonky.marketplace.processor.LoanProcessor;
import cz.chaluja7.zonky.marketplace.remote.client.MarketplaceClient;
import cz.chaluja7.zonky.marketplace.remote.data.LoanDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@EnableConfigurationProperties(ZonkyApiProperties.class)
class MarketplaceServiceImpl implements MarketplaceService {

    private static final Logger logger = LoggerFactory.getLogger(MarketplaceServiceImpl.class);

    static final String ORDER_FOR_BATCH = "datePublished";

    static final String HEADER_TOTAL_COUNT = "X-Total";

    private final MarketplaceClient marketplaceClient;

    private final MarketplaceInfoService marketplaceInfoService;

    private final ZonkyApiProperties zonkyApiProperties;

    private final List<LoanProcessor> loanProcessors;

    public MarketplaceServiceImpl(MarketplaceClient marketplaceClient,
                                  MarketplaceInfoService marketplaceInfoService,
                                  ZonkyApiProperties zonkyApiProperties,
                                  List<LoanProcessor> loanProcessors) {
        this.marketplaceClient = marketplaceClient;
        this.marketplaceInfoService = marketplaceInfoService;
        this.zonkyApiProperties = zonkyApiProperties;
        this.loanProcessors = loanProcessors;
    }

    @Override
    public void processNotYetProcessedMarketplaceLoans(@NonNull JobKey jobKey) {
        Optional<ZonedDateTime> lastProcessedRecordTimeBefore = marketplaceInfoService.getLastProcessedRecordTime(jobKey);
        logger.info("loans after {} are about to be processed", lastProcessedRecordTimeBefore.orElse(null));

        Optional<ZonedDateTime> lastProcessedRecordTimeAfter = processMarketplaceLoans(lastProcessedRecordTimeBefore.orElse(null));

        if (lastProcessedRecordTimeAfter.isPresent()) {
            logger.info("loans have been processed up to {}", lastProcessedRecordTimeAfter.get());
            marketplaceInfoService.putLastProcessedRecordTime(jobKey, lastProcessedRecordTimeAfter.get());
        } else {
            logger.info("no new loans to be processed were available");
        }
    }

    private Optional<ZonedDateTime> processMarketplaceLoans(@Nullable ZonedDateTime publishedAfter) {
        Integer totalNumberOfRecords = null;
        int numberOfProcessedRecords = 0;
        int page = 0;
        ZonedDateTime lastlyProcessedLoanPublishedDate = null;
        ResponseEntity<List<LoanDto>> response;
        do {
            if (publishedAfter == null) {
                // error response should be handled by properly configured hystrix in the real environment
                response = marketplaceClient.getLoans(page, zonkyApiProperties.getBatchPageSize(), ORDER_FOR_BATCH);
            } else {
                response = marketplaceClient.getLoansAfter(page, zonkyApiProperties.getBatchPageSize(), ORDER_FOR_BATCH, publishedAfter.toOffsetDateTime());
            }

            if (response.getBody() != null && !response.getBody().isEmpty()) {
                processLoans(response.getBody());
                numberOfProcessedRecords += response.getBody().size();
                lastlyProcessedLoanPublishedDate = response.getBody().get(response.getBody().size() - 1).getDatePublished();
                logger.info("processed {} records for now from marketplace in batch", numberOfProcessedRecords);
            }

            totalNumberOfRecords = handleTotalNumberOfRecords(totalNumberOfRecords, getTotalNumberOfRecords(response));
            page++;
        } while (numberOfProcessedRecords < totalNumberOfRecords);

        return Optional.ofNullable(lastlyProcessedLoanPublishedDate);
    }

    void processLoans(@NonNull List<LoanDto> loans) {
        loanProcessors.forEach(loanProcessor -> loanProcessor.process(loans));
    }

    int getTotalNumberOfRecords(@NonNull ResponseEntity responseEntity) {
        if (responseEntity.getHeaders().containsKey(HEADER_TOTAL_COUNT)) {
            return Integer.parseInt(Objects.requireNonNull(responseEntity.getHeaders().getFirst(HEADER_TOTAL_COUNT)));
        }

        throw new IllegalStateException("Must-have " + HEADER_TOTAL_COUNT + " header not present in response");
    }

    int handleTotalNumberOfRecords(@Nullable Integer previousTotalNumberOfRecords, int currentTotalNumberOfRecords) {
        if (previousTotalNumberOfRecords != null && !previousTotalNumberOfRecords.equals(currentTotalNumberOfRecords)) {
            // don't know how the marketplace works right now, but if loans can be removed, some loan could possibly be skipped
            // with respect to pagination. If that's true some improvements should be done here.
            logger.warn("total number of records has changed while processing");
        }

        return currentTotalNumberOfRecords;
    }

}

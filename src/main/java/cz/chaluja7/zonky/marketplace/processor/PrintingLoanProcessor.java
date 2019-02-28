package cz.chaluja7.zonky.marketplace.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.chaluja7.zonky.marketplace.remote.data.LoanDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Order(1)
class PrintingLoanProcessor implements LoanProcessor {

    private static final Logger logger = LoggerFactory.getLogger(PrintingLoanProcessor.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void process(@NonNull List<LoanDto> loans) {
        logger.info("start processing {} loans", loans.size());
        loans.forEach(loan -> {
            try {
                System.out.println(mapper.writeValueAsString(loan));
            } catch (JsonProcessingException e) {
                logger.error("error while unmarshalling loan", e);
            }
        });
        logger.info("end processing {} loans", loans.size());
    }

}

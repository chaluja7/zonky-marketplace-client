package cz.chaluja7.zonky.marketplace.processor;

import cz.chaluja7.zonky.marketplace.remote.data.LoanDto;
import org.springframework.lang.NonNull;

import java.util.Collections;
import java.util.List;

public interface LoanProcessor {

    @SuppressWarnings("unused")
    default void process(@NonNull LoanDto loan) {
        process(Collections.singletonList(loan));
    }

    void process(@NonNull List<LoanDto> loans);

}

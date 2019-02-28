package cz.chaluja7.zonky.marketplace.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.chaluja7.zonky.marketplace.remote.data.LoanDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class PrintingLoanProcessorTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private final PrintStream originalOut = System.out;

    private final PrintingLoanProcessor printingLoanProcessor = new PrintingLoanProcessor();

    @Before
    public void before() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void after() {
        System.setOut(originalOut);
    }

    @Test
    public void shouldPrintSingleLoan() throws JsonProcessingException {
        LoanDto loan = prepareLoan("XXX");

        assertThat(outContent.toString())
                .isEmpty();

        printingLoanProcessor.process(Collections.singletonList(loan));

        assertThat(outContent.toString())
                .contains(new ObjectMapper().writeValueAsString(loan));
    }

    @Test
    public void shouldPrintMultipleLoans() throws JsonProcessingException {
        LoanDto loan1 = prepareLoan("XXX");
        LoanDto loan2 = prepareLoan("YYY");

        assertThat(outContent.toString())
                .isEmpty();

        printingLoanProcessor.process(Arrays.asList(loan1, loan2));

        assertThat(outContent.toString())
                .contains(new ObjectMapper().writeValueAsString(loan1));

        assertThat(outContent.toString())
                .contains(new ObjectMapper().writeValueAsString(loan2));
    }

    private LoanDto prepareLoan(String name) {
        LoanDto loanDto = new LoanDto();
        loanDto.setName(name);

        return loanDto;
    }

}

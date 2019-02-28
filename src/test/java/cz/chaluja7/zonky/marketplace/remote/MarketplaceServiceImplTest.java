package cz.chaluja7.zonky.marketplace.remote;

import cz.chaluja7.zonky.configuration.properties.ZonkyApiProperties;
import cz.chaluja7.zonky.marketplace.job.JobKey;
import cz.chaluja7.zonky.marketplace.processor.LoanProcessor;
import cz.chaluja7.zonky.marketplace.remote.client.MarketplaceClient;
import cz.chaluja7.zonky.marketplace.remote.data.LoanDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static cz.chaluja7.zonky.marketplace.remote.MarketplaceServiceImpl.HEADER_TOTAL_COUNT;
import static cz.chaluja7.zonky.marketplace.remote.MarketplaceServiceImpl.ORDER_FOR_BATCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MarketplaceServiceImplTest {

    @Mock
    private MarketplaceClient marketplaceClientMock;

    @Mock
    private MarketplaceInfoService marketplaceInfoServiceMock;

    @Mock
    private ZonkyApiProperties zonkyApiPropertiesMock;

    @Mock
    private LoanProcessor firstLoanProcessorMock;

    @Mock
    private LoanProcessor secondLoanProcessorMock;

    @Mock
    private ResponseEntity responseEntityMock;

    @Captor
    private ArgumentCaptor<List<LoanDto>> loansCaptor;

    private MarketplaceServiceImpl marketplaceService;

    @Before
    public void before() {
        marketplaceService = new MarketplaceServiceImpl(marketplaceClientMock, marketplaceInfoServiceMock,
                zonkyApiPropertiesMock, Arrays.asList(firstLoanProcessorMock, secondLoanProcessorMock));

        when(zonkyApiPropertiesMock.getBatchPageSize())
                .thenReturn(1);
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldProcessNotYetProcessedMarketplaceLoans_firstRun() {
        final ZonedDateTime time1 = ZonedDateTime.now().minusMinutes(2);
        final ZonedDateTime time2 = ZonedDateTime.now().minusMinutes(1);
        final ZonedDateTime time3 = ZonedDateTime.now();

        LoanDto loan1 = prepareLoan(1L, time1);
        LoanDto loan2 = prepareLoan(2L, time2);
        LoanDto loan3 = prepareLoan(3L, time3);

        when(marketplaceInfoServiceMock.getLastProcessedRecordTime(JobKey.PERIODIC))
                .thenReturn(Optional.empty());

        when(marketplaceClientMock.getLoans(0, 1, ORDER_FOR_BATCH))
                .thenReturn(prepareResponseEntity(3, loan1));

        when(marketplaceClientMock.getLoans(1, 1, ORDER_FOR_BATCH))
                .thenReturn(prepareResponseEntity(3, loan2));

        when(marketplaceClientMock.getLoans(2, 1, ORDER_FOR_BATCH))
                .thenReturn(prepareResponseEntity(3, loan3));

        doNothing().when(firstLoanProcessorMock)
                .process(anyList());

        doNothing().when(secondLoanProcessorMock)
                .process(anyList());

        doNothing().when(marketplaceInfoServiceMock)
                .putLastProcessedRecordTime(JobKey.PERIODIC, time3);

        marketplaceService.processNotYetProcessedMarketplaceLoans(JobKey.PERIODIC);

        verify(marketplaceInfoServiceMock, times(1))
                .getLastProcessedRecordTime(JobKey.PERIODIC);

        verify(marketplaceClientMock, times(1))
                .getLoans(0, 1, ORDER_FOR_BATCH);

        verify(marketplaceClientMock, times(1))
                .getLoans(1, 1, ORDER_FOR_BATCH);

        verify(marketplaceClientMock, times(1))
                .getLoans(2, 1, ORDER_FOR_BATCH);

        verify(firstLoanProcessorMock, times(3))
                .process(loansCaptor.capture());

        verify(secondLoanProcessorMock, times(3))
                .process(loansCaptor.capture());

        verify(marketplaceInfoServiceMock, times(1))
                .putLastProcessedRecordTime(JobKey.PERIODIC, time3);

        verifyNoMoreInteractions(marketplaceInfoServiceMock, marketplaceClientMock, firstLoanProcessorMock, secondLoanProcessorMock);

        List<List<LoanDto>> allCapturedValues = loansCaptor.getAllValues();
        assertThat(allCapturedValues.size())
                .isEqualTo(6);

        assertThat(allCapturedValues.get(0))
                .hasSize(1);
        assertThat(allCapturedValues.get(0).get(0).getId())
                .isEqualTo(1L);
        assertThat(allCapturedValues.get(1))
                .hasSize(1);
        assertThat(allCapturedValues.get(1).get(0).getId())
                .isEqualTo(2L);
        assertThat(allCapturedValues.get(2))
                .hasSize(1);
        assertThat(allCapturedValues.get(2).get(0).getId())
                .isEqualTo(3L);

        assertThat(allCapturedValues.get(3))
                .hasSize(1);
        assertThat(allCapturedValues.get(3).get(0).getId())
                .isEqualTo(1L);
        assertThat(allCapturedValues.get(4))
                .hasSize(1);
        assertThat(allCapturedValues.get(4).get(0).getId())
                .isEqualTo(2L);
        assertThat(allCapturedValues.get(5))
                .hasSize(1);
        assertThat(allCapturedValues.get(5).get(0).getId())
                .isEqualTo(3L);
    }

    @SuppressWarnings("SameParameterValue")
    private ResponseEntity<List<LoanDto>> prepareResponseEntity(int totalCount, LoanDto... loanDtos) {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HEADER_TOTAL_COUNT, String.valueOf(totalCount));

        return new ResponseEntity<>(Arrays.asList(loanDtos), headers, HttpStatus.OK);
    }

    @Test
    public void shouldProcessLoans() {
        LoanDto loan1 = prepareLoan(1L, ZonedDateTime.now().minusMinutes(1));
        LoanDto loan2 = prepareLoan(2L, ZonedDateTime.now());

        doNothing().when(firstLoanProcessorMock)
                .process(anyList());

        doNothing().when(secondLoanProcessorMock)
                .process(anyList());

        marketplaceService.processLoans(Arrays.asList(loan1, loan2));

        verify(firstLoanProcessorMock, times(1))
                .process(loansCaptor.capture());

        verify(secondLoanProcessorMock, times(1))
                .process(loansCaptor.capture());

        verifyNoMoreInteractions(firstLoanProcessorMock, secondLoanProcessorMock);

        List<List<LoanDto>> allCapturedLoans = loansCaptor.getAllValues();
        assertThat(allCapturedLoans)
                .hasSize(2);

        assertThat(allCapturedLoans.get(0))
                .hasSize(2);
        assertThat(allCapturedLoans.get(0).get(0))
                .isEqualTo(loan1);
        assertThat(allCapturedLoans.get(0).get(1))
                .isEqualTo(loan2);

        assertThat(allCapturedLoans.get(1))
                .hasSize(2);
        assertThat(allCapturedLoans.get(1).get(0))
                .isEqualTo(loan1);
        assertThat(allCapturedLoans.get(1).get(1))
                .isEqualTo(loan2);
    }

    @Test
    public void shouldReturnTotalNumberOfRecords() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(HEADER_TOTAL_COUNT, Collections.singletonList("5"));

        when(responseEntityMock.getHeaders())
                .thenReturn(httpHeaders);

        int totalNumberOfRecords = marketplaceService.getTotalNumberOfRecords(responseEntityMock);

        assertThat(totalNumberOfRecords)
                .isEqualTo(5);
    }

    @Test
    public void shouldThrowNumberFormatExceptionWhenTotalNumberOfRecordsIsNotANumber() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(HEADER_TOTAL_COUNT, Collections.singletonList("X"));

        when(responseEntityMock.getHeaders())
                .thenReturn(httpHeaders);

        assertThatThrownBy(() -> marketplaceService.getTotalNumberOfRecords(responseEntityMock))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenTotalNumberOfRecordsIsNotPresented() {
        when(responseEntityMock.getHeaders())
                .thenReturn(new HttpHeaders());

        assertThatThrownBy(() -> marketplaceService.getTotalNumberOfRecords(responseEntityMock))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void handleTotalNumberOfRecordsShouldReturnSecondParameter() {
        assertThat(marketplaceService.handleTotalNumberOfRecords(null, 1))
                .isEqualTo(1);

        assertThat(marketplaceService.handleTotalNumberOfRecords(1, 2))
                .isEqualTo(2);
    }

    private LoanDto prepareLoan(Long id, ZonedDateTime datePublished) {
        LoanDto loanDto = new LoanDto();
        loanDto.setId(id);
        loanDto.setDatePublished(datePublished);

        return loanDto;
    }

}

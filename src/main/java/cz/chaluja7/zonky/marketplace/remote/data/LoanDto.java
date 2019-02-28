package cz.chaluja7.zonky.marketplace.remote.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoanDto {

    private Long id;
    private String url;
    private String name;
    private String story;
    private String purpose;
    private List<PhotoDto> photos;
    private Integer termInMonths;
    private BigDecimal interestRate;
    private BigDecimal revenueRate;
    private BigDecimal annuityWithInsurance;
    private String rating;
    private Boolean topped;
    private BigDecimal amount;
    private BigDecimal remainingInvestment;
    private BigDecimal reservedAmount;
    private BigDecimal investmentRate;
    private Boolean covered;
    private ZonedDateTime datePublished;
    private Boolean published;
    private ZonedDateTime deadline;
    private Integer investmentsCount;
    private Integer questionsCount;
    private String region;
    private String mainIncomeType;
    private Boolean insuranceActive;
    private List<InsuranceHistoryDto> insuranceHistory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public List<PhotoDto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoDto> photos) {
        this.photos = photos;
    }

    public Integer getTermInMonths() {
        return termInMonths;
    }

    public void setTermInMonths(Integer termInMonths) {
        this.termInMonths = termInMonths;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getRevenueRate() {
        return revenueRate;
    }

    public void setRevenueRate(BigDecimal revenueRate) {
        this.revenueRate = revenueRate;
    }

    public BigDecimal getAnnuityWithInsurance() {
        return annuityWithInsurance;
    }

    public void setAnnuityWithInsurance(BigDecimal annuityWithInsurance) {
        this.annuityWithInsurance = annuityWithInsurance;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Boolean getTopped() {
        return topped;
    }

    public void setTopped(Boolean topped) {
        this.topped = topped;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRemainingInvestment() {
        return remainingInvestment;
    }

    public void setRemainingInvestment(BigDecimal remainingInvestment) {
        this.remainingInvestment = remainingInvestment;
    }

    public BigDecimal getReservedAmount() {
        return reservedAmount;
    }

    public void setReservedAmount(BigDecimal reservedAmount) {
        this.reservedAmount = reservedAmount;
    }

    public BigDecimal getInvestmentRate() {
        return investmentRate;
    }

    public void setInvestmentRate(BigDecimal investmentRate) {
        this.investmentRate = investmentRate;
    }

    public Boolean getCovered() {
        return covered;
    }

    public void setCovered(Boolean covered) {
        this.covered = covered;
    }

    public ZonedDateTime getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(ZonedDateTime datePublished) {
        this.datePublished = datePublished;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public ZonedDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(ZonedDateTime deadline) {
        this.deadline = deadline;
    }

    public Integer getInvestmentsCount() {
        return investmentsCount;
    }

    public void setInvestmentsCount(Integer investmentsCount) {
        this.investmentsCount = investmentsCount;
    }

    public Integer getQuestionsCount() {
        return questionsCount;
    }

    public void setQuestionsCount(Integer questionsCount) {
        this.questionsCount = questionsCount;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getMainIncomeType() {
        return mainIncomeType;
    }

    public void setMainIncomeType(String mainIncomeType) {
        this.mainIncomeType = mainIncomeType;
    }

    public Boolean getInsuranceActive() {
        return insuranceActive;
    }

    public void setInsuranceActive(Boolean insuranceActive) {
        this.insuranceActive = insuranceActive;
    }

    public List<InsuranceHistoryDto> getInsuranceHistory() {
        return insuranceHistory;
    }

    public void setInsuranceHistory(List<InsuranceHistoryDto> insuranceHistory) {
        this.insuranceHistory = insuranceHistory;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class PhotoDto {

        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class InsuranceHistoryDto {

        private LocalDate policyPeriodFrom;
        private LocalDate policyPeriodTo;

        public LocalDate getPolicyPeriodFrom() {
            return policyPeriodFrom;
        }

        public void setPolicyPeriodFrom(LocalDate policyPeriodFrom) {
            this.policyPeriodFrom = policyPeriodFrom;
        }

        public LocalDate getPolicyPeriodTo() {
            return policyPeriodTo;
        }

        public void setPolicyPeriodTo(LocalDate policyPeriodTo) {
            this.policyPeriodTo = policyPeriodTo;
        }

    }

}

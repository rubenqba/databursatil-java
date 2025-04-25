package com.github.rubenqba.databursatil;

import com.github.rubenqba.databursatil.models.FinancialStatementType;
import com.github.rubenqba.databursatil.models.filter.FinancialReportFilter;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.stream.Collectors;

import static com.github.rubenqba.databursatil.ResourceUtil.loadJsonFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class FinancialReportsTest extends BaseStockMarketServiceTest {

    @Test
    void fetchFinancialReport() {

        // Given
        final var filter = FinancialReportFilter.builder()
                .issuerCode("AMX")
                .period("2T_2024")
                .includeAllStatements()
                .build();
        final var reportTypes = filter.include().stream()
                .map(FinancialStatementType::code)
                .map(String::toLowerCase)
                .collect(Collectors.joining(","));

        server.expect(requestToUriTemplate("/financieros?emisora={code}&periodo={period}&financieros={reports}", filter.issuerCode(), filter.period(), reportTypes))
                .andRespond(withSuccess(loadJsonFile("data/full_financial_statement.json"), MediaType.APPLICATION_JSON));

        // When
        assertThat(service.fetchFinancialReports(filter))
                .describedAs("Get nothing here...")
                .isNotNull()
                .satisfies(reports -> {
                    assertThat(reports.current())
                            .describedAs("Current report is null")
                            .isNotNull()
                            .satisfies(current -> {
                                assertThat(current.period()).isEqualTo(filter.period());
                                assertThat(current.balanceSheet())
                                        .describedAs("Current balance sheet statement is null")
                                        .isNotNull();
                                assertThat(current.incomeQuarterly())
                                        .describedAs("Current income quarterly statement is null")
                                        .isNotNull();
                                assertThat(current.balanceSheet())
                                        .describedAs("Current income YTD statement is null")
                                        .isNotNull();
                                assertThat(current.balanceSheet())
                                        .describedAs("Current cash flow statement is null")
                                        .isNotNull();
                            });
                    assertThat(reports.previous())
                            .describedAs("Previous report is null")
                            .isNotNull()
                            .satisfies(previous -> {
                                assertThat(previous.period()).startsWith(filter.period().substring(0, filter.period().length() - 1));
                                assertThat(previous.balanceSheet())
                                        .describedAs("Previous balance sheet statement is null")
                                        .isNotNull();
                                assertThat(previous.incomeQuarterly())
                                        .describedAs("Previous income quarterly statement is null")
                                        .isNotNull();
                                assertThat(previous.balanceSheet())
                                        .describedAs("Previous income YTD statement is null")
                                        .isNotNull();
                                assertThat(previous.balanceSheet())
                                        .describedAs("Previous cash flow statement is null")
                                        .isNotNull();
                            });
                });
    }
}

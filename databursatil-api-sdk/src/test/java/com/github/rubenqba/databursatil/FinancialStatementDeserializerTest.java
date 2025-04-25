package com.github.rubenqba.databursatil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rubenqba.databursatil.models.BalanceSheetStatementDetail;
import com.github.rubenqba.databursatil.models.CashFlowStatementDetail;
import com.github.rubenqba.databursatil.models.IncomeStatementDetail;
import com.github.rubenqba.databursatil.models.api.FinancialStatementResponse;
import com.github.rubenqba.databursatil.models.error.BalanceSheetValidationException;
import com.github.rubenqba.databursatil.models.error.CashFlowStatementValidationException;
import com.github.rubenqba.databursatil.models.error.IncomeStatementValidationException;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

public class FinancialStatementDeserializerTest {

    final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void deserializeBalanceSheet() {
        try (var is = new FileInputStream("src/test/resources/data/balance_sheet.json")) {
            BalanceSheetStatementDetail balanceSheet = mapper.readValue(is, BalanceSheetStatementDetail.class);
            assertThat(balanceSheet).isNotNull();

            // validate every field using real data
            assertThat(balanceSheet.totalAssets()).isEqualByComparingTo("35141033000");
            assertThat(balanceSheet.currentAssets()).isEqualByComparingTo("10601479000");
            assertThat(balanceSheet.nonCurrentAssets()).isEqualByComparingTo("24539554000");
            assertThat(balanceSheet.cashAndCashEquivalents()).isEqualByComparingTo("4940404000");
            assertThat(balanceSheet.tradeAndOtherReceivables()).isEqualByComparingTo("1986476000");
            assertThat(balanceSheet.inventories()).isEqualByComparingTo("1357402000");
            assertThat(balanceSheet.propertyPlantAndEquipment()).isEqualByComparingTo("18047025000");
            assertThat(balanceSheet.investmentProperty()).isEqualByComparingTo("448211000");
            assertThat(balanceSheet.goodwill()).isEqualByComparingTo("3309190000");
            assertThat(balanceSheet.intangibleAssets()).isEqualByComparingTo("124311000");
            assertThat(balanceSheet.deferredTaxAssets()).isEqualByComparingTo("471592000");
            assertThat(balanceSheet.rightOfUseAssets()).isEqualByComparingTo("483955000");

            assertThat(balanceSheet.totalLiabilities()).isEqualByComparingTo("13590214000");
            assertThat(balanceSheet.currentLiabilities()).isEqualByComparingTo("2170732000");
            assertThat(balanceSheet.tradeAndOtherPayable()).isEqualByComparingTo("1259945000");
            assertThat(balanceSheet.nonCurrentLiabilities()).isEqualByComparingTo("11419482000");
            assertThat(balanceSheet.deferredTaxLiabilities()).isEqualByComparingTo("1566735000");
            assertThat(balanceSheet.currentLeaseLiabilities()).isEqualByComparingTo("113757000");
            assertThat(balanceSheet.nonCurrentLeaseLiabilities()).isEqualByComparingTo("299348000");

            assertThat(balanceSheet.totalEquity()).isEqualByComparingTo("21550819000");
            assertThat(balanceSheet.issuedCapital()).isEqualByComparingTo("2003496000");
            assertThat(balanceSheet.sharePremium()).isEqualByComparingTo("9043000");
            assertThat(balanceSheet.retainedEarnings()).isEqualByComparingTo("19654547000");
            assertThat(balanceSheet.treasuryShares()).isEqualByComparingTo("2859429000");
            assertThat(balanceSheet.otherReserves()).isEqualByComparingTo("104734000");
            assertThat(balanceSheet.nonControllingInterests()).isEqualByComparingTo("2638428000");

            assertThat(balanceSheet.equityAndLiabilitiesTotal()).isEqualByComparingTo("35141033000");

            assertThatCode(balanceSheet::validate).doesNotThrowAnyException();

        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void deserializeBalanceSheetWithErrors() {
        try (var is = new FileInputStream("src/test/resources/data/balance_sheet_with_errors.json")) {
            BalanceSheetStatementDetail balanceSheet = mapper.readValue(is, BalanceSheetStatementDetail.class);
            assertThat(balanceSheet).isNotNull();

            // validate exception is thrown with detailed errors
            assertThatThrownBy(balanceSheet::validate)
                    .isInstanceOf(BalanceSheetValidationException.class)
                    .extracting("errors", as(InstanceOfAssertFactories.list(String.class)))
                    .containsExactlyInAnyOrder(
                            "Total Assets must equal Total Liabilities plus Total Equity: Expected 25141033000, current 35145033000",
                            "Total Assets must equal Current Assets plus Non-current Assets: Expected 35141033000, current 35145033000",
                            "Total Liabilities must equal Current Liabilities plus Non-current Liabilities: Expected 13590214000, current 3590214000",
                            "Total Equity must equal the sum of its components (Issued Capital + Share Premium + Retained Earnings - Treasury Shares + Other Reserves + Non-controlling Interests): Expected 24550819000, current 21550819000",
                            "Equity and Liabilities Total must equal Total Liabilities plus Total Equity: Expected 25141033000, current 35141033000"
                    );

        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void deserializeIncomeStatement() {
        try (var is = new FileInputStream("src/test/resources/data/income_statement_quarterly.json")) {
            IncomeStatementDetail incomeStatementDetail = mapper.readValue(is, IncomeStatementDetail.class);
            assertThat(incomeStatementDetail).isNotNull();

            assertThat(incomeStatementDetail.revenue()).isEqualByComparingTo("3403942000");
            assertThat(incomeStatementDetail.costOfGoodsSold()).isEqualByComparingTo("2077150000");
            assertThat(incomeStatementDetail.grossProfit()).isEqualByComparingTo("1326792000");

            assertThat(incomeStatementDetail.administrativeExpenses()).isEqualByComparingTo("90280000");
            assertThat(incomeStatementDetail.distributionExpenses()).isEqualByComparingTo("0");
            assertThat(incomeStatementDetail.otherOperatingExpenses()).isEqualByComparingTo("44156000");

            assertThat(incomeStatementDetail.financeIncome()).isEqualByComparingTo("168686000");
            assertThat(incomeStatementDetail.financeCosts()).isEqualByComparingTo("201757000");

            assertThat(incomeStatementDetail.profitBeforeTax()).isEqualByComparingTo("1173657000");
            assertThat(incomeStatementDetail.incomeTaxExpense()).isEqualByComparingTo("314379000");
            assertThat(incomeStatementDetail.netProfit()).isEqualByComparingTo("859278000");

            assertThat(incomeStatementDetail.profitAttributableToParentOwners()).isEqualByComparingTo("733800000");
            assertThat(incomeStatementDetail.profitAttributableToNoncontrollingInterests()).isEqualByComparingTo("125478000");

            assertThat(incomeStatementDetail.basicEarningsPerShare()).isEqualByComparingTo("0.09");
            assertThat(incomeStatementDetail.dilutedEarningsPerShare()).isEqualByComparingTo("0.09");

            assertThat(incomeStatementDetail.profitFromContinuingOperations()).isEqualByComparingTo("859278000");
            assertThat(incomeStatementDetail.profitFromDiscontinuedOperations()).isEqualByComparingTo("0");

            assertThat(incomeStatementDetail.shareOfProfitFromAssociatesAndJointVentures()).isEqualByComparingTo("14372000");

            // Ejecutar validación acumulativa
            assertThatCode(incomeStatementDetail::validate).doesNotThrowAnyException();

        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }

    @Test
    void deserializeIncomeStatementWithErrors() {
        try (var is = new FileInputStream("src/test/resources/data/income_statement_ytd_with_errors.json")) {
            IncomeStatementDetail statement = mapper.readValue(is, IncomeStatementDetail.class);
            assertThat(statement).isNotNull();

            // validate exception is thrown
            assertThatThrownBy(statement::validate)
                    .isInstanceOf(IncomeStatementValidationException.class)
                    .extracting("errors", as(InstanceOfAssertFactories.list(String.class)))
                    .isNotEmpty()
                    .containsExactlyInAnyOrder(
                            "Gross Profit must equal Revenue minus Cost of Goods Sold: Expected 6196391300, current 6195991000",
                            "Profit Before Tax calculation mismatch: Expected 5533096000, current 5653096000",
                            "Net Profit must equal Profit Before Tax minus Income Tax Expense: Expected 3979843000, current 5979843000",
                            "Net Profit must equal the sum of Parent Owners and Non-controlling Interests profits: Expected 3979843000, current 5979843000",
                            "Net Profit must equal sum of profits from Continuing and Discontinued Operations: Expected 3979843000, current 5979843000"
                    );
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void deserializeCashFlowStatement() {
        try (var is = new FileInputStream("src/test/resources/data/cash_flow_statement.json")) {
            CashFlowStatementDetail statement = mapper.readValue(is, CashFlowStatementDetail.class);
            assertThat(statement).isNotNull();

            assertThat(statement.profitLoss()).isEqualByComparingTo("-912465000");
            assertThat(statement.cashAndCashEquivalentsAtBeginning()).isEqualByComparingTo("4277140000");
            assertThat(statement.cashAndCashEquivalentsAtEnd()).isEqualByComparingTo("1038859000");
            assertThat(statement.increaseDecreaseInCashAndCashEquivalents()).isEqualByComparingTo("-3238281000");
            assertThat(statement.increaseDecreaseInCashAndCashEquivalentsBeforeExchangeRateEffects()).isEqualByComparingTo("-3105682000");
            assertThat(statement.effectOfExchangeRateChangesOnCash()).isEqualByComparingTo("-132599000");

            assertThat(statement.netCashFromOperatingActivities()).isEqualByComparingTo("2220974000");
            assertThat(statement.adjustmentsToReconcileProfitLoss()).isEqualByComparingTo("3135072000");
            assertThat(statement.depreciationAndAmortisationExpense()).isEqualByComparingTo("9803000");
            assertThat(statement.financeCosts()).isEqualByComparingTo("195404000");
            assertThat(statement.incomeTaxExpense()).isEqualByComparingTo("7109000");
            assertThat(statement.shareBasedPayments()).isEqualByComparingTo("55560000");
            assertThat(statement.fairValueGainsLosses()).isEqualByComparingTo("2998548000");
            assertThat(statement.unrealisedForeignExchangeLossesGains()).isEqualByComparingTo("-510174000");
            assertThat(statement.gainsLossesOnDisposalOfNonCurrentAssets()).isEqualByComparingTo("-24000");
            assertThat(statement.adjustmentForImpairmentLoss()).isEqualByComparingTo("1698000");
            assertThat(statement.adjustmentsForProvisions()).isEqualByComparingTo("0");
            assertThat(statement.adjustmentsForDecreaseIncreaseInInventories()).isEqualByComparingTo("0");
            assertThat(statement.decreaseIncreaseInTradeAccountReceivable()).isEqualByComparingTo("0");
            assertThat(statement.increaseDecreaseInTradeAccountPayable()).isEqualByComparingTo("-4503000");
            assertThat(statement.decreaseIncreaseInOtherOperatingReceivables()).isEqualByComparingTo("-68890000");
            assertThat(statement.increaseDecreaseInOtherOperatingPayables()).isEqualByComparingTo("112128000");
            assertThat(statement.otherOperatingActivities()).isEqualByComparingTo("0");
            assertThat(statement.dividendsReceivedOperating()).isEqualByComparingTo("0");
            assertThat(statement.interestPaidOperating()).isEqualByComparingTo("0");
            assertThat(statement.interestReceivedOperating()).isEqualByComparingTo("0");
            assertThat(statement.incomeTaxesPaidOperating()).isEqualByComparingTo("1633000");

            assertThat(statement.netCashFromInvestingActivities()).isEqualByComparingTo("-12658360000");
            assertThat(statement.purchaseOfPropertyPlantAndEquipment()).isEqualByComparingTo("2373000");
            assertThat(statement.purchaseOfIntangibleAssets()).isEqualByComparingTo("1384000");
            assertThat(statement.purchaseOfOtherLongTermAssets()).isEqualByComparingTo("13036649000");
            assertThat(statement.proceedsFromSalesOfOtherLongTermAssets()).isEqualByComparingTo("41502000");
            assertThat(statement.proceedsFromSalesOfPropertyPlantAndEquipment()).isEqualByComparingTo("0");
            assertThat(statement.proceedsFromSalesOfIntangibleAssets()).isEqualByComparingTo("0");
            assertThat(statement.interestPaidInvesting()).isEqualByComparingTo("0");
            assertThat(statement.interestReceivedInvesting()).isEqualByComparingTo("341746000");
            assertThat(statement.dividendsReceivedInvesting()).isEqualByComparingTo("0");
            assertThat(statement.cashFlowsFromLosingControlOfSubsidiaries()).isEqualByComparingTo("0");
            assertThat(statement.cashFlowsUsedInObtainingControlOfSubsidiaries()).isEqualByComparingTo("0");
            assertThat(statement.otherInvestingActivities()).isEqualByComparingTo("-1202000");

            assertThat(statement.netCashFromFinancingActivities()).isEqualByComparingTo("7331704000");
            assertThat(statement.proceedsFromIssuingShares()).isEqualByComparingTo("6830572000");
            assertThat(statement.proceedsFromBorrowings()).isEqualByComparingTo("9368041000");
            assertThat(statement.repaymentsOfBorrowings()).isEqualByComparingTo("6277337000");
            assertThat(statement.dividendsPaid()).isEqualByComparingTo("1498925000");
            assertThat(statement.paymentsToAcquireOrRedeemEntityShares()).isEqualByComparingTo("64833000");
            assertThat(statement.interestPaidFinancing()).isEqualByComparingTo("453218000");
            assertThat(statement.paymentsOfLeaseLiabilities()).isEqualByComparingTo("4361000");
            assertThat(statement.paymentsOfFinanceLeaseLiabilities()).isEqualByComparingTo("0");
            assertThat(statement.proceedsFromGovernmentGrants()).isEqualByComparingTo("0");
            assertThat(statement.proceedsFromChangesInOwnershipInterestsInSubsidiaries()).isEqualByComparingTo("0");
            assertThat(statement.paymentsFromChangesInOwnershipInterestsInSubsidiaries()).isEqualByComparingTo("0");
            assertThat(statement.otherFinancingActivities()).isEqualByComparingTo("-568235000");

            // Ejecutar validación acumulativa
            assertThatCode(statement::validate).doesNotThrowAnyException();

        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }

    @Test
    void deserializeCashFlowStatementWithErrors() {
        try (var is = new FileInputStream("src/test/resources/data/cash_flow_statement_with_errors.json")) {
            CashFlowStatementDetail cashFlow = mapper.readValue(is, CashFlowStatementDetail.class);
            assertThat(cashFlow).isNotNull();

            assertThatThrownBy(cashFlow::validate)
                    .isInstanceOf(CashFlowStatementValidationException.class)
                    .extracting("errors", as(InstanceOfAssertFactories.list(String.class)))
                    .isNotEmpty()
                    .containsExactlyInAnyOrder(
                            "Cash reconciliation failed.: Expected 5440404000, current 4940406700",
                            "Increase/decrease before exchange rate effects mismatch.: Expected -745583000, current -964583000",
                            "Operating cash flow reconciliation failed.: Expected 7253684000, current 5481204000"
                    );

        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }

    @Test
    void deserializeFullResponse() {
        try (var is = new FileInputStream("src/test/resources/data/full_financial_statement.json")) {
            var reports = mapper.readValue(is, FinancialStatementResponse.class);
            assertThat(reports).isNotNull();

            assertThat(reports.balanceSheet())
                    .describedAs("Balance Sheet report is null")
                    .isNotNull()
                    .satisfies(balanceSheetReport -> {
                        assertThat(balanceSheetReport.previous()).isNotNull();
                        assertThat(balanceSheetReport.current()).isNotNull();
                    });

            assertThat(reports.incomeQuarterly())
                    .describedAs("Income Quarterly report is null")
                    .isNotNull()
                    .satisfies(incomeQuarterlyReport -> {
                        assertThat(incomeQuarterlyReport.previous()).isNotNull();
                        assertThat(incomeQuarterlyReport.current()).isNotNull();
                    });

            assertThat(reports.incomeYearToDate())
                    .describedAs("Income YTD report is null")
                    .isNotNull()
                    .satisfies(incomeYearToDateReport -> {
                        assertThat(incomeYearToDateReport.previous()).isNotNull();
                        assertThat(incomeYearToDateReport.current()).isNotNull();
                    });

            assertThat(reports.cashFlow())
                    .describedAs("Cash Flow report is null")
                    .isNotNull()
                    .satisfies(cashFlowReport -> {
                        assertThat(cashFlowReport.previous()).isNotNull();
                        assertThat(cashFlowReport.current()).isNotNull();
                    });
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
    }
}

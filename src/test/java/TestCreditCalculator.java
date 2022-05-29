import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCreditCalculator {
    private static final int MIN_CREDIT_MOTHS = 6;
    private static final int MAX_CREDIT_MOTHS = 60;
    private static final int MIN_CREDIT_SUM = 1000;
    private static final int MAX_CREDIT_SUM = 1000000;
    private static final int MIN_CREDIT_PERCENT = 12;
    private static final int MAX_CREDIT_PERCENT = 36;

    @ParameterizedTest
    @MethodSource("parametersForTests")
    public void testEveryMonthPayment(int creditMonths, int creditSum, int percent) {
        CreditCalculator calculator = new CreditCalculator();
//        получение результата тестируемого метода
        BigDecimal testResult = calculator.everyMonthPayment(creditMonths, creditSum, percent);
//        вычисление проверочного значения
        BigDecimal percentInMonth =
                BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP)
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal sumDivideMonths =
                BigDecimal.valueOf(creditSum).divide(BigDecimal.valueOf(creditMonths), 2, RoundingMode.HALF_UP);
        BigDecimal checkingValue = sumDivideMonths.multiply(BigDecimal.valueOf(1).add(percentInMonth)).scaleByPowerOfTen(2);
//        проверка
        assertEquals(testResult, checkingValue,
                "Неправильный ежемесячный платеж при сумме кредита " + creditSum
                        + " на " + creditMonths
                        + " мес. под " + percent + "% годовых: ");
    }

    @ParameterizedTest
    @MethodSource("parametersForTests")
    public void testTotalSum(int creditMonths, int creditSum, int percent) {
        CreditCalculator calculator = new CreditCalculator();
//        получение результата тестируемого метода
        BigDecimal testResult = calculator.totalSum(creditMonths, creditSum, percent);
//        вычисление проверочного значения
        BigDecimal percentInMonth =
                BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP)
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal sumDivideMonths =
                BigDecimal.valueOf(creditSum).divide(BigDecimal.valueOf(creditMonths), 2, RoundingMode.HALF_UP);
        BigDecimal checkingValue =
                sumDivideMonths.multiply(BigDecimal.valueOf(1).add(percentInMonth))
                        .multiply(BigDecimal.valueOf(creditMonths))
                        .scaleByPowerOfTen(2);
//        проверка
        assertEquals(testResult, checkingValue,
                "Неправильный общий возврат в банк при сумме кредита " + creditSum
                        + " на " + creditMonths
                        + " мес. под " + percent + "% годовых: ");
    }

    @ParameterizedTest
    @MethodSource("parametersForTests")
    public void testOverPayment(int creditMonths, int creditSum, int percent) {
        CreditCalculator calculator = new CreditCalculator();
//        получение результата тестируемого метода
        BigDecimal testResult = calculator.overPayment(creditMonths, creditSum, percent);
//        вычисление проверочного значения
        BigDecimal percentInMonth =
                BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP)
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal sumDivideMonths =
                BigDecimal.valueOf(creditSum).divide(BigDecimal.valueOf(creditMonths), 2, RoundingMode.HALF_UP);
        BigDecimal checkingValue =
                sumDivideMonths.multiply(BigDecimal.valueOf(1).add(percentInMonth))
                        .multiply(BigDecimal.valueOf(creditMonths))
                        .subtract(BigDecimal.valueOf(creditSum))
                        .scaleByPowerOfTen(2);
//        проверка
        assertEquals(testResult, checkingValue,
                "Неправильная сумма переплаты при сумме кредита " + creditSum
                        + " на " + creditMonths
                        + " мес. под " + percent + "% годовых: ");
    }

    public static Stream<Arguments> parametersForTests() {
        //подготовка тестовых значений
        final Random random = new Random();
        int creditMonths = random.nextInt(MAX_CREDIT_MOTHS) + MIN_CREDIT_MOTHS;
        int creditSum = random.nextInt(MAX_CREDIT_SUM) + MIN_CREDIT_SUM;
        int percent = random.nextInt(MAX_CREDIT_PERCENT) + MIN_CREDIT_PERCENT;
        return Stream.of(Arguments.of(creditMonths, creditSum, percent));
    }
}

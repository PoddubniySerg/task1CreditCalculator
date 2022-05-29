import java.math.BigDecimal;
import java.math.RoundingMode;

public class CreditCalculator {

    public BigDecimal everyMonthPayment(int months, int sum, int percent) {
        return this.everyMonthWithoutScale(months, sum, percent).scaleByPowerOfTen(2);
    }

    public BigDecimal totalSum(int months, int sum, int percent) {
        return this.everyMonthWithoutScale(months, sum, percent)
                .multiply(BigDecimal.valueOf(months))
                .scaleByPowerOfTen(2);
    }

    public BigDecimal overPayment(int months, int sum, int percent) {
        return this.everyMonthWithoutScale(months, sum, percent)
                .multiply(BigDecimal.valueOf(months))
                .subtract(BigDecimal.valueOf(sum))
                .scaleByPowerOfTen(2);
    }

    private BigDecimal everyMonthWithoutScale(int months, int sum, int percent) {
        BigDecimal percentInMonth =
                BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP)
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal sumDivideMonths =
                BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
        return sumDivideMonths.multiply(BigDecimal.valueOf(1).add(percentInMonth));
    }
}

package jp.mappiekochi.sample.semantickernel.webapi.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class DateTimePlugin {
    @DefineKernelFunction(name = "getCurrentDate", description = "Returns the current date and time", returnDescription = "Current date and time in 'yyyy-MM-dd HH:mm:ss' format")
    public static String getCurrentDateTimeString() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString();
    }

    @DefineKernelFunction(name = "getJapaneseHoliday", description = "Determine if it is a holiday in Japan")
    public static boolean isJapaneseHoliday(
            @KernelFunctionParameter(name = "year", description = "The year, part of date you want to determine") String yearString,
            @KernelFunctionParameter(name = "month", description = "The month, part of date you want to determine") String monthString,
            @KernelFunctionParameter(name = "day", description = "The day, part of date you want to determine") String dayString) {

        // convert parameters to integer
        int year = Integer.parseInt(yearString);
        int month = Integer.parseInt(monthString);
        int day = Integer.parseInt(dayString);

        // Fixed date holidays
        if ((month == 1 && day == 1) || // 元日
                (month == 2 && (day == 11 || day == 23)) || // 建国記念の日、天皇誕生日
                (month == 4 && day == 29) || // 昭和の日
                (month == 5 && (day >= 3 && day <= 5)) || // 憲法記念日、みどりの日、こどもの日
                (month == 8 && day == 11) || // 山の日
                (month == 11 && (day == 3 || day == 23))) // 文化の日、勤労感謝の日
        {
            return true;
        }

        // Holidays that depend on specific weekdays
        if ((month == 1 && day == getDayOfSecondMonday(year, 1).getDayOfMonth()) || // 成人の日
                (month == 7 && day == getDayOfThirdMonday(year, 7).getDayOfMonth()) || // 海の日
                (month == 9 && day == getDayOfThirdMonday(year, 9).getDayOfMonth()) || // 敬老の日
                (month == 10 && day == getDayOfSecondMonday(year, 10).getDayOfMonth())) // 体育の日
        {
            return true;
        }

        // 春分の日と秋分の日
        if ((month == 3 && day == getVernalEquinoxDay(year).getDayOfMonth()) || // 春分の日
                (month == 9 && day == getAutumnalEquinoxDay(year).getDayOfMonth())) // 秋分の日
        {
            return true;
        }

        return false;
    }

    private static LocalDate getVernalEquinoxDay(int year) {
        if (year == 2027 || year == 2031 || year == 2035 || year == 2039 || year == 2043 || year == 2047) {
            return LocalDate.of(year, 3, 21);
        } else if (year >= 2025 && year <= 2050) {
            return LocalDate.of(year, 3, 20);
        }
        // More realistic estimation formula for the Vernal Equinox Day (approximation formula for years after 1980)
        int day = (int)(20.8431 + 0.242194 * (year - 1980) - Math.floor((year - 1980) / 4.0));
        return LocalDate.of(year, 3, day);
    }

    private static LocalDate getAutumnalEquinoxDay(int year) {
        if (year == 2028 || year == 2032 || year == 2036 || year == 2040 || year == 2044 || year == 2045 || year == 2048
                || year == 2049) {
            return LocalDate.of(year, 9, 22);
        } else if (year >= 2025 && year <= 2050) {
            return LocalDate.of(year, 9, 23);
        }
        // This logic is not completely accurate.
        // The date of the Autumnal Equinox varies by year, and this approximation may result in errors for some years.
        // For more details and accurate calculation methods, please refer to the following:
        // https://www.nao.ac.jp/faq/a0301.html (国立天文台)
        return LocalDate.of(year, 9, (year - 2028) % 4 == 0 ? 22 : 23);
    }

    private static LocalDate getDayOfNthMonday(int year, int month, int nth) {
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate firstMonday = firstDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        // If nth is 1, return as is; otherwise, add (nth-1) weeks
        return firstMonday.plusWeeks(nth - 1);
    }
    private static LocalDate getDayOfSecondMonday(int year, int month) {
        return getDayOfNthMonday(year, month, 2);
    }

    private static LocalDate getDayOfThirdMonday(int year, int month) {
        return getDayOfNthMonday(year, month, 3);
    }
}

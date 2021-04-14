package coral.bedwars.trainer.faster;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class Format {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");

    private Format() {
        throw new AssertionError("Nope.");
    }

    public static String decimal(long val) {
        return DECIMAL_FORMAT.format(val).replace(",", ".");
    }

    public static String decimal(double val) {
        return DECIMAL_FORMAT.format(val).replace(",", ".");
    }

    public static String timeStamp(long millis) {
        long stamp = System.currentTimeMillis() - millis;

        if (stamp < 60000) {
            return decimal(stamp / 1000.0);
        }

        TimeStorage timeStorage = TimeStorage.millis(stamp);

        int secs = timeStorage.getLeft(TimeUnit.SECONDS);
        int mins = timeStorage.getLeft(TimeUnit.MINUTES);
        int hours = timeStorage.getLeft(TimeUnit.HOURS);
        int days = timeStorage.getLeft(TimeUnit.DAYS);

        return (days > 0 ? ((days >= 10)
                ? Integer.valueOf(days)
                : ("0" + days))
                + ":" : "") + (hours > 0 ? ((hours >= 10)
                ? Integer.valueOf(hours)
                : ("0" + hours))
                + ":" : "") + ((mins >= 10)
                ? Integer.valueOf(mins)
                : ("0" + mins))
                + ":" + ((secs >= 10)
                ? Integer.valueOf(secs)
                : ("0" + secs));
    }

}

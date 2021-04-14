package coral.bedwars.trainer.faster;

import java.util.concurrent.TimeUnit;

public class TimeStorage {

    private final int totalTimeInSeconds, days, hours, minutes, seconds;

    public TimeStorage(Number seconds) {
        this.totalTimeInSeconds = (int) seconds;
        int remainder = this.totalTimeInSeconds % 86400;
        this.days = this.totalTimeInSeconds / 86400;
        this.hours = remainder / 3600;
        this.minutes = remainder / 60 - this.hours * 60;
        this.seconds = remainder % 3600 - this.minutes * 60;
    }

    public int getLeft(TimeUnit type) {
        return (type == TimeUnit.DAYS)
                ? this.days
                : ((type == TimeUnit.HOURS)
                ? this.hours
                : ((type == TimeUnit.MINUTES)
                ? this.minutes
                : ((type == TimeUnit.SECONDS)
                ? this.seconds
                : this.totalTimeInSeconds)));
    }

    public static TimeStorage millis(long millis) {
        return new TimeStorage(Long.valueOf(millis / 1000).intValue());
    }

}

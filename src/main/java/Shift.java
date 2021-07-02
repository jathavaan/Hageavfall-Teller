import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Shift implements Comparable<Shift> {
    private final String shiftCode;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private int count;

    /**
     * Følgende verdier skal defineres i konstruktøren:
     * shiftCode, startTime, endTime
     */
    public Shift() {
        this.shiftCode = generateShiftCode();
        this.startTime = generateStartTime();
        this.endTime = generateEndTime();
        this.count = 0;
    }

    /**
     * Lager et LocalDateTime objekt fra timer og minutter
     *
     * @param hrs
     * @param min
     * @return time
     */
    public static LocalDateTime intToTime(final int hrs, final int min) {
        if (hrs >= 24 || hrs < 0)
            throw new IllegalArgumentException("Invalid hour");

        if (min >= 60 || min < 0)
            throw new IllegalArgumentException("Invalid minute");

        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();

        return LocalDateTime.of(year, month, day, hrs, min);
    }

    /**
     * Konverterer et LocalDateTime objekt til en streng som representerer tiden på en lesbar måte
     *
     * @param time
     * @return timeString
     */
    public static String timeToString(final LocalDateTime time) {
        return null;
    }

    /**
     * @return shiftCode
     */
    public String getShiftCode() {
        return shiftCode;
    }

    /**
     * @return startTime
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * @return startTime
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * @return count
     */
    public int getCount() {
        return count;
    }


    // Generering

    public void incrementCount() {
        this.count += 1;
    }

    /**
     * Generer shiftCode basert på hvilken dag det er og hva klokken er
     *
     * @return shiftCode
     */
    public String generateShiftCode() {
        LocalDateTime start = generateStartTime();

        int day = start.getDayOfWeek().getValue();
        int hrs = start.getHour();
        int min = start.getMinute();

        switch (day) {
            case 7:
                throw new IllegalStateException("No shift on sundays");
            case 6:
            case 5:
                return "FR-LØR-T";
            default:
                if (hrs == 7) {
                    return "TIDL1-T";
                } else {
                    return "SENT3-T";
                }
        }
    }

    /**
     * Genererer slutt-tid for skfit basert på hvilken dag det er og hva kloken er nå
     *
     * @return endTime
     */
    public LocalDateTime generateStartTime() {
        long secondsToday = localDateTimeToSeconds(LocalDateTime.now());
        int day = LocalDateTime.now().getDayOfWeek().getValue();
        LocalDateTime startTime = null;

        switch (day) {
            case 7:
                throw new IllegalStateException("There are no shifts on sundays");
            case 6:
                if (30_600L > secondsToday)
                    throw new IllegalStateException("Saturday shift cannot start before 08:30");

                if (61_200L < secondsToday)
                    throw new IllegalStateException("Saturday shift cannot start after 17:00");

                startTime = intToTime(8, 30);
                break;
            case 5:
                if (30_600L > secondsToday)
                    throw new IllegalStateException("Friday shift cannot start before 08:30");

                if (61_200L < secondsToday)
                    throw new IllegalStateException("Friday shift cannot start after 17:30");

                startTime = intToTime(8, 30);
                break;
            default:
                if (25_200L > secondsToday)
                    throw new IllegalStateException("Shift cannot start before 7:00");

                if (77_400L < secondsToday)
                    throw new IllegalStateException("Shift cannot start after 21:30");

                if (secondsToday < 52_200L) {
                    startTime = intToTime(7, 0);
                } else {
                    startTime = intToTime(14, 30);
                }
                break;
        }

        startTimeValidation(startTime);
        return startTime;
    }

    // Konvertering

    /**
     * Genererer slutt-tid for skfit basert på hvilken dag det er og hva kloken er nå
     *
     * @return endTime
     */
    public LocalDateTime generateEndTime() {
        long secondsToday = localDateTimeToSeconds(LocalDateTime.now());
        int day = LocalDateTime.now().getDayOfWeek().getValue();
        LocalDateTime endTime = null;

        switch (day) {
            case 7:
                throw new IllegalStateException("There are no shifts on sundays");
            case 6:
                if (30_600L > secondsToday)
                    throw new IllegalStateException("Saturday shift cannot end before 08:30");

                if (61_200L < secondsToday)
                    throw new IllegalStateException("Saturday shift cannot end after 17:00");

                endTime = intToTime(17, 0);
                break;
            case 5:
                if (30_600L > secondsToday)
                    throw new IllegalStateException("Friday shift cannot end before 08:30");

                if (61_200L < secondsToday)
                    throw new IllegalStateException("Friday shift cannot end after 17:30");

                endTime = intToTime(17, 0);
                break;
            default:
                if (25_200L > secondsToday)
                    throw new IllegalStateException("Shift cannot end before 7:00");

                if (77_400L < secondsToday)
                    throw new IllegalStateException("Shift cannot end after 21:30");

                if (secondsToday < 52_200L) {
                    endTime = intToTime(14, 30);
                } else {
                    endTime = intToTime(21, 30);
                }
                break;
        }

        endTimeValidation(endTime);
        return endTime;
    }

    /**
     * Sammenligner to Shift objekter
     *
     * @param o
     * @return neg om this er større enn o, pos om this er mindre enn o, og 0 hvis de er like
     */
    @Override
    public int compareTo(Shift o) {
        if (localDateTimeToSeconds(this.getStartTime()) - localDateTimeToSeconds(o.getStartTime()) != 0)
            return (int) (localDateTimeToSeconds(this.getStartTime()) - localDateTimeToSeconds(o.getStartTime()));

        if (localDateTimeToSeconds(this.getEndTime()) - localDateTimeToSeconds(o.getEndTime()) != 0)
            return (int) (localDateTimeToSeconds(this.getEndTime()) - localDateTimeToSeconds(o.getEndTime()));

        return 0;
    }

    /**
     * Sekunder fra midnatt til oppgitte LocalDateTimeObjekt
     *
     * @param time
     * @return seconds
     */
    private long localDateTimeToSeconds(LocalDateTime time) {
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        LocalDateTime midnight = intToTime(0, 0);
        return time.toEpochSecond(zoneOffset) - midnight.toEpochSecond(zoneOffset);
    }

    // Valideringsmetoder

    @Override
    public String toString() {
        return getShiftCode() + ": [" + getStartTime() +  " -> " + getEndTime() + "]"
                + "\nCount: " + getCount();
    }

    private void shiftCodeValidation(String shiftCode) {
        if (shiftCode == null || shiftCode.isBlank())
            throw new IllegalArgumentException("Shift code cannot be null or blank");

        shiftCode = shiftCode.toUpperCase();

        if (!isValidShiftCode(shiftCode))
            throw new IllegalArgumentException("Shift code is invalid: " + shiftCode);
    }

    /**
     * Sjekker om shiftCode samsvarer med dag og klokkeslett
     *
     * @param shiftCode
     * @return
     */
    private boolean isValidShiftCode(String shiftCode) {
        return true;
    }

    private void startTimeValidation(LocalDateTime startTime) {
        if (startTime == null)
            throw new IllegalArgumentException("Start time cannot be null");

        if (startTime.isBefore(intToTime(7, 0)))
            throw new IllegalArgumentException("Start time cannot be before 07:00");

        if (startTime.isAfter(intToTime(14, 15)))
            throw new IllegalArgumentException("Start time cannot be after 14:15");
    }

    private void endTimeValidation(LocalDateTime endTime) {
        if (endTime == null)
            throw new IllegalArgumentException("End time cannot be null");

        if (endTime.isBefore(intToTime(14, 15)))
            throw new IllegalArgumentException("End time cannot be before 14:15");

        if (endTime.isAfter(intToTime(21, 30)))
            throw new IllegalArgumentException("End time cannot be after 21:30");
    }
}

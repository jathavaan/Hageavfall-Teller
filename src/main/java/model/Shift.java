package model;

import java.time.LocalDateTime;

public class Shift implements Comparable<Shift> {
    private final String shiftCode;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private int count;

    /**
     * Konstruktør for testing
     * @param name skiftnavn
     * @param start starten av skift
     * @param end slutten av skift
     */
    public Shift(String name, LocalDateTime start, LocalDateTime end) {
        this.shiftCode = name;
        this.startTime = start;
        this.endTime = end;
    }

    /**
     * Konstruktør som auto-genererer skiftkode, start-tid og slutt-tid
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
     * @param hrs 0-23
     * @param min 0-59
     * @return time LocalDateTime objekt med datoen i dag og timer og minutter
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
     * @param time LocalDateTime objekt
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

    /**
     * Øker count med 1
     */
    public void incrementCount() {
        if (this.count + 1 < 0)
            throw new IllegalArgumentException("Count cannot be a negative number");

        this.count += 1;
    }

    /**
     * Reduserer count med 1
     */
    public void decrementCount() {
        if (this.count - 1 < 0)
            throw new IllegalArgumentException("Count cannot be a negative number");

        this.count -= 1;
    }

    // Generering


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

        timeValidation(startTime);
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

        timeValidation(endTime);
        return endTime;
    }

    /**
     * Sammenligner to Shift objekter
     *
     * @param o Et annet Shift objekt som blir sammenlignet med this
     * @return negativt tall om this er større enn o, positivt tall om this er mindre enn o, og 0 hvis de er like
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
     * @param time LocalDateTime objekt
     * @return seconds
     */
    private long localDateTimeToSeconds(LocalDateTime time) {
        LocalDateTime midnight = intToTime(0, 0);

        long timeSeconds = time.getHour() * 3600 + time.getMinute() * 60 + time.getSecond();
        long midnightSeconds = midnight.getSecond() * 3600 + midnight.getMinute() * 60 + midnight.getSecond();

        return timeSeconds - midnightSeconds;
    }

    /**
    * Lager en lesbar representasjon av Shift objektet
    * @return String
    */

    @Override public String toString() {
        return getShiftCode() + ": [" + getStartTime() + " -> " + getEndTime() + "]"
        + "\nCount: " + getCount();
    }

    // Valideringsmetoder

    private void shiftCodeValidation(String shiftCode) {
        if (shiftCode == null || shiftCode.isBlank())
            throw new IllegalArgumentException("model.Shift code cannot be null or blank");

        shiftCode = shiftCode.toUpperCase();

        if (!isValidShiftCode(shiftCode))
            throw new IllegalArgumentException("model.Shift code is invalid: " + shiftCode);
    }

    private boolean isValidShiftCode(String shiftCode) {
        return true;
    }


    /**
     * Validering av LocalDateTime objekt
     * @param time tid
     */
    private void timeValidation(LocalDateTime time) {
        if (time == null)
            throw new IllegalArgumentException("Time cannot be null");
    }
}

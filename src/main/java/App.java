import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class App {
    private static final Counter counter = new Counter();

    public static void main(String[] args) throws InterruptedException {
        App app = new App();
        app.run();
    }

    public void run() throws InterruptedException {
        Shift shift = new Shift();
        while (true) {

            try {
                if (counter.clearShiftList())
                    System.out.println("Cleared shift list");

                if (shift.getEndTime().equals(LocalDateTime.now()))
                    shift = new Shift();

                if (counter.addShift(shift)) {
                    System.out.println("Shift added: " + shift);
                } else {
                    shift.incrementCount();
                    System.out.println(shift);
                }

            } catch (IllegalArgumentException e) {
                System.err.println(e);
            } catch (IllegalStateException e) {
                System.err.println(e);
            }

            TimeUnit.SECONDS.sleep(1);
        }
    }
}

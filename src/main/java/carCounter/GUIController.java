package carCounter;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class GUIController implements Initializable {

    private final Counter counter = new Counter();

    @FXML
    private Text firstShiftNameLabel = new Text();
    @FXML
    private Text secondShiftNameLabel = new Text();
    @FXML
    private Text firstShiftCountLabel = new Text();
    @FXML
    private Text secondShiftCountLabel = new Text();
    @FXML
    private Text outputText = new Text();

    /**
     * Kjøres helt på starten av programmet
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            new Thread(() -> {

                try {
                    Shift shift = new Shift();

                    while (true) {

                        if (counter.clearShiftList(23, 59)) {
                            outputText.setText("Cleared shift list");
                            firstShiftNameLabel.setText("N/A");
                            secondShiftNameLabel.setText("N/A");
                            firstShiftCountLabel.setText("N/A");
                            secondShiftCountLabel.setText("N/A");
                            TimeUnit.SECONDS.sleep(5);
                        }

                        if (counter.getShiftList().size() > 0) {
                            if (counter.currentShift().getEndTime().isBefore(LocalDateTime.now()))
                                shift = new Shift();
                        }

                        if (counter.addShift(shift)) {

                            if (counter.getShiftList().size() >= 2) {
                                secondShiftNameLabel.setText(shift.getShiftCode());
                            } else if (counter.getShiftList().size() == 1) {
                                firstShiftNameLabel.setText(shift.getShiftCode());
                            }

                            outputText.setText("A new shift has started");
                            TimeUnit.SECONDS.sleep(5);
                            outputText.setText("");
                        }

                        if (counter.currentShift() != null) {
                            String count = String.valueOf(counter.currentShift().getCount());

                            if (counter.getShiftList().size() == 1) {
                                firstShiftCountLabel.setText(count);
                            } else if (counter.getShiftList().size() == 2) {
                                secondShiftCountLabel.setText(count);
                            }
                        }

                        TimeUnit.MILLISECONDS.sleep(10L);
                    }
                } catch (IllegalStateException | IllegalArgumentException e) {
                    outputText.setText(e.getMessage());
                } catch (NullPointerException e) {
                    outputText.setText("No shift has started, try again during opening times");
                } catch (Exception e) {
                    outputText.setText("Something went wrong");
                }

            }).start();

        } catch (Exception e) {
            outputText.setText("Something went wrong");
        }
    }

    public void onIncrease() {
        try {

            Shift s = new Shift(); // Bare for å kunne kaste evt. feilmld

            Shift currentShift = counter.currentShift();
            if (currentShift == null) {
                outputText.setText("No shifts right now! Cannot increase count");
                return;
            }

            currentShift.incrementCount();
            outputText.setText("Count has been manually increased");
        } catch (IllegalStateException | IllegalArgumentException e) {
            outputText.setText(e.getMessage());
        } catch (NullPointerException e) {
            outputText.setText("No shift has started, try again during opening times");
        } catch (Exception e) {
            outputText.setText("Something went wrong");
        }
    }

    public void onDecrease() {
        try {

            Shift s = new Shift(); // Bare for å kunne kaste evt. feilmld

            Shift currentShift = counter.currentShift();
            if (currentShift == null) {
                outputText.setText("No shifts right now! Cannot decrease count!");
                return;
            }

            currentShift.decrementCount();

            outputText.setText("Count has been manually decreased");
        } catch (IllegalStateException | IllegalArgumentException e) {
            outputText.setText(e.getMessage());
        } catch (NullPointerException e) {
            outputText.setText("No shift has started, try again during opening times");
        } catch (Exception e) {
            outputText.setText("Something went wrong");
        }
    }

    public void registeredCar() {
        try {
            final GpioController gpio = GpioFactory.getInstance();
        } catch (IllegalStateException | IllegalArgumentException e) {
            outputText.setText(e.getMessage());
        } catch (NullPointerException e) {
            outputText.setText("No shift has started, try again during opening times");
        } catch (Exception e) {
            outputText.setText("Something went wrong");
        }
    }
}

package fxui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import model.Counter;
import model.Shift;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class GUIController implements Initializable {

    private Counter counter = new Counter();

    @FXML
    private Text firstShiftNameLabel = new Text(), secondShiftNameLabel = new Text();
    @FXML
    private Text firstShiftCountLabel = new Text(), secondShiftCountLabel = new Text();
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
                /**
                 * Problem nå:
                 * Text blir automatisk satt tilbake til N/A i initialize
                 */

                Shift shift = new Shift("Dette er en test", Shift.intToTime(7, 15), Shift.intToTime(15, 0));
                while (true) {

                    if (counter.clearShiftList())
                        outputText.setText("Cleared shift list");

                    if (counter.getShiftList().size() > 0) {
                        if (counter.currentShift().getEndTime().isEqual(LocalDateTime.now()))
                            shift = new Shift();
                    }

                    if (!counter.addShift(shift)) {
                        shift.incrementCount();
                        firstShiftNameLabel.setText(shift.getShiftCode());
                        // firstShiftCountLabel.setText(String.valueOf(shift.getCount()));
                        outputText.setText("A new shift has started");
                        if (counter.getShiftList().size() >= 2) {
                            secondShiftNameLabel.setText(shift.getShiftCode());
                            // secondShiftCountLabel.setText(String.valueOf(shift.getCount()));
                        }
                    }

                    try {
                        TimeUnit.MILLISECONDS.sleep(1000L);
                    } catch (InterruptedException e) {
                        outputText.setText("Something went wrong");
                    }

                }

            }).start();



        } catch (IllegalStateException e) {
            outputText.setText(e.getMessage());
        } catch (IllegalArgumentException e) {
            outputText.setText(e.getMessage());
        } catch (NullPointerException e) {
            outputText.setText("No shift has started, try again during opening times");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onIncrease() {
        try {
            Shift currentShift = counter.currentShift();
            currentShift.incrementCount();
            initialize(null, null);
            outputText.setText("Count has been overwritten");
        } catch (NullPointerException e) {
            outputText.setText("Cannot increase count right now");
        } catch (Exception e) {
            outputText.setText(e.getMessage());
        }
    }

    public void onDecrease() {
        try {
            Shift currentShift = counter.currentShift();
            currentShift.decrementCount();
            initialize(null, null);
            outputText.setText("Count has been overwritten");
        } catch (NullPointerException e) {
            outputText.setText("Cannot decrease count right now");
        } catch (Exception e) {
            outputText.setText(e.getMessage());
        }
    }

    public void registeredCar() {

    }
}

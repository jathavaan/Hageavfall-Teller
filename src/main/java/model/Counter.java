package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Counter {
    private ArrayList<Shift> shiftList; // fungerer også som observert listen

    public Counter() {
        this.shiftList = new ArrayList<>();
    }

    public boolean addShift(Shift shift) {
        if (getShiftList().stream().anyMatch(s -> s.compareTo(shift) == 0))
            return false;

        this.shiftList.add(shift);
        return true;
    }

    public boolean removeShift(Shift shift) {
        if (getShiftList().stream().noneMatch(s -> s.equals(shift)))
            return false;

        this.shiftList = (ArrayList<Shift>) getShiftList().stream()
                .filter(s -> !s.equals(shift))
                .collect(Collectors.toList());
        return true;
    }

    public boolean clearShiftList() {
        if (LocalDateTime.now().isAfter(Shift.intToTime(15, 00))) {
            shiftList.clear();
            return true;
        }
        return false;
    }

    public Shift currentShift() {
        if (this.shiftList.size() == 0)
            return null;

        return this.shiftList.get(this.shiftList.size() - 1);
    }

    public ArrayList<Shift> getShiftList() {
        return new ArrayList<>(this.shiftList);
    }

    public void run() throws InterruptedException {
        Counter counter = new Counter();
        Shift shift = new Shift();
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

        TimeUnit.SECONDS.sleep(1);

    }
}

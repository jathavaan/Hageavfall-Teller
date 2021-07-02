import java.time.LocalDateTime;
import java.util.ArrayList;
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
        if (LocalDateTime.now().equals(Shift.intToTime(0, 0))) {
            shiftList.clear();
            return true;
        }
        return false;
    }

    public ArrayList<Shift> getShiftList() {
        return new ArrayList<>(this.shiftList);
    }
}

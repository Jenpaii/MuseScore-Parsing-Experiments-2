import java.util.ArrayList;

public class Staff {

    private Part parentPart;
    private int staffNumber;

    private ArrayList<Voice> voices;

    public Staff(int staffNumber, Part parentPart) {

        this.staffNumber = staffNumber;
        this.parentPart = parentPart;
        setVoices();
    }

    public void setVoices() {
        voices = new ArrayList<>();
        for (int i = 1; i <= 4; i++) { //voice numbers start on 1
            voices.add(new Voice(parentPart, this, i));
        }
    }

    public int getStaffNumber() {
        return staffNumber;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "parentPart=" + parentPart.getScorePartId() +
                ", staffNumber=" + staffNumber +
                ", voices=" + voices +
                '}';
    }
}

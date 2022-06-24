/*
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Staff { //currently unused

    private Part parentPart;
    private int staffNumber;
    private Map<Integer, Voice> voices; //voices are numbered

    public Staff(int staffNumber, Part parentPart) {

        this.staffNumber = staffNumber;
        this.parentPart = parentPart;
        setVoices();
    }

    public void setVoices() {
        voices = new HashMap<>();

        for (int i = 1; i <= 4; i++) { //voice numbers start on 1
            voices.put(i, new Voice(parentPart, this, i));
        }
    }

    public Map<Integer, Voice> getVoices() {
        return voices;

    }
    public int getStaffNumber() {
        return staffNumber;
    }

    public String voicesToString() {
        StringBuilder vb = new StringBuilder();
        vb.append("{");
        for (Voice voice : voices.values()) {
            vb.append("\n\t\t").append(voice.getVoiceNumber()).append("=").append(voice);
        } vb.append("}");
        return vb.toString();
    }

    @Override
    public String toString() {
        return "Staff{" +
                "parentPart=" + parentPart.getScorePartId() +
                ", staffNumber=" + staffNumber +
                ", voices=" + voicesToString() +
                '}';
    }
}*/

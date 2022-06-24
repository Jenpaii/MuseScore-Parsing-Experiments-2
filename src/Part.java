import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Part {

    int scorePartId;
    int midiProgram;
    String instrumentName;
    private Map<Integer, Staff> staves; //staves are numbered
    private Map<Integer, Measure> measures; //measures are numbered. Parts DO HAVE MEASURES.

    public Part(int scorePartId, int midiProgram, String instrumentName) {
        this.scorePartId = scorePartId;
        this.midiProgram = midiProgram;
        this.instrumentName = instrumentName;
        staves = new HashMap<>();
        measures = new HashMap<>();
    }

    public void addStaff(int staffNumber, Staff staff) {
        staves.put(staffNumber, staff);
    }
    public String getInstrumentName() {
        return instrumentName;
    }

    public boolean hasStaff(Staff staff) {
        return staves.containsValue(staff);
    }

    public int getStavesAmount() {
        return staves.size();
    }

    public int getMeasuresAmount() {
        return measures.size();
    }

    public Measure getMeasure(int measureNumber) {
        return measures.get(measureNumber);
    }

    public Measure getPreviousMeasure(Measure measure) {
        int measureNumber = measure.getMeasureNumber();
        int previousMeasureNumber = measure.getMeasureNumber()-1;
        return measures.get(previousMeasureNumber);
    }

    public void addMeasure(int measureNumber, Measure measure) {
        measures.put(measureNumber, measure);
    }
    public Staff getStaff(int staffNumber) {
        return staves.get(staffNumber);
    }

    public Voice getVoice(Staff staff, int voiceNumber) {
        return staff.getVoices().get(voiceNumber);
    }

    public int getScorePartId() {
        return scorePartId;
    }

    public String stavesToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Staff staff : staves.values()) {
            sb.append("\n\t").append(staff.getStaffNumber()).append("=").append(staff);
        } sb.append("}");
        return sb.toString();
    }

    public String measuresToString() {
        StringBuilder mb = new StringBuilder();
        mb.append("{");
        for (Measure measure : measures.values()) {
            mb.append("\n\t\t").append(measure.getMeasureNumber()).append("=").append(measure);
        } mb.append("}");
        return mb.toString();
    }
    @Override
    public String toString() {
        return "Part{" +
                "scorePartId=" + scorePartId +
                ", midiProgram=" + midiProgram +
                ", instrumentName='" + instrumentName + '\'' +
                ", \n\tstaves=" + stavesToString() +
                ", \n\tmeasures=" + measuresToString() +
                '}';
    }
}

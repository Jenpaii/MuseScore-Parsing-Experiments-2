import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class Part {

    int scorePartId;
    int midiProgram;
    String instrumentName;
    private ArrayList<Staff> staves;

    public Part(int scorePartId, int midiProgram, String instrumentName) {
        this.scorePartId = scorePartId;
        this.midiProgram = midiProgram;
        this.instrumentName = instrumentName;
        staves = new ArrayList<>();
    }

    public void addStaff(Staff staff) {
        staves.add(staff);
    }
    public String getInstrumentName() {
        return instrumentName;
    }

    public boolean hasStaff(Staff staff) {
        return staves.contains(staff);
    }

    public int getScorePartId() {
        return scorePartId;
    }

    @Override
    public String toString() {
        return "Part{" +
                "scorePartId=" + scorePartId +
                ", midiProgram=" + midiProgram +
                ", instrumentName='" + instrumentName + '\'' +
                ", staves=" + staves +
                '}';
    }
}

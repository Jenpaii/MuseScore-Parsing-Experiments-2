import java.util.ArrayList;

public class Part {

    int partId;
    int midiProgram;
    String instrumentName;

    private ArrayList<Staff> staves;

    public Part(int partId, int midiProgram, String instrumentName) {
        this.partId = partId;
        this.midiProgram = midiProgram;
        this.instrumentName = instrumentName;
        staves = new ArrayList<>();
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    @Override
    public String toString() {
        return "Part{" +
                "partId=" + partId +
                ", midiProgram=" + midiProgram +
                ", instrumentName='" + instrumentName + '\'' +
                '}';
    }
}

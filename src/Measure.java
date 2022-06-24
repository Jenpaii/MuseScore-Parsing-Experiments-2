import java.util.HashMap;
import java.util.Map;

public class Measure {

    private Part parentPart;
    //private Staff parentStaff; //why did I even add this? A measure is part of both staves.
    //private Voice parentVoice; //similarly, why did I add this? a measure doesn't have voices... the notes inside do.

    private int measureNumber;
    private int beatsAmount;
    private int beatType; //don't know if I need, but might as well save.
    private Map<Integer, Beat> beats;

    public Measure(Part parentPart, int measureNumber) { //if this constructor is used, it means setBeatDetails will be called later.
        this.parentPart = parentPart;
        this.measureNumber = measureNumber;
    }

    public void setBeatDetails(int beatsAmount, int beatType) {
        this.beatsAmount = beatsAmount;
        this.beatType = beatType;
        beats = new HashMap<>();
        for (int i = 1; i <= beatsAmount; i++) { //between 1 and (beatsAmount) amount of beats
            beats.put(i, new Beat(beatType)); //beats don't really have anything but a beat type right now, this will change soon.
        }
    }

    public int getMeasureNumber() {
        return measureNumber;
    }

    public int getBeatsAmount() {
        return beatsAmount;
    }

    public int getBeatType() {
        return beatType;
    }

    @Override
    public String toString() {
        return "Measure{parentPart="+parentPart.getScorePartId() +
                ", measureNumber=" + measureNumber +
                ", beatsAmount=" + beatsAmount +
                ", beatType=" + beatType +
                ", beats=" + beats +
                '}';
    }
}

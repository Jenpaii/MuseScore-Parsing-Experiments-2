import java.util.ArrayList;
import java.util.Arrays;

public class Chord {

    private Part parentPart;
    private Measure parentMeasure;
    private int voiceNumber; //private Voice parentVoice;
    private int staffNumber; //private Staff parentStaff;
    private Beat parentBeat;

    private ArrayList<Note> notes;

    public void addNote(Note note) {
        notes.add(note);
    }

    public Chord (Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber, Note... newNotes) {
        this.parentPart = parentPart;
        this.parentMeasure = parentMeasure;
        this.voiceNumber = voiceNumber;
        this.staffNumber = staffNumber;

        notes = new ArrayList<>();
        notes.addAll(Arrays.asList(newNotes));
    }

    @Override
    public String toString() {
        return "Chord{" +
                "parentPart=" + parentPart.getScorePartId() +
                ", parentMeasure=" + parentMeasure.getMeasureNumber() +
                ", voiceNumber=" + voiceNumber +
                ", staffNumber=" + staffNumber +
                //", parentBeat=" + parentBeat +
                ", notes=" + notes +
                '}';
    }
}
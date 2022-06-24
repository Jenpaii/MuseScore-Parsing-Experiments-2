import java.util.ArrayList;

public class Chord {

    private Part parentPart;
    private Measure parentMeasure;
    private int staffNumber; //private Staff parentStaff;
    private int voiceNumber; //private Voice parentVoice;
    private Beat parentBeat;

    private ArrayList<Note> notes;

    public void addNote(Note note) {
        notes.add(note);
    }

}
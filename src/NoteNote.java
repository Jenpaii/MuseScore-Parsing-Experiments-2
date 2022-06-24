public class NoteNote extends Note {

    private int alter;
    private int octave;

    public NoteNote(Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber, Chord parentChord, double duration) {
        super(parentPart, parentMeasure, voiceNumber, staffNumber, parentChord, duration);
    }

}

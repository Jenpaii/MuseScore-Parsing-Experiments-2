public class RestNote extends Note {

    public RestNote(Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber, Chord parentChord, double duration) {
        super(parentPart, parentMeasure, voiceNumber, staffNumber, parentChord, duration);
    }

    @Override
    public String toString() {
        return "Note{" +
                "parentPart=" + parentPart.getScorePartId() +
                ", parentMeasure=" + parentMeasure.getMeasureNumber() +
                ", voiceNumber=" + voiceNumber +
                ", staffNumber=" + staffNumber +
                //", parentBeat=" + parentBeat +
                ", duration=" + duration +
                '}';
    }
}

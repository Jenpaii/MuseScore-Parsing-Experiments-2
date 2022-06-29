package com.herokuapp.mstogw2.chord;

import com.herokuapp.mstogw2.main.Measure;
import com.herokuapp.mstogw2.part.Part;

public class GraceNote extends PitchNote {

    public GraceNote(Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber, Chord parentChord, double duration, char step, int alter, int octave) {
        super(parentPart, parentMeasure,voiceNumber, staffNumber, parentChord, duration, step, alter, octave);
    }

    public GraceNote(Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber, double duration, char step, int alter, int octave) {
        super(parentPart, parentMeasure,voiceNumber, staffNumber, duration, step, alter, octave);
    }

    public String getNoteType() {
        return "Grace" + super.getNoteType();
    }

}

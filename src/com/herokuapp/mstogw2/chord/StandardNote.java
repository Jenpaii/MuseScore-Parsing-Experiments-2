package com.herokuapp.mstogw2.chord;

import com.herokuapp.mstogw2.main.Measure;
import com.herokuapp.mstogw2.part.Part;

public class StandardNote extends PitchNote {

    public StandardNote(Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber, Chord parentChord, double duration, char step, int alter, int octave) {
        super(parentPart, parentMeasure, voiceNumber, staffNumber, parentChord, duration, step, alter, octave);
    }
    public String getNoteType() {
        return "Standard" + super.getNoteType();
    }

}

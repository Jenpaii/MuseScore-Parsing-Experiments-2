package com.herokuapp.mstogw2.chord;

import com.herokuapp.mstogw2.main.Measure;
import com.herokuapp.mstogw2.part.Part;

public class GraceNote extends Note {

    public GraceNote(Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber, Chord parentChord, double duration) {
        super(parentPart, parentMeasure,100, 100, parentChord, 100.0);
    }

    @Override
    public String toString() {
        return "Grace" + super.toString();
    }

}

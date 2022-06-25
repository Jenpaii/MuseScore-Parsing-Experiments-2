package com.herokuapp.mstogw2.chord;

import com.herokuapp.mstogw2.chord.Chord;
import com.herokuapp.mstogw2.main.Measure;
import com.herokuapp.mstogw2.part.Part;

public class GraceChord extends Chord {

    public GraceChord(Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber) {
        super(parentPart, parentMeasure, voiceNumber, staffNumber);
    }

    @Override
    public String toString() {
        return "Grace" + super.toString();
    }

}

package com.herokuapp.mstogw2.chord;

import com.herokuapp.mstogw2.main.Measure;
import com.herokuapp.mstogw2.part.Part;

public class RestChord extends Chord {
    public RestChord(Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber) {
        super(parentPart, parentMeasure, voiceNumber, staffNumber);
    }

    @Override
    public String getChordType() {
        return "Rest" + super.getChordType();
    }
}

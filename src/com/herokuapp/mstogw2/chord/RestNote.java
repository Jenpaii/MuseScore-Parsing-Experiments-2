package com.herokuapp.mstogw2.chord;

import com.herokuapp.mstogw2.main.Measure;
import com.herokuapp.mstogw2.part.Part;

public class RestNote extends Note {

    public RestNote(Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber, Chord parentChord, double duration) {
        super(parentPart, parentMeasure, voiceNumber, staffNumber, parentChord, duration);
    }

    public String getNoteType() {
        return "Rest" + super.getNoteType();
    }

}

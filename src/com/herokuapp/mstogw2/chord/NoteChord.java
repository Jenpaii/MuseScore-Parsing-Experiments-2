package com.herokuapp.mstogw2.chord;

import com.herokuapp.mstogw2.chord.Chord;
import com.herokuapp.mstogw2.main.Measure;
import com.herokuapp.mstogw2.part.Part;

public class NoteChord extends Chord {

    public NoteChord(Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber) {
        super(parentPart, parentMeasure, voiceNumber, staffNumber);
    }

    @Override
    public String toString() {
        return "Note" + super.toString();
    }

}

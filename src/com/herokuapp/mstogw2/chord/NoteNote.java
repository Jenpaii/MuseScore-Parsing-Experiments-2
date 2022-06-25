package com.herokuapp.mstogw2.chord;

import com.herokuapp.mstogw2.main.Measure;
import com.herokuapp.mstogw2.part.Part;

public class NoteNote extends Note {

    private int alter;
    private int octave;

    public NoteNote(Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber, Chord parentChord, double duration) {
        super(parentPart, parentMeasure, voiceNumber, staffNumber, parentChord, duration);
    }

    @Override
    public String toString() {
        return "Note" + super.toString();
    }

}

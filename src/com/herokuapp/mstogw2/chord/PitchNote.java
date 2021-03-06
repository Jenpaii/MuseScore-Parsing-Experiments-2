package com.herokuapp.mstogw2.chord;

import com.herokuapp.mstogw2.main.Measure;
import com.herokuapp.mstogw2.part.Part;

public abstract class PitchNote extends Note { //Abstract, cannot be instantiated

    private char step;
    private int alter;
    private int octave;
    public PitchNote(Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber, Chord parentChord, double duration, char step, int alter, int octave) {
        super(parentPart, parentMeasure, voiceNumber, staffNumber, parentChord, duration);
        this.step = step;
        this.alter = alter;
        this.octave = octave;
    }

    public PitchNote(Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber, double duration, char step, int alter, int octave) {
        super(parentPart, parentMeasure, voiceNumber, staffNumber, duration);
        this.step = step;
        this.alter = alter;
        this.octave = octave;
    }

    @Override
    public String toString() {
        return getNoteType() + "{" +
                "parentPart=" + parentPart.getScorePartId() +
                ", parentMeasure=" + parentMeasure.getMeasureNumber() +
                ", voiceNumber=" + voiceNumber +
                ", staffNumber=" + staffNumber +
                ", duration=" + duration +
                ", step=" + step +
                ", alter=" + alter +
                ", octave=" + octave +
                '}';
    }
}

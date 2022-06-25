package com.herokuapp.mstogw2.chord;

import com.herokuapp.mstogw2.main.Beat;
import com.herokuapp.mstogw2.main.Measure;
import com.herokuapp.mstogw2.part.*;

public class Note {

    protected Part parentPart;
    protected Measure parentMeasure;
    protected int voiceNumber;
    protected int staffNumber;
    private Beat parentBeat;
    private Chord parentChord;

    protected double duration;

    public Note(Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber, Chord parentChord, double duration) {
        this.parentPart = parentPart;
        this.parentMeasure = parentMeasure;
        this.voiceNumber = voiceNumber;
        this.staffNumber = staffNumber;
        this.parentChord = parentChord;
        this.duration = duration;
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
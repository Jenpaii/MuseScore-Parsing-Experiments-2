package com.herokuapp.mstogw2.chord;

import com.herokuapp.mstogw2.main.Beat;
import com.herokuapp.mstogw2.main.Measure;
import com.herokuapp.mstogw2.part.Part;

public abstract class Note { //Abstract, cannot be instantiated

    protected Part parentPart;
    private int noteNumberInPart;
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
        this.duration = duration/parentPart.getDivisions(); //normalise the duration based on the parent part's divisions value.
    }


    public Note(Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber, double duration) { //if using this constructor, make SURE you use
        //make SURE you use setParentChord immediately after.
        this.parentPart = parentPart;
        this.parentMeasure = parentMeasure;
        this.voiceNumber = voiceNumber;
        this.staffNumber = staffNumber;
        this.duration = duration/parentPart.getDivisions(); //normalise the duration based on the parent part's divisions value.
    }

    public String getNoteType() {
        return "Note";
    }

    public int getVoice() {
        return voiceNumber;
    }

    public int getStaff() {
        return staffNumber;
    }

    public Chord getParentChord() {
        return parentChord;
    }

    public void setParentChord(Chord parentChord) { //Use this immediately after the 2nd constructor
        this.parentChord = parentChord;
    }
    public double getDuration() {
        return duration;
    }

    public int getNoteNumberInPart() {
        return noteNumberInPart;
    }
    public void setNoteNumberInPart(int noteNumberInPart) {
        this.noteNumberInPart = noteNumberInPart;
    }
    @Override
    public String toString() {
        return getNoteType() + "{" +
                "parentPart=" + parentPart.getScorePartId() +
                ", parentMeasure=" + parentMeasure.getMeasureNumber() +
                ", voiceNumber=" + voiceNumber +
                ", staffNumber=" + staffNumber +
                //", parentBeat=" + parentBeat +
                ", duration=" + duration +
                '}';
    }
}
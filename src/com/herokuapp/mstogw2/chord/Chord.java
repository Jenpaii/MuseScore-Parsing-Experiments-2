package com.herokuapp.mstogw2.chord;

import com.herokuapp.mstogw2.main.Beat;
import com.herokuapp.mstogw2.main.Measure;
import com.herokuapp.mstogw2.part.Part;

import java.util.ArrayList;
import java.util.Arrays;
public class Chord { //Chord with grace and/or standard notes
    private Part parentPart;
    private Measure parentMeasure;
    private int voiceNumber; //private Voice parentVoice;
    private int staffNumber; //private Staff parentStaff;
    private double duration;
    private Beat parentBeat;

    private ArrayList<Note> notes;

    public Chord (Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber, Note... newNotes) { //Constructor needs to be updated with pitch variables
        this.parentPart = parentPart;
        this.parentMeasure = parentMeasure;
        this.voiceNumber = voiceNumber;
        this.staffNumber = staffNumber;

        notes = new ArrayList<>();
        notes.addAll(Arrays.asList(newNotes));
    }

    public void addNote(Note note) {
        notes.add(note);
        addDuration(note.getDuration());
    }

    public void addDuration(double duration) {
        this.duration += duration;
    }

    public double getDuration() {
        return duration;
    }

    public String getChordType() {
        return "Chord";
    }

    public Note getFirstNote() {
        return notes.get(0);
    }

    @Override
    public String toString() {
        return getChordType() + "{" +
                "parentPart=" + parentPart.getScorePartId() +
                ", parentMeasure=" + parentMeasure.getMeasureNumber() +
                ", voiceNumber=" + voiceNumber +
                ", staffNumber=" + staffNumber +
                ", duration=" + duration +
                //", parentBeat=" + parentBeat +
                ", notes=" + notes +
                '}';
    }
}
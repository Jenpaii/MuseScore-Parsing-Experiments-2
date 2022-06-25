package com.herokuapp.mstogw2.chord;

import java.util.ArrayList;
import java.util.Arrays;

import com.herokuapp.mstogw2.main.Beat;
import com.herokuapp.mstogw2.main.Measure;
import com.herokuapp.mstogw2.part.*;
public class Chord { //Chord with grace and/or standard notes
    private Part parentPart;
    private Measure parentMeasure;
    private int voiceNumber; //private Voice parentVoice;
    private int staffNumber; //private Staff parentStaff;
    private Beat parentBeat;

    private ArrayList<Note> notes;

    public void addNote(Note note) {
        notes.add(note);
    }

    public Chord (Part parentPart, Measure parentMeasure, int voiceNumber, int staffNumber, Note... newNotes) { //Constructor needs to be updated with pitch variables
        this.parentPart = parentPart;
        this.parentMeasure = parentMeasure;
        this.voiceNumber = voiceNumber;
        this.staffNumber = staffNumber;

        notes = new ArrayList<>();
        notes.addAll(Arrays.asList(newNotes));
    }

    public String getChordType() {
        return "Chord";
    }
    @Override
    public String toString() {
        return getChordType() +"{" +
                "parentPart=" + parentPart.getScorePartId() +
                ", parentMeasure=" + parentMeasure.getMeasureNumber() +
                ", voiceNumber=" + voiceNumber +
                ", staffNumber=" + staffNumber +
                //", parentBeat=" + parentBeat +
                ", notes=" + notes +
                '}';
    }
}
package com.herokuapp.mstogw2.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.herokuapp.mstogw2.chord.Chord;
import com.herokuapp.mstogw2.part.*;

public class Measure {

    private Part parentPart;
    private int measureNumber;
    private int beatsAmount;
    private int beatType; //don't know if I need, but might as well save.
    private Map<Integer, Beat> beats;
    private ArrayList<Chord> chords;

    public Measure(Part parentPart, int measureNumber) { //setBeatDetails is important and will be called after a com.herokuapp.mstogw2.main.Measure is made.
        this.parentPart = parentPart;
        this.measureNumber = measureNumber;
        chords = new ArrayList<>();
    }

    public void setBeatDetails(int beatsAmount, int beatType) {
        this.beatsAmount = beatsAmount;
        this.beatType = beatType;
        beats = new HashMap<>();
        for (int i = 1; i <= beatsAmount; i++) { //between 1 and (beatsAmount) amount of beats
            beats.put(i, new Beat(beatType)); //beats don't really have anything but a beat number and type right now, this will change soon.
        }
    }

    public int getMeasureNumber() {
        return measureNumber;
    }

    public int getBeatsAmount() {
        return beatsAmount;
    }

    public int getBeatType() {
        return beatType;
    }

    public Chord getPreviousChord(Chord chord) {
        int chordIndex = chords.indexOf(chord);
        if (chordIndex > 0) {
            return chords.get(chordIndex-1);
        }
        return null; //there is no previous chord!
    }

    public void addChord(Chord chord) {
        chords.add(chord);
    }

    public Part getParentPart() {
        return parentPart;
    }

    public String chordsToString() {
        StringBuilder cb = new StringBuilder();
        cb.append("{");
        for (Chord chord : chords) {
            cb.append("\n\t\t\t").append(chord);
        } cb.append("}");
        return cb.toString();
    }

    @Override
    public String toString() {
        return "Measure{" +
                "parentPart=" + parentPart.getScorePartId() +
                ", measureNumber=" + measureNumber +
                ", beatsAmount=" + beatsAmount +
                ", beatType=" + beatType +
                //", beats=" + beats +
                ", chords=" + chordsToString() +
                '}';
    }
}
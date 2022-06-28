package com.herokuapp.mstogw2.main;

import com.herokuapp.mstogw2.chord.Chord;
import com.herokuapp.mstogw2.part.Part;

import java.util.*;

public class Measure {

    private Part parentPart;
    private int measureNumber;
    private int beatsAmount = -1;
    private int beatType = -1; //don't know if I need, but might as well save.

    private double beatsDuration = -1;
    private boolean repeatStart = false;
    private boolean repeatEnd = false;
    private double duration = 0;
    private Map<Integer, Beat> beats;
    private ArrayList<Chord> chords;
    private Set<Integer> uniqueStaffNumbers;
    private Set<Integer> uniqueVoiceNumbers;

    public Measure(Part parentPart, int measureNumber) { //setBeatDetails is important and will be called after a com.herokuapp.mstogw2.main.Measure is made.
        this.parentPart = parentPart;
        this.measureNumber = measureNumber;
        chords = new ArrayList<>();
        uniqueStaffNumbers = new HashSet<>();
        uniqueVoiceNumbers = new HashSet<>();
    }

    public void setBeatDetails(int beatsAmount, int beatType) {
        this.beatsAmount = beatsAmount;
        this.beatType = beatType;
        this.beatsDuration = duration / beatsAmount;

        beats = new HashMap<>();
        for (int i = 1; i <= beatsAmount; i++) { //between 1 and (beatsAmount) amount of beats
            beats.put(i, new Beat(parentPart, this, beatType, getBeatsDuration())); //beats don't really have anything but a beat number and type right now, this will change soon.
        }
    }

    public boolean isRepeatStart() {
        return repeatStart;
    }

    public boolean isRepeatEnd() {
        return repeatEnd;
    }

    public void setRepeatStart(boolean repeatStart) {
        this.repeatStart = repeatStart;
    }

    public void setRepeatEnd(boolean repeatEnd) {
        this.repeatEnd = repeatEnd;
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

    public void addStaffNumber(int staffNumber) {
        uniqueStaffNumbers.add(staffNumber);
    }

    public int getStaffAmount() {
        return uniqueStaffNumbers.size();
    }

    public void addVoiceNumber(int voiceNumber) {
        uniqueVoiceNumbers.add(voiceNumber);
    }

    public int getVoiceAmount() {
        return uniqueVoiceNumbers.size();
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
        if(chord.getFirstNote().getStaff() == 1 && chord.getFirstNote().getVoice() == 1) { //measure duration should only count staff 1 and voice 1
            //for now, chords don't have their own staff and voice, so we use the first note of the chord to find that
            addDuration(chord.getDuration());
        }
    }

    public void addDuration(double duration) {
        this.duration += duration;
    }

    public Part getParentPart() {
        return parentPart;
    }

    public double getBeatsDuration() {
        return beatsDuration;
    }

    public void setBeatsDuration(double beatsDuration) {
        this.beatsDuration = beatsDuration;
    }

    public double getDuration() {
        return duration;
    }

    public String chordsToString() {
        StringBuilder cb = new StringBuilder();
        cb.append("{");
        for (Chord chord : chords) {
            cb.append("\n\t\t\t").append(chord);
        } cb.append("}");
        return cb.toString();
    }

    public String beatsToString() {
        StringBuilder bb = new StringBuilder();
        bb.append("{");
        for (Beat beat : beats.values()) {
            bb.append("\n\t\t\t").append(beat);
        } bb.append("}");
        return bb.toString();
    }

    @Override
    public String toString() {
        return "Measure{" +
                "parentPart=" + parentPart.getScorePartId() +
                ", measureNumber=" + measureNumber +
                ", beatsAmount=" + beatsAmount +
                ", beatType=" + beatType +
                ", repeatStart=" + repeatStart +
                ", repeatEnd=" + repeatEnd +
                ", duration=" + duration +
                ", beats=" + beatsToString() +
                ", \n\n\t\t\tchords=" + chordsToString() +
                '}';
    }
}
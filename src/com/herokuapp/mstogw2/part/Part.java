package com.herokuapp.mstogw2.part;

import com.herokuapp.mstogw2.chord.Chord;
import com.herokuapp.mstogw2.chord.Note;
import com.herokuapp.mstogw2.main.Measure;

import java.util.HashMap;
import java.util.Map;

public class Part {

    int scorePartId;
    int midiProgram;
    String instrumentName;
    private int divisions; //helps normalise note durations. Real duration is duration divided by divisions.
    private Map<Integer, Measure> measures; //measures are numbered. Parts DO HAVE MEASURES.

    int notesAmount = 0; //keeps track of how many notes there are.
    private Map<Integer, Note> notes; //Perhaps parts should store all their notes?

    public Part(int scorePartId, int midiProgram, String instrumentName) {
        this.scorePartId = scorePartId;
        this.midiProgram = midiProgram;
        this.instrumentName = instrumentName;
        measures = new HashMap<>();
        notes = new HashMap<>();
    }
    public String getInstrumentName() {
        return instrumentName;
    }

    public int getMeasuresAmount() {
        return measures.size();
    }

    public Measure getMeasure(int measureNumber) {
        return measures.get(measureNumber);
    }

    public Measure getPreviousMeasure(Measure measure) {
        int measureNumber = measure.getMeasureNumber();
        int previousMeasureNumber = measureNumber-1;

        if (previousMeasureNumber < 0) {
            return null; //there is no measure before this one.
        }

        return measures.get(previousMeasureNumber);
    }

    public void addNote(Note note) {
        notesAmount++;
        notes.put(notesAmount, note);
    }

    public Note getPreviousNote(Note note) {
        int noteNumberInPart = note.getNoteNumberInPart();
        int previousNoteNumberInPart = noteNumberInPart-1;

        if (previousNoteNumberInPart < 0) {
            return null; //there is no note before this one.
        }

        return notes.get(previousNoteNumberInPart);
    }

    public Chord getPreviousChord(Note note) {
        System.out.println(getPreviousNote(note));
        return getPreviousNote(note).getParentChord();
    }

    public int getNotesAmount() {
        return notesAmount;
    }
    public void addMeasure(int measureNumber, Measure measure) {
        measures.put(measureNumber, measure);
    }

    public int getScorePartId() {
        return scorePartId;
    }

    public Map<Integer, Measure> getMeasures() {
        return measures;
    }

    public int getDivisions() {
        return divisions;
    }
    public void setDivisions(int divisions) {
        this.divisions = divisions;
    }

    public String measuresToString() {
        StringBuilder mb = new StringBuilder();
        mb.append("{");
        for (Measure measure : measures.values()) {
            mb.append("\n\n\t\t").append(measure.getMeasureNumber()).append("=").append(measure);
        } mb.append("}");
        return mb.toString();
    }

    @Override
    public String toString() {
        return "Part{" +
                "scorePartId=" + scorePartId +
                ", midiProgram=" + midiProgram +
                ", instrumentName='" + instrumentName + '\'' +
                ", divisions=" + divisions +
                ", \n\tmeasures=" + measuresToString() +
                '}';
    }

}
package com.herokuapp.mstogw2.part;

import com.herokuapp.mstogw2.main.Measure;

import java.util.HashMap;
import java.util.Map;

public class Part {

    int scorePartId;
    int midiProgram;
    String instrumentName;
    private int divisions; //helps normalise note durations. Real duration is duration divided by divisions.
    private Map<Integer, Measure> measures; //measures are numbered. Parts DO HAVE MEASURES.

    public Part(int scorePartId, int midiProgram, String instrumentName) {
        this.scorePartId = scorePartId;
        this.midiProgram = midiProgram;
        this.instrumentName = instrumentName;
        //staves = new HashMap<>(); //removing staves for now and adding it as a variable for each chord
        measures = new HashMap<>();
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
            mb.append("\n\t\t").append(measure.getMeasureNumber()).append("=").append(measure);
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
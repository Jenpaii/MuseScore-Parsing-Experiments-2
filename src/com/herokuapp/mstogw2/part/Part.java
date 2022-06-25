package com.herokuapp.mstogw2.part;

import java.util.HashMap;
import java.util.Map;

import com.herokuapp.mstogw2.main.*;

public class Part {

    int scorePartId;
    int midiProgram;
    String instrumentName;
    //private Map<Integer, Staff> staves; //removing staves for now and adding it as a variable for each chord
    private Map<Integer, Measure> measures; //measures are numbered. Parts DO HAVE MEASURES.

    public Part(int scorePartId, int midiProgram, String instrumentName) {
        this.scorePartId = scorePartId;
        this.midiProgram = midiProgram;
        this.instrumentName = instrumentName;
        //staves = new HashMap<>(); //removing staves for now and adding it as a variable for each chord
        measures = new HashMap<>();
    }

    //public void addStaff(int staffNumber, Staff staff) {
        //staves.put(staffNumber, staff); //removing staves for now and adding it as a variable for each chord
    //}
    public String getInstrumentName() {
        return instrumentName;
    }

    //public boolean hasStaff(Staff staff) {
        //return staves.containsValue(staff); //removing staves for now and adding it as a variable for each chord
    //}

    //public int getStavesAmount() { //removing staves for now and adding it as a variable for each chord
        //return staves.size();
    //}

    public int getMeasuresAmount() {
        return measures.size();
    }

    public Measure getMeasure(int measureNumber) {
        return measures.get(measureNumber);
    }

    public Measure getPreviousMeasure(Measure measure) {
        int measureNumber = measure.getMeasureNumber();
        int previousMeasureNumber = measure.getMeasureNumber()-1;
        return measures.get(previousMeasureNumber);
    }

    public void addMeasure(int measureNumber, Measure measure) {
        measures.put(measureNumber, measure);
    }

    //public Staff getStaff(int staffNumber) { //removing staves for now and adding it as a variable for each chord
        //return staves.get(staffNumber);
    //}

    //public Voice getVoice(Staff staff, int voiceNumber) { //removing staves for now and adding it as a variable for each chord
    //    return staff.getVoices().get(voiceNumber);
    //}

    public int getScorePartId() {
        return scorePartId;
    }

/*    public String stavesToString() { //removing staves for now and adding it as a variable for each chord
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Staff staff : staves.values()) {
            sb.append("\n\t").append(staff.getStaffNumber()).append("=").append(staff);
        } sb.append("}");
        return sb.toString();
    }*/

    public Map<Integer, Measure> getMeasures() {
        return measures;
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
                //", \n\tstaves=" + stavesToString() + //removing staves for now and adding it as a variable for each chord
                ", \n\tmeasures=" + measuresToString() +
                '}';
    }
}
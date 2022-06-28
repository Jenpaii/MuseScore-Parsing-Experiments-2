package com.herokuapp.mstogw2.main;

import com.herokuapp.mstogw2.part.Part;

public class Beat {

    private Part parentPart;
    private Measure parentMeasure;

    private int beatType;
    private double duration;

    public Beat(Part parentPart, Measure parentMeasure, int beatType, double duration) {
        this.parentPart = parentPart;
        this.parentMeasure = parentMeasure;
        this.beatType = beatType;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Beat{" +
                "parentPart=" + parentPart.getScorePartId() +
                ", parentMeasure=" + parentMeasure.getMeasureNumber() +
                ", beatType=" + beatType +
                ", duration=" + duration +
                '}';
    }
}
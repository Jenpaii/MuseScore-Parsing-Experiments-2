package com.herokuapp.mstogw2.main;

import com.herokuapp.mstogw2.part.Part;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
public class MeasureHandler { //Completely free from xPath stuff.

    public void setPartMeasures(Score score, Document doc, int partNumber) {

        Part part = score.getPart(partNumber); //parentPart

        //Measures
        NodeList measureNodes = doc.getElementsByTagName("measure");
        int measuresPerPart = measureNodes.getLength() / score.getPartsAmount(); //amount of measures per part is just total amount of measures divided by amount of parts.

        for (int measureNumber = 1; measureNumber <= measuresPerPart; measureNumber++) { //measure numbers start on 1, and no need to get the measure number, just loop and count up {
            part.addMeasure(measureNumber, new Measure(part, measureNumber));
        }

        setPartMeasureBeatDetails(score, doc, partNumber); //gotta set stuff like the beat amount and the beat-type
    }

    public void setPartMeasureBeatDetails(Score score, Document doc, int partNumber) {

        setExistingBeatDetails(score, doc, partNumber); //Not every measure in the doc has beat details.
        fillRemainingBeatDetails(score, partNumber);

    }

    public void setExistingBeatDetails(Score score, Document doc, int partNumber) {

        Part part = score.getPart(partNumber);

        if (partNumber == 1) { //we only do this for part 1, since other parts can just copy part 1.

            NodeList partNodes = doc.getElementsByTagName("part");
            NodeList beatsNodes = null;
            NodeList beatTypeNodes = null;
            for (int a = 0; a < partNodes.getLength(); a++) {
                if ( ((Element) partNodes.item(a)).getAttribute("id").equals("P1")) {
                    beatsNodes = ((Element) partNodes.item(a)).getElementsByTagName("beats");
                    beatTypeNodes = ((Element) partNodes.item(a)).getElementsByTagName("beat-type");
                }
            }

            setPartOneBeatDetails(beatsNodes, beatTypeNodes, part); //as mentioned, only for part 1.

        }
    }

    public void setPartOneBeatDetails(NodeList beatsNodes, NodeList beatTypeNodes, Part part) {
        for (int b = 0; b < beatsNodes.getLength(); b++) { //there are as many beats nodes as there are measures in the part. Every measure has a beat amount.

            String beatsString = beatsNodes.item(b).getTextContent();
            String beatTypeString = beatTypeNodes.item(b).getTextContent();

            String measureNumberWithBeatChange = ((Element) beatsNodes.item(b).getParentNode().getParentNode().getParentNode()).getAttribute("number");
            Measure measure = part.getMeasure(Integer.parseInt(measureNumberWithBeatChange));
            measure.setBeatDetails(Integer.parseInt(beatsString), Integer.parseInt(beatTypeString));

        }
    }

    public void fillRemainingBeatDetails(Score score, int partNumber) {

        Part part = score.getPart(partNumber);

        for (int measureNumber = 1; measureNumber <= part.getMeasuresAmount(); measureNumber++) {

            if (partNumber == 1) { //only part 1 measures need to do this...
                Measure measure = part.getMeasure(measureNumber);
                if (measure.getBeatsAmount() == -1) { //Anything other than -1 means the beat details have already been assigned.
                    Measure previousMeasure = part.getPreviousMeasure(measure); //so we assign it the same as what the previous measure had.
                    int partMeasureBeatsAmount = previousMeasure.getBeatsAmount();
                    int partMeasureBeatType = previousMeasure.getBeatType();
                    measure.setBeatDetails(partMeasureBeatsAmount, partMeasureBeatType);
                }
            } else { // other parts can just copy the beat details of part 1. Nothing in these is assigned yet.
                copyPartOneBeatDetails(score, part, measureNumber);
            }

        }
    }

    public void copyPartOneBeatDetails(Score score, Part part, int measureNumber) {
        Part partOne = score.getPart(1);
        Measure partOneMeasure = partOne.getMeasure(measureNumber);

        int partMeasureBeatsAmount = partOneMeasure.getBeatsAmount(); //copying from part 1
        int partMeasureBeatType = partOneMeasure.getBeatType(); //copying from part 1

        Measure measure = part.getMeasure(measureNumber);
        measure.setBeatDetails(partMeasureBeatsAmount, partMeasureBeatType);
    }

}

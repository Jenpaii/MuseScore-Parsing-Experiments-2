package com.herokuapp.mstogw2.main;

import com.herokuapp.mstogw2.part.Part;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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

        setPartMeasureDetails(score, doc, partNumber); //gotta set stuff like the beat amount and the beat-type
    }

    public void setPartMeasureRepeatDetail(NodeList measureNodes, int measureIndex, Measure measure) {
        //sets measures to repeat start and/or end
        if (((Element) measureNodes.item(measureIndex)).getElementsByTagName("repeat").item(0) != null) {
            String measureRepeat = ((Element) ((Element) measureNodes.item(measureIndex)).getElementsByTagName("repeat").item(0)).getAttribute("direction");
            if (measureRepeat.equals("forward")) {
                measure.setRepeatStart(true);
            } else if (measureRepeat.equals("backward")) {
                measure.setRepeatEnd(true);
            }
        }
    }

    public void setPartMeasureDetails(Score score, Document doc, int partNumber) {

        setExistingDetails(score, doc, partNumber); //Not every measure in the doc has beat and repeat details.
        fillRemainingDetails(score, partNumber);

    }

    public void setExistingDetails(Score score, Document doc, int partNumber) {

        Part part = score.getPart(partNumber);

        if (partNumber == 1) { //we only do this for part 1, since other parts can just copy part 1.

            NodeList partNodes = doc.getElementsByTagName("part");
            int partIndex = partNumber-1;

            NodeList beatsNodes = ((Element) partNodes.item(partIndex)).getElementsByTagName("beats");
            NodeList beatTypeNodes = ((Element) partNodes.item(partIndex)).getElementsByTagName("beat-type");
            NodeList repeatNodes = ((Element) partNodes.item(partIndex)).getElementsByTagName("repeat");

            setPartOneDetails(beatsNodes, beatTypeNodes, repeatNodes, part); //as mentioned, only for part 1.

        }
    }

    public void setPartOneDetails(NodeList beatsNodes, NodeList beatTypeNodes, NodeList repeatNodes, Part part) {
        setPartOneBeatDetails(beatsNodes, beatTypeNodes, part);
        setPartOneRepeatDetails(repeatNodes, part);
    }

    public void setPartOneBeatDetails(NodeList beatsNodes, NodeList beatTypeNodes, Part part) {
        for (int b = 0; b < beatsNodes.getLength(); b++) {

            Node beatNode = beatsNodes.item(b);
            String beatsString = beatNode.getTextContent();

            Node beatTypeNode = beatTypeNodes.item(b);
            String beatTypeString = beatTypeNode.getTextContent();

            String measureNumberWithBeatChange = ((Element) beatNode.getParentNode().getParentNode().getParentNode()).getAttribute("number");
            Measure measure = part.getMeasure(Integer.parseInt(measureNumberWithBeatChange));
            measure.setBeatDetails(Integer.parseInt(beatsString), Integer.parseInt(beatTypeString));

        }
    }

    public void setPartOneRepeatDetails(NodeList repeatNodes, Part part) {
        for (int b = 0; b < repeatNodes.getLength(); b++) {

            Node repeatNode = repeatNodes.item(b);
            String repeatString = ((Element) repeatNode).getAttribute("direction");

            String measureNumberWithRepeatChange = ((Element) repeatNode.getParentNode().getParentNode()).getAttribute("number");
            Measure measure = part.getMeasure(Integer.parseInt(measureNumberWithRepeatChange));

            if (repeatString.equals("forward")) {
                measure.setRepeatStart(true);
            } else if (repeatString.equals("backward")) {
                measure.setRepeatEnd(true);
            }

        }
    }

    public void fillRemainingDetails(Score score, int partNumber) {

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
            } else { // other parts can just copy the details of part 1. No details in these measures are assigned yet.
                copyPartOneDetails(score, part, measureNumber);
            }

        }
    }

    public void copyPartOneDetails(Score score, Part part, int measureNumber) {
        Part partOne = score.getPart(1);
        Measure partOneMeasure = partOne.getMeasure(measureNumber);

        int partMeasureBeatsAmount = partOneMeasure.getBeatsAmount(); //copying from part 1
        int partMeasureBeatType = partOneMeasure.getBeatType(); //copying from part 1
        boolean repeatStart = partOneMeasure.isRepeatStart();
        boolean repeatEnd = partOneMeasure.isRepeatEnd();

        Measure measure = part.getMeasure(measureNumber);
        measure.setBeatDetails(partMeasureBeatsAmount, partMeasureBeatType);
        measure.setRepeatStart(repeatStart);
        measure.setRepeatEnd(repeatEnd);
    }

}

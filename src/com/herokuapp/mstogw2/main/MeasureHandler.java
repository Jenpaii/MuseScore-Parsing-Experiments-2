package com.herokuapp.mstogw2.main;

import com.herokuapp.mstogw2.part.Part;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Map;

public class MeasureHandler { //Completely free from xPath stuff.

    public void setPartMeasures(Score score, int partNumber, int measuresPerPart) {

        Part part = score.getPart(partNumber); //parentPart

        for (int measureNumber = 1; measureNumber <= measuresPerPart; measureNumber++) { //measure numbers start on 1, and no need to get the measure number, just loop and count up {
            part.addMeasure(measureNumber, new Measure(part, measureNumber));
        }

    }

    public void setPartMeasureDetails(Score score, Document doc, int partNumber) {

        setExistingDetails(score, doc, partNumber); //Not every measure in the doc has beat and repeat details.
        fillRemainingDetails(score, partNumber);

    }

    public void setExistingDetails(Score score, Document doc, int partNumber) {

        if (partNumber == 1) { //we only do this for part 1, since other parts can just copy part 1.

            Part part = score.getPart(partNumber);

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

        //For-loop
        int beatsNodesLength = beatsNodes.getLength(); //faster to only set this once

        for (int b = 0; b < beatsNodesLength; b++) {

            Node beatNode = beatsNodes.item(b);
            String beatsString = beatNode.getTextContent();

            Node beatTypeNode = beatTypeNodes.item(b);
            String beatTypeString = beatTypeNode.getTextContent();

            String measureNumberWithBeatChange = ((Element) beatNode.getParentNode().getParentNode().getParentNode()).getAttribute("number");
            Measure measure = part.getMeasure(Integer.parseInt(measureNumberWithBeatChange));
            measure.setBeatDetails(Integer.parseInt(beatsString), Integer.parseInt(beatTypeString));

        }




        //While-loop....
    }

    public void setPartOneRepeatDetails(NodeList repeatNodes, Part part) {

        //For-loop
        int repeatNodesLength = repeatNodes.getLength(); //faster to only set this once

        for (int b = 0; b < repeatNodesLength; b++) {

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

    public void setAllMeasuresChords(Score score, Document doc, int partNumber) {
        Part part = score.getPart(partNumber);
        Map<Integer, Measure> measures = part.getMeasures();

        NodeList partNodes = doc.getElementsByTagName("part");

        int partIndex = partNumber-1;
        //finds the measureNodes of partNumber. This used to be a loop. Why?
        NodeList partMeasureNodes = ((Element) partNodes.item(partIndex)).getElementsByTagName("measure"); //this used to be in setMeasureChordDetails and was running
        //hundreds of times for no reason, slowing the whole program down massively.

        for (int measureNumber = 1; measureNumber <= measures.size(); measureNumber++) {
            setMeasureChordDetails(score, partNumber, measureNumber, partMeasureNodes);
        }
    }

    public void setMeasureChordDetails(Score score, int partNumber, int measureNumber, NodeList partMeasureNodes) {

        Measure measure = score.getPart(partNumber).getMeasure(measureNumber);

        int measureIndex = measureNumber-1;
        //finds the noteNodes of measureNumber. This used to be a loop. Why?
        NodeList measureNoteNodes = ((Element) partMeasureNodes.item(measureIndex)).getElementsByTagName("note");

        setEachMeasureChordDetail(measure, measureNoteNodes);

    }

    public void setEachMeasureChordDetail(Measure measure, NodeList measureNoteNodes) {

        ChordHandler chorder = new ChordHandler();

        //For-loop
        for (int i = 0; i < measureNoteNodes.getLength(); i++) { //Has to stay, because removeChild changes the length.

            Node node = measureNoteNodes.item(i);

            node.getParentNode().removeChild(node); //make faster? Yes a bit. Decrement i to reflect an item being removed?
            i--; //Yeah, this was necessary because some notes were being skipped when the indexing changed.

            Element note = (Element) node;

            chorder.addChordToMeasure(note, measure);

        }

/*        Node measureNoteNode = measureNoteNodes.item(0); //While-loop. Slower than the for-loop for some reason.

        while (measureNoteNode != null) {

            measureNoteNode.getParentNode().removeChild(measureNoteNode);

            if (measureNoteNode.getNodeName().equals("note")) {
                Element note = (Element) measureNoteNode;
                chorder.addChordToMeasure(note, measure);
            }

            measureNoteNode = measureNoteNode.getNextSibling();
        }*/

    }

}

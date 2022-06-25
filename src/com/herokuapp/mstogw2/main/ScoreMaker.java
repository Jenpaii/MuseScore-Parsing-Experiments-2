package com.herokuapp.mstogw2.main;

import com.herokuapp.mstogw2.part.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Map;

public class ScoreMaker {

    private final Score score;
    public ScoreMaker() {
        score = new Score();
    }

    public void setScore(Document doc) {

        setScoreParts(doc);
        setPartDetails(doc);

    }

    public void setScoreParts(Document doc) {

        NodeList scorePartNodes = doc.getElementsByTagName("score-part");

        for (int i = 0; i < scorePartNodes.getLength(); i++) {

            Node node = scorePartNodes.item(i);
            Element element = (Element) node;
            int scorePartId = Integer.parseInt(element.getAttribute("id").split("P")[1]);
            int midiProgram = Integer.parseInt(element.getElementsByTagName("midi-program").item(0).getTextContent());
            Part part = createPart(scorePartId, midiProgram);

            score.addPart(part);
        }
    }

    private Part createPart(int scorePartId, int midiProgram) {

        return switch (midiProgram) {
            case 1 -> new MinstrelPart(scorePartId, midiProgram);
            case 15 -> new BellPart(scorePartId, midiProgram);
            case 16 -> new ChoirBellPart(scorePartId, midiProgram);
            case 25 -> new LutePart(scorePartId, midiProgram);
            case 34 -> new BassPart(scorePartId, midiProgram);
            case 47 -> new HarpPart(scorePartId, midiProgram);
            case 57 -> new HornPart(scorePartId, midiProgram);
            case 59 -> new VerdarachPart(scorePartId, midiProgram);
            case 74 -> new FlutePart(scorePartId, midiProgram);
            case 115 -> new DrumPart(scorePartId, midiProgram);
            default -> new Part(scorePartId, midiProgram, "Unknown");
        };
    }

    public void setPartDetails(Document doc) {

        MeasureHandler measureHandler = new MeasureHandler();
        NodeList partNodes = doc.getElementsByTagName("part");

        for (int partNumber = 1; partNumber <= partNodes.getLength(); partNumber++) { //starts on 1 because it's equal to the score part ID, which starts on 1. For each part...
            measureHandler.setPartMeasures(score, doc, partNumber); //set the parts' measures.
            setAllMeasuresChords(doc, partNumber);
        }
    }

    public void setAllMeasuresChords(Document doc, int partNumber) {
        Part part = score.getPart(partNumber);
        Map<Integer, Measure> measures = part.getMeasures();

        for (int measureNumber = 1; measureNumber <= measures.size(); measureNumber++) {
            setMeasureChordDetails(partNumber, measureNumber, doc);
        }
    }

    public void setMeasureChordDetails(int partNumber, int measureNumber, Document doc) {

        Measure measure = score.getPart(partNumber).getMeasure(measureNumber);

        NodeList partNodes = doc.getElementsByTagName("part");
        NodeList partMeasureNodes = null;
        NodeList measureNoteNodes = null;

        for (int a = 0; a < partNodes.getLength(); a++) { //finds the measureNodes of partNumber
            if ( ((Element) partNodes.item(a)).getAttribute("id").equals("P" +  String.valueOf(partNumber)  )) {
                partMeasureNodes = ((Element) partNodes.item(a)).getElementsByTagName("measure");
            }
        }

        for (int b = 0; b < partMeasureNodes.getLength(); b++) { //finds the noteNodes of measureNumber
            if ( ((Element) partMeasureNodes.item(b)).getAttribute("number").equals( String.valueOf(measureNumber) )) {
                measureNoteNodes = ((Element) partMeasureNodes.item(b)).getElementsByTagName("note");
            }
        }

        setEachMeasureChordDetail(measure, measureNoteNodes);

    }

    public void setEachMeasureChordDetail(Measure measure, NodeList measureNoteNodes) {

        ChordHandler chorder = new ChordHandler();

        for (int i = 0; i < measureNoteNodes.getLength(); i++) {

            Node node = measureNoteNodes.item(i);
            Element note = (Element) node;

            chorder.addChordToMeasure(note, measure);

        }
    }

    public Score getScore() {
        return score;
    }

}

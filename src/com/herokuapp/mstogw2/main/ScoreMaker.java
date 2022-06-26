package com.herokuapp.mstogw2.main;

import com.herokuapp.mstogw2.part.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

        int scorePartNodesLength = scorePartNodes.getLength(); //go faster

        for (int i = 0; i < scorePartNodesLength; i++) {

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
        int partNodesLength = partNodes.getLength(); //go faster

        for (int partNumber = 1; partNumber <= partNodesLength; partNumber++) { //starts on 1 because it's equal to the score part ID, which starts on 1. For each part...
            measureHandler.setPartMeasures(score, doc, partNumber); //set the parts' measures.
            measureHandler.setAllMeasuresChords(score, doc, partNumber); //move this into measureHandler? measureHandler.setAllMeasuresChords(doc, partNumber);
        }
    }

    public Score getScore() {
        return score;
    }

}

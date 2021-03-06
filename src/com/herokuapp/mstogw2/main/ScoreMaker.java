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

      //For-loop
        int scorePartNodesLength = scorePartNodes.getLength(); //go faster
        for (int i = 0; i < scorePartNodesLength; i++) {

            Node node = scorePartNodes.item(i);
            Element element = (Element) node;
            int scorePartId = Integer.parseInt(element.getAttribute("id").split("P")[1]);

            //Not the greatest fix, but if the midi-program is null, it was probably a drum.
            int midiProgram = element.getElementsByTagName("midi-program").item(0) == null ?
                    115 : Integer.parseInt(element.getElementsByTagName("midi-program").item(0).getTextContent());

            Part part = createPart(scorePartId, midiProgram);

            score.addPart(part);
        }


/*        Node currentNode = scorePartNodes.item(0); //While-loop instead! But it's a bit slower for some reason?
        while (currentNode != null) {
            if (currentNode.getNodeName().equals("score-part")) {
                Element element = (Element) currentNode;
                int scorePartId = Integer.parseInt(element.getAttribute("id").split("P")[1]);
                int midiProgram = Integer.parseInt(element.getElementsByTagName("midi-program").item(0).getTextContent());
                Part part = createPart(scorePartId, midiProgram);
                score.addPart(part);
            }

            currentNode = currentNode.getNextSibling();
        }*/
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
        int partNodesLength = score.getPartsAmount();

        //Divisions
        int divisions = getDivisions(doc);

        //Measures
        NodeList measureNodes = doc.getElementsByTagName("measure");
        int measuresPerPart = measureNodes.getLength() / score.getPartsAmount(); //amount of measures per part is just total amount of measures divided by amount of parts.

        for (int partNumber = 1; partNumber <= partNodesLength; partNumber++) { //starts on 1 because it's equal to the score part ID, which starts on 1. For each part...
            score.getPart(partNumber).setDivisions(divisions);
            measureHandler.setPartMeasures(score, partNumber, measuresPerPart); //set the parts' measures.
            measureHandler.setAllMeasuresChords(score, doc, partNumber);
            measureHandler.setPartMeasureDetails(score, doc, partNumber); //this used to be a part of setPartMeasures. Does it still work like this?
        }
    }

    public int getDivisions(Document doc) {
        NodeList partDivisionsNodes = doc.getElementsByTagName("divisions");
        Node divisionsNode = partDivisionsNodes.item(0); //it looks to me like every part has the same amount of divisions. So we just need to check one node.
        String divisionsString = divisionsNode.getTextContent();
        return Integer.parseInt(divisionsString);
    }

    public Score getScore() {
        return score;
    }

}

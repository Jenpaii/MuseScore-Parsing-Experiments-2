package com.herokuapp.mstogw2.main;

import com.herokuapp.mstogw2.chord.Chord;
import com.herokuapp.mstogw2.chord.RestChord;
import com.herokuapp.mstogw2.chord.RestNote;
import com.herokuapp.mstogw2.part.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.util.Map;

public class ScoreMaker {

    private final Score score;
    public ScoreMaker() {
        score = new Score();
    }

    public void setScore(Document doc) throws XPathExpressionException {

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();

        setScoreParts(doc, xPath);
        setAllParts(doc, xPath);

    }

    public void setScoreParts(Document doc, XPath xPath) throws XPathExpressionException {

        NodeList scorePartIdNodes = getNodesWithExpr(doc,
                xPath.compile("//part-list/score-part/@id"));

        NodeList midiProgramNodes = getNodesWithExpr(doc,
                xPath.compile("//part-list/score-part/midi-instrument/midi-program/text()"));

        for (int i = 0; i < scorePartIdNodes.getLength(); i++) {
            int scorePartId = getScorePartId(scorePartIdNodes, i);
            int midiProgram = getMidiProgram(midiProgramNodes, i);
            Part part = createPart(scorePartId, midiProgram);
            score.addPart(part);
        }
    }

    public int getScorePartId(NodeList scorePartIdNodes, int index) {
        String scorePartIdString = scorePartIdNodes.item(index).getNodeValue();
        return Integer.parseInt(scorePartIdString.split("P")[1]);
    }

    public int getMidiProgram(NodeList midiProgramNodes, int index) {
        String midiProgramString;
        if (midiProgramNodes.item(index) != null) {
            midiProgramString = midiProgramNodes.item(index).getNodeValue();
        } else { //will be null if it's a Drum, sadly. But there might also be other non-GW2 instruments that return null.
            midiProgramString = "115"; //setting it to Drum manually. Not the best solution but yeah... works for now...
        }
        return Integer.parseInt(midiProgramString);
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

    public void setAllParts(Document doc, XPath xPath) throws XPathExpressionException {


        NodeList partNodes = getNodesWithExpr(doc,
                xPath.compile("//part"));

        for (int i = 1; i <= partNodes.getLength(); i++) { //starts on 1 because it's equal to the score part ID, which starts on 1. For each part...
            //setPartStaves(doc, xPath, i); //removing staves for now and adding it as a variable for each chord
            setPartMeasures(doc, xPath, i); //set the parts' measures.
            setAllMeasuresChords(doc, xPath, i);

        }

    }

    public void setPartMeasures(Document doc, XPath xPath, int index) throws XPathExpressionException {

        Part part = score.getPart(index); //parentPart

        //Measures
        String partMeasureNodesExpr = "//part[@id='P" + index + "'" + "]/measure";
        NodeList partMeasureNodes = getNodesWithExpr(doc,
                xPath.compile(partMeasureNodesExpr));
        int partMeasureNodesAmount = partMeasureNodes.getLength();

        for (int i = 1; i <= partMeasureNodesAmount; i++) { //measure numbers start on 1, and no need to get the measure number, just loop and count {
            part.addMeasure(i, new Measure(part, i));
        }

        setPartMeasureDetails(doc, xPath, index); //gotta set stuff like the beat amount and the beat-type

    }

    public void setPartMeasureDetails(Document doc, XPath xPath, int index) throws XPathExpressionException {
        Part part = score.getPart(index);
        for (int i = 1; i <= part.getMeasuresAmount(); i++) { //i is the measureNumber
            //com.herokuapp.mstogw2.main.Measure amount of beats
            String partMeasureBeatsNodesExpr = "//part[@id='P" + index + "'" + "]/" +
                    "measure[@number='" + i + "'" + "]/" +
                    "attributes/time/beats/text()";
            NodeList partMeasureBeatsNodes = getNodesWithExpr(doc,
                    xPath.compile(partMeasureBeatsNodesExpr));

            //com.herokuapp.mstogw2.main.Measure beat type
            String partMeasureBeatTypeNodesExpr = "//part[@id='P" + index + "'" + "]/" +
                    "measure[@number='" + i + "'" + "]/" +
                    "attributes/time/beat-type/text()";
            NodeList partMeasureBeatTypeNodes = getNodesWithExpr(doc,
                    xPath.compile(partMeasureBeatTypeNodesExpr));

            Measure measure = part.getMeasure(i);
            setMeasureBeatDetails(part, partMeasureBeatsNodes, partMeasureBeatTypeNodes, measure);
        }
    }

    public void setMeasureBeatDetails(Part part, NodeList partMeasureBeatsNodes, NodeList partMeasureBeatTypeNodes, Measure measure) {
        int partMeasureBeatsAmount;
        int partMeasureBeatType;

        if (partMeasureBeatsNodes.item(0) != null) { //new beat details spotted
            partMeasureBeatsAmount = Integer.parseInt(partMeasureBeatsNodes.item(0).getNodeValue());
            partMeasureBeatType = Integer.parseInt(partMeasureBeatTypeNodes.item(0).getNodeValue());
        } else { //beat details need to be the same as previous measure
            Measure previousMeasure = part.getPreviousMeasure(measure);
            partMeasureBeatsAmount = previousMeasure.getBeatsAmount();
            partMeasureBeatType = previousMeasure.getBeatType();
        }
        measure.setBeatDetails(partMeasureBeatsAmount, partMeasureBeatType);
    }

    public void setAllMeasuresChords(Document doc, XPath xPath, int index) throws XPathExpressionException {
        Part part = score.getPart(index);
        Map<Integer, Measure> measures = part.getMeasures();

        for (int i = 1; i <= measures.size(); i++) {
            setMeasureChordDetails(index, i, doc, xPath);
        }
    }

    public void setMeasureChordDetails(int index, int i, Document doc, XPath xPath) throws XPathExpressionException {

        Measure measure = score.getPart(index).getMeasure(i);

        String measureNoteExpr = "//part[@id='P" + index + "'" + "]/" +
                "measure[@number='" + i + "'" + "]/note";
        NodeList measureNoteNodes = getNodesWithExpr(doc, xPath.compile(measureNoteExpr));

        setEachMeasureChordDetail(measure, measureNoteNodes);

    }

    public void setEachMeasureChordDetail(Measure measure, NodeList measureNoteNodes) {

        for (int i = 0; i < measureNoteNodes.getLength(); i++) {
            Node node = measureNoteNodes.item(i);
            Element note = (Element) node;

            addChordToMeasure(note, measure);

        }
    }

    public NodeList getNodesWithExpr(Document doc, XPathExpression expr) {
        try {
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            return (NodeList) result;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
    public void addChordToMeasure(Element note, Measure measure) {

        Part parentPart = measure.getParentPart();

        int noteVoice = handleNoteVoice(note);
        int noteStaff = handleNoteStaff(note);
        double noteDuration = handleNoteDuration(note);
        if (!handleNoteStep(note)) { //if this returns false, it means it was a rest. so just continue to the next loop.
            addRestChordToMeasure(parentPart, measure, noteVoice, noteStaff, noteDuration); }
        else {
            handleNoteAlter(note);
            handleNoteOctave(note);
            handleNoteGrace(note);
            handleNoteChord(note);
        }

        System.out.println("-------------");

        measure.addChord(new Chord(parentPart, measure, noteVoice, noteStaff));
    }

    public void addRestChordToMeasure(Part parentPart, Measure measure, int noteVoice, int noteStaff, double noteDuration) {
        Chord restChord = new RestChord(parentPart, measure, noteVoice, noteStaff);
        restChord.addNote(new RestNote(parentPart, measure, noteVoice, noteStaff, restChord, noteDuration));
        measure.addChord(restChord);
    }
    public int handleNoteVoice(Element note) {
        System.out.println("Voice: " + note.getElementsByTagName("voice").item(0).getTextContent());
        String voiceNumberString = note.getElementsByTagName("voice").item(0).getTextContent();
        int voiceNumber = Integer.parseInt(voiceNumberString);
        return (voiceNumber % 4) == 0 ?  1 : (voiceNumber % 4); // staves higher than staff 1 have voice numbers that are higher than 4
    }

    public int handleNoteStaff(Element note) {
        if (note.getElementsByTagName("staff").item(0) != null) {
            System.out.println("Staff: " + note.getElementsByTagName("staff").item(0).getTextContent());
            String staffNumberString = note.getElementsByTagName("staff").item(0).getTextContent();
            return Integer.parseInt(staffNumberString);
        } else {
            System.out.println("Staff: N/A");
            return 1; //default staff number
        }
    }

    public double handleNoteDuration(Element note) {
        if (note.getElementsByTagName("duration").item(0) != null) {
            System.out.println("Duration: " + note.getElementsByTagName("duration").item(0).getTextContent());
            String noteDurationString = note.getElementsByTagName("duration").item(0).getTextContent();
            return Double.parseDouble(noteDurationString);
        } else {
            System.out.println("Duration: N/A");
            return 0.0; //only grace notes have this duration
        }
    }

    public boolean handleNoteStep(Element note) {
        if (note.getElementsByTagName("step").item(0) != null) {
            System.out.println("Pitch Step: " + note.getElementsByTagName("step").item(0).getTextContent());
            return true;
        } else { System.out.println("Pitch Step: N/A");
            System.out.println("Pitch Alter: N/A");
            System.out.println("Pitch Octave: N/A");
            System.out.println("Grace: N/A");
            System.out.println("Chord: N/A");
            return false; }
    }

    public void handleNoteAlter(Element note) {
        if (note.getElementsByTagName("alter").item(0) != null) {
            System.out.println("Pitch Alter: " + note.getElementsByTagName("alter").item(0).getTextContent());
        } else { System.out.println("Pitch Alter: N/A"); }
    }

    public void handleNoteOctave(Element note) {
        if (note.getElementsByTagName("octave").item(0) != null) {
            System.out.println("Pitch Octave: " + note.getElementsByTagName("octave").item(0).getTextContent()); }
    }

    public boolean handleNoteGrace(Element note) {
        if (note.getElementsByTagName("grace").item(0) != null) {
            System.out.println("Grace: yes");
            return true;
        } else { System.out.println("Grace: N/A");
            return false; }
    }

    public boolean handleNoteChord(Element note) {
        if (note.getElementsByTagName("chord").item(0) != null) {
            System.out.println("Chord: yes");
            return true;
        } else { System.out.println("Chord: N/A");
            return false; }
    }

    public Score getScore() {
        return score;
    }

}

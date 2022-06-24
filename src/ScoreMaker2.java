import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ScoreMaker2 {

    private final Score score;

    public ScoreMaker2() {
        score = new Score();

    }

    public void parseFile(String fileName) {
        try {
            File inputFile = new File(fileName + ".musicxml");
            InputStream inputFileStream = new FileInputStream(inputFile);
            parseFile(".musicxml", inputFileStream);
        } catch (IOException | XPathExpressionException e) {
            e.printStackTrace();
        }

    }

    public void parseFile(String fileType, InputStream fileContent) throws XPathExpressionException {
        Document doc = getDocument(fileType, fileContent);
        if (doc != null) {

            setScore(doc);

        }
    }

    public Document getDocument(String fileType, InputStream fileContent) {
        try {
            InputStream inputStream = getFilesInputStream(fileType, fileContent);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            if (inputStream == null) { return null; }
            else {
                return dBuilder.parse(inputStream);
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream getFilesInputStream(String fileType, InputStream fileContent) {
        try {
            if (fileType.equalsIgnoreCase(".musicxml")) {
                return fileContent;
            } else if (fileType.equalsIgnoreCase(".mxl")) {
                ZipInputStream zipInputStream = new ZipInputStream(fileContent);
                ZipEntry zipEntry;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if(!zipEntry.getName().contains("container") && zipEntry.getName().contains(".xml")) {
                        break;
                    }
                } return zipInputStream;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } return null;
    }

    public NodeList getNodesWithExpr(Document doc, XPathExpression expr) {
        try {
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            return (NodeList) result;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
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
        String midiProgramString = midiProgramNodes.item(index).getNodeValue();
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
            setPartStaves(doc, xPath, i); //set parts' staves. (and voices).
            setPartMeasures(doc, xPath, i); //set the parts' measures.

        }

    }

    public void setPartStaves(Document doc, XPath xPath, int index) throws XPathExpressionException {

        Part part = score.getPart(index);
        int partStavesAmount = 1; //default amount of staves

        String partMeasureStavesNodesExpr = "//part[@id='P" + index + "'" + "]/measure/attributes/staves/text()";
        NodeList partMeasureStavesNodes = getNodesWithExpr(doc,
                xPath.compile(partMeasureStavesNodesExpr));

        if (partMeasureStavesNodes.item(0) != null) {
            partStavesAmount = Integer.parseInt(partMeasureStavesNodes.item(0).getNodeValue());
        }

        addStavesToPart(part, partStavesAmount);

    }

    public void addStavesToPart(Part part, int partStavesAmount) {
        for (int i = 1; i <= partStavesAmount; i++) { //staff numbers start on 1
            part.addStaff(i, new Staff(i, part));
        }
    }

    public void setPartMeasures(Document doc, XPath xPath, int index) throws XPathExpressionException {

        Part part = score.getPart(index); //parentPart
        //int staffNumber = 1; // measures don't have a staff number, the chords/notes do.
        //int voiceNumber; // measures don't have a voice number, the chords/notes do.

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
            //Measure beats
            String partMeasureBeatsNodesExpr = "//part[@id='P" + index + "'" + "]/" +
                    "measure[@number='" + i + "'" + "]/" +
                    "attributes/time/beats/text()";
            NodeList partMeasureBeatsNodes = getNodesWithExpr(doc,
                    xPath.compile(partMeasureBeatsNodesExpr));

            //Measure beat type
            String partMeasureBeatTypeNodesExpr = "//part[@id='P" + index + "'" + "]/" +
                    "measure[@number='" + i + "'" + "]/" +
                    "attributes/time/beat-type/text()";
            NodeList partMeasureBeatTypeNodes = getNodesWithExpr(doc,
                    xPath.compile(partMeasureBeatTypeNodesExpr));

            setMeasureBeatDetails(part, partMeasureBeatsNodes, partMeasureBeatTypeNodes, i);
        }







    }

    public void setMeasureBeatDetails(Part part, NodeList partMeasureBeatsNodes, NodeList partMeasureBeatTypeNodes, int measureNumber) {
        Measure measure = part.getMeasure(measureNumber);
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



    public Score getScore() {
        return score;
    }

    @Override
    public String toString() {
        return score.toString();
    }
}

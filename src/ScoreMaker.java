import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ScoreMaker {

    private final Score score;

    public ScoreMaker() {
        score = new Score();
    }

    public void parseFile(String fileName) {
        try {
            File inputFile = new File(fileName + ".musicxml");
            InputStream inputFileStream = new FileInputStream(inputFile);
            parseFile(".musicxml", inputFileStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void parseFile(String fileType, InputStream fileContent) {
        Document doc = getDocument(fileType, fileContent);
        if (doc != null) {

            NodeList scorePartNodes = doc.getElementsByTagName("score-part");
            setScoreParts(scorePartNodes);

            //parseInstruments(doc);
            //parseAllInstrumentMeasures(doc);
            //setAllChordsTimeOfArrival();
        }
    }

    public Document getDocument(String fileType, InputStream fileContent) {
        try {
            InputStream inputStream = getFilesInputStream(fileType, fileContent);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
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

    public void setScoreParts(NodeList scorePartNodes) {
        for (int i = 0; i < scorePartNodes.getLength(); i++) {
            Node scorePartNode = scorePartNodes.item(i);
            Element scorePartElement = (Element) scorePartNode;
            int scorePartId = getScorePartId(scorePartElement);
            int midiProgram = getScoreMidiProgram(scorePartElement);
            Part scorePart = createPart(scorePartId, midiProgram);
            score.addPart(scorePart);
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

    public int getScorePartId(Element scorePartElement) {
        String scorePartIdString = scorePartElement.getAttribute("id");
        String scorePartIdStringWithoutP = scorePartIdString.split("P")[1];
        return Integer.parseInt(scorePartIdStringWithoutP);
    }

    public int getScoreMidiProgram(Element scorePartElement) {
        String midiProgramString = scorePartElement.getElementsByTagName("midi-program").item(0).getTextContent();
        return Integer.parseInt(midiProgramString);
    }

    public Score getScore() {
        return score;
    }

    @Override
    public String toString() {
        return score.toString();
    }
}

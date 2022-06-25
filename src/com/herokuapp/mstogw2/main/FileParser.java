package com.herokuapp.mstogw2.main;

import org.w3c.dom.Document;
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

public class FileParser {

    public Document parseFile(String fileName) {
        try {
            File inputFile = new File(fileName + ".musicxml");
            InputStream inputFileStream = new FileInputStream(inputFile);
            return parseFile(".musicxml", inputFileStream);
        } catch (IOException | XPathExpressionException e) {
            e.printStackTrace();
            return null; //it hit an error anyways...
        }
    }

    public Document parseFile(String fileType, InputStream fileContent) throws XPathExpressionException {
        Document doc = getDocument(fileType, fileContent);
        return doc; //doc can be null, keep this in mind.
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

}
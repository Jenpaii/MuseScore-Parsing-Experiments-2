package com.herokuapp.mstogw2.main;

import org.w3c.dom.Document;

import javax.xml.xpath.XPathExpressionException;

public class MainProgram {

    public static void main(String[] args) throws XPathExpressionException {

        FileParser fileParser = new FileParser();
        ScoreMaker scoreMaker = new ScoreMaker();

        Document doc = fileParser.parseFile("D:\\Coding Stuff\\Musescore Parsing Experiments\\Test");

        scoreMaker.setScore(doc);

        System.out.println(scoreMaker.getScore());

    }

}

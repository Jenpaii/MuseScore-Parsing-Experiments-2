package com.herokuapp.mstogw2.main;

import org.w3c.dom.Document;

import javax.xml.xpath.XPathExpressionException;

public class MainProgram {

    public static void main(String[] args) throws XPathExpressionException {

        final long startTime = System.currentTimeMillis();

        String file = "D:\\Coding Stuff\\MuseScore Parsing Experiments\\";
        file += "bosun"; // change the file name here.

        FileParser fileParser = new FileParser();
        ScoreMaker scoreMaker = new ScoreMaker();

        Document doc = fileParser.parseFile(file); //the file parser gives us the doc file
        scoreMaker.setScore(doc); //the score maker makes a score with the doc file

        System.out.println(scoreMaker.getScore());

        final long endTime = System.currentTimeMillis();
        System.out.println("\n-------------------------------\nTotal execution time: " + (endTime - startTime) + "ms");


    }

}

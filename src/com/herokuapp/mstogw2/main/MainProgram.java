package com.herokuapp.mstogw2.main;

import org.w3c.dom.Document;

import javax.xml.xpath.XPathExpressionException;

public class MainProgram {

    public static void main(String[] args) throws XPathExpressionException {

        FileParser fp = new FileParser();
        ScoreMaker sm = new ScoreMaker();

        Document doc = fp.parseFile("D:\\Coding Stuff\\Musescore Parsing Experiments\\Test");

        sm.setScore(doc);

        System.out.println(sm.getScore());

    }

}

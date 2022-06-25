package com.herokuapp.mstogw2.main;

import com.herokuapp.mstogw2.chord.*;
import com.herokuapp.mstogw2.part.*;

import org.w3c.dom.Element;

public class ChordHandler {

    public void addChordToMeasure(Element note, Measure measure) {

        Part parentPart = measure.getParentPart();

        int noteVoice = getNoteVoice(note); //all notes have this stuff
        int noteStaff = getNoteStaff(note);
        double noteDuration = getNoteDuration(note);

        if (isRest(note)) {
            addRestChordToMeasure(parentPart, measure, noteVoice, noteStaff, noteDuration);
        }

        else { //standard chords
            addChordToMeasure(note, parentPart, measure, noteVoice, noteStaff);
        }

        System.out.println("-------------");

    }

    public void addChordToMeasure(Element note, Part parentPart, Measure measure, int noteVoice, int noteStaff) {

        char noteStep = getNoteStep(note); //only pitched notes have this stuff
        int noteAlter = getNoteAlter(note);
        int noteOctave = getNoteOctave(note);
        boolean isGrace = getNoteGrace(note);
        boolean isChord = getNoteChord(note);

        Chord chord = makeChord(parentPart, measure, noteVoice, noteStaff); //just temporary. Standard chord with no notes
        measure.addChord(chord);
    }

    public Chord makeChord(Part parentPart, Measure measure, int noteVoice, int noteStaff, Note... notes) {
        return new Chord(parentPart, measure, noteVoice, noteStaff, notes);
    }

    public void addRestChordToMeasure(Part parentPart, Measure measure, int noteVoice, int noteStaff, double noteDuration) {

        System.out.println("Pitch Step: N/A");
        System.out.println("Pitch Alter: N/A");
        System.out.println("Pitch Octave: N/A");
        System.out.println("Grace: N/A");
        System.out.println("Chord: N/A");

        Chord restChord = makeRestChord(parentPart, measure, noteVoice, noteStaff, noteDuration);
        measure.addChord(restChord);

    }

    public RestChord makeRestChord(Part parentPart, Measure measure, int noteVoice, int noteStaff, double noteDuration) {
        RestChord restChord = new RestChord(parentPart, measure, noteVoice, noteStaff);
        restChord.addNote(new RestNote(parentPart, measure, noteVoice, noteStaff, restChord, noteDuration));
        return restChord;
    }
    public boolean isRest(Element note) {
        return note.getElementsByTagName("step").item(0) == null;
    }
    public int getNoteVoice(Element note) {
        String voiceNumberString = note.getElementsByTagName("voice").item(0).getTextContent();
        System.out.println("Voice: " + voiceNumberString);
        int voiceNumber = Integer.parseInt(voiceNumberString);
        return (voiceNumber % 4) == 0 ?  1 : (voiceNumber % 4); // staves higher than staff 1 have voice numbers that are higher than 4
    }

    public int getNoteStaff(Element note) {
        if (note.getElementsByTagName("staff").item(0) != null) {
            String staffNumberString = note.getElementsByTagName("staff").item(0).getTextContent();
            System.out.println("Staff: " + staffNumberString);
            return Integer.parseInt(staffNumberString);
        } else {
            System.out.println("Staff: N/A");
            return 1; //default staff number
        }
    }

    public double getNoteDuration(Element note) {
        if (note.getElementsByTagName("duration").item(0) != null) {
            String noteDurationString = note.getElementsByTagName("duration").item(0).getTextContent();
            System.out.println("Duration: " + noteDurationString);
            return Double.parseDouble(noteDurationString);
        } else {
            System.out.println("Duration: N/A");
            return 0.0; //only grace notes have this duration
        }
    }

    public char getNoteStep(Element note) {
        String noteStepString = note.getElementsByTagName("step").item(0).getTextContent();
        System.out.println("Pitch Step: " + noteStepString);
        return noteStepString.charAt(0); //the string should only have one character anyway
    }

    public int getNoteAlter(Element note) {
        if (note.getElementsByTagName("alter").item(0) != null) { //not every pitched note has an alter
            String noteAlterString = note.getElementsByTagName("alter").item(0).getTextContent();
            System.out.println("Pitch Alter: " + noteAlterString);
            return Integer.parseInt(noteAlterString);
        } else {
            System.out.println("Pitch Alter: N/A");
            return 0; //0 means un-altered.
        }
    }

    public int getNoteOctave(Element note) {
        String noteOctaveString = note.getElementsByTagName("octave").item(0).getTextContent();
        System.out.println("Pitch Octave: " + noteOctaveString);
        return Integer.parseInt(noteOctaveString);
    }

    public boolean getNoteGrace(Element note) {
        if (note.getElementsByTagName("grace").item(0) != null) {
            System.out.println("Grace: yes");
            return true;
        } else { System.out.println("Grace: N/A");
            return false; }
    }

    public boolean getNoteChord(Element note) {
        if (note.getElementsByTagName("chord").item(0) != null) {
            System.out.println("Chord: yes");
            return true;
        } else { System.out.println("Chord: N/A");
            return false; }
    }

}

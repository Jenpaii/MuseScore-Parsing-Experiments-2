package com.herokuapp.mstogw2.main;

import com.herokuapp.mstogw2.chord.*;
import com.herokuapp.mstogw2.part.Part;
import org.w3c.dom.Element;

import java.util.ArrayList;

public class ChordHandler {
    private boolean printsEnabled = true;
    private ArrayList<Note> graceBacklog = new ArrayList<>();
    public void addChordToMeasure(Element note, Measure measure) {

        Part parentPart = measure.getParentPart();

        int noteVoice = getNoteVoice(note); //all notes have this stuff
        int noteStaff = getNoteStaff(note);
        double noteDuration = getNoteDuration(note);

        if (isRest(note)) {
            addRestChordToMeasure(parentPart, measure, noteVoice, noteStaff, noteDuration); }

        else { //standard chords
            addChordToMeasure(note, parentPart, measure, noteVoice, noteStaff, noteDuration); }

        measure.addVoiceNumber(noteVoice);
        measure.addStaffNumber(noteStaff);

        if (printsEnabled) { System.out.println("-------------"); }

    }

    public void addChordToMeasure(Element note, Part parentPart, Measure measure, int noteVoice, int noteStaff, double noteDuration) {

        char noteStep = getNoteStep(note); //only pitched notes have this stuff
        int noteAlter = getNoteAlter(note);
        int noteOctave = getNoteOctave(note);
        boolean isGrace = getNoteIsGrace(note);
        boolean isChorded = getNoteIsChorded(note);

        Note newNote = isGrace ? new GraceNote(parentPart, measure, noteVoice, noteStaff, noteDuration, noteStep, noteAlter, noteOctave) :
                new StandardNote(parentPart, measure, noteVoice, noteStaff, noteDuration, noteStep, noteAlter, noteOctave);

        parentPart.addNote(newNote); //adding notes to part, as well.
        newNote.setNoteNumberInPart(parentPart.getNotesAmount()); //setting the number on the note as well. Starts on 1 for each part.

        if (!isChorded && !isGrace) { //only do this for the first note of a chord, and if it's not a grace note
            addNewChordToMeasure(newNote, parentPart, measure, noteVoice, noteStaff);
        } else if (isChorded && !isGrace) { //this note is chorded, meaning it belongs to the previous note's chord. Still not grace though.
            addNoteToPreviousChord(newNote, parentPart);
        } else if (isGrace) {graceBacklog.add(newNote);} //add it to the backlog.
    }

    public void addNewChordToMeasure(Note newNote, Part parentPart, Measure measure, int noteVoice, int noteStaff) {

        Chord chord = makeChord(parentPart, measure, noteVoice, noteStaff);
        if(!graceBacklog.isEmpty()) { //if there's notes in the grace backlog
            chord.addNotes(graceBacklog); //add the notes to this chord.
            graceBacklog.clear(); //clear the backlog.
        }
        chord.addNote(newNote);
        //newNote.setParentChord(chord); //not needed i think, since addNote now has note.setParentChord(this);

        measure.addChord(chord);
    }

    public void addNoteToPreviousChord(Note newNote, Part parentPart) {

        Chord chord = parentPart.getPreviousChord(newNote); //finding the chord that we must add newNote to.
        chord.addNote(newNote);
        //newNote.setParentChord(chord); //not needed i think, since addNote now has note.setParentChord(this);

        //no measure.addChord here, because it's not a new chord.
    }

    public Chord makeChord(Part parentPart, Measure measure, int noteVoice, int noteStaff, Note... notes) {
        return new Chord(parentPart, measure, noteVoice, noteStaff, notes);
    }

    public void addRestChordToMeasure(Part parentPart, Measure measure, int noteVoice, int noteStaff, double noteDuration) {

        if (printsEnabled) {
            System.out.println("Pitch Step: N/A");
            System.out.println("Pitch Alter: N/A");
            System.out.println("Pitch Octave: N/A");
            System.out.println("Grace: N/A");
            System.out.println("Chord: N/A");
        }

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

        if(printsEnabled) { System.out.println("Voice: " + voiceNumberString); }

        int voiceNumber = Integer.parseInt(voiceNumberString);
        return (voiceNumber % 4) == 0 ?  1 : (voiceNumber % 4); // staves higher than staff 1 have voice numbers that are higher than 4
    }

    public int getNoteStaff(Element note) {

        if (note.getElementsByTagName("staff").item(0) != null) {
            String staffNumberString = note.getElementsByTagName("staff").item(0).getTextContent();

            if(printsEnabled) { System.out.println("Staff: " + staffNumberString); }

            return Integer.parseInt(staffNumberString);
        } else {

            if(printsEnabled) { System.out.println("Staff: N/A"); }

            return 1; //default staff number
        }
    }

    public double getNoteDuration(Element note) {
        if (note.getElementsByTagName("duration").item(0) != null) {
            String noteDurationString = note.getElementsByTagName("duration").item(0).getTextContent();
            if (printsEnabled) { System.out.println("Duration: " + Double.parseDouble(noteDurationString)); }
            return Double.parseDouble(noteDurationString);
        } else {
            if (printsEnabled) { System.out.println("Duration: N/A"); }
            return 0.0; //only grace notes have this duration
        }
    }

    public char getNoteStep(Element note) {
        String noteStepString = note.getElementsByTagName("step").item(0).getTextContent();
        if (printsEnabled) { System.out.println("Pitch Step: " + noteStepString); }
        return noteStepString.charAt(0); //the string should only have one character anyway
    }

    public int getNoteAlter(Element note) {
        if (note.getElementsByTagName("alter").item(0) != null) { //not every pitched note has an alter
            String noteAlterString = note.getElementsByTagName("alter").item(0).getTextContent();
            if (printsEnabled) { System.out.println("Pitch Alter: " + noteAlterString); }
            return Integer.parseInt(noteAlterString);
        } else {
            if (printsEnabled) { System.out.println("Pitch Alter: N/A"); }
            return 0; //0 means un-altered.
        }
    }

    public int getNoteOctave(Element note) {
        String noteOctaveString = note.getElementsByTagName("octave").item(0).getTextContent();
        if (printsEnabled) { System.out.println("Pitch Octave: " + noteOctaveString); }
        return Integer.parseInt(noteOctaveString);
    }

    public boolean getNoteIsGrace(Element note) {
        if (note.getElementsByTagName("grace").item(0) != null) {
            if (printsEnabled) { System.out.println("Grace: yes"); }
            return true;
        } else {
            if (printsEnabled) { System.out.println("Grace: N/A"); }
            return false; }
    }

    public boolean getNoteIsChorded(Element note) {
        if (note.getElementsByTagName("chord").item(0) != null) {
            if (printsEnabled) { System.out.println("Chorded: yes"); }
            return true;
        } else {
            if (printsEnabled) { System.out.println("Chorded: N/A"); }
            return false; }
    }

}

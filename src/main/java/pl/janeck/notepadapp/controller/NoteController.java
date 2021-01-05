package pl.janeck.notepadapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.janeck.notepadapp.model.Note;
import pl.janeck.notepadapp.service.NoteService;

import java.util.List;
import java.util.Optional;

/**
 * ... comment class...
 *
 * @author JKN janeck@protonmail.com
 * @since 03 January 2021 @ 01:26
 */

@RestController
@RequestMapping(("/api/notepad"))
public class NoteController {


    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }


    @GetMapping
    public ResponseEntity<List<Note>> getNotes() {
        return new ResponseEntity<>(noteService.getAllNotes(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable long id) {
        Optional<Note> note = noteService.getNoteById(id);
        return note.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Note> addNote(@RequestBody Note note) {
        Optional<Note> noteAdded = noteService.addNote(note);
        return noteAdded.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Note> removeNote(@PathVariable long id) {
        Optional<Note> noteDeleted = noteService.removeNote(id);
        return noteDeleted.map(note -> new ResponseEntity<>(note, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }


    @PutMapping
    public ResponseEntity<Note> updateNote(@RequestBody Note note) {
        Optional<Note> noteUpdated = noteService.update(note);
        return noteUpdated.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}

package pl.janeck.notepadapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.janeck.notepadapp.model.Note;
import pl.janeck.notepadapp.repository.NoteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * ... comment class...
 *
 * @author JKN janeck@protonmail.com
 * @since 03 January 2021 @ 01:26
 */

@Service
public class NoteService {

    private NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }


    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }


    public Optional<Note> getNoteById(long id) {
        return noteRepository.findById(id);
    }


    public Optional<Note> addNote(Note note) {
        note.setDate(LocalDateTime.now());
        return Optional.of(noteRepository.save(note));
    }


    public Optional<Note> removeNote(long id) {
        Optional<Note> noteToRemove = getNoteById(id);
        noteRepository.deleteById(id);
        return noteToRemove;

    }


    public Optional<Note> update(Note noteToUpdate) {
        Optional<Note> note = getNoteById(noteToUpdate.getId());
        if (note.isPresent()) {
            noteRepository.save(noteToUpdate);
            return Optional.of(noteToUpdate);

        }
        return Optional.empty();
    }


}

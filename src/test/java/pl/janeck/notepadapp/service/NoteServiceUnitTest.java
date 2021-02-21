package pl.janeck.notepadapp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.janeck.notepadapp.model.Note;
import pl.janeck.notepadapp.repository.NoteRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

/**
 * ... comment class...
 *
 * @author JKN janeck@protonmail.com
 * @since 06 February 2021 @ 20:42
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class NoteServiceUnitTest {

    @InjectMocks
    private NoteService noteService;

    @Mock
    private NoteRepository noteRepository;


    @Test
    void get_All_Notes() {

        // given
        List<Note> testNoteList = new ArrayList<>();
        Note note1 = new Note("Test note title #1", "Test note text #1", LocalDateTime.of(2020, 5, 15, 10, 17, 7));
        Note note2 = new Note("Test note title #2", "Test note text #2", LocalDateTime.of(2020, 7, 27, 13, 33, 17));
        note1.setId(1L);
        note2.setId(2L);
        testNoteList.add(note1);
        testNoteList.add(note2);

        //When & Then
        Mockito.when(noteRepository.findAll()).thenReturn(testNoteList);
        List<Note> allNotes = noteService.getAllNotes();
        Assertions.assertEquals("Test note title #1", allNotes.get(0).getTitle());
        Assertions.assertEquals("Test note text #2", allNotes.get(1).getText());
        Assertions.assertEquals(LocalDateTime.of(2020, 7, 27, 13, 33, 17), allNotes.get(1).getDate());
        Assertions.assertEquals(1L, allNotes.get(0).getId());
    }

    @Test
    void get_Note_ById() {
        // given
        Note note1 = new Note("Test note title #1", "Test note text #1", LocalDateTime.of(2020, 5, 15, 10, 17, 7));
        note1.setId(1L);


        //When & Then
        Mockito.when(noteRepository.findById(1L)).thenReturn(Optional.of(note1));
        Optional<Note> note = noteService.getNoteById(1L);
        Assertions.assertEquals(1L, note.get().getId());

    }

    @Test
    void add_Note() {

        // given
        Note note2 = new Note("Test note title #2", "Test note text #2", LocalDateTime.of(2020, 7, 27, 13, 33, 17));
        note2.setId(2L);

        //When & Then
        Mockito.when(noteRepository.save(any(Note.class))).thenReturn(note2);
        Optional<Note> created = noteService.addNote(note2);
        Assertions.assertSame(note2, created.get());

    }

    @Test
    void remove_Note() {

        // given
        Note note1 = new Note("Test note title #1", "Test note text #1", LocalDateTime.of(2020, 5, 15, 10, 17, 7));
        note1.setId(1L);

        //When & Then
        Mockito.when(noteRepository.findById(note1.getId())).thenReturn(Optional.of(note1));
        noteService.removeNote(note1.getId());
        Mockito.verify(noteRepository).deleteById(note1.getId());
    }

    @Test
    void update_Note() {

        // given
        Note noteToUpdate = new Note("Test note title - before update", "Test note text - before update", LocalDateTime.of(2020, 8, 29, 2, 13, 57));
        noteToUpdate.setId(3L);

        //When & Then
        Mockito.when(noteRepository.findById(noteToUpdate.getId())).thenReturn(Optional.of(noteToUpdate));
        Mockito.when(noteRepository.save(any(Note.class))).thenReturn(noteToUpdate);

        Optional<Note> updated = noteService.update(noteToUpdate);

        Assertions.assertSame(noteToUpdate, updated.get());
        Mockito.verify(noteRepository).save(any(Note.class));
        Mockito.verify(noteRepository).findById(noteToUpdate.getId());

    }
}
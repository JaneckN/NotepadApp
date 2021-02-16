package pl.janeck.notepadapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.janeck.notepadapp.model.Note;
import pl.janeck.notepadapp.repository.NoteRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ... comment class...
 *
 * @author JKN janeck@protonmail.com
 * @since 20 January 2021 @ 22:24
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class NoteControllerIntegrationTest {

    private String API_BASE_URL = "/api/notepad";


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteRepository noteRepository;

    @Test
    void should_return_all_notes() throws Exception {
        // preparation
        List<Note> noteList = new ArrayList<>();
        Note note1 = new Note("Test note title #1", "Test note text #1", LocalDateTime.of(2020, 5, 15, 10, 17, 7));
        Note note2 = new Note("Test note title #2", "Test note text #2", LocalDateTime.of(2020, 7, 27, 13, 33, 17));
        note1.setId(1L);
        note2.setId(2L);
        noteList.add(note1);
        noteList.add(note2);
        Mockito.when(noteRepository.findAll()).thenReturn(noteList);


        // action + assertion
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy @ HH:mm:ss");
        mockMvc.perform(get("/api/notepad"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test note title #1")))
                .andExpect(jsonPath("$[0].text", is("Test note text #1")))
                .andExpect(jsonPath("$[0].date", is(LocalDateTime.of(2020, 5, 15, 10, 17, 7).format(formatter))))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Test note title #2")))
                .andExpect(jsonPath("$[1].text", is("Test note text #2")))
                .andExpect(jsonPath("$[1].date", is(LocalDateTime.of(2020, 7, 27, 13, 33, 17).format(formatter))));

        verify(noteRepository, times(1)).findAll();

    }

    @Test
    void should_return_note_by_id() throws Exception {
        // preparation
        Note noteById = new Note("Test note title #1", "Test note text #1", LocalDateTime.of(2020, 5, 15, 10, 17, 7));
        noteById.setId(1L);
        Mockito.when(noteRepository.findById(1L)).thenReturn(Optional.of(noteById));

        // action + assertion
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy @ HH:mm:ss");
        mockMvc.perform(get(API_BASE_URL+"/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test note title #1")))
                .andExpect(jsonPath("$.text", is("Test note text #1")))
                .andExpect(jsonPath("$.date", is(LocalDateTime.of(2020, 5, 15, 10, 17, 7).format(formatter))));


        verify(noteRepository, times(1)).findById(1L);

    }

    @Test
    void should_add_note() {
        
    }

    @Test
    void should_remove_note() {
    }

    @Test
    void should_update_note() {
    }
}
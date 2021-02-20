package pl.janeck.notepadapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.janeck.notepadapp.model.Note;
import pl.janeck.notepadapp.repository.NoteRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
@ActiveProfiles("test")
class NoteControllerIntegrationTest {


    private final String API_BASE_URL = "/api/notepad";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy @ HH:mm:ss");
    private static final ObjectMapper mapper = new ObjectMapper(); // to send object in json format


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteRepository noteRepository;


    @Test
    @Sql({"/schema.sql"})
    @Sql({"/data.sql"})
    void should_return_all_notes() throws Exception {

        // action + assertion
        mockMvc.perform(get("/api/notepad").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test note title #1")))
                .andExpect(jsonPath("$[0].text", is("Test note text #1")))
                .andExpect(jsonPath("$[0].date", is(LocalDateTime.of(2021, 2, 17, 22, 04, 48).format(formatter))))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Test note title #2")))
                .andExpect(jsonPath("$[1].text", is("Test note text #2")))
                .andExpect(jsonPath("$[1].date", is(LocalDateTime.of(2020, 7, 30, 07, 22, 33).format(formatter))));
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql({"/data.sql"})
    void should_return_note_by_id() throws Exception {

        // preparation
        Note noteById = new Note("Test note title #1", "Test note text #1", LocalDateTime.of(2021, 2, 17, 22, 4, 48));
        noteById.setId(1L);

        // action + assertion
        mockMvc.perform(get(API_BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is(noteById.getTitle())))
                .andExpect(jsonPath("$.text", is(noteById.getText())))
                .andExpect(jsonPath("$.date", is(noteById.getDate().format(formatter))));
    }


    @Test
    @Sql({"/schema.sql"})
    @Sql({"/data.sql"})
    void should_add_note() throws Exception {

        // preparation
        Note note = new Note();
        note.setTitle("Test note title #3");
        note.setText("Test note text #3");

        // action + assertion

        mockMvc.perform(post(API_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(note)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.title", is("Test note title #3")))
                .andExpect(jsonPath("$.text", is("Test note text #3")))
                .andExpect(jsonPath("$.date", is(notNullValue())));
    }

    @Test
    @Sql({"/schema.sql"})
    @Sql({"/data.sql"})
    void should_remove_note() throws Exception {

        // preparation, create user witch should be deleted,  according to credentials from temp h2 database.
        Note noteToDelete = new Note("Test note title #2", "Test note text #2", LocalDateTime.of(2020, 7, 30, 7, 22, 33));

        // action + assertion
        mockMvc.perform(delete(API_BASE_URL + "/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is(noteToDelete.getTitle())))
                .andExpect(jsonPath("$.text", is(noteToDelete.getText())))
                .andExpect(jsonPath("$.date", is(noteToDelete.getDate().format(formatter))));
    }


    @Test
    @Sql({"/schema.sql"})
    @Sql({"/data.sql"})
    void should_update_note() throws Exception {

        // preparation
        Note noteToUpdate = new Note("Test note title #update", "Test note text #update", LocalDateTime.of(2020, 7, 30, 7, 22, 33));
        noteToUpdate.setId(2);
        // I can not use ObjectMapper. because date has got specific format.
        String noteToUpdateInString = "{\"id\":2, \"title\":\"Test note title #update\", \"text\":\"Test note text #update\", \"date\":\"30-07-2020 @ 07:22:33\"}";

        // action + assertion

        mockMvc.perform(put(API_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(noteToUpdateInString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is("Test note title #update")))
                .andExpect(jsonPath("$.text", is("Test note text #update")))
                .andExpect(jsonPath("$.date", is(noteToUpdate.getDate().format(formatter))));
    }
}
package pl.janeck.notepadapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * ... comment class...
 *
 * @author JKN janeck@protonmail.com
 * @since 03 January 2021 @ 01:17
 */


@Entity
@Getter
@Setter
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String text;
    @JsonFormat(pattern="dd-MM-yyyy @ HH:mm:ss")
    private LocalDateTime date;


    public Note() {
    }

    public Note(String title, String text, LocalDateTime date) {
        this.title = title;
        this.text = text;
        this.date = date;
    }


}

package pl.janeck.notepadapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.janeck.notepadapp.model.Note;

/**
 * ... comment class...
 *
 * @author JKN janeck@protonmail.com
 * @since 03 January 2021 @ 01:22
 */

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

}

package it.uniroma3.siw.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.CommentoRepository;

/** Unit tests for the comment use cases (no database needed). */
@ExtendWith(MockitoExtension.class)
class CommentoServiceTest {

    @Mock
    private CommentoRepository commentoRepository;

    @InjectMocks
    private CommentoService commentoService;

    private static Utente utente(String email) {
        Utente u = new Utente();
        u.setEmail(email);
        return u;
    }

    private static Commento commentoDi(Utente autore, String testo) {
        Commento c = new Commento();
        c.setId(1L);
        c.setAutore(autore);
        c.setTesto(testo);
        return c;
    }

    @Test
    void inserisciCommento_setsAuthorAndDate() {
        Utente autore = utente("mario@example.com");
        Commento commento = new Commento();
        commento.setTesto("Bella partita");

        commentoService.inserisciCommento(commento, autore);

        assertEquals(autore, commento.getAutore());
        assertNotNull(commento.getData());
        verify(commentoRepository).save(commento);
    }

    @Test
    void modificaCommento_byTheAuthor_updatesTheText() {
        Utente autore = utente("mario@example.com");
        Commento commento = commentoDi(autore, "testo vecchio");
        when(commentoRepository.findById(1L)).thenReturn(Optional.of(commento));

        commentoService.modificaCommento(1L, "testo nuovo", utente("mario@example.com"));

        assertEquals("testo nuovo", commento.getTesto());
        verify(commentoRepository).save(commento);
    }

    @Test
    void modificaCommento_byAnotherUser_isIgnored() {
        Commento commento = commentoDi(utente("mario@example.com"), "testo vecchio");
        when(commentoRepository.findById(1L)).thenReturn(Optional.of(commento));

        commentoService.modificaCommento(1L, "testo nuovo", utente("giulia@example.com"));

        assertEquals("testo vecchio", commento.getTesto());
        verify(commentoRepository, never()).save(any());
    }
}

package it.uniroma3.siw.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.repository.CredenzialiRepository;

/** Unit tests for the registration of new credentials (no database needed). */
@ExtendWith(MockitoExtension.class)
class CredenzialiServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CredenzialiRepository credenzialiRepository;

    @InjectMocks
    private CredenzialiService credenzialiService;

    @Test
    void saveCredenziali_alwaysAssignsUserRoleAndEncodesPassword() {
        when(passwordEncoder.encode("segreta")).thenReturn("HASH");
        when(credenzialiRepository.save(any(Credenziali.class))).thenAnswer(inv -> inv.getArgument(0));
        Credenziali credenziali = new Credenziali();
        credenziali.setUsername("nuovo");
        credenziali.setPassword("segreta");
        credenziali.setRuolo(Credenziali.ADMIN_ROLE); // a user cannot register as admin

        Credenziali salvate = credenzialiService.saveCredenziali(credenziali);

        assertEquals(Credenziali.USER_ROLE, salvate.getRuolo());
        assertEquals("HASH", salvate.getPassword());
    }
}

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START WITH 1 INCREMENT BY 1;

-- ====================================================================
-- 1. UTENTI E CREDENZIALI (Password per entrambi: 'password')
-- ====================================================================
INSERT INTO utente (id, nome, cognome, email) VALUES (nextval('hibernate_sequence'), 'Sara', 'Barn', 'sara.b@gmail.com');
INSERT INTO utente (id, nome, cognome, email) VALUES (nextval('hibernate_sequence'), 'Mario', 'Rossi', 'mario.rossi@gmail.com');
INSERT INTO credenziali (id, username, password, ruolo, utente_id) VALUES (nextval('hibernate_sequence'), 'admin', '$2a$10$r8V8fHnOnvE49R0Ues8YfevE8f0QzU/62L/M8IuVOnVvF2Cby2Wp.', 'ADMIN', (SELECT id FROM utente WHERE email = 'sara.b@gmail.com'));
INSERT INTO credenziali (id, username, password, ruolo, utente_id) VALUES (nextval('hibernate_sequence'), 'mario', '$2a$10$r8V8fHnOnvE49R0Ues8YfevE8f0QzU/62L/M8IuVOnVvF2Cby2Wp.', 'USER', (SELECT id FROM utente WHERE email = 'mario.rossi@gmail.com'));

-- ====================================================================
-- 2. ARBITRI AMATORIALI
-- ====================================================================
INSERT INTO arbitro (id, nome, cognome, codice_arbitrale) VALUES (nextval('hibernate_sequence'), 'Francesco', 'Galli', 'LAZ-ARB01');
INSERT INTO arbitro (id, nome, cognome, codice_arbitrale) VALUES (nextval('hibernate_sequence'), 'Luca', 'Ferri', 'LAZ-ARB02');

-- ====================================================================
-- 3. TORNEI
-- ====================================================================
INSERT INTO torneo (id, nome, anno, descrizione) VALUES (nextval('hibernate_sequence'), 'Torneo Estivo Garbatella 2026', 2026, 'Il classico torneo amatoriale di calciotto del quartiere, aperto a squadre locali e aziendali');

-- ====================================================================
-- 4. SQUADRE AMATORIALI
-- ====================================================================
INSERT INTO squadra (id, nome, citta, anno_fondazione) VALUES (nextval('hibernate_sequence'), 'ASD San Lorenzo', 'Roma', 2015);
INSERT INTO squadra (id, nome, citta, anno_fondazione) VALUES (nextval('hibernate_sequence'), 'Polisportiva Ostiense', 'Roma', 2010);
INSERT INTO squadra (id, nome, citta, anno_fondazione) VALUES (nextval('hibernate_sequence'), 'Bar da Gigi FC', 'Roma', 2022);
INSERT INTO squadra (id, nome, citta, anno_fondazione) VALUES (nextval('hibernate_sequence'), 'Atletico Trastevere', 'Roma', 2018);

-- ====================================================================
-- 5. GIOCATORI AMATORIALI (con altezza e ruoli realistici)
-- ====================================================================
INSERT INTO giocatore (id, nome, cognome, data_nascita, ruolo, altezza, squadra_id) VALUES (nextval('hibernate_sequence'), 'Lorenzo', 'Ricci', '1995-04-12', 'Attaccante', 180, (SELECT id FROM squadra WHERE nome = 'ASD San Lorenzo'));
INSERT INTO giocatore (id, nome, cognome, data_nascita, ruolo, altezza, squadra_id) VALUES (nextval('hibernate_sequence'), 'Matteo', 'Esposito', '1998-09-23', 'Centrocampista', 175, (SELECT id FROM squadra WHERE nome = 'ASD San Lorenzo'));
INSERT INTO giocatore (id, nome, cognome, data_nascita, ruolo, altezza, squadra_id) VALUES (nextval('hibernate_sequence'), 'Gabriele', 'Mancini', '1992-11-05', 'Attaccante', 185, (SELECT id FROM squadra WHERE nome = 'Polisportiva Ostiense'));
INSERT INTO giocatore (id, nome, cognome, data_nascita, ruolo, altezza, squadra_id) VALUES (nextval('hibernate_sequence'), 'Davide', 'De Luca', '1996-02-18', 'Centrocampista', 178, (SELECT id FROM squadra WHERE nome = 'Polisportiva Ostiense'));
INSERT INTO giocatore (id, nome, cognome, data_nascita, ruolo, altezza, squadra_id) VALUES (nextval('hibernate_sequence'), 'Simone', 'Bruno', '2001-07-30', 'Attaccante', 182, (SELECT id FROM squadra WHERE nome = 'Bar da Gigi FC'));
INSERT INTO giocatore (id, nome, cognome, data_nascita, ruolo, altezza, squadra_id) VALUES (nextval('hibernate_sequence'), 'Francesco', 'Conti', '1994-05-14', 'Centrocampista', 174, (SELECT id FROM squadra WHERE nome = 'Bar da Gigi FC'));

-- ====================================================================
-- 6. PARTECIPAZIONI (Situazione Classifica del Torneo)
-- ====================================================================
INSERT INTO partecipazione (id, punti, torneo_id, squadra_id) VALUES (nextval('hibernate_sequence'), 3, (SELECT id FROM torneo WHERE nome = 'Torneo Estivo Garbatella 2026'), (SELECT id FROM squadra WHERE nome = 'ASD San Lorenzo'));
INSERT INTO partecipazione (id, punti, torneo_id, squadra_id) VALUES (nextval('hibernate_sequence'), 1, (SELECT id FROM torneo WHERE nome = 'Torneo Estivo Garbatella 2026'), (SELECT id FROM squadra WHERE nome = 'Polisportiva Ostiense'));
INSERT INTO partecipazione (id, punti, torneo_id, squadra_id) VALUES (nextval('hibernate_sequence'), 1, (SELECT id FROM torneo WHERE nome = 'Torneo Estivo Garbatella 2026'), (SELECT id FROM squadra WHERE nome = 'Bar da Gigi FC'));
INSERT INTO partecipazione (id, punti, torneo_id, squadra_id) VALUES (nextval('hibernate_sequence'), 0, (SELECT id FROM torneo WHERE nome = 'Torneo Estivo Garbatella 2026'), (SELECT id FROM squadra WHERE nome = 'Atletico Trastevere'));

-- ====================================================================
-- 7. PARTITE AMATORIALI
-- ====================================================================
INSERT INTO partita (id, luogo, goals_home, goals_away, data_eora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id) VALUES (nextval('hibernate_sequence'), 'Centro Sportivo Ostiense - Campo A', 3, 1, '2026-05-10 21:00:00', 'PLAYED', (SELECT id FROM arbitro WHERE codice_arbitrale = 'LAZ-ARB01'), (SELECT id FROM torneo WHERE nome = 'Torneo Estivo Garbatella 2026'), (SELECT id FROM squadra WHERE nome = 'ASD San Lorenzo'), (SELECT id FROM squadra WHERE nome = 'Atletico Trastevere'));
INSERT INTO partita (id, luogo, goals_home, goals_away, data_eora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id) VALUES (nextval('hibernate_sequence'), 'Campo Parrocchiale Garbatella', 2, 2, '2026-05-12 21:00:00', 'PLAYED', (SELECT id FROM arbitro WHERE codice_arbitrale = 'LAZ-ARB02'), (SELECT id FROM torneo WHERE nome = 'Torneo Estivo Garbatella 2026'), (SELECT id FROM squadra WHERE nome = 'Bar da Gigi FC'), (SELECT id FROM squadra WHERE nome = 'Polisportiva Ostiense'));
INSERT INTO partita (id, luogo, goals_home, goals_away, data_eora, stato, arbitro_id, torneo_id, squadra_casa_id, squadra_ospite_id) VALUES (nextval('hibernate_sequence'), 'Centro Sportivo Ostiense - Campo B', NULL, NULL, '2026-06-15 21:00:00', 'SCHEDULED', (SELECT id FROM arbitro WHERE codice_arbitrale = 'LAZ-ARB01'), (SELECT id FROM torneo WHERE nome = 'Torneo Estivo Garbatella 2026'), (SELECT id FROM squadra WHERE nome = 'Polisportiva Ostiense'), (SELECT id FROM squadra WHERE nome = 'ASD San Lorenzo'));

-- ====================================================================
-- 8. COMMENTI DI TEST
-- ====================================================================
INSERT INTO commento (id, testo, data, autore_id, partita_commentata_id) VALUES (nextval('hibernate_sequence'), 'Bellissima partita all-Ostiense! Ricci trascinatore assoluto con una tripletta clamorosa.', '2026-05-10 23:15:00', (SELECT id FROM utente WHERE email = 'mario.rossi@gmail.com'), (SELECT id FROM partita WHERE luogo = 'Centro Sportivo Ostiense - Campo A' AND stato = 'PLAYED' LIMIT 1));
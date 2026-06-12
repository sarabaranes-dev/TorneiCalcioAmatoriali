package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.repository.CredenzialiRepository;
import jakarta.transaction.Transactional;

@Service
public class CredenzialiService {

	 @Autowired
	 protected PasswordEncoder passwordEncoder; 
	 
	 @Autowired
	 protected CredenzialiRepository credenzialiRepository;

	 @Transactional
	 public Credenziali getCredenziali(Long id) {
		 Optional<Credenziali> risultato = this.credenzialiRepository.findById(id);
		 return risultato.orElse(null);
	 }

	 @Transactional
	 public Credenziali getCredenziali(String username) {       
		 Optional<Credenziali> risultato = this.credenzialiRepository.findByUsername(username);
	     return risultato.orElse(null);
	 }

	 @Transactional
	 public Credenziali saveCredenziali(Credenziali credenziali) {
		 credenziali.setRuolo(Credenziali.USER_ROLE);
	   	 credenziali.setPassword(this.passwordEncoder.encode(credenziali.getPassword()));
	     return this.credenzialiRepository.save(credenziali);
	 }
}

package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Arbitro;
import it.uniroma3.siw.repository.ArbitroRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArbitroService {


	@Autowired
	private ArbitroRepository arbitroRepository;
	
	@Transactional(readOnly = true)
    public Iterable<Arbitro> findAll() {
	    return arbitroRepository.findAll();
	}
}

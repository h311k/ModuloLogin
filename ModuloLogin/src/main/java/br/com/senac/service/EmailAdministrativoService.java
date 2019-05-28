package br.com.senac.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.senac.domain.EmailAdministrativo;
import br.com.senac.repository.EmailAdministrativoRepository;
import br.com.senac.service.exception.ObjectNotFoundException;

@Service
public class EmailAdministrativoService {

	@Autowired
	private EmailAdministrativoRepository repository;
	
	public EmailAdministrativo insere(EmailAdministrativo emailAdministrativo) {
		emailAdministrativo.setId(null);
		return repository.save(emailAdministrativo);
	}
	
	public EmailAdministrativo busca(Integer id) {
		Optional<EmailAdministrativo> emailAdministrativo = repository.findById(id);
		return emailAdministrativo.orElseThrow(() -> new ObjectNotFoundException("E-Mail não encontrado! Id: "+id+", Tipo: "+EmailAdministrativo.class.getName()));
	}
}

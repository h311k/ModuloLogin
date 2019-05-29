package br.com.senac.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.senac.domain.Usuario;
import br.com.senac.repository.UsuarioRepository;
import br.com.senac.util.Security;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	
	Security security = new Security();
	
	public Usuario findUsuarioSenha(Usuario usuario) {
		usuario.setSenha(security.encriptaSenha(usuario.getSenha()));
		usuario = repository.findByUsuarioSenha(usuario.getEmail(), usuario.getSenha());
		return usuario;
	}
	
	public Usuario insere(Usuario usuario) {
		usuario.setId(null);
		usuario.setSenha(security.encriptaSenha(usuario.getSenha()));
		return repository.save(usuario);
	}
}

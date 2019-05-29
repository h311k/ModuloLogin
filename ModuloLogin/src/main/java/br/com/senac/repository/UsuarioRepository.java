package br.com.senac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.senac.domain.Usuario;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	@Query("select u from Usuario u where u.email=?1 and senha=?2")
	Usuario findByUsuarioSenha(String email, String senha);
	
	@Query("select u from Usuario u where u.email=?1")
	Usuario findByEmail(String email);
	
	@Query("select u from Usuario u where u.id=?1 and email=?2")
	Usuario findByIdEmail(Integer id, String email);
}

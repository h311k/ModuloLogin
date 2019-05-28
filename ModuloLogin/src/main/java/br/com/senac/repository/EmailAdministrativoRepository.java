package br.com.senac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.senac.domain.EmailAdministrativo;


@Repository
public interface EmailAdministrativoRepository extends JpaRepository<EmailAdministrativo, Integer> {

}

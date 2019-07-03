package br.com.senac.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import br.com.senac.domain.EmailAdministrativo;
import br.com.senac.repository.EmailAdministrativoRepository;
import br.com.senac.util.Security;

@Component
public class Init implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	EmailAdministrativoRepository mailRepository;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		Security security = new Security();
		
		EmailAdministrativo mail = new EmailAdministrativo();
		
		mail.setHost("smtp.gmail.com");
		mail.setPorta("465");
		mail.setNome("recuperacaoSenha");
		mail.setUsuario("seu@email.com");
		mail.setSenha(security.encode("senhaforte"));
		
		mailRepository.save(mail);
		
	}

}

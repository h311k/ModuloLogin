package br.com.senac.controller;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.senac.domain.EmailAdministrativo;
import br.com.senac.domain.Usuario;
import br.com.senac.service.EmailAdministrativoService;
import br.com.senac.service.UsuarioService;
import br.com.senac.util.Security;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService service;

	@Autowired
	private EmailAdministrativoService mailService;

	Security security = new Security();

	@GetMapping("/adiciona")
	public ModelAndView adiciona(RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView("usuario/adiciona");
		mv.addObject("usuario", new Usuario());
		return mv;
	}

	@PostMapping("/insere")
	public String salva(Usuario usuario, RedirectAttributes redirectAttributes) {
		if (usuario.getEmail().equals("") || usuario.getSenha().equals("")) {
			redirectAttributes.addFlashAttribute("mensagem", "Os campos de usuário e senha são obrigatórios.");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
		} else {
			redirectAttributes.addFlashAttribute("mensagem", "Novo usuário criado com sucesso.");
			redirectAttributes.addFlashAttribute("alertClass", "alert-success");
			service.insere(usuario);
		}
		return "redirect:/usuario/adiciona";
	}

	@GetMapping("/autentica")
	public ModelAndView autentica(RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView("usuario/autentica");
		mv.addObject("usuario", new Usuario());
		return mv;
	}

	@PostMapping("/login")
	public String login(Usuario usuario, RedirectAttributes redirectAttributes) {
		if (usuario.getEmail().equals("") || usuario.getSenha().equals("")) {
			redirectAttributes.addFlashAttribute("mensagem", "Os campos de usuário e senha são obrigatórios.");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
		} else {
			redirectAttributes.addFlashAttribute("mensagem", "Novo usuário criado com sucesso.");
			redirectAttributes.addFlashAttribute("alertClass", "alert-success");
			usuario = service.findUsuarioSenha(usuario);
			if (usuario == null) {
				redirectAttributes.addFlashAttribute("mensagem", "Nome de usuário ou senha inválidos.");
				redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			} else {
				redirectAttributes.addFlashAttribute("mensagem", "Novo usuário autenticado com sucesso.");
				redirectAttributes.addFlashAttribute("alertClass", "alert-success");
			}
		}
		return "redirect:/usuario/autentica";
	}

	@PostMapping("/atualizasenha")
	public ModelAndView atualizaSenha(Usuario usuario) {
		usuario = service.atualizaSenha(usuario);
		ModelAndView mv = new ModelAndView("usuario/autentica");
		mv.addObject("usuario", new Usuario());
		return mv;
	}

	@GetMapping("/recuperasenha")
	public ModelAndView recuperaSenha(Usuario usuario) {
		ModelAndView mv = new ModelAndView("usuario/recuperasenha");
		mv.addObject("usuario", usuario);
		return mv;
	}

	@GetMapping("/recuperasenha/decode")
	public ModelAndView login(@RequestParam String chave) {
		chave = security.decode(chave);
		Usuario usuario = new Usuario();
		usuario.setId(Integer.valueOf(chave.split("-")[0]));
		usuario.setEmail(chave.split("-")[1]);
		usuario = service.findIdEmail(usuario);
		return recuperaSenha(usuario);
	}

	@GetMapping("/eviaemailrecuperacaosenha/{email}")
	private String enviaEmailRecuperacaoSenha(@PathVariable("email") String email) {
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario = service.findByEmail(usuario);
		String token = security.encode(usuario.getId() + "-" + usuario.getEmail());
		String url = "http://localhost:8080/usuario/recuperasenha/decode?chave=" + token;
		EmailAdministrativo mail = mailService.busca(1);

		Properties props = new Properties();
		props.put("mail.smtp.host", mail.getHost());
		props.put("mail.smtp.socketFactory.port", mail.getPorta());
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", mail.getPorta());

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mail.getUsuario(), security.decode(mail.getSenha()));
			}
		});

		Message message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(mail.getUsuario()));
			Address[] toUser = InternetAddress.parse(usuario.getEmail());

			message.setRecipients(Message.RecipientType.TO, toUser);
			message.setSubject("Recuperação de senha");
			message.setContent("<a href="+url+">Clique nesse link para recuperar sua senha.</a>", "text/html");
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return "redirect:/usuario/autentica";
	}
}

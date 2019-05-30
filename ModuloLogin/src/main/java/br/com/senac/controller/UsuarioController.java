package br.com.senac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.senac.domain.Usuario;
import br.com.senac.service.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService service;
	
	@GetMapping("/adiciona")
	public ModelAndView adiciona(RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView("usuario/adiciona");
		mv.addObject("usuario", new Usuario());
		return mv;
	}
	
	@PostMapping("/insere")
	public String salva(Usuario usuario, RedirectAttributes redirectAttributes) {
		if(usuario.getEmail().equals("") || usuario.getSenha().equals("")) {
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
		if(usuario.getEmail().equals("") || usuario.getSenha().equals("")) {
			redirectAttributes.addFlashAttribute("mensagem", "Os campos de usuário e senha são obrigatórios.");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
		} else {
			redirectAttributes.addFlashAttribute("mensagem", "Novo usuário criado com sucesso.");
			redirectAttributes.addFlashAttribute("alertClass", "alert-success");
			usuario = service.findUsuarioSenha(usuario);
			if(usuario==null) {
				redirectAttributes.addFlashAttribute("mensagem", "Nome de usuário ou senha inválidos.");
				redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			} else {
				redirectAttributes.addFlashAttribute("mensagem", "Novo usuário autenticado com sucesso.");
				redirectAttributes.addFlashAttribute("alertClass", "alert-success");
			}
		}
		return "redirect:/usuario/autentica";
	}
	
	@GetMapping("/recuperasenha")
	public ModelAndView recuperaSenha(Usuario usuario) {
		ModelAndView mv = new ModelAndView("usuario/recuperasenha");
		mv.addObject("usuario", usuario);
		return mv;
	}
	
	@GetMapping("/recuperasenha/decode")
	public ModelAndView login(@RequestParam String chave) {
		Usuario usuario = new Usuario();
		usuario.setId(Integer.valueOf(chave.split("-")[0]));
		usuario.setEmail(chave.split("-")[1]);
		usuario = service.findIdEmail(usuario);
		return recuperaSenha(usuario);
	}
}

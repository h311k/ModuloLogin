package br.com.senac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}

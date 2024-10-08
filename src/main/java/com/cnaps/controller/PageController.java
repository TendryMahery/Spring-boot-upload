package com.cnaps.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class PageController {
	@Autowired
	private KeycloakDeployment keycloakDeployment;
	
	@GetMapping("/pages/accueil.html")
	@CrossOrigin
	public ModelAndView pageaccueil (Model model) {
		return new ModelAndView ("/pages/accueil");
	}
	
//	@GetMapping("/logout")
//	@CrossOrigin
//	public String logout(HttpServletRequest request) throws ServletException {
//		request.logout();
//		return "Déconnexionnnn !!!!";
//	}
	
	@GetMapping("/pages/acc.html")
	@CrossOrigin
	public ModelAndView pageacc (Model model) {
		return new ModelAndView ("/pages/acc");
	}

	@GetMapping("/pages/QRcode.html")
	@CrossOrigin
	public ModelAndView pageQRcode (Model model) {
		return new ModelAndView ("/pages/QRcode");
	}
	
	@GetMapping("/pages/importbulk.html")
	@CrossOrigin
	public ModelAndView pageimportbulk (Model model) {
		return new ModelAndView ("/pages/importbulk");
	}
	
	@GetMapping("/pages/listebulk.html")
	@CrossOrigin
	public ModelAndView listebulk (Model model) {
		return new ModelAndView ("/pages/listebulk");
	}
	
	@GetMapping("/pages/ibulk.html")
	@CrossOrigin
	public ModelAndView listeibulk (Model model) {
		return new ModelAndView ("/pages/ibulk");
	}
	
	@GetMapping("/pages/fbulk.html")
	@CrossOrigin
	public ModelAndView listefbulk (Model model) {
		return new ModelAndView ("/pages/fbulk");
	}
	
	@GetMapping("/pages/nbulk.html")
	@CrossOrigin
	public ModelAndView listenbulk (Model model) {
		return new ModelAndView ("/pages/nbulk");
	}
	
	@PostMapping("/logout")
	@CrossOrigin
	public ModelAndView logoutok(HttpServletRequest request) throws ServletException {
		KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();

		KeycloakSecurityContext session = (KeycloakSecurityContext) token.getCredentials();

		// Révocation des tokens
		if (session instanceof RefreshableKeycloakSecurityContext) {
			RefreshableKeycloakSecurityContext refreshableSession = (RefreshableKeycloakSecurityContext) session;
			// System.out.println(refreshableSession.getRefreshToken());
			refreshableSession.logout(keycloakDeployment);
		}
		request.logout();

		return new ModelAndView("/pages/accueil.html");

	}

}
package br.com.ifc.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifc.auth.service.AuthenticationService;
import br.com.ifc.auth.vo.AuthTokenVO;
import br.com.ifc.auth.vo.UserCredentialVO;

@CrossOrigin(origins="*", maxAge = 3600)
@RestController
@RequestMapping("/token")
public class AuthenticationController {
	
    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(value="/generate-token", method=RequestMethod.POST, produces="application/json")
    public ResponseEntity<AuthTokenVO> register(@RequestBody UserCredentialVO userCredentialVO) throws AuthenticationException {
    	UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userCredentialVO.getName(), userCredentialVO.getPassword());    	
    	AuthTokenVO authTokenVO = authenticationService.getToken(usernamePasswordAuthenticationToken);
    	return ResponseEntity.ok(authTokenVO);
    }
}

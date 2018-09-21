package br.com.ifc.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifc.auth.model.User;
import br.com.ifc.auth.service.UserDetailsServiceImpl;
import br.com.ifc.auth.vo.UserVO;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDetailsServiceImpl userService;
        
    @PreAuthorize("hasRole('AdminWrite')")
    @RequestMapping(value="/save", method=RequestMethod.POST, consumes="application/json", produces="application/json")
    public ResponseEntity<String> register(@RequestBody UserVO userVO) throws AuthenticationException {
    	User user = new User(userVO.getName(), userVO.getPassword());
    	user.setRules(userVO.getRules());
    	userService.save(user);
    	return ResponseEntity.ok(new String());
    }
}

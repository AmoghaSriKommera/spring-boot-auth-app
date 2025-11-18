package com.example.springauth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AuthController {
  @Autowired UserRepository repo;
  BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  @GetMapping({"/", "/login"})
  public String loginPage(@RequestParam(name="error", required=false) String error, Model m){
    m.addAttribute("error", error);
    return "login";
  }

  @PostMapping("/login")
  public String login(@RequestParam String username, @RequestParam String password, Model m, HttpSession session){
    Optional<User> uOpt = repo.findByUsername(username);
    if(uOpt.isPresent() && encoder.matches(password, uOpt.get().getPassword())){
      User u = uOpt.get();
      // store user in session so GET /home can use it
      session.setAttribute("user", u);
      // add user to model for immediate rendering after login
      m.addAttribute("user", u);
      return "home";
    }
    m.addAttribute("error","Invalid username or password");
    return "login";
  }

  // NEW: GET /home mapping so direct /home works (or refresh)
  @GetMapping("/home")
  public String home(HttpSession session, Model m){
    User u = (User) session.getAttribute("user");
    if(u == null){
      // not logged in — redirect to login
      return "redirect:/login";
    }
    m.addAttribute("user", u);
    return "home";
  }

  @GetMapping("/register")
  public String regPage(){ return "register"; }

  @PostMapping("/register")
  public String register(User user, Model m){
    if(user.getUsername()==null || user.getPassword()==null) {
      m.addAttribute("error","Username and password required");
      return "register";
    }
    user.setPassword(encoder.encode(user.getPassword()));
    repo.save(user);
    return "redirect:/login?registered=true";
  }

  // simple logout endpoint
  @PostMapping("/logout")
  public String logout(HttpSession session){
    session.invalidate();
    return "redirect:/login";
  }
}

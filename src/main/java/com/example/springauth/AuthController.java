package com.example.springauth;
import org.springframework.stereotype.Controller; import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*; import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;
@Controller
public class AuthController {
  @Autowired UserRepository repo; BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
  @GetMapping({"/","/login"}) public String loginPage(@RequestParam(name="error", required=false) String error, Model m){ m.addAttribute("error", error); return "login"; }
  @PostMapping("/login") public String login(@RequestParam String username, @RequestParam String password, Model m){
    Optional<User> u = repo.findByUsername(username);
    if(u.isPresent() && encoder.matches(password, u.get().getPassword())){ m.addAttribute("user", u.get()); return "home"; }
    m.addAttribute("error","Invalid username or password"); return "login"; }
  @GetMapping("/register") public String regPage(){ return "register"; }
  @PostMapping("/register") public String register(User user, Model m){
    if(user.getUsername()==null || user.getPassword()==null){ m.addAttribute("error","Username and password required"); return "register"; }
    user.setPassword(encoder.encode(user.getPassword())); repo.save(user); return "redirect:/login?registered=true"; }
}

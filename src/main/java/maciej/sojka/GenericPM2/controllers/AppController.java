package maciej.sojka.GenericPM2.controllers;

import maciej.sojka.GenericPM2.entities.PassRepo;
import maciej.sojka.GenericPM2.entities.Password;
import maciej.sojka.GenericPM2.entities.User;
import maciej.sojka.GenericPM2.entities.UserRepo;
import maciej.sojka.GenericPM2.misc.CryptoFunc;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class AppController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PassRepo passRepo;

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }

    @PostMapping("/register")
    public String registerUser(User u, Model m) throws Exception {
        String salt = RandomString.make(64);
        u.setSalt(salt);
        if(u.getMethod().equals("aes")){
            u.setAcc_pass(CryptoFunc.encrypt(
                            CryptoFunc.calculateSHA512(
                                    u.getAcc_pass() + salt),
                                        CryptoFunc.generateKey(CryptoFunc.PEPPER)));
        } else {
            u.setAcc_pass(CryptoFunc.calculateHMAC(
                                        CryptoFunc.calculateSHA512(
                                                u.getAcc_pass() + salt),
                                                    CryptoFunc.PEPPER));
        }
        userRepo.save(u);
        m.addAttribute("passwords", passRepo.findAllByUserId(u.getId()));
        m.addAttribute("newpass", new Password(u.getId()));
        return "welcome";
    }

    @PostMapping("/login")
    public String loginUser(User u, Model m) throws Exception {
        String formPass = "";
        User DBuser = userRepo.findByLogin(u.getLogin());
        if(DBuser != null) {
            if (DBuser.getMethod().equals("aes")) {
                formPass = CryptoFunc.encrypt(
                                        CryptoFunc.calculateSHA512(
                                                u.getAcc_pass() + DBuser.getSalt()),
                                                    CryptoFunc.generateKey(CryptoFunc.PEPPER));
            } else {
                formPass = CryptoFunc.calculateHMAC(
                                        CryptoFunc.calculateSHA512(
                                                u.getAcc_pass() + DBuser.getSalt()),
                                                    CryptoFunc.PEPPER);
            }
            if(!formPass.equals(DBuser.getAcc_pass())) return "loginError";
        } else return "loginError";
        m.addAttribute("passwords", passRepo.findAllByUserId(DBuser.getId()));
        m.addAttribute("newpass", new Password(DBuser.getId()));
        return "welcome";
    }

    @PostMapping("/reset")
    public String resetMasterPassword(User u, Model m) throws Exception {
        String salt = RandomString.make(64);
        User DBuser = userRepo.findByLogin(u.getLogin());
        if(DBuser != null) {
            if (DBuser.getMethod().equals("aes")) {
                DBuser.setAcc_pass(
                        CryptoFunc.encrypt(
                                    CryptoFunc.calculateSHA512(
                                            u.getAcc_pass() + salt),
                                                CryptoFunc.generateKey(CryptoFunc.PEPPER)));
            } else {
                DBuser.setAcc_pass(
                        CryptoFunc.calculateHMAC(
                                    CryptoFunc.calculateSHA512(
                                            u.getAcc_pass() + salt), CryptoFunc.PEPPER));
            }
            DBuser.setSalt(salt);
        } else return "loginError";
        userRepo.save(DBuser);
        m.addAttribute("passwords", passRepo.findAllByUserId(DBuser.getId()));
        m.addAttribute("newpass", new Password(DBuser.getId()));
        return "welcome";
    }

    @PostMapping("/password")
    public String setPassword(Password p, Model m) {
        passRepo.save(p);
        User DBuser = userRepo.findByIdAlt(p.getId_user());
        m.addAttribute("passwords", passRepo.findAllByUserId(DBuser.getId()));
        m.addAttribute("newpass", new Password(DBuser.getId()));
        return "welcome";
    }

}

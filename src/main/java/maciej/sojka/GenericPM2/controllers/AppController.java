package maciej.sojka.GenericPM2.controllers;

import maciej.sojka.GenericPM2.entities.*;
import maciej.sojka.GenericPM2.repos.*;
import maciej.sojka.GenericPM2.misc.CryptoFunc;

import net.bytebuddy.utility.RandomString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AppController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PassRepo passRepo;
    @Autowired
    private IPLRepo iplRepo;
    @Autowired
    private ULRepo ulRepo;
    @Autowired
    private SPRepo spRepo;

    public String loginError(Model m, String userLogin, HttpServletRequest request) {
        ulRepo.save(new UserLogin(userLogin,false));
        iplRepo.save(new IPLogin(request.getRemoteAddr(),false));
        m.addAttribute("numberOfFails", ulRepo.getNumberOfFails(userLogin));
        m.addAttribute("numberOfFailsIP", iplRepo.getNumberOfFails(request.getRemoteAddr()));
        return "loginError";
    }

    public String enter(Model m, User u, HttpServletRequest request, Boolean log) {
        if(log) {
            ulRepo.save(new UserLogin(u.getLogin(), true));
            iplRepo.save(new IPLogin(request.getRemoteAddr(), true));
        }
        m.addAttribute("passwords", passRepo.findAllByUserId(u.getId()));
        m.addAttribute("newpass", new Password(u.getId()));
        m.addAttribute("lastSuccess", ulRepo.getLastSuccess());
        m.addAttribute("lastFail", ulRepo.getLastFail());
        m.addAttribute("numberOfFails", ulRepo.getNumberOfFails(u.getLogin()));
        m.addAttribute("ipLoginAttempts", iplRepo.findAll());
        m.addAttribute("newshare", new SharedPass());
        List<Password> sharedPasswords = new ArrayList<Password>();
        for(Long x : spRepo.findIdsByBenefactor(u.getLogin()))
            sharedPasswords.addAll(passRepo.findAllById(x));
        m.addAttribute("sharedPasswords",sharedPasswords);
        return "welcome";
    }

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }

    @PostMapping("/register")
    public String registerUser(User u, Model m, HttpServletRequest request) throws Exception {
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
        return enter(m, u, request, true);
    }

    @PostMapping("/reset")
    public String resetMasterPassword(User u, Model m, HttpServletRequest r) throws Exception {
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
        } else return loginError(m, u.getLogin(), r);
        userRepo.save(DBuser);
        return enter(m, DBuser, r, true);
    }

    @PostMapping("/login")
    public String loginUser(User u, Model m, HttpServletRequest request) throws Exception {
        String formPass;
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
            if(!formPass.equals(DBuser.getAcc_pass())) return loginError(m, u.getLogin(), request);
        } else return loginError(m, u.getLogin(), request);
        return enter(m, DBuser, request, true);
    }

    @PostMapping("/password")
    public String setPassword(Password p, Model m, HttpServletRequest request) {
        passRepo.save(p);
        User DBuser = userRepo.findByIdAlt(p.getId_user());
        return enter(m, DBuser, request, false);
    }

    @PostMapping("/clear")
    public String clearIpLoginTable(Password p, Model m, HttpServletRequest request) {
        iplRepo.deleteAll();
        User u = userRepo.findByIdAlt(p.getId_user());
        return enter(m, u, request, false);
    }

    @PostMapping("/share")
    public String sharePassword(SharedPass sp, String idSharedUser, String idSharedPass,
                                Model m, HttpServletRequest request) {
        if(userRepo.findByLogin(sp.getBenefactor())!=null) {
            sp.setId_owner(Long.parseLong(idSharedUser));
            sp.setId_password(Long.parseLong(idSharedPass));
            spRepo.save(sp);
            User u = userRepo.findByIdAlt(sp.getId_owner());
            return enter(m, u, request, false);
        }
        else return "shareError";
    }

}

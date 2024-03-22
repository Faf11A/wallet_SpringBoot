package com.wallet_SpringBoot.controller;

import com.wallet_SpringBoot.model.Category;
import com.wallet_SpringBoot.model.User;
import com.wallet_SpringBoot.model.UserDetails;
import com.wallet_SpringBoot.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.Objects;


@Controller
public class AuthController {

    @Autowired
    UserService userService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BudgetService budgetService;
    @Autowired
    TransactionService transactionService;

    //show login page
    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(value = "mode", defaultValue = "login") String mode) {
        model.addAttribute("mode", mode);
        categoryService.autoFillCategories();
        return "login";
    }

    //signing in
    @PostMapping("/login")
    public String processLogin(@RequestParam("login") String login,
                               @RequestParam("password") String password,
                               HttpSession session,
                               RedirectAttributes attributes) {
        String pswd = userService.findPasswordByLogin(login);

        if (pswd != null && Objects.equals(pswd, password)) {
            Long userId = userService.findIdByLogin(login);
            UserDetails userDetails = userDetailsService.findUserDetailsByUserId(userId).orElse(null);
            String username = userDetailsService.findNameById(userDetails.getId());
            session.setAttribute("userId", userId);
            session.setAttribute("username", username);
            return "redirect:/wallet";
        } else {
            attributes.addFlashAttribute("error", "Invalid login or password");
            return "redirect:/login";
        }
    }

    //registration
    @PostMapping("/register")
    public String processRegistration(@RequestParam("firstname") String firstName,
                                      @RequestParam("lastname") String lastName,
                                      @RequestParam("email") String email,
                                      @RequestParam("login") String login,
                                      @RequestParam("password") String password,
                                      @RequestParam("dob") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);

        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(firstName);
        userDetails.setLastName(lastName);
        userDetails.setEmail(email);
        userDetails.setBirthDate(dob);
        userDetails.setUser(user);

        userService.saveUser(user);
        userDetailsService.saveUserDetails(userDetails);
        budgetService.createFirstBudgetForNewUser(user);
        Category category = categoryService.findCategoryById(100L);
        transactionService.addFirstTransaction(category, user);

        return "redirect:/login?mode=login";
    }

    //logging out
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}

package com.wallet_SpringBoot.controller;

import com.wallet_SpringBoot.model.User;
import com.wallet_SpringBoot.model.UserDetails;
import com.wallet_SpringBoot.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EditProfileController {

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
    @Autowired
    GoalService goalService;

    @RequestMapping("/edit-profile")
    public String EditProfilePage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        User user = userService.findUserById(userId);
        UserDetails userDetails = userDetailsService.getUserDetailsById(userId);

        model.addAttribute("user", user);
        model.addAttribute("userDetails", userDetails);

        return "edit-profile";
    }

    @PostMapping("/edit-profile")
    public String updateProfile(@ModelAttribute User user, @ModelAttribute UserDetails userDetails, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/logout";

        User existingUser = userService.findUserById(userId);
        UserDetails existingUserDetails = userDetailsService.findUserDetailsByUserId(userId).orElse(null);

        if (user.getLogin() != null && !user.getLogin().isBlank()) {
            existingUser.setLogin(user.getLogin());
        }
        existingUser.setPassword(user.getPassword());
        existingUserDetails.setFirstName(userDetails.getFirstName());
        existingUserDetails.setLastName(userDetails.getLastName());
        existingUserDetails.setEmail(userDetails.getEmail());

        userService.updateUser(existingUser);
        userDetailsService.updateUserDetails(existingUserDetails);

        session.setAttribute("user", existingUser);
        session.setAttribute("userDetails", existingUserDetails);

        model.addAttribute("success", "Profile updated successfully");
        model.addAttribute("user", existingUser);
        model.addAttribute("userDetails", existingUserDetails);

        return "edit-profile";
    }

    @PostMapping("/del-profile")
    public String deleteProfile(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        UserDetails userDetails = userDetailsService.findUserDetailsByUserId(userId).orElse(null);
        userDetailsService.deleteUserDetails(userDetails.getId());
        transactionService.deleteTransactionsByUserId(userId);
        goalService.deleteGoalsByUserId(userId);
        budgetService.deleteBudgetsByUserId(userId);

        userService.deleteUserById(userId);
        return "redirect:/login";
    }
}
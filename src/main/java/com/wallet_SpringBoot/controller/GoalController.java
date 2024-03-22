package com.wallet_SpringBoot.controller;

import com.wallet_SpringBoot.model.*;
import com.wallet_SpringBoot.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
public class GoalController {

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

    @GetMapping("/goals")
    public String showGoals(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        List<Goal> currentGoals = goalService.findCurrentGoals(userId);
        List<Goal> completedGoals = goalService.findCompletedGoals(userId);
        List<Goal> expiredGoals = goalService.findExpiredGoals(userId);

        Optional<Budget> budgetOptional = budgetService.getBudgetByUserId(userId);
        if (budgetOptional.isPresent()) {
            Budget budget = budgetOptional.get();
            model.addAttribute("balance", budget.getAmount());
            model.addAttribute("currentGoals", currentGoals);
            model.addAttribute("completedGoals", completedGoals);
            model.addAttribute("expiredGoals", expiredGoals);
            return "goals";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/goals")
    public String addGoal(@RequestParam String goalName,
                          @RequestParam BigDecimal targetAmount,
                          @RequestParam String targetDate,
                          HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        Goal goal = new Goal();
        goal.setGoalName(goalName);
        goal.setCurrentAmount(BigDecimal.valueOf(0));
        goal.setTargetAmount(targetAmount);
        goal.setTargetDate(LocalDate.parse(targetDate));

        User user = userService.findUserById(userId);
        goal.setUser(user);

        goalService.saveGoal(goal);
        return "redirect:/goals";
    }

    @PostMapping("/addAmountToGoal")
    public String addAmountToGoal(@RequestParam Long goalId,
                                  @RequestParam BigDecimal amount,
                                  HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        User user = userService.findUserById(userId);

        Goal goal = goalService.findGoalById(goalId);
        goal.setCurrentAmount(goal.getCurrentAmount().add(amount));

        BigDecimal negativeAmount = amount.negate();
        budgetService.updateBalance(userId, negativeAmount);


        Transaction transaction = new Transaction();
        Category category = categoryService.findCategoryById(12L);
        transaction.setUser(user);
        transaction.setAmount(amount);
        transaction.setDescription("Amount added to your goals");
        transaction.setCategory(category);
        transaction.setDate(LocalDate.now());

        transactionService.save(transaction);
        goalService.saveGoal(goal);


        return "redirect:/goals";
    }

    @PostMapping("/deleteGoal")
    public String deleteGoal(@RequestParam Long goalId) {
        goalService.deleteGoal(goalId);
        return "redirect:/goals";
    }

    @PostMapping("/editGoalDate")
    public String editGoalDate(@RequestParam String newDate,
                               @RequestParam Long goalId,
                               HttpSession session){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate newGoalDate = LocalDate.parse(newDate, formatter);

        Long userId = (Long) session.getAttribute("userId");
        Goal goal = goalService.findGoalById(goalId);
        goal.setTargetDate(newGoalDate);

        goalService.saveGoal(goal);

        return "redirect:/goals";
    }

    @GetMapping("/edit/{goalId}")
    public String showEditGoalPage(@PathVariable Long goalId, Model model) {
        Goal goal = goalService.findGoalById(goalId);
        if (goal != null) {
            model.addAttribute("goalId", goal.getId());
            model.addAttribute("goalName", goal.getGoalName());
            model.addAttribute("targetAmount", goal.getTargetAmount());
            model.addAttribute("targetDate", goal.getTargetDate());
            return "edit-goal";
        } else {
            return "redirect:/goals";
        }
    }

    @PostMapping("/updateGoal")
    public String updateGoal(@ModelAttribute("goalId") Long goalId,
                             @ModelAttribute("goalName") String goalName,
                             @ModelAttribute("targetAmount") BigDecimal targetAmount,
                             @ModelAttribute("targetDate") String targetDate) {
        Goal goal = goalService.findGoalById(goalId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate newGoalDate = LocalDate.parse(targetDate, formatter);

        if (goal != null) {
            goal.setGoalName(goalName);
            goal.setTargetAmount(targetAmount);
            goal.setTargetDate(newGoalDate);
            goalService.saveGoal(goal);
        }
        return "redirect:/goals";
    }
}

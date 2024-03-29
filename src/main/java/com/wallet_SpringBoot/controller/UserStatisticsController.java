package com.wallet_SpringBoot.controller;

import com.wallet_SpringBoot.model.Goal;
import com.wallet_SpringBoot.model.User;
import com.wallet_SpringBoot.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserStatisticsController {

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

    @GetMapping("/stats")
    public String showUserStatistics(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        User user = userService.findUserById(userId);

        List<Goal> currentGoals = goalService.findCurrentGoals(userId);
        List<Goal> completedGoals = goalService.findCompletedGoals(userId);
        List<Goal> expiredGoals = goalService.findExpiredGoals(userId);

        //Goals
        int countOfCurrentGoals = currentGoals.size();
        int countOfDoneGoals = completedGoals.size();
        int countOfExpiredGoals = expiredGoals.size();
        int countOfGoals = countOfCurrentGoals + countOfDoneGoals + countOfExpiredGoals;

        model.addAttribute("countOfCurrentGoals", countOfCurrentGoals);
        model.addAttribute("countOfDoneGoals", countOfDoneGoals);
        model.addAttribute("countOfExpiredGoals", countOfExpiredGoals);
        model.addAttribute("countOfGoals", countOfGoals);

        // Transactions
        Long countOfAddAmount = transactionService.getTransactionCountByCategory(11L);
        Long countOfAddAmountCat1 = transactionService.getTransactionCountByCategory(1L);
        Long countOfAddAmountCat2 = transactionService.getTransactionCountByCategory(2L);
        Long countOfAddAmountCat3 = transactionService.getTransactionCountByCategory(3L);
        Long countOfAddAmountCat4 = transactionService.getTransactionCountByCategory(4L);
        Long countOfAddAmountCat5 = transactionService.getTransactionCountByCategory(5L);
        Long countOfAddAmountCat6 = transactionService.getTransactionCountByCategory(6L);
        Long countOfAddAmountCat7 = transactionService.getTransactionCountByCategory(7L);
        Long countOfAddAmountCat8 = transactionService.getTransactionCountByCategory(8L);
        Long countOfAddAmountCat9 = transactionService.getTransactionCountByCategory(9L);
        Long countOfAddAmountCat10 = transactionService.getTransactionCountByCategory(10L);
        Long countOfAddAmountCat12 = transactionService.getTransactionCountByCategory(12L);

        Long countOfTransactions = countOfAddAmountCat1 + countOfAddAmountCat2 + countOfAddAmountCat3 + countOfAddAmountCat4 + countOfAddAmountCat5 + countOfAddAmountCat6 + countOfAddAmountCat7 + countOfAddAmountCat8 + countOfAddAmountCat9 + countOfAddAmountCat10 + countOfAddAmountCat12;

        Double sumOfTrCat1 = transactionService.getTransactionSumByCategory(1L);
        Double sumOfTrCat2 = transactionService.getTransactionSumByCategory(2L);
        Double sumOfTrCat3 = transactionService.getTransactionSumByCategory(3L);
        Double sumOfTrCat4 = transactionService.getTransactionSumByCategory(4L);
        Double sumOfTrCat5 = transactionService.getTransactionSumByCategory(5L);
        Double sumOfTrCat6 = transactionService.getTransactionSumByCategory(6L);
        Double sumOfTrCat7 = transactionService.getTransactionSumByCategory(7L);
        Double sumOfTrCat8 = transactionService.getTransactionSumByCategory(8L);
        Double sumOfTrCat9 = transactionService.getTransactionSumByCategory(9L);
        Double sumOfTrCat10 = transactionService.getTransactionSumByCategory(10L);
        Double sumOfTrCat12 = transactionService.getTransactionSumByCategory(12L);

        Double sumOfTr = sumOfTrCat1 + sumOfTrCat2 + sumOfTrCat3 + sumOfTrCat4 + sumOfTrCat5 + sumOfTrCat6 + sumOfTrCat7 + sumOfTrCat8 + sumOfTrCat9 + sumOfTrCat10 + sumOfTrCat12;
        Double amountOfAdds = transactionService.getTransactionSumByCategory(11L);

        model.addAttribute("countOfAddAmount", countOfAddAmount);
        model.addAttribute("countOfAddAmountCat1", countOfAddAmountCat1);
        model.addAttribute("countOfAddAmountCat2", countOfAddAmountCat2);
        model.addAttribute("countOfAddAmountCat3", countOfAddAmountCat3);
        model.addAttribute("countOfAddAmountCat4", countOfAddAmountCat4);
        model.addAttribute("countOfAddAmountCat5", countOfAddAmountCat5);
        model.addAttribute("countOfAddAmountCat6", countOfAddAmountCat6);
        model.addAttribute("countOfAddAmountCat7", countOfAddAmountCat7);
        model.addAttribute("countOfAddAmountCat8", countOfAddAmountCat8);
        model.addAttribute("countOfAddAmountCat9", countOfAddAmountCat9);
        model.addAttribute("countOfAddAmountCat10", countOfAddAmountCat10);
        model.addAttribute("countOfAddAmountCat12", countOfAddAmountCat12);
        model.addAttribute("sumOfTrCat1", sumOfTrCat1);
        model.addAttribute("sumOfTrCat2", sumOfTrCat2);
        model.addAttribute("sumOfTrCat3", sumOfTrCat3);
        model.addAttribute("sumOfTrCat4", sumOfTrCat4);
        model.addAttribute("sumOfTrCat5", sumOfTrCat5);
        model.addAttribute("sumOfTrCat6", sumOfTrCat6);
        model.addAttribute("sumOfTrCat7", sumOfTrCat7);
        model.addAttribute("sumOfTrCat8", sumOfTrCat8);
        model.addAttribute("sumOfTrCat9", sumOfTrCat9);
        model.addAttribute("sumOfTrCat10", sumOfTrCat10);
        model.addAttribute("sumOfTrCat12", sumOfTrCat12);
        model.addAttribute("countOfTransactions", countOfTransactions);
        model.addAttribute("sumOfTr", sumOfTr);
        model.addAttribute("amountOfAdds", amountOfAdds);

        // %%%%%%%%
        double percentDoneGoals = (countOfGoals > 0) ? (double) countOfDoneGoals / countOfGoals * 100 : 0;
        double percentCurrentGoals = (countOfGoals > 0) ? (double) countOfCurrentGoals / countOfGoals * 100 : 0;
        double percentExpiredGoals = (countOfGoals > 0) ? (double) countOfExpiredGoals / countOfGoals * 100 : 0;
        model.addAttribute("percentDoneGoals", percentDoneGoals);
        model.addAttribute("percentCurrentGoals", percentCurrentGoals);
        model.addAttribute("percentExpiredGoals", percentExpiredGoals);

        return "stats";
    }
}
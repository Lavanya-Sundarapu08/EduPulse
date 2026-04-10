package com.edupulse.controller;

import com.edupulse.model.User;
import com.edupulse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/password")
public class PasswordController {

    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/change")
    public String changePassword(
            Authentication authentication,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model) {

        // Get current logged-in user
        String username = authentication.getName();
        Optional<User> userOpt = userRepo.findByUsername(username);

        if (userOpt.isEmpty()) {
            model.addAttribute("error", "User not found!");
            return "redirect:/staff/dashboard";
        }

        User user = userOpt.get();

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            model.addAttribute("error", "Current password is incorrect!");
            return "redirect:/staff/dashboard";
        }

        // Check if new passwords match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match!");
            return "redirect:/staff/dashboard";
        }

        // Check password length
        if (newPassword.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters!");
            return "redirect:/staff/dashboard";
        }

        // Check if new password is same as old password
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            model.addAttribute("error", "New password cannot be the same as current password!");
            return "redirect:/staff/dashboard";
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        model.addAttribute("success", "Password changed successfully!");
        
        // Check if user is principal or staff
        boolean isPrincipal = user.getRole().toString().equals("PRINCIPAL");
        return "redirect:/" + (isPrincipal ? "principal" : "staff") + "/dashboard";
    }
}
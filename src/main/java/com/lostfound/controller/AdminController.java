package com.lostfound.controller;

import com.lostfound.service.ClaimService;
import com.lostfound.service.FoundItemService;
import com.lostfound.service.LostItemService;
import com.lostfound.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private LostItemService lostItemService;

    @Autowired
    private FoundItemService foundItemService;

    @Autowired
    private ClaimService claimService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("totalUsers", userService.countUsers());
        model.addAttribute("totalLostReports", lostItemService.countTotalLost());
        model.addAttribute("totalFoundReports", foundItemService.countTotalFound());
        model.addAttribute("pendingClaimsCount", claimService.countPendingClaims());
        model.addAttribute("returnedItemsCount", lostItemService.countReturned());

        model.addAttribute("usersList", userService.getAllUsers());
        model.addAttribute("lostItemsList", lostItemService.getAllLostItems());
        model.addAttribute("foundItemsList", foundItemService.getAllFoundItems());
        model.addAttribute("claimsList", claimService.getAllClaims());

        return "admin-dashboard";
    }

    @PostMapping("/claim/approve/{id}")
    public String approveClaim(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            claimService.approveClaim(id);
            redirectAttributes.addFlashAttribute("successMessage", "Claim approved successfully! Item status updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error approving claim: " + e.getMessage());
        }
        return "redirect:/admin/dashboard#claims-section";
    }

    @PostMapping("/claim/reject/{id}")
    public String rejectClaim(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            claimService.rejectClaim(id);
            redirectAttributes.addFlashAttribute("successMessage", "Claim rejected.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error rejecting claim: " + e.getMessage());
        }
        return "redirect:/admin/dashboard#claims-section";
    }

    @PostMapping("/lost/mark-returned/{id}")
    public String markLostAsReturned(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        lostItemService.updateStatus(id, "RETURNED");
        redirectAttributes.addFlashAttribute("successMessage", "Item marked as returned.");
        return "redirect:/admin/dashboard#lost-section";
    }

    @PostMapping("/found/mark-returned/{id}")
    public String markFoundAsReturned(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        foundItemService.updateStatus(id, "CLAIMED");
        redirectAttributes.addFlashAttribute("successMessage", "Item marked as returned/claimed.");
        return "redirect:/admin/dashboard#found-section";
    }

    @PostMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "User account removed.");
        return "redirect:/admin/dashboard#users-section";
    }
}

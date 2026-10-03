package com.lostfound.controller;

import com.lostfound.model.Claim;
import com.lostfound.model.FoundItem;
import com.lostfound.model.User;
import com.lostfound.service.ClaimService;
import com.lostfound.service.FoundItemService;
import com.lostfound.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/claim")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    @Autowired
    private FoundItemService foundItemService;

    @Autowired
    private UserService userService;

    @GetMapping("/item/{foundItemId}")
    public String showClaimForm(@PathVariable("foundItemId") Long foundItemId, Model model, Authentication authentication) {
        FoundItem foundItem = foundItemService.getFoundItemById(foundItemId)
                .orElseThrow(() -> new IllegalArgumentException("Found item not found."));

        if (!"OPEN".equalsIgnoreCase(foundItem.getStatus())) {
            model.addAttribute("errorMessage", "This item is currently not open for claiming.");
            return "redirect:/item/found/" + foundItemId;
        }

        User currentUser = userService.findByEmail(authentication.getName()).orElseThrow();
        model.addAttribute("foundItem", foundItem);
        model.addAttribute("currentUser", currentUser);
        return "claim-form";
    }

    @PostMapping("/submit")
    public String submitClaim(
            @RequestParam("foundItemId") Long foundItemId,
            @RequestParam("claimantName") String claimantName,
            @RequestParam("description") String description,
            @RequestParam(value = "additionalInfo", required = false) String additionalInfo,
            @RequestParam(value = "proofImageFile", required = false) MultipartFile proofImageFile,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            User currentUser = userService.findByEmail(authentication.getName()).orElseThrow();
            Claim claim = claimService.submitClaim(foundItemId, currentUser, claimantName, description, additionalInfo, proofImageFile);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Claim submitted successfully! Claim ID: " + claim.getClaimCode());
            return "redirect:/dashboard";
        } catch (IllegalStateException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/claim/item/" + foundItemId;
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload proof document. Please try again.");
            return "redirect:/claim/item/" + foundItemId;
        }
    }
}

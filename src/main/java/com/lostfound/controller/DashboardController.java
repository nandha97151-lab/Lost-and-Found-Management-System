package com.lostfound.controller;

import com.lostfound.model.Claim;
import com.lostfound.model.FoundItem;
import com.lostfound.model.LostItem;
import com.lostfound.model.User;
import com.lostfound.service.ClaimService;
import com.lostfound.service.FoundItemService;
import com.lostfound.service.LostItemService;
import com.lostfound.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private LostItemService lostItemService;

    @Autowired
    private FoundItemService foundItemService;

    @Autowired
    private ClaimService claimService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String userDashboard(Authentication authentication, Model model) {
        User currentUser = userService.findByEmail(authentication.getName()).orElseThrow();

        List<LostItem> myLostItems = lostItemService.getLostItemsByUser(currentUser.getId());
        List<FoundItem> myFoundItems = foundItemService.getFoundItemsByUser(currentUser.getId());
        List<Claim> myClaims = claimService.getClaimsByUser(currentUser.getId());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("myLostItems", myLostItems);
        model.addAttribute("myFoundItems", myFoundItems);
        model.addAttribute("myClaims", myClaims);

        return "dashboard";
    }

    @PostMapping("/lost-item/delete/{id}")
    public String deleteLostItem(@PathVariable("id") Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        User currentUser = userService.findByEmail(authentication.getName()).orElseThrow();
        LostItem item = lostItemService.getLostItemById(id).orElse(null);

        if (item != null && (item.getUser().getId().equals(currentUser.getId()) || "ADMIN".equals(currentUser.getRole()))) {
            lostItemService.deleteLostItem(id);
            redirectAttributes.addFlashAttribute("successMessage", "Lost item report deleted.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized action.");
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/found-item/delete/{id}")
    public String deleteFoundItem(@PathVariable("id") Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        User currentUser = userService.findByEmail(authentication.getName()).orElseThrow();
        FoundItem item = foundItemService.getFoundItemById(id).orElse(null);

        if (item != null && (item.getUser().getId().equals(currentUser.getId()) || "ADMIN".equals(currentUser.getRole()))) {
            foundItemService.deleteFoundItem(id);
            redirectAttributes.addFlashAttribute("successMessage", "Found item report deleted.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized action.");
        }
        return "redirect:/dashboard";
    }
}

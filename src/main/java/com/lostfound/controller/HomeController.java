package com.lostfound.controller;

import com.lostfound.service.ClaimService;
import com.lostfound.service.FoundItemService;
import com.lostfound.service.LostItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private LostItemService lostItemService;

    @Autowired
    private FoundItemService foundItemService;

    @Autowired
    private ClaimService claimService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("totalLost", lostItemService.countTotalLost());
        model.addAttribute("totalFound", foundItemService.countTotalFound());
        model.addAttribute("itemsReturned", lostItemService.countReturned());
        model.addAttribute("pendingClaims", claimService.countPendingClaims());
        
        // Show recent 6 items on home page
        model.addAttribute("recentLostItems", lostItemService.getAllLostItems().stream().limit(3).toList());
        model.addAttribute("recentFoundItems", foundItemService.getAllFoundItems().stream().limit(3).toList());
        
        return "home";
    }
}

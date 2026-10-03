package com.lostfound.controller;

import com.lostfound.model.LostItem;
import com.lostfound.model.User;
import com.lostfound.service.LostItemService;
import com.lostfound.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class LostItemController {

    @Autowired
    private LostItemService lostItemService;

    @Autowired
    private UserService userService;

    @GetMapping("/report-lost")
    public String showReportLostForm(Model model) {
        if (!model.containsAttribute("lostItem")) {
            model.addAttribute("lostItem", new LostItem());
        }
        return "report-lost";
    }

    @PostMapping("/report-lost")
    public String submitLostItem(@Valid @ModelAttribute("lostItem") LostItem lostItem,
                                 BindingResult bindingResult,
                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "report-lost";
        }

        try {
            User currentUser = userService.findByEmail(authentication.getName()).orElseThrow();
            LostItem saved = lostItemService.saveLostItem(lostItem, currentUser, imageFile);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Lost item reported successfully! Tracking ID: " + saved.getItemCode());
            return "redirect:/dashboard";
        } catch (IOException e) {
            bindingResult.reject("upload.error", "Failed to upload image. Please try again.");
            return "report-lost";
        }
    }
}

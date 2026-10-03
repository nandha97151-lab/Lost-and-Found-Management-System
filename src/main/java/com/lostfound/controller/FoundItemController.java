package com.lostfound.controller;

import com.lostfound.model.FoundItem;
import com.lostfound.model.User;
import com.lostfound.service.FoundItemService;
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
public class FoundItemController {

    @Autowired
    private FoundItemService foundItemService;

    @Autowired
    private UserService userService;

    @GetMapping("/report-found")
    public String showReportFoundForm(Model model) {
        if (!model.containsAttribute("foundItem")) {
            model.addAttribute("foundItem", new FoundItem());
        }
        return "report-found";
    }

    @PostMapping("/report-found")
    public String submitFoundItem(@Valid @ModelAttribute("foundItem") FoundItem foundItem,
                                  BindingResult bindingResult,
                                  @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "report-found";
        }

        try {
            User currentUser = userService.findByEmail(authentication.getName()).orElseThrow();
            FoundItem saved = foundItemService.saveFoundItem(foundItem, currentUser, imageFile);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Found item reported successfully! Tracking ID: " + saved.getItemCode());
            return "redirect:/dashboard";
        } catch (IOException e) {
            bindingResult.reject("upload.error", "Failed to upload image. Please try again.");
            return "report-found";
        }
    }
}

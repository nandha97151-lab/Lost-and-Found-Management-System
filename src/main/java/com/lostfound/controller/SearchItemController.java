package com.lostfound.controller;

import com.lostfound.model.FoundItem;
import com.lostfound.model.LostItem;
import com.lostfound.service.FoundItemService;
import com.lostfound.service.LostItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class SearchItemController {

    @Autowired
    private LostItemService lostItemService;

    @Autowired
    private FoundItemService foundItemService;

    @GetMapping("/search")
    public String searchPage(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "type", required = false, defaultValue = "ALL") String type,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        List<LostItem> lostItems = List.of();
        List<FoundItem> foundItems = List.of();

        if ("ALL".equalsIgnoreCase(type) || "LOST".equalsIgnoreCase(type)) {
            lostItems = lostItemService.searchLostItems(keyword, category, location, date);
        }
        if ("ALL".equalsIgnoreCase(type) || "FOUND".equalsIgnoreCase(type)) {
            foundItems = foundItemService.searchFoundItems(keyword, category, location, date);
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("location", location);
        model.addAttribute("type", type);
        model.addAttribute("date", date);

        model.addAttribute("lostItems", lostItems);
        model.addAttribute("foundItems", foundItems);

        return "search";
    }

    @GetMapping("/item/lost/{id}")
    public String viewLostItemDetails(@PathVariable("id") Long id, Model model) {
        LostItem lostItem = lostItemService.getLostItemById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Lost Item ID: " + id));
        model.addAttribute("item", lostItem);
        model.addAttribute("itemType", "LOST");
        return "item-details";
    }

    @GetMapping("/item/found/{id}")
    public String viewFoundItemDetails(@PathVariable("id") Long id, Model model) {
        FoundItem foundItem = foundItemService.getFoundItemById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Found Item ID: " + id));
        model.addAttribute("item", foundItem);
        model.addAttribute("itemType", "FOUND");
        return "item-details";
    }
}

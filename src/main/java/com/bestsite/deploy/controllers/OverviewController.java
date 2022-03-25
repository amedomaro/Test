package com.bestsite.deploy.controllers;

import com.bestsite.deploy.model.Comment;
import com.bestsite.deploy.model.Overview;
import com.bestsite.deploy.repository.CommentRepository;
import com.bestsite.deploy.repository.OverviewRepository;
import com.bestsite.deploy.repository.UserRepository;
import com.bestsite.deploy.service.OverviewService;
import com.bestsite.deploy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
public class OverviewController {

    private final OverviewService overviewService;
    private final OverviewRepository overviewRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Autowired
    public OverviewController(OverviewService overviewService, OverviewRepository overviewRepository, UserRepository userRepository,
                              UserService userService, CommentRepository commentRepository) {
        this.overviewService = overviewService;
        this.overviewRepository = overviewRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/overview")
    public String showOverviewPage(Model model) {
        Iterable<Overview> overviews = overviewRepository.findAll();
        model.addAttribute("overviews", overviews);
        return "overviews/overview";
    }

    @GetMapping("/overview/add")
    public String addOverview(Model model) {
        model.addAttribute("user",
                userRepository.findByUsername(userService.getCurrentUser().getName()).orElseThrow());
        return "overviews/overview-add";
    }

    @GetMapping("/overview/add/{id}")
    public String addOverviewFromUser(@PathVariable(value = "id") long id, Model model) {
        model.addAttribute("user", userRepository.findById(id).orElseThrow());
        return "overviews/overview-add";
    }

    @PostMapping("/overview/add/{id}")
    public String addOverviewFromUser(@PathVariable(value = "id") long id, @ModelAttribute("overview") Overview overview,
                                      @RequestParam Optional<MultipartFile> newImage) {
        overviewService.saveOverview(id, overview, newImage);
        return "redirect:/overview";
    }

    @GetMapping("/overview/{id}")
    public String showDetail(@PathVariable(name = "id") long id, Model model) {
        if (overviewService.receiveData(id, model)) return "redirect:/overview";
        Iterable<Comment> comments = commentRepository.findByOverview(overviewRepository.findById(id).orElseThrow());
        model.addAttribute("comments", comments);
        return "overviews/overview-detail";
    }

    @PostMapping("/overview/{id}")
    public String addComment(@PathVariable(value = "id") long id, @RequestParam(name = "textComment") String text) {
        overviewService.saveComment(id, text);
        return "redirect:/overview/{id}";
    }

    @GetMapping("/overview/{id}/edit")
    public String edit(@PathVariable(name = "id") long id, Model model) {
        if (overviewService.receiveData(id, model)) return "redirect:/overview";
        return "overviews/overview-edit";
    }

    @PostMapping("/overview/{id}/edit")
    public String overviewUpdate(@PathVariable(name = "id") long id, @ModelAttribute("overview") Overview overview,
                                 @RequestParam Optional<MultipartFile> newImage) {
        overviewService.editOverview(id, overview, newImage);
        return "redirect:/overview";
    }

    @PostMapping("/overview/{id}/delete")
    public String overviewDelete(@PathVariable(name = "id") long id) {
        overviewService.deleteOverview(id);
        return "redirect:/overview";
    }
}

package com.bestsite.deploy.service;

import com.bestsite.deploy.model.Comment;
import com.bestsite.deploy.model.Overview;
import com.bestsite.deploy.model.User;
import com.bestsite.deploy.repository.CommentRepository;
import com.bestsite.deploy.repository.OverviewRepository;
import com.bestsite.deploy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class OverviewService {

    private Overview overview;
    private final OverviewRepository overviewRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CloudService cloudService;
    private final UserService userService;

    @Autowired
    public OverviewService(OverviewRepository overviewRepository, CommentRepository commentRepository,
                           UserRepository userRepository, CloudService cloudService, UserService userService) {
        this.overviewRepository = overviewRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.cloudService = cloudService;
        this.userService = userService;
    }

    public boolean receiveData(@PathVariable(name = "id") long id, Model model) {
        if (!overviewRepository.existsById(id)) return true;             // check for ID
        Optional<Overview> overview = overviewRepository.findById(id);
        ArrayList<Overview> result = new ArrayList<>();
        overview.ifPresent(result::add);
        model.addAttribute("overview", result);
        return false;
    }

    public void saveOverview(long id, Overview overview, Optional<MultipartFile> newImage){
        overview.setAuthor(userRepository.findById(id).orElseThrow());
        String image = cloudService.uploadFile(newImage.orElseThrow());
        overview.setImage(image);
        overviewRepository.save(overview);
    }

    public void deleteOverview(long id){
        overview = overviewRepository.findById(id).orElseThrow();
        if(overview.getImage() != null) cloudService.deleteFile(overview.getImage());
        overviewRepository.delete(overview);
    }

    public void editOverview(long id, Overview updatedOverview, Optional<MultipartFile> newImage){
        overview = overviewRepository.findById(id).orElseThrow();
        overview.setTitle(updatedOverview.getTitle());
        overview.setDescription(updatedOverview.getDescription());
        overview.setText(updatedOverview.getText());
        if (cloudService.fileIsPresent(newImage.orElseThrow())){
            String image = cloudService.uploadFile(newImage.orElseThrow());
            overview.setImage(image);
        }
        overviewRepository.save(overview);
    }

    public void saveComment(long id, String text) {
        overview = overviewRepository.findById(id).orElseThrow();
        User user = userRepository.findByUsername(userService.getCurrentUser().getName()).orElseThrow();
        Comment comment = new Comment(text, overview, user);
        commentRepository.save(comment);
    }
}

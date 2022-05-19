package com.example.controllers;

import com.example.facades.ForumFacade;
import com.example.facades.RedisFacade;
import com.example.models.Comment;
import com.example.models.Post;
import com.example.payload.request.LoginRequest;
import com.example.payload.response.MessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.GsonBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.UnknownHostException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/forum")
public class ForumController {
    GsonBuilder GSON = new GsonBuilder().setPrettyPrinting();

    @GetMapping("/public")
    public String testAccess(){
        return "Success!";
    }

    @GetMapping("/post/all")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String getAllPosts() throws UnknownHostException, JsonProcessingException {
        ForumFacade facade = new ForumFacade();
        RedisFacade redisFacade = new RedisFacade();
        List<Post> cache = redisFacade.getAllPosts();
        if(cache.isEmpty()){
            List<Post> result = facade.getAllPosts();
            return GSON.create().toJson(result);
        } else {
            return GSON.create().toJson(cache);
        }

    }

    @PostMapping("/post")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> makePost(@Valid @RequestBody Post post) throws UnknownHostException {
        ForumFacade facade = new ForumFacade();
        Post resPost = facade.addPost(post);
        return ResponseEntity.ok(new MessageResponse("Post was registered successfully!"));
    }

    // To use this, remember to send in the hex-converted id in request body
    @PostMapping("/post/like")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> likePost(@Valid @RequestBody Post post) throws UnknownHostException {
        ForumFacade facade = new ForumFacade();
        Post resPost = facade.likePost(post);
        return ResponseEntity.ok(new MessageResponse("Post was liked successfully!"));
    }

    // To use this, remember to send in the hex-converted id in request body
    @PostMapping("/post/comment")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> commentPost(@Valid @RequestBody Comment comment) throws UnknownHostException {
        ForumFacade facade = new ForumFacade();
        Post resPost = facade.commentPost(comment);
        return ResponseEntity.ok(new MessageResponse("Post was commented successfully!"));
    }
}

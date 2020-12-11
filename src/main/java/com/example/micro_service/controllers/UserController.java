package com.example.micro_service.controllers;

import com.example.micro_service.entities.Post;
import com.example.micro_service.entities.User;
import com.example.micro_service.exeptions.UserNotFoundException;
import com.example.micro_service.services.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {


    @Autowired
    UserDaoService service;

    @GetMapping(path = "/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }


    @GetMapping(path = "/users/{id}/posts")
    public List<Post> retrieveAllPostByUserId(@PathVariable int id) {
        Optional<User> user = service.findOne(id);
        if (!user.isPresent()) {
            throw new UserNotFoundException("Id - " + id + "NOT FOUND");
        }
        return user.get().getPosts();
    }

    @GetMapping("users/{id}")
    public EntityModel<User> retreiveOne(@PathVariable int id) {
        Optional<User> userOptional = service.findOne(id);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("id-" + id + "not found");
        }
        EntityModel<User> resource = EntityModel.of(userOptional.get());

        WebMvcLinkBuilder linkTo =
                linkTo(methodOn(this.getClass()).retrieveAllUsers());

        resource.add(linkTo.withRel("all-users"));



        return resource;

    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {

        User newUser = service.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Object> createPost(@PathVariable int id, @RequestBody Post post) {
        Post newPost = service.createPost(id, post);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newPost.getId()).toUri();

        return ResponseEntity.created(location).build();
    }


    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        this.service.deleteById(id);
    }


}

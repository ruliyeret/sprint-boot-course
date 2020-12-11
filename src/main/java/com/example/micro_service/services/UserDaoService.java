package com.example.micro_service.services;

import com.example.micro_service.entities.Post;
import com.example.micro_service.entities.User;
import com.example.micro_service.exeptions.UserNotFoundException;
import com.example.micro_service.repoistory.PostRepository;
import com.example.micro_service.repoistory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class UserDaoService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User newUser) {
        return userRepository.save(newUser);
    }

    public Optional<User> findOne(int id){
        return userRepository.findById(id);
    }

    public void deleteById(int id){
        userRepository.deleteById(id);
    }

    public Post createPost(int userId, Post post){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            throw  new UserNotFoundException("Id - " + userId + " not found");
        }

        post.setUser(user.get());
        return postRepository.save(post);


    }
}

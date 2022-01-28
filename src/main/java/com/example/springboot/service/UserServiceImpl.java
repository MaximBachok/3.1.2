package com.example.springboot.service;

import com.example.springboot.model.User;
import com.example.springboot.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public void updateUser(User user) {
        String passwordFromForm = user.getPassword();
        String encodedPasswordFromBase = userRepository.findUserById(user.getId()).getPassword();
        if(passwordFromForm.equals(encodedPasswordFromBase)) {
            user.setPassword(encodedPasswordFromBase);
        } else {
            if(passwordEncoder.matches(passwordFromForm, encodedPasswordFromBase)){
                user.setPassword(encodedPasswordFromBase);
            } else {
                user.setPassword(passwordEncoder.encode(passwordFromForm));
            }
        }
        userRepository.save(user);
    }

    @Override
    public User getByName(String name) {
        return userRepository.findUserByName(name);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userRepository.findUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user with that name");
        }
        return user;
    }
}

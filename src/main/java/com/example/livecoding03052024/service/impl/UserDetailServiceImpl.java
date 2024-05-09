package com.example.livecoding03052024.service.impl;


import com.example.livecoding03052024.dto.CustomUserDetailDTO;
import com.example.livecoding03052024.repository.UserRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service("userDetailServiceImpl")
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userInfo = userRepository.findUserInfoByUsernameIgnoreCase(username).orElseThrow(() ->
                new ResponseStatusException(HttpStatusCode.valueOf(401)));
        return new CustomUserDetailDTO(userInfo);
    }
}

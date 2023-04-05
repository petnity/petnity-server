package com.example.petnity.service.impl;

import com.example.petnity.data.entity.UserEntity;
import com.example.petnity.data.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findOneWithAuthoritiesByUserAccount(username)
                .map(user -> createUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException((username + "-> 일치하는 계정이 없습니다.")));
    }

    private User createUser(String username, UserEntity user) {
        if (!user.isUserActivated()) {
            throw new RuntimeException(username + "-> 활성화 되어 있지 않습니다.");
        }

        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return new User(user.getUserAccount(),
                user.getUserPassword(),
                grantedAuthorities);
    }

//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String userAccount) throws UsernameNotFoundException {
//        Account account = userRepository.findAccountByUserAccount(userAccount)
//                .orElseThrow(()-> new UsernameNotFoundException("해당 이메일과 일치하는 계정이 없습니다."));
//        return new AccountDetails(account);
//    }
}

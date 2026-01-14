package com.code.Sales.Stock.service;

import com.code.Sales.Stock.Repository.UserCredRepo;
import com.code.Sales.Stock.model.UserCred;
import com.code.Sales.Stock.model.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserCredRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCred user = userRepo.findByUsername(username);
        if (user == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException(username);
        }
        return new UserPrinciple(user);
    }
}

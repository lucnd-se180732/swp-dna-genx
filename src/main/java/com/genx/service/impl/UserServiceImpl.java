package com.genx.service.impl;

import com.genx.entity.User;
import com.genx.repository.IUserRepository;
import com.genx.service.interfaces.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;


    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) {
    try {
        System.out.println("🔍 Đang tìm user với username/email = " + usernameOrEmail);

        return userRepository.findByUsernameOrEmail(usernameOrEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (RuntimeException e) {
            // Log the exception if needed
            throw e; // Re-throw the exception to be handled by the caller
        }

    }
}

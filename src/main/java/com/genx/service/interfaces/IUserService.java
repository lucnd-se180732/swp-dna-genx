package com.genx.service.interfaces;

import com.genx.entity.User;

public interface IUserService {
    User findByUsernameOrEmail(String usernameOrEmail);
}

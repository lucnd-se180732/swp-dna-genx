package com.genx.service.interfaces;

import com.genx.dto.request.ChangePasswordRequest;
import com.genx.dto.request.UpdateProfileRequest;
import com.genx.dto.response.UserProfileResponse;
import com.genx.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {

    UserProfileResponse getUserProfileByUsername(String username);

    UserProfileResponse updateUserProfile(String username, UpdateProfileRequest request);

    String uploadAvatar(String username, MultipartFile file);

    void changePassword(String username, ChangePasswordRequest request);
}

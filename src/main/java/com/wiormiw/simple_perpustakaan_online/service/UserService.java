package com.wiormiw.simple_perpustakaan_online.service;

import com.wiormiw.simple_perpustakaan_online.models.dto.image.ImageDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.user.UserCreateDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.user.UserDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.user.UserProfileDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.user.UserUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserService {
    public UserDTO createUser(UserCreateDTO dto);
    public Page<UserDTO> getUsers(String search, int page, int size);
    public UserProfileDTO getUserById(UUID id);
    public UserProfileDTO getUserByEmail(String email);
    public long getTotalUsers();
    public UserProfileDTO updateUser(UUID id, UserUpdateDTO userUpdateDTO);
    public ImageDTO updateProfileImage(UUID id, ImageDTO img);
}

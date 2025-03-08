package com.wiormiw.simple_perpustakaan_online.service.implementation;

import com.wiormiw.simple_perpustakaan_online.config.exception.ResourceNotFoundException;
import com.wiormiw.simple_perpustakaan_online.models.Profile;
import com.wiormiw.simple_perpustakaan_online.models.Role;
import com.wiormiw.simple_perpustakaan_online.models.User;
import com.wiormiw.simple_perpustakaan_online.models.dto.image.ImageDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.user.ProfileDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.user.UserCreateDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.user.UserDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.user.UserProfileDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.user.UserUpdateDTO;
import com.wiormiw.simple_perpustakaan_online.repository.RentRepository;
import com.wiormiw.simple_perpustakaan_online.repository.RoleRepository;
import com.wiormiw.simple_perpustakaan_online.repository.UserRepository;
import com.wiormiw.simple_perpustakaan_online.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RentRepository rentRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            RentRepository rentRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.rentRepository = rentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDTO createUser(UserCreateDTO dto) {
        Role userRole = roleRepository.findByName(Role.RoleType.USER)
                .orElseThrow(() -> new IllegalStateException("USER role not found"));

        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(userRole));

        Profile profile = new Profile();
        profile.setFullName(dto.fullName());
        profile.setAddress(dto.address());
        profile.setPhoneNumber(dto.phoneNumber());
        profile.setUser(user);
        user.setProfile(profile);

        user = userRepository.save(user);
        return mapToUserDTO(user);
    }

    public Page<UserDTO> getUsers(String search, int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        Page<User> users = (search == null || search.isBlank())
                ? userRepository.findAll(pageable)
                : userRepository.findByEmailContainingIgnoreCase(search, pageable);

        return users.map(this::mapToUserDTO);
    }

    public UserProfileDTO getUserById(UUID id) {
        return userRepository.findById(id)
                .map(this::mapToUserProfileDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    public UserProfileDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToUserProfileDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    @Transactional
    public UserProfileDTO updateUser(UUID id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (userUpdateDTO.password() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.password()));
        }

        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
            user.setProfile(profile);
        }

        if (userUpdateDTO.fullName() != null) profile.setFullName(userUpdateDTO.fullName());
        if (userUpdateDTO.address() != null) profile.setAddress(userUpdateDTO.address());
        if (userUpdateDTO.phoneNumber() != null) profile.setPhoneNumber(userUpdateDTO.phoneNumber());

        return mapToUserProfileDTO(user);
    }

    @Transactional
    public ImageDTO updateProfileImage(UUID id, ImageDTO img) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
            user.setProfile(profile);
        }

        if (img.imageUrl() != null) profile.setProfileUrl(img.imageUrl());

        return new ImageDTO(profile.getProfileUrl());
    }

    private UserDTO mapToUserDTO(User user) {
        boolean hasActiveRentals = rentRepository.existsByUserIdAndStatus(user.getId());
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                hasActiveRentals
        );
    }

    private UserProfileDTO mapToUserProfileDTO(User user) {
        Profile profile = user.getProfile();
        boolean hasActiveRentals = rentRepository.existsByUserIdAndStatus(user.getId());
        return new UserProfileDTO(
                user.getId(),
                user.getEmail(),
                hasActiveRentals,
                new ProfileDTO(
                        profile.getFullName(),
                        profile.getAddress(),
                        profile.getProfileUrl(),
                        profile.getPhoneNumber()
                )
        );
    }
}

package com.wiormiw.simple_perpustakaan_online.controller;

import com.wiormiw.simple_perpustakaan_online.models.CustomUserDetails;
import com.wiormiw.simple_perpustakaan_online.models.dto.image.ImageDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.user.UserDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.user.UserProfileDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.user.UserUpdateDTO;
import com.wiormiw.simple_perpustakaan_online.service.implementation.CloudinaryServiceImpl;
import com.wiormiw.simple_perpustakaan_online.service.implementation.UserServiceImpl;
import com.wiormiw.simple_perpustakaan_online.utils.ImageMimeValidator;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userServiceImpl;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;

    public UserController(
            UserServiceImpl userServiceImpl,
            CloudinaryServiceImpl cloudinaryServiceImpl
    ) {
        this.userServiceImpl = userServiceImpl;
        this.cloudinaryServiceImpl = cloudinaryServiceImpl;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserProfileDTO> getUserById() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        UUID userId = userDetails.getId();
        return ResponseEntity.ok(userServiceImpl.getUserById(userId));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserProfileDTO> updateUser(
            @Valid @RequestBody UserUpdateDTO userUpdateDTO
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        UUID userId = userDetails.getId();
        return ResponseEntity.ok(userServiceImpl.updateUser(userId, userUpdateDTO));
    }

    @PutMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ImageDTO> updateProfileImage(
            @RequestPart("image") MultipartFile image
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        UUID userId = userDetails.getId();

        if (ImageMimeValidator.isNotValidImage(image)) {
            throw new IllegalArgumentException("Invalid image format. Only PNG, JPG, JPEG, and WEBP are allowed.");
        }
        String imageUrl = cloudinaryServiceImpl.uploadImage(image, "e-library");
        ImageDTO imageDTO = new ImageDTO(imageUrl);

        return ResponseEntity.ok(userServiceImpl.updateProfileImage(userId, imageDTO));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> getUsers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userServiceImpl.getUsers(search, page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userServiceImpl.getUserById(id));
    }

    @GetMapping("/by-email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userServiceImpl.getUserByEmail(email));
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getUserCount() {
        return ResponseEntity.ok(userServiceImpl.getTotalUsers());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileDTO> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO
    ) {
        return ResponseEntity.ok(userServiceImpl.updateUser(id, userUpdateDTO));
    }

    @PutMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageDTO> updateProfileImage(
            @PathVariable UUID id,
            @RequestPart("image") MultipartFile image
    ) {
        if (ImageMimeValidator.isNotValidImage(image)) {
            throw new IllegalArgumentException("Invalid image format. Only PNG, JPG, JPEG, and WEBP are allowed.");
        }
        String imageUrl = cloudinaryServiceImpl.uploadImage(image, "e-library");
        ImageDTO imageDTO = new ImageDTO(imageUrl);

        return ResponseEntity.ok(userServiceImpl.updateProfileImage(id, imageDTO));
    }
}

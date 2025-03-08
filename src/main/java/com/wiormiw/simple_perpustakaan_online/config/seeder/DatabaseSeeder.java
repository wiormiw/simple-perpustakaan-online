package com.wiormiw.simple_perpustakaan_online.config.seeder;

import com.wiormiw.simple_perpustakaan_online.models.*;
import com.wiormiw.simple_perpustakaan_online.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(
            RoleRepository roleRepository,
            UserRepository userRepository,
            GenreRepository genreRepository,
            ProfileRepository profileRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional // Ensure all operations occur in one transaction
    public void run(String... args) {
        seedRoles();
        seedSuperUser();
        seedGenres();
    }

    private void seedRoles() {
        if (roleRepository.count() == 0) { // Only insert if roles are missing
            Role userRole = new Role();
            userRole.setName(Role.RoleType.USER);

            Role adminRole = new Role();
            adminRole.setName(Role.RoleType.ADMIN);

            roleRepository.saveAll(List.of(userRole, adminRole));
            System.out.println("Default roles USER and ADMIN created!");
        }
    }

    private void seedSuperUser() {
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            Role userRole = roleRepository.findByName(Role.RoleType.USER)
                    .orElseThrow(() -> new IllegalStateException("User role not found!"));

            Role adminRole = roleRepository.findByName(Role.RoleType.ADMIN)
                    .orElseThrow(() -> new IllegalStateException("Admin role not found!"));

            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("Password"));
            admin.setRoles(new HashSet<>(Set.of(userRole, adminRole)));

            Profile adminProfile = new Profile();
            adminProfile.setUser(admin);
            adminProfile.setFullName("Admin Contoh");
            adminProfile.setAddress("Jl. Para Admin III");
            adminProfile.setProfileUrl("https://asset.cloudinary.com/dwshiglkf/0404818aec82bf021ea3d720670fce96");
            adminProfile.setPhoneNumber("087883828182");
            admin.setProfile(adminProfile);

            userRepository.save(admin); // Cascade saves profile due to CascadeType.ALL
            System.out.println("Admin user and profile created!");
        }
    }

    private void seedGenres() {
        List<String> genres = List.of("Science Fiction", "Fantasy", "Mystery", "Horror", "Romance");

        for (String genreName : genres) {
            // Assuming GenreRepository has findByName; add it if missing
            if (genreRepository.findByName(genreName).isEmpty()) {
                Genre newGenre = new Genre();
                newGenre.setName(genreName);
                genreRepository.save(newGenre);
            }
        }
        System.out.println("Default genres seeded!");
    }

    // Utility method to avoid Optional check verbosity (add to GenreRepository if needed)
    private Optional<Genre> findByName(String name) {
        return genreRepository.findAll().stream()
                .filter(g -> g.getName().equals(name))
                .findFirst();
    }
}
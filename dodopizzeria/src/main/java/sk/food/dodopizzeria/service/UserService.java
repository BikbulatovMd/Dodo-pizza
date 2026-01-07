package sk.food.dodopizzeria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.food.dodopizzeria.dto.PasswordChangeDto;
import sk.food.dodopizzeria.dto.UserProfileDto;
import sk.food.dodopizzeria.dto.UserRegistrationDto;
import sk.food.dodopizzeria.entity.Role;
import sk.food.dodopizzeria.entity.User;
import sk.food.dodopizzeria.exception.EmailAlreadyExistsException;
import sk.food.dodopizzeria.exception.InvalidPasswordException;
import sk.food.dodopizzeria.exception.ResourceNotFoundException;
import sk.food.dodopizzeria.repository.RoleRepository;
import sk.food.dodopizzeria.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setDeliveryAddress(dto.getDeliveryAddress());
        user.setEnabled(true);

        // Assign CUSTOMER role by default
        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "ROLE_CUSTOMER"));
        user.getRoles().add(customerRole);

        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Používateľ", id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Používateľ", "email", email));
    }

    @Transactional
    public User updateProfile(Long userId, UserProfileDto dto) {
        User user = findById(userId);

        // Check if email is being changed and if new email already exists
        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setDeliveryAddress(dto.getDeliveryAddress());

        return userRepository.save(user);
    }

    @Transactional
    public void updateProfileImage(Long userId, String imageUrl) {
        User user = findById(userId);
        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, PasswordChangeDto dto) {
        User user = findById(userId);

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPasswordHash())) {
            throw new InvalidPasswordException();
        }

        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    public Page<User> searchUsers(String search, Pageable pageable) {
        if (search == null || search.isBlank()) {
            return userRepository.findAll(pageable);
        }
        return userRepository.searchUsers(search, pageable);
    }

    public List<User> findByRole(String roleName) {
        return userRepository.findByRoleName(roleName);
    }

    @Transactional
    public void toggleUserEnabled(Long userId) {
        User user = findById(userId);
        user.setEnabled(!user.getEnabled());
        userRepository.save(user);
    }

    @Transactional
    public void assignRole(Long userId, String roleName) {
        User user = findById(userId);
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName));
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Transactional
    public void removeRole(Long userId, String roleName) {
        User user = findById(userId);
        user.getRoles().removeIf(r -> r.getName().equals(roleName));
        userRepository.save(user);
    }

    public UserProfileDto toProfileDto(User user) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setDeliveryAddress(user.getDeliveryAddress());
        dto.setProfileImageUrl(user.getProfileImageUrl());
        return dto;
    }
}


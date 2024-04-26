package com.github.malyshevhen.services.impl;

import com.github.malyshevhen.exceptions.EntityAlreadyExistsExeption;
import com.github.malyshevhen.exceptions.EntityNotFoundException;
import com.github.malyshevhen.exceptions.UserValidationException;
import com.github.malyshevhen.models.Address;
import com.github.malyshevhen.models.User;
import com.github.malyshevhen.repositories.UserRepository;
import com.github.malyshevhen.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Value("${user.min-age}")
    private int requiredAge;

    @Transactional
    @Override
    public User save(User userToRegister) {
        assertThatAgeIsLegal(userToRegister);
        assertThatEmailNotExists(userToRegister.getEmail());

        return userRepository.save(userToRegister);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(STR."User with id \{id} not found"));
    }

    @Transactional
    @Override
    public User updateById(Long id, User user) {
        var existingUser = getById(id);
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setAddress(user.getAddress());
        existingUser.setPhone(user.getPhone());

        var existingEmail = existingUser.getEmail();
        var emailToUpdate = user.getEmail();
        if (!Objects.equals(existingEmail, emailToUpdate)) {
            assertThatEmailNotExists(emailToUpdate);
            existingUser.setEmail(user.getEmail());
        }

        return existingUser;
    }

    @Transactional
    @Override
    public User updateEmail(Long id, String email) {
        var existingUser = getById(id);
        existingUser.setEmail(email);

        return existingUser;
    }

    @Transactional
    @Override
    public User updateAddress(Long id, Address address) {
        var existingUser = getById(id);
        existingUser.setAddress(address);

        return existingUser;
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        var existingUser = getById(id);
        userRepository.delete(existingUser);
    }

    private void assertThatEmailNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EntityAlreadyExistsExeption("User with this email already registered");
        }
    }

    private void assertThatAgeIsLegal(User user) {
        long userAge = ChronoUnit.YEARS.between(user.getBirthDate(), LocalDate.now());
        if (userAge < requiredAge) {
            var message = STR."Users age must be greater than or equal to \{requiredAge}";
            throw new UserValidationException(message);
        }
    }
}
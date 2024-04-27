package com.github.malyshevhen.services;

import com.github.malyshevhen.models.DateRange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import com.github.malyshevhen.exceptions.EntityAlreadyExistsException;
import com.github.malyshevhen.exceptions.EntityNotFoundException;
import com.github.malyshevhen.models.Address;
import com.github.malyshevhen.models.User;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Provides methods for managing user data, including creating, retrieving,
 * updating, and deleting users.
 *
 * @author Evhen Malysh
 */
@Validated
public interface UserService {

    /**
     * Saves a new user to the system.
     *
     * @param userToRegister the user to be registered
     * @return the saved user
     *
     * @throws {@link EntityAlreadyExistsException} if email provided by the
     *                registration form is already taken.
     */
    User save(@Valid User userToRegister) throws EntityAlreadyExistsException;

    /**
     * Retrieve page of users based on given pageable.
     *
     * @param pageable  the pagination details
     * @param dateRange
     * @return a {@link Page} object containing the list of users
     * and page details
     */
    Page<User> getAll(@NotNull Pageable pageable, @Valid DateRange dateRange);

    /**
     * Gets the user by identifier.
     *
     * @param id The identifier
     * @return The {@link User} by identifier.
     *
     * @throws {@link EntityNotFoundException} if the user for retrieve is not found
     *                in DB
     */
    User getById(@NotNull Long id) throws EntityNotFoundException;

    /**
     * Update all user fields.
     *
     * @param id   The identifier
     * @param user The parameters for users update
     * @return Updated {@link User}
     *
     * @throws {@link EntityAlreadyExistsException} if the email to update is
     * @throws {@link EntityNotFoundException} if the user to update is not found in
     *                DB.
     */
    User updateById(@NotNull Long id, @Valid User user)
            throws EntityNotFoundException, EntityAlreadyExistsException;

    /**
     * Delete user by unique identifier.
     *
     * @param id The identifier
     *
     * @throws {@link EntityNotFoundException} if the user is not found
     */
    void deleteById(@NotNull Long id) throws EntityNotFoundException;

    /**
     * Update users email field
     *
     * @param id    The identifier
     * @param email The email
     * @return Updated {@link User}
     *
     * @throws {@link EntityAlreadyExistsException} if the email to update is
     *                already taken.
     * @throws {@link EntityNotFoundException} if the user for update is not
     *                found ib DB.
     */
    User updateEmail(Long id, String email) throws EntityNotFoundException, EntityAlreadyExistsException;

    /**
     * Update users address
     *
     * @param id      The identifier
     * @param address The address
     * @return Updated {@link User}
     *
     * @throws {@link EntityNotFoundException} if the user for update is not found
     *                in DB.
     */
    User updateAddress(Long id, Address address) throws EntityNotFoundException;

}

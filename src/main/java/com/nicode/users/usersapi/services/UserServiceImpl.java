package com.nicode.users.usersapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nicode.users.usersapi.DTO.UserDto;
import com.nicode.users.usersapi.exceptions.DataAccessException;
import com.nicode.users.usersapi.exceptions.EntityAlreadyExistsException;
import com.nicode.users.usersapi.exceptions.EntityNotFoundException;
import com.nicode.users.usersapi.models.entities.User;
import com.nicode.users.usersapi.models.entities.UserContactInformation;
import com.nicode.users.usersapi.models.entities.UserCredentials;
import com.nicode.users.usersapi.repositories.UserContactInformationRepository;
import com.nicode.users.usersapi.repositories.UserCredentialsRepository;
import com.nicode.users.usersapi.repositories.UserRepository;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository;

    private final UserCredentialsRepository credentialsRepository;

    private final UserContactInformationRepository contactInfoRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserCredentialsRepository credentialsRepository,
            UserContactInformationRepository contactInfoRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.credentialsRepository = credentialsRepository;
        this.contactInfoRepository = contactInfoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String create(UserDto userDto) {
        Optional<UserCredentials> userCredentialsSaved = this.credentialsRepository
                .findByUsernameLike(userDto.getEmail());

        if (!userCredentialsSaved.isPresent()) {
            UserContactInformation contactInformation = new UserContactInformation();
            contactInformation.setEmail(userDto.getEmail());
            this.contactInfoRepository.save(contactInformation);

            User user = new User();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setIdNumber(userDto.getIdNumber());
            user.setDateOfBirth(userDto.getDateOfBirth());

            user.setContactInformation(contactInformation);

            UserCredentials userCredentials = new UserCredentials();
            userCredentials.setUsername(userDto.getEmail());
            userCredentials.setDisabled(false);
            userCredentials.setLocked(false);
            userCredentials.setPassword(passwordEncoder.encode(userDto.getPassword()));
            if (userDto.getRole() != null && !userDto.getRole().isEmpty() && !userDto.getRole().isBlank()) {
                userCredentials.setRole(userDto.getRole());
            } else {
                userCredentials.setRole("USER");
            }
            userCredentials.setUserInfo(user);

            this.credentialsRepository.save(userCredentials);
            this.userRepository.save(user);

            return ("Se guardó con éxito.");
        } else {
            throw new EntityAlreadyExistsException("El e-mail " + userDto.getEmail() +
                    " ya está registrado. Por favor verifique los datos ingresados.");

        }

    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) this.userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        try {
            return this.userRepository.findById(id);
        } catch (DataAccessException ex) {
            throw new DataAccessException("Data access error: ", ex);
        }
    }

    @Transactional
    public String update(Long id, UserDto userUpdates) {
        Optional<User> userToUpdate = this.userRepository.findById(id);
        Optional<User> userToUpdateConfirm = this.userRepository.findById(userUpdates.getUserId());

        if (!(userToUpdate.isPresent() && userToUpdateConfirm.isPresent())) {
            throw new EntityNotFoundException("no se encontró al usuario. Type: id missmatch exc." +
                    "Info: User DTO id: " + userUpdates.getUserId() + ". User id: " + id);
        }

        if (userToUpdate.isPresent()) {
            User user = userToUpdate.get();
            user.setFirstName(userUpdates.getFirstName());
            user.setLastName(userUpdates.getLastName());
            user.setDateOfBirth(userUpdates.getDateOfBirth());
            user.setIdNumber(userUpdates.getIdNumber());

            UserContactInformation contactInfo = this.contactInfoRepository
                    .findById(user.getContactInformation().getUserContactInfoId()).get();
            contactInfo.setAddress(userUpdates.getAddress());
            contactInfo.setEmail(userUpdates.getEmail());
            contactInfo.setPhoneNumber(userUpdates.getPhoneNumber());

            user.setContactInformation(contactInfo);

            UserCredentials userCredentials = this.credentialsRepository.findByUserInfoUserIdLike(id).get();
            userCredentials.setUsername(userUpdates.getEmail());
            userCredentials.setDisabled(userUpdates.getDisabled());
            userCredentials.setLocked(userUpdates.getLocked());
            userCredentials.setPassword(passwordEncoder.encode(userUpdates.getPassword()));
            if(!userUpdates.getRole().equals("")){
                userCredentials.setRole(userUpdates.getRole());
            } else {
                userCredentials.setRole("USER");
            }

            this.credentialsRepository.save(userCredentials);
            this.userRepository.save(user);

            return ("Se acuaizó el perfil con exito.");
        } else {
            throw new EntityNotFoundException("No se encotró el perfil de usuario correspondiente al e-mail: " +
                    userUpdates.getEmail());
        }

    }

    @Transactional
    public String delete(Long id) {
        Optional<User> userToDelete = this.userRepository.findById(id);
        if (userToDelete.isPresent()) {
            UserCredentials userCredentials = this.credentialsRepository.findByUserInfoUserIdLike(id).get();
            this.credentialsRepository.deleteById(userCredentials.getUserCredentialId());
            this.userRepository.deleteById(id);
            return ("Eliminado con éxito.");
        } else {
            throw new EntityNotFoundException("No se encotró el perfil de usuario correspondiente");
        }
    }
}

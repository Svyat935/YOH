package com.yoh.backend.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yoh.backend.entity.Game;
import com.yoh.backend.entity.Organization;
import com.yoh.backend.entity.User;
import com.yoh.backend.repository.OrganizationRepository;
import com.yoh.backend.repository.UserRepository;
import com.yoh.backend.validators.UserValidator;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final Algorithm algorithm = Algorithm.HMAC256("SECRET_LETTER");

    public void createUser(User user) throws IllegalArgumentException{
        new UserValidator().validate(user);
        checksExistUser(user);

        String password = user.getPassword();
        String hashString = BCrypt.withDefaults().hashToString(13, password.toCharArray());
        user.setPassword(hashString);
        userRepository.createUser(user);
    }

    public void saveUser(User user) throws IllegalArgumentException{
        userRepository.createUser(user);
    }

    //Не применяется
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public List<User> getAllUsersByAdmin(Integer role, String regex, String order) {
        return userRepository.getAllUsersByAdmin(role, order, regex);
    }

    public List<User> getAllUsersByAdminPaginated(Integer role, String regex, String order, int start, int count) {
        return userRepository.getAllUsersByAdminPaginated(role, order, regex, start, count);
    }

    public int getAllUsersByAdminCount(Integer role, String regex) {
        return userRepository.getAllUsersByAdminCount(role, regex);
    }

    public void updateUser(User user) throws IllegalArgumentException{
        userRepository.createUser(user);
    }

    public void deleteUser(User user) throws IllegalArgumentException{
        userRepository.deleteUser(user);
    }

    public User getUser(String credentials, String password) throws IllegalArgumentException{
        User user = userRepository.getUserByCredential(credentials);
        if (user != null) {
            boolean status = BCrypt.verifyer()
                    .verify(password.toCharArray(), user.getPassword())
                    .verified;
            if (status) {
                return user;
            }
        }
        throw new IllegalArgumentException(
                String.format("Sorry, but User with this credentials (%s, %s) wasn't found.",
                        credentials, password)
        );
    }

//    public Integer getRoleById(UUID id){
//        return userRepository.getRoleByUUID(id);
//    }

    public User getUserById(UUID id) throws IllegalArgumentException{
        User user = userRepository.getUserByUUID(id);
        if (user != null) {
            return user;
        }
        else throw new IllegalArgumentException(
                String.format("Sorry, but User with this id (%s) wasn't found.", id)
        );
    }

    private void checksExistUser(User user) throws IllegalArgumentException{
        User user_copy = userRepository.getUserByEmail(user.getEmail());
        if (user_copy != null){
            throw new IllegalArgumentException("Sorry, but User with this email was found. Check your account.");
        }
        user_copy = userRepository.getUserByLogin(user.getLogin());
        if (user_copy != null){
            throw new IllegalArgumentException("Sorry, but User with this login was found. Make up new login.");
        }
    }

    public String generateToken(UUID userId){
        LocalDateTime expireLocalDateTime = LocalDateTime.now().plusDays(7);
        Date expireDate = Timestamp.valueOf(expireLocalDateTime);

        return JWT.create()
                .withClaim("user", userId.toString())
                .withExpiresAt(expireDate)
                .sign(algorithm);
    }

    public UUID verifyToken(String token){
        DecodedJWT jwt;
        try{
            JWTVerifier verifier = JWT.require(algorithm).build();
            jwt = verifier.verify(token);
        }catch (JWTVerificationException e){
            // FIXME Change exception message.
            throw new IllegalArgumentException("Problem with token");
        }
        jwt = JWT.decode(token);
        String userId = jwt.getClaim("user").asString();
        return UUID.fromString(userId);
    }
}

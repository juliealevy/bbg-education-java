package com.play.java.bbgeducation.application.users;

import com.play.java.bbgeducation.application.common.exceptions.validation.EmailExistsValidationFailed;
import com.play.java.bbgeducation.application.common.exceptions.validation.NameExistsValidationFailed;
import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.domain.UserEntity;
import com.play.java.bbgeducation.infrastructure.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public OneOf2<UserResult, ValidationFailed> createUser(String firstName, String lastName, String email, String password) {

        if (userRepository.existsByEmail(email)){
            return OneOf2.fromOption2(new EmailExistsValidationFailed());
        }

        UserEntity saved = userRepository.save(UserEntity.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .build()
        );
        return OneOf2.fromOption1(UserResult.builder()
                        .id(saved.getId())
                        .firstName(saved.getFirstName())
                        .lastName(saved.getLastName())
                        .email(saved.getEmail())
                        .build());
    }

    @Override
    public OneOf3<Success, NotFound, ValidationFailed> updateUser(Long id, String firstName, String lastName, String email, String password) {

        Optional<UserEntity> found = userRepository.findById(id);
        if (found.isEmpty()){
            return OneOf3.fromOption2(new NotFound());
        }

        if (!found.get().getEmail().equals(email) && userRepository.existsByEmail(email)) {
            return OneOf3.fromOption3(new EmailExistsValidationFailed());
        }

        userRepository.save(UserEntity.builder()
                .id(id)
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .password(password)
                .build());

        return OneOf3.fromOption1(new Success());
    }

    @Override
    public OneOf2<Success, NotFound> deleteUser(Long id) {
        return null;
    }

    @Override
    public List<UserResult> getAll() {
        return null;
    }

    @Override
    public OneOf2<UserResult, NotFound> getById(Long id) {

        Optional<UserEntity> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()){
            return OneOf2.fromOption2(new NotFound());
        }
        UserResult result = UserResult.builder()
                .id(foundUser.get().getId())
                .firstName(foundUser.get().getFirstName())
                .lastName(foundUser.get().getLastName())
                .email(foundUser.get().getEmail())
                .build();
        return OneOf2.fromOption1(result);

    }

    @Override
    public OneOf2<UserResult, NotFound> getByEmail(String email) {
        return null;
    }

    @Override
    public boolean emailExists(String email) {
        return false;
    }
}

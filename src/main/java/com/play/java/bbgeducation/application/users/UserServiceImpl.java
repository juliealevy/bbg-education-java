package com.play.java.bbgeducation.application.users;

import com.play.java.bbgeducation.application.common.validation.EmailExistsValidationFailed;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.infrastructure.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final Mapper<UserEntity, UserResult> userMapper;

    public UserServiceImpl(UserRepository userRepository, Mapper<UserEntity, UserResult> userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public OneOf3<Success, NotFound, ValidationFailed> updateUser(Long id, String firstName, String lastName, String email, String password) {

        Optional<UserEntity> found = userRepository.findById(id);
        if (found.isEmpty()) {
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
                .isAdmin(found.get().getIsAdmin())   //no updating this via this call
                .build());

        return OneOf3.fromOption1(new Success());
    }

    @Override
    public OneOf2<Success, NotFound> deleteUser(Long id) {
        Optional<UserEntity> find = userRepository.findById(id);
        if (find.isEmpty()){
            return OneOf2.fromOption2(new NotFound());
        }
        userRepository.deleteById(id);
        return OneOf2.fromOption1(new Success());
    }

    @Override
    public List<UserResult> getAll() {
        Iterable<UserEntity> userEntities = userRepository.findAll();
        return StreamSupport.stream(userEntities.spliterator(), false)
                .map(userMapper::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public OneOf2<UserResult, NotFound> getById(Long id) {

        Optional<UserEntity> foundUser = userRepository.findById(id);

        return foundUser.<OneOf2<UserResult, NotFound>>map(
                userEntity -> OneOf2.fromOption1(userMapper.mapTo(userEntity)))
                .orElseGet(() -> OneOf2.fromOption2(new NotFound()));
    }
}

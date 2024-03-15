package com.play.java.bbgeducation.application.users;

import com.play.java.bbgeducation.application.common.validation.EmailExistsValidationFailed;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.domain.valueobjects.emailaddress.EmailAddress;
import com.play.java.bbgeducation.domain.valueobjects.firstname.FirstName;
import com.play.java.bbgeducation.domain.valueobjects.lastname.LastName;
import com.play.java.bbgeducation.infrastructure.repositories.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final Mapper<UserEntity, UserResult> userMapper;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, Mapper<UserEntity, UserResult> userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public OneOf3<Success, NotFound, ValidationFailed> updateUser(Long id, FirstName firstName, LastName lastName, EmailAddress email) {

        Optional<UserEntity> found = userRepository.findById(id);
        if (found.isEmpty()) {
            return OneOf3.fromOption2(new NotFound());
        }

        if (!found.get().getEmail().equals(email) && userRepository.existsByEmail(email)) {
            return OneOf3.fromOption3(new EmailExistsValidationFailed());
        }

        try {
            UserEntity userEntity =UserEntity.build(
                    id, firstName, lastName, email, "", found.get().getIsAdmin());
            userRepository.save(userEntity);
        }catch(TransactionSystemException cvex){
            logger.error("Error updating user", cvex);
            Throwable mostSpecificCause = cvex.getMostSpecificCause();
            return OneOf3.fromOption3(ValidationFailed.Conflict("", mostSpecificCause.getMessage()));
        }
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

    @Override
    public OneOf2<UserResult, NotFound> getByEmail(EmailAddress emailAddress) {
        Optional<UserEntity> foundUser = userRepository.findByEmail(emailAddress);
        return foundUser.<OneOf2<UserResult, NotFound>>map(
                        userEntity -> OneOf2.fromOption1(userMapper.mapTo(userEntity)))
                .orElseGet(() -> OneOf2.fromOption2(new NotFound()));
    }
}

package com.play.java.bbgeducation.unit.users;

import com.play.java.bbgeducation.api.auth.RegisterRequest;
import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.users.UserResult;
import com.play.java.bbgeducation.application.users.UserResultMapper;
import com.play.java.bbgeducation.application.users.UserService;
import com.play.java.bbgeducation.application.users.UserServiceImpl;
import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTests {
    private UserService underTest;
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final Mapper<UserEntity, UserResult> userMapper = new UserResultMapper();

    @BeforeEach
    public void init(){
        underTest = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    public void createUser_ShouldReturnUser_WhenInputValid(){
       RegisterRequest userToCreate = getUserRequest();

       when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
       when(userRepository.save(any(UserEntity.class))).then(returnsFirstArg());

        OneOf2<UserResult, ValidationFailed> result = underTest.createUser(userToCreate.getFirstName(), userToCreate.getLastName(),
                userToCreate.getEmail(), userToCreate.getPassword());

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();
        assertThat(result.asOption1().getEmail()).isEqualTo(userToCreate.getEmail());
        assertThat(result.asOption1().getFirstName()).isEqualTo(userToCreate.getFirstName());
        assertThat(result.asOption1().getLastName()).isEqualTo(userToCreate.getLastName());
    }

    @Test
    public void createUser_ShouldReturnFailure_WhenEmailExists(){
        RegisterRequest userToCreate = getUserRequest();

        when(userRepository.existsByEmail(any(String.class))).thenReturn(true);

        OneOf2<UserResult, ValidationFailed> result = underTest.createUser(userToCreate.getFirstName(), userToCreate.getLastName(),
                userToCreate.getEmail(), userToCreate.getPassword());

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();
    }

    private static RegisterRequest getUserRequest() {
        return RegisterRequest.builder()
                .firstName("Mary")
                .lastName("Poppins")
                .email("mary@fly.com")
                .password("123456")
                .build();
    }
}

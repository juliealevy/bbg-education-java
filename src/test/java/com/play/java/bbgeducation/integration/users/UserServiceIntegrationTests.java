package com.play.java.bbgeducation.integration.users;

import com.play.java.bbgeducation.api.users.UpdateUserRequest;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.EmailExistsValidationFailed;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.users.UserResult;
import com.play.java.bbgeducation.application.users.UserService;
import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.play.java.bbgeducation.integration.users.DataUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceIntegrationTests {
    private final UserService underTest;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceIntegrationTests(UserService userService, UserRepository userRepository) {
        this.underTest = userService;
        this.userRepository = userRepository;
    }

    @Test
    public void UpdateUser_ShouldSucceed_WhenInputValid(){
        UserEntity userEntity1 = buildUserEntity1();
        UserEntity saved = userRepository.save(userEntity1);

        saved.setLastName(saved.getLastName() + " updated");

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.updateUser(saved.getId(), saved.getFirstName(), saved.getLastName(),
                saved.getEmail(), saved.getPassword());

        OneOf2<UserResult, NotFound> fetched = underTest.getById(saved.getId());

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();
        assertThat(fetched).isNotNull();
        assertThat(fetched.hasOption1()).isTrue();
        assertThat(fetched.asOption1().getLastName()).isEqualTo(saved.getLastName());
    }

    @Test
    public void UpdateUser_ShouldReturnNotFound_WhenIdInvalid() {
        UpdateUserRequest userRequest = buildUpdateUserRequest();

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.updateUser(100L, userRequest.getFirstName(), userRequest.getLastName(),
                userRequest.getEmail(), userRequest.getPassword());

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();
    }

    @Test
    public void UpdateUser_ShouldFail_WhenEmailExists() {
        UserEntity user1 = buildUserEntity1();
        userRepository.save(user1);
        UserEntity user2 = buildUserEntity2();
        userRepository.save(user2);

        user1.setEmail(user2.getEmail());

        OneOf3<Success, NotFound, ValidationFailed> updated = underTest.updateUser(user1.getId(), user1.getFirstName(), user1.getLastName(),
                user1.getEmail(), user1.getPassword());

        assertThat(updated).isNotNull();
        assertThat(updated.hasOption3()).isTrue();
        assertThat(updated.asOption3().getClass()).isEqualTo(EmailExistsValidationFailed.class);
    }

    @Test
    public void GetAll_CanReturnZero() {
        List<UserResult> all = underTest.getAll();

        assertThat(all).hasSize(0);
    }

    @Test
    public void GetAll_ReturnsList() {
        UserEntity user1 = buildUserEntity1();
        userRepository.save(user1);
        UserEntity user2 = buildUserEntity2();
        userRepository.save(user2);

        List<UserResult> all = underTest.getAll();
        assertThat(all).hasSize(2);
    }

    @Test
    public void GetById_ReturnsUser_WhenIdExists(){
        UserEntity user1 = buildUserEntity1();
        UserEntity created = userRepository.save(user1);

        OneOf2<UserResult, NotFound> fetch = underTest.getById(created.getId());

        assertThat(fetch).isNotNull();
        assertThat(fetch.hasOption1()).isTrue();
        assertThat(fetch.asOption1().getEmail()).isEqualTo(user1.getEmail());
    }

    @Test
    public void GetById_ReturnsNotFound_WhenIdNotExists(){
        OneOf2<UserResult, NotFound> fetch = underTest.getById(100L);

        assertThat(fetch).isNotNull();
        assertThat(fetch.hasOption2()).isTrue();
    }

    @Test
    public void Delete_ReturnsNoContent_WhenIdExists(){
        UserEntity user1 = buildUserEntity1();
        UserEntity created = userRepository.save(user1);

        OneOf2<Success, NotFound> deleted = underTest.deleteUser(created.getId());

        assertThat(deleted).isNotNull();
        assertThat(deleted.hasOption1()).isTrue();

    }

    @Test
    public void Delete_ReturnsNotFound_WhenIdNotExists(){

        OneOf2<Success, NotFound> deleted = underTest.deleteUser(100L);

        assertThat(deleted).isNotNull();
        assertThat(deleted.hasOption2()).isTrue();

    }

}

package com.play.java.bbgeducation.integration.users;

import com.play.java.bbgeducation.api.users.UserRequest;
import com.play.java.bbgeducation.application.common.exceptions.validation.EmailExistsValidationFailed;
import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.users.UserResult;
import com.play.java.bbgeducation.application.users.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.play.java.bbgeducation.integration.users.DataUtils.buildUserRequest1;
import static com.play.java.bbgeducation.integration.users.DataUtils.buildUserRequest2;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceIntegrationTests {
    private final UserService underTest;

    @Autowired
    public UserServiceIntegrationTests(UserService userService) {
        this.underTest = userService;
    }

    @Test
    public void UserCreate_ShouldSucceedandFetch_WhenInputValid(){
        UserRequest userRequest = buildUserRequest1();

        OneOf2<UserResult, ValidationFailed> created = underTest.createUser(userRequest.getFirstName(), userRequest.getLastName(),
                userRequest.getEmail(), userRequest.getPassword());

        assertThat(created).isNotNull();
        assertThat(created.hasOption1()).isTrue();
        assertThat(created.asOption1().getFirstName()).isEqualTo(userRequest.getFirstName());
        assertThat(created.asOption1().getLastName()).isEqualTo(userRequest.getLastName());
        assertThat(created.asOption1().getEmail()).isEqualTo(userRequest.getEmail());
    }

    @Test
    public void CreateUser_ShouldFailValidation_WhenEmailExists(){
        UserRequest userRequest = buildUserRequest1();

        OneOf2<UserResult, ValidationFailed> created1 = underTest.createUser(userRequest.getFirstName(), userRequest.getLastName(),
                userRequest.getEmail(), userRequest.getPassword());
        OneOf2<UserResult, ValidationFailed> created2 = underTest.createUser(userRequest.getFirstName(), userRequest.getLastName(),
                userRequest.getEmail(), userRequest.getPassword());

        assertThat(created1).isNotNull();
        assertThat(created1.hasOption1()).isTrue();
        assertThat(created2).isNotNull();
        assertThat(created2.hasOption2()).isTrue();
        assertThat(created2.asOption2().getClass()).isEqualTo(EmailExistsValidationFailed.class);
    }

    @Test
    public void UpdateUser_ShouldSucceed_WhenInputValid(){
        UserRequest userRequest = buildUserRequest1();
        OneOf2<UserResult, ValidationFailed> created = underTest.createUser(userRequest.getFirstName(), userRequest.getLastName(),
                userRequest.getEmail(), userRequest.getPassword());

        UserRequest updatedRequest = UserRequest.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName() + " updated")
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.updateUser(created.asOption1().getId(), updatedRequest.getFirstName(), updatedRequest.getLastName(),
                updatedRequest.getEmail(), updatedRequest.getPassword());

        OneOf2<UserResult, NotFound> fetched = underTest.getById(created.asOption1().getId());

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();
        assertThat(fetched).isNotNull();
        assertThat(fetched.hasOption1()).isTrue();
        assertThat(fetched.asOption1().getLastName()).isEqualTo(updatedRequest.getLastName());
    }

    @Test
    public void UpdateUser_ShouldReturnNotFound_WhenIdInvalid() {
        UserRequest userRequest = buildUserRequest1();

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.updateUser(100L, userRequest.getFirstName(), userRequest.getLastName(),
                userRequest.getEmail(), userRequest.getPassword());

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();
    }

    @Test
    public void UpdateUser_ShouldFail_WhenEmailExists() {
        UserRequest userRequest = buildUserRequest1();
        OneOf2<UserResult, ValidationFailed> created1 = underTest.createUser(userRequest.getFirstName(), userRequest.getLastName(),
                userRequest.getEmail(), userRequest.getPassword());
        UserRequest userRequest2 = buildUserRequest2();
        OneOf2<UserResult, ValidationFailed> created2 = underTest.createUser(userRequest2.getFirstName(), userRequest2.getLastName(),
                userRequest2.getEmail(), userRequest2.getPassword());

        userRequest.setEmail(userRequest2.getEmail());

        OneOf3<Success, NotFound, ValidationFailed> updated = underTest.updateUser(created1.asOption1().getId(), userRequest.getFirstName(), userRequest.getLastName(),
                userRequest.getEmail(), userRequest.getPassword());

        assertThat(updated).isNotNull();
        assertThat(updated.hasOption3()).isTrue();
        assertThat(updated.asOption3().getClass()).isEqualTo(EmailExistsValidationFailed.class);
    }
}

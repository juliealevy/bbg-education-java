package com.play.java.bbgeducation.integration.users;

import com.play.java.bbgeducation.api.users.CreateUserRequest;
import com.play.java.bbgeducation.api.users.UpdateUserRequest;
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

import java.util.List;

import static com.play.java.bbgeducation.integration.users.DataUtils.*;
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
        CreateUserRequest userRequest = buildUserRequest1();

        OneOf2<UserResult, ValidationFailed> created = createUser(userRequest);

        assertThat(created).isNotNull();
        assertThat(created.hasOption1()).isTrue();
        assertThat(created.asOption1().getFirstName()).isEqualTo(userRequest.getFirstName());
        assertThat(created.asOption1().getLastName()).isEqualTo(userRequest.getLastName());
        assertThat(created.asOption1().getEmail()).isEqualTo(userRequest.getEmail());
    }

    @Test
    public void CreateUser_ShouldFailValidation_WhenEmailExists(){
        CreateUserRequest userRequest = buildUserRequest1();

        OneOf2<UserResult, ValidationFailed> created1 = createUser(userRequest);
        OneOf2<UserResult, ValidationFailed> created2 = createUser(userRequest);

        assertThat(created1).isNotNull();
        assertThat(created1.hasOption1()).isTrue();
        assertThat(created2).isNotNull();
        assertThat(created2.hasOption2()).isTrue();
        assertThat(created2.asOption2().getClass()).isEqualTo(EmailExistsValidationFailed.class);
    }

    @Test
    public void UpdateUser_ShouldSucceed_WhenInputValid(){
        CreateUserRequest userRequest = buildUserRequest1();
        OneOf2<UserResult, ValidationFailed> created = createUser(userRequest);

        UpdateUserRequest updatedRequest = UpdateUserRequest.builder()
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
        UpdateUserRequest userRequest = buildUpdateUserRequest();

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.updateUser(100L, userRequest.getFirstName(), userRequest.getLastName(),
                userRequest.getEmail(), userRequest.getPassword());

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();
    }

    @Test
    public void UpdateUser_ShouldFail_WhenEmailExists() {
        CreateUserRequest userRequest = buildUserRequest1();
        OneOf2<UserResult, ValidationFailed> created1 = createUser(userRequest);;
        CreateUserRequest userRequest2 = buildUserRequest2();
        OneOf2<UserResult, ValidationFailed> created2 = createUser(userRequest2);

        userRequest.setEmail(userRequest2.getEmail());

        OneOf3<Success, NotFound, ValidationFailed> updated = underTest.updateUser(created1.asOption1().getId(), userRequest.getFirstName(), userRequest.getLastName(),
                userRequest.getEmail(), userRequest.getPassword());

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
        CreateUserRequest userRequest = buildUserRequest1();
        OneOf2<UserResult, ValidationFailed> created1 = createUser(userRequest);
        CreateUserRequest userRequest2 = buildUserRequest2();
        OneOf2<UserResult, ValidationFailed> created2 = createUser(userRequest2);

        List<UserResult> all = underTest.getAll();
        assertThat(all).hasSize(2);
    }

    @Test
    public void GetById_ReturnsUser_WhenIdExists(){
        CreateUserRequest userRequest = buildUserRequest1();
        OneOf2<UserResult, ValidationFailed> created1 = createUser(userRequest);

        OneOf2<UserResult, NotFound> fetch = underTest.getById(created1.asOption1().getId());

        assertThat(fetch).isNotNull();
        assertThat(fetch.hasOption1()).isTrue();
        assertThat(fetch.asOption1().getEmail()).isEqualTo(userRequest.getEmail());
    }

    @Test
    public void GetById_ReturnsNotFound_WhenIdNotExists(){
        OneOf2<UserResult, NotFound> fetch = underTest.getById(100L);

        assertThat(fetch).isNotNull();
        assertThat(fetch.hasOption2()).isTrue();
    }

    @Test
    public void Delete_ReturnsNoContent_WhenIdExists(){
        CreateUserRequest userRequest = buildUserRequest1();
        OneOf2<UserResult, ValidationFailed> created1 = createUser(userRequest);

        OneOf2<Success, NotFound> deleted = underTest.deleteUser(created1.asOption1().getId());

        assertThat(deleted).isNotNull();
        assertThat(deleted.hasOption1()).isTrue();

    }

    @Test
    public void Delete_ReturnsNotFound_WhenIdNotExists(){

        OneOf2<Success, NotFound> deleted = underTest.deleteUser(100L);

        assertThat(deleted).isNotNull();
        assertThat(deleted.hasOption2()).isTrue();

    }

    private OneOf2<UserResult, ValidationFailed> createUser(CreateUserRequest userRequest) {
        return underTest.createUser(userRequest.getFirstName(), userRequest.getLastName(),
                userRequest.getEmail(), userRequest.getPassword(), userRequest.getIsAdmin());
    }
}

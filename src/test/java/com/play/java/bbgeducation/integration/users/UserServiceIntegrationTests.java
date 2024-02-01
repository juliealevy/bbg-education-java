package com.play.java.bbgeducation.integration.users;

import com.play.java.bbgeducation.api.users.UserRequest;
import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.users.UserResult;
import com.play.java.bbgeducation.application.users.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.play.java.bbgeducation.integration.users.DataUtils.buildUserRequest1;
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
    }
}

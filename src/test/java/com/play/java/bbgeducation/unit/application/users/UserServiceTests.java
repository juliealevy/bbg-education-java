package com.play.java.bbgeducation.unit.application.users;

import com.play.java.bbgeducation.api.users.UpdateUserRequest;
import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.EmailExistsValidationFailed;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.exceptions.InvalidEmailFormatException;
import com.play.java.bbgeducation.application.users.UserResult;
import com.play.java.bbgeducation.application.users.UserService;
import com.play.java.bbgeducation.application.users.UserServiceImpl;
import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.domain.valueobjects.emailaddress.EmailAddress;
import com.play.java.bbgeducation.domain.valueobjects.firstname.FirstName;
import com.play.java.bbgeducation.domain.valueobjects.lastname.LastName;
import com.play.java.bbgeducation.infrastructure.repositories.UserRepository;
import com.play.java.bbgeducation.integration.api.users.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTests {
    private UserService underTest;
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private UserEntity userEntity;

    @Autowired
    public UserServiceTests(Mapper<UserEntity, UserResult> userMapper) {
        this.underTest = new UserServiceImpl(userRepository, userMapper);
    }

    @BeforeEach
    void setUp() {
        userEntity = DataUtils.buildUserEntity(1);
    }

    @Test
    public void UpdateUser_ShouldSucceed_WhenSameEmail(){
        userEntity.setId(100L);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(userEntity));

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.updateUser(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getEmail());

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();
    }

    @Test
    public void UpdateUser_ShouldSucceed_WhenEmailChangeToUnusedEmail(){
        userEntity.setId(100L);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(userEntity));
        when(userRepository.existsByEmail(any(EmailAddress.class))).thenReturn(false);

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.updateUser(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(),
                EmailAddress.from("unused@blah.com"));

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();
    }

    @Test
    public void UpdateUser_ShouldReturnNotFound_WhenIdInvalid() {
        UpdateUserRequest userRequest = DataUtils.buildUpdateUserRequest();
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.updateUser(
                100L, FirstName.from(userRequest.getFirstName()), LastName.from(userRequest.getLastName()),
                EmailAddress.from(userRequest.getEmail()));

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();
    }

    @Test
    public void UpdateUser_ShouldFail_WhenEmailExists() {
        userEntity.setId(100L);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(userEntity));
        when(userRepository.existsByEmail(any(EmailAddress.class))).thenReturn(true);

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.updateUser(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(),
                EmailAddress.from("updated@blah.com"));

        assertThat(result).isNotNull();
        assertThat(result.hasOption3()).isTrue();
        assertThat(result.asOption3().getClass()).isEqualTo(EmailExistsValidationFailed.class);
    }

    @Test
    public void UpdateUser_ShouldThrow_WhenEmailInvalid() {
        userEntity.setId(100L);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(userEntity));
        when(userRepository.existsByEmail(any(EmailAddress.class))).thenReturn(false);

        assertThatThrownBy(() ->
                underTest.updateUser(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(),
                    EmailAddress.from("unused@blah.cominvalid")))
                .isInstanceOf(InvalidEmailFormatException.class);
    }

    @Test
    public void GetAll_CanReturnZero() {
        List<UserEntity> emptyList = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(emptyList);

        List<UserResult> all = underTest.getAll();

        assertThat(all).hasSize(0);
    }

    @Test
    public void GetAll_ReturnsList() {
        List<UserEntity> userList = new ArrayList<>();
        userList.add(userEntity);
        UserEntity user2 = DataUtils.buildUserEntity(2);
        userList.add(user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<UserResult> all = underTest.getAll();

        assertThat(all).hasSize(2);
    }

    @Test
    public void GetById_ReturnsUser_WhenIdExists(){
        userEntity.setId(100L);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(userEntity));

        OneOf2<UserResult, NotFound> fetch = underTest.getById(userEntity.getId());

        assertThat(fetch).isNotNull();
        assertThat(fetch.hasOption1()).isTrue();
        assertThat(fetch.asOption1().getEmail()).isEqualTo(userEntity.getEmail().toString());
    }

    @Test
    public void GetById_ReturnsNotFound_WhenIdNotExists(){
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        OneOf2<UserResult, NotFound> fetch = underTest.getById(100L);

        assertThat(fetch).isNotNull();
        assertThat(fetch.hasOption2()).isTrue();
    }

    @Test
    public void Delete_ReturnsNoContent_WhenIdExists(){
        userEntity.setId(100L);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(userEntity));
        doNothing().when(userRepository).deleteById(any(Long.class));

        OneOf2<Success, NotFound> deleted = underTest.deleteUser(userEntity.getId());

        assertThat(deleted).isNotNull();
        assertThat(deleted.hasOption1()).isTrue();

    }

    @Test
    public void Delete_ReturnsNotFound_WhenIdNotExists(){

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        OneOf2<Success, NotFound> deleted = underTest.deleteUser(100L);

        assertThat(deleted).isNotNull();
        assertThat(deleted.hasOption2()).isTrue();

    }

}

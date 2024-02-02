package com.play.java.bbgeducation.integration.users;

import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.play.java.bbgeducation.integration.users.DataUtils.buildUserEntity1;
import static com.play.java.bbgeducation.integration.users.DataUtils.buildUserEntity2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryIntegrationTests {

    private final UserRepository underTest;

    @Autowired
    public UserRepositoryIntegrationTests(UserRepository userRepository) {
        this.underTest = userRepository;
    }

    @Test
    public void CreateUser_ShouldSucceedAndFetch_WhenInputValid(){
        UserEntity user = buildUserEntity1();
        UserEntity saved = underTest.save(user);

        Optional<UserEntity> fetched = underTest.findById(saved.getId());

        assertThat(fetched).isPresent();
        assertThat(fetched.get()).isEqualTo(user);
    }

    @Test
    public void CreateUserMany_ShouldSucceedAndFetch_WhenInputValid(){
        UserEntity user1 = buildUserEntity1();
        UserEntity saved1 = underTest.save(user1);

        UserEntity user2 = buildUserEntity2();
        UserEntity saved2 = underTest.save(user2);

        Iterable<UserEntity> results = underTest.findAll();

        assertThat(results)
                .hasSize(2)
                .containsExactly(user1, user2);
    }

    @Test
    public void CreateUser_ShouldFail_WhenEmailExists(){
        UserEntity user1 = buildUserEntity1();
        UserEntity saved1 = underTest.save(user1);

        UserEntity user2 = buildUserEntity2();
        user2.setEmail(user1.getEmail());

        assertThatThrownBy(() -> underTest.save(user2))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    public void UpdateUser_ShouldSucceed_WhenInputValid() {
        UserEntity user1 = buildUserEntity1();
        UserEntity saved1 = underTest.save(user1);

        saved1.setLastName(saved1.getLastName() + " updated");
        underTest.save(saved1);

        Optional<UserEntity> fetched = underTest.findById(saved1.getId());

        assertThat(fetched).isPresent();
        assertThat(fetched.get().getId()).isEqualTo(saved1.getId());
        assertThat(fetched.get().getEmail()).isEqualTo(saved1.getEmail());
        assertThat(fetched.get().getFirstName()).isEqualTo(saved1.getFirstName());
        assertThat(fetched.get().getLastName()).isEqualTo(saved1.getLastName());

    }

    @Test
    public void UpdateUser_ShouldFail_WhenEmailExists() {
        UserEntity user1 = buildUserEntity1();
        UserEntity saved1 = underTest.save(user1);
        UserEntity user2 = buildUserEntity2();
        UserEntity saved2 = underTest.save(user2);

        saved2.setEmail(saved1.getEmail());

        assertThatThrownBy(() -> underTest.save(saved2))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    public void DeleteUser_ShouldSucceed_WhenIdExists(){
        UserEntity user1 = buildUserEntity1();
        UserEntity saved1 = underTest.save(user1);

        underTest.deleteById(saved1.getId());
        Optional<UserEntity> fetched = underTest.findById(saved1.getId());

        assertThat(fetched).isEmpty();

    }

    @Test
    public void DeleteUser_ShouldSucceed_WhenIdNotExists() {
        Long userId = 100L;
        Optional<UserEntity> fetched = underTest.findById(userId);

        underTest.deleteById(userId);

        assertThat(fetched).isEmpty();
    }

    @Test
    public void FindUserByEmail_ShouldReturnTrue_WhenEmailFound(){
        UserEntity user1 = buildUserEntity1();
        UserEntity saved1 = underTest.save(user1);
        UserEntity user2 = buildUserEntity2();
        UserEntity saved2 = underTest.save(user2);

        boolean found = underTest.existsByEmail(saved1.getEmail());

        assertThat(found).isTrue();
    }
    @Test
    public void FindUserByEmail_ShouldSucceed_WhenEmailExists(){
        UserEntity user1 = buildUserEntity1();
        UserEntity saved1 = underTest.save(user1);
        UserEntity user2 = buildUserEntity2();
        UserEntity saved2 = underTest.save(user2);

        Optional<UserEntity> fetched = underTest.findByEmail(saved1.getEmail());

        assertThat(fetched).isPresent();
        assertThat(fetched.get().getId()).isEqualTo(saved1.getId());
        assertThat(fetched.get().getEmail()).isEqualTo(saved1.getEmail());
    }

    @Test
    public void FindUserByEmail_ShouldReturnEmpty_WhenEmailNotExists(){
        UserEntity user1 = buildUserEntity1();
        UserEntity saved1 = underTest.save(user1);

        Optional<UserEntity> fetched = underTest.findByEmail(saved1.getEmail() + " wrong");

        assertThat(fetched).isEmpty();
    }


}

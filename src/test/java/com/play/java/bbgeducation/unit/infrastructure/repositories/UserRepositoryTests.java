package com.play.java.bbgeducation.unit.infrastructure.repositories;

import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.domain.valueobjects.emailaddress.EmailAddress;
import com.play.java.bbgeducation.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.play.java.bbgeducation.integration.api.users.DataUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryTests {

    private final UserRepository underTest;
    private UserEntity user1;

    @Autowired
    public UserRepositoryTests(UserRepository userRepository) {
        this.underTest = userRepository;
    }

    @BeforeEach
    void setUp() {
        user1 = buildUserEntity(1);
    }

    @Test
    public void FindUserByEmail_ShouldReturnTrue_WhenEmailFound(){
        UserEntity user1 = buildUserEntity(1);
        UserEntity saved1 = underTest.save(user1);
        UserEntity user2 = buildUserEntity(2);
        UserEntity saved2 = underTest.save(user2);

        boolean found = underTest.existsByEmail(saved1.getEmail());

        assertThat(found).isTrue();
    }
    @Test
    public void FindUserByEmail_ShouldSucceed_WhenEmailExists(){
        UserEntity saved1 = underTest.save(user1);
        UserEntity user2 = buildUserEntity(2);
        UserEntity saved2 = underTest.save(user2);

        Optional<UserEntity> fetched = underTest.findByEmail(saved1.getEmail());

        assertThat(fetched).isPresent();
        assertThat(fetched.get().getId()).isEqualTo(saved1.getId());
        assertThat(fetched.get().getEmail()).isEqualTo(saved1.getEmail());
    }

    @Test
    public void FindUserByEmail_ShouldReturnEmpty_WhenEmailNotExists(){
        UserEntity saved1 = underTest.save(user1);

        Optional<UserEntity> fetched = underTest.findByEmail(EmailAddress.from("wrong" + saved1.getEmail()));

        assertThat(fetched).isEmpty();
    }

    @Test
    public void ExistsByEmail_ShouldReturnTrue_WhenEmailExists(){
        UserEntity saved1 = underTest.save(user1);

        boolean result = underTest.existsByEmail(saved1.getEmail());

        assertThat(result).isTrue();
    }

    @Test
    public void ExistsByEmail_ShouldReturnFalse_WhenEmailNotExists(){
        UserEntity saved1 = underTest.save(user1);

        boolean result = underTest.existsByEmail(EmailAddress.from("wrong" + saved1.getEmail()));

        assertThat(result).isFalse();
    }


}

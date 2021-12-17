package maciej.sojka.GenericPM2.repos;

import maciej.sojka.GenericPM2.entities.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface ULRepo extends JpaRepository<UserLogin, Long> {

    @Query("SELECT MAX(log_date) FROM UserLogin WHERE valid = TRUE")
    Date getLastSuccess();

    @Query("SELECT MAX(log_date) FROM UserLogin WHERE valid = FALSE")
    Date getLastFail();

    @Query("SELECT COUNT(log_date) FROM UserLogin WHERE valid = FALSE AND login = ?1")
    Long getNumberOfFails(String login);

}

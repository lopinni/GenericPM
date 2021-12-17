package maciej.sojka.GenericPM2.repos;

import maciej.sojka.GenericPM2.entities.IPLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IPLRepo extends JpaRepository<IPLogin, Long> {

    @Query("SELECT COUNT(log_date) FROM IPLogin WHERE valid = FALSE AND ip = ?1")
    Long getNumberOfFails(String IP);

}

package maciej.sojka.GenericPM2.repos;

import maciej.sojka.GenericPM2.entities.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PassRepo extends JpaRepository<Password, Long> {

    @Query("SELECT p FROM Password p WHERE p.id_user = ?1")
    List<Password> findAllByUserId(Long uid);

}

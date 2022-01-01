package maciej.sojka.GenericPM2.repos;

import maciej.sojka.GenericPM2.entities.SharedPass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SPRepo extends JpaRepository<SharedPass, Long> {

    @Query("SELECT id_password FROM SharedPass WHERE benefactor = ?1")
    List<Long> findIdsByBenefactor(String b_login);

}

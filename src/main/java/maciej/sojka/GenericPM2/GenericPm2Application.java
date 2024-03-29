package maciej.sojka.GenericPM2;

import maciej.sojka.GenericPM2.repos.PassRepo;
import maciej.sojka.GenericPM2.entities.Password;
import maciej.sojka.GenericPM2.entities.User;
import maciej.sojka.GenericPM2.repos.UserRepo;
import maciej.sojka.GenericPM2.misc.CryptoFunc;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class GenericPm2Application {

	@Autowired
	private UserRepo repo;

	@Autowired
	private PassRepo repo2;

	public static void main(String[] args) {
		SpringApplication.run(GenericPm2Application.class, args);
	}

	@PostConstruct
	public void init() throws Exception {
		String salt1 = RandomString.make(64);
		String salt2 = RandomString.make(64);
		repo.save(new User(
				"lopinni",
				CryptoFunc.encrypt(CryptoFunc.calculateSHA512("lopinni" + salt1),
									CryptoFunc.generateKey(CryptoFunc.PEPPER)),
				salt1,
				"aes"
		));
		repo2.save(new Password(1L, "1", "1", "1"));
		repo.save(new User(
				"user1",
				CryptoFunc.encrypt(CryptoFunc.calculateSHA512("user1" + salt2),
									CryptoFunc.generateKey(CryptoFunc.PEPPER)),
				salt2,
				"aes"
		));
		repo2.save(new Password(2L, "2", "2", "2"));
	}

}

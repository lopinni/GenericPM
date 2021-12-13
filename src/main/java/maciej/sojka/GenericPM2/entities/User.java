package maciej.sojka.GenericPM2.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String login;

    @Column
    private String acc_pass;

    @Column
    private String salt;

    @Column
    private String method;

    public User(String l, String ap, String s, String m){
        this.login = l;
        this.acc_pass = ap;
        this.salt = s;
        this.method = m;
    }

}

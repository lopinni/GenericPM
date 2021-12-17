package maciej.sojka.GenericPM2.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@Entity
@Table(name = "attemptsUser")
@Getter
@Setter
@NoArgsConstructor
public class UserLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String login;

    @Column
    private Date log_date;

    @Column
    private boolean valid;

    public UserLogin(String l, boolean v){
        this.login = l;
        this.log_date = new Date();
        this.valid = v;
    }

}

package maciej.sojka.GenericPM2.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@Entity
@Table(name = "attemptsIp")
@Getter
@Setter
@NoArgsConstructor
public class IPLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String ip;

    @Column
    private Date log_date;

    @Column
    private boolean valid;

    public IPLogin(String ip, boolean v){
        this.ip = ip;
        this.log_date = new Date();
        this.valid = v;
    }

}

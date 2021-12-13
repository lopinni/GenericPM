package maciej.sojka.GenericPM2.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@Entity
@Table(name = "passwords")
@Getter
@Setter
@NoArgsConstructor
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long id_user;

    @Column
    private String site;

    @Column
    private String site_login;

    @Column
    private String site_pass;

    public Password(Long idu, String s, String sl, String sp) {
        this.id_user = idu;
        this.site = s;
        this.site_login = sl;
        this.site_pass = sp;
    }

    public Password(Long idu) { this.id_user = idu; }

}

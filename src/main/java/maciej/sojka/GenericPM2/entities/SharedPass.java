package maciej.sojka.GenericPM2.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@Entity
@Table(name = "sharedPasswords")
@Getter
@Setter
@NoArgsConstructor
public class SharedPass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long id_owner;

    @Column
    private Long id_password;

    @Column
    private String benefactor;

}

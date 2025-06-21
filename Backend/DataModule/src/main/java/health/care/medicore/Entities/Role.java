package health.care.medicore.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Table(name = "ROLE")
@Entity
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ROLE_ID")
    private long roleId;

    @Column(name = "ROLE")
    private String role;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<Users> users;
}

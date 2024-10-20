package TreeProject.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table
public class Tree {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "lat")
    private String lat;

    @Column(name = "lng")
    private String lng;
    @Column(name = "treetype")
    private String treeType;
    @Column(name = "treename")
    private String treeName;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
}

package sk.tuke.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Setter
@Getter
public class Station implements Serializable {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "station")
    private List<Score> scores;

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station() {

    }
}

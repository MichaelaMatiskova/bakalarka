package sk.tuke.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Competitor implements Serializable {

    @Id
    private Long id;

    private String name;

    private String surname;

    private int age;

    @OneToMany(mappedBy = "competitor")
    @JsonIgnore
    private List<Score> scores;

    public Competitor() {

    }

    public Competitor(Long id, String name, String surname, int age) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
    }
}
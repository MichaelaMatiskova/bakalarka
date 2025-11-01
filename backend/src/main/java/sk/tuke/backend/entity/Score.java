package sk.tuke.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(
        name = "score",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_competitor", "id_station"})
)
public class Score implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_competitor")
    @JsonIgnoreProperties({"scores"})
    private Competitor competitor;

    @ManyToOne
    @JoinColumn(name = "id_station")
    @JsonIgnoreProperties({"scores"})
    private Station station;

    private int points;

    public Score(Station station, Competitor competitor, int points) {
        this.station = station;
        this.competitor = competitor;
        this.points = points;
    }

    public Score() {
    }
}

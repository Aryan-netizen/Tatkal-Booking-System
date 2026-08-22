package     com.example.Tatkal.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Trains")
@Getter
@Setter
public class Train {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    @Column(nullable = false, columnDefinition = "longtext")
    private String name;

    @OneToMany(mappedBy = "trainNumber")
    private Set<TrainStop> trainNumberTrainStops = new HashSet<>();

    @OneToMany(mappedBy = "trainNumber")
    private Set<Trip> trainNumberTrips = new HashSet<>();

}

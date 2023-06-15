package src.akorovai.climbingalgorithm.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class City {
    private int idCity;
    private Map<Integer, Integer> distancesToNeighbors;

    public City(int idCity) {
        this.idCity = idCity;
        this.distancesToNeighbors = new HashMap<>();
    }

    public int getIdCity() {
        return idCity;
    }



    public void addNeighbor(int neighborId, int distance) {
        distancesToNeighbors.put(neighborId, distance);
    }

    public int getDistanceToNeighbor(int neighborId) {
        Integer distance = distancesToNeighbors.get(neighborId);
        if (distance == null) {
            throw new IllegalArgumentException("Neighbor not found: " + neighborId);
        }
        return distance;
    }
}
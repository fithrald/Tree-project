package TreeProject.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TreeDTO {

    private double price;
    private double lat;
    private double lng;
    private String region;
    private String treeType;
    private String treeName;

    public TreeDTO(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}

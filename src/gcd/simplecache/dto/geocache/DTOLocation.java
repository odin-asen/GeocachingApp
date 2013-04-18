package gcd.simplecache.dto.geocache;

/**
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 17.04.13
 */
public class DTOLocation {
  public Double latitude;
  public Double longitude;

  public DTOLocation(Double latitude, Double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  @Override
  public String toString() {
    return "DTOLocation{" +
        "latitude=" + latitude +
        ", longitude=" + longitude +
        '}';
  }
}

package gcd.simplecache.dto.geocache;

import java.io.Serializable;

/**
 * This data transfer object represents a global location.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 17.04.13
 */
public class DTOLocation implements Serializable {
  public Double latitude;
  public Double longitude;

  public DTOLocation(Double latitude, Double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  @Override
  public String toString() {
    return "{ " + latitude + ", " + longitude + " }";
  }
}

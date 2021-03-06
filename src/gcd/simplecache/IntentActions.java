package gcd.simplecache;

/**
 * Interface for intent action IDs.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 22.04.13
 */
public interface IntentActions {
  /* GPS listener constants */
  String ACTION_ID_GPS = "LocationChanged";

  /* sensor listener constants */
  String ACTION_ID_COMPASS = "SensorChanged";

  /* Navigation mode constants */
  String ACTION_ID_NAVIGATION = "SetNavigationMode";
  /** boolean */
  String NAVIGATION_ENABLED = "enabled";
  /** {@link gcd.simplecache.dto.geocache.DTOGeocache} */
  String NAVIGATION_DESTINATION = "destination";

  /* Show geocache description constants */
  String ACTION_ID_DETAILS = "ShowDetails";
  /** {@link gcd.simplecache.business.geocaching.Geocache#getId()} */
  String DETAILS_ID = "id";
}

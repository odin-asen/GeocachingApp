package gcd.simplecache;

import android.app.Dialog;
import android.os.Bundle;

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
  String ACTION_ID_DESCRIPTION = "ShowDescription";
  /** {@link gcd.simplecache.business.geocaching.Geocache#getId()} */
  String DESCRIPTION_ID = "id";
  /** {@link gcd.simplecache.business.geocaching.Geocache#getName()} */
  String DESCRIPTION_NAME = "name";
  /** {@link gcd.simplecache.business.geocaching.Geocache#getHint()} */
  String DESCRIPTION_HINT = "hint";
  /** {@link gcd.simplecache.business.geocaching.Geocache#getOwner()} */
  String DESCRIPTION_OWNER = "owner";
  /** {@link gcd.simplecache.business.geocaching.Geocache#getDescription()} */
  String DESCRIPTION_TEXT = "description";
}

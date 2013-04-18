package gcd.simplecache.data;

import gcd.simplecache.dto.geocache.DTOGeocache;

import java.util.IllegalFormatException;

/**
 * A GPXReader object should be able to interpret a gpx formatted geocache file.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 18.04.13
 */
public interface GPXReader {
  /**
   * Reads a gpx formatted string containing the geocache data.
   * @param gpxString Gpx formatted string to read.
   * @return A {@link DTOGeocache} object that represents the geocache.
   * @throws IllegalFormatException If a problem with the syntax of the file
   * occurred.
   */
  public DTOGeocache readGPX(String gpxString) throws IllegalFormatException;
}

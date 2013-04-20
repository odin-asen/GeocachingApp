package gcd.simplecache.data;

import gcd.simplecache.dto.geocache.DTOGeocache;

import java.util.List;

/**
 * A JSONReader object should be able to interpret a json formatted geocache file.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public interface JSONReader {
  /**
   * Reads a json formatted string containing the geocache data. Returns
   * null if a problem with the syntax of the file occurred.
   * @param jsonString Json formatted string to read.
   * @return A {@link DTOGeocache} object that represents the geocache.
   */
  public DTOGeocache readJSON(String jsonString);
  /**
   * Reads a json formatted string containing many geocaches. Returns
   * null if a problem with the syntax of the file occurred.
   * @param jsonString Json formatted string to read.
   * @return A {@link DTOGeocache} object that represents the geocache.
   */
  public List<DTOGeocache> readManyJSON(String jsonString);
}

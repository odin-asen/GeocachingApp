package gcd.simplecache.data;

import gcd.simplecache.dto.geocache.DTOGeocache;

import java.util.IllegalFormatException;

/**
 * A JSONReader object should be able to interpret a json formatted geocache file.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public interface JSONReader {
  /**
   * Reads a json formatted string containing the geocache data.
   * @param jsonString Json formatted string to read.
   * @return A {@link DTOGeocache} object that represents the geocache.
   * @throws IllegalFormatException If a problem with the syntax of the file
   * occurred.
   */
  public DTOGeocache readJSON(String jsonString) throws IllegalFormatException;
}

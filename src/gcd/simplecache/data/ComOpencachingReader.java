package gcd.simplecache.data;

import gcd.simplecache.dto.geocache.DTOGeocache;

import java.util.IllegalFormatException;

/**
 * An object of this file can read files that are created from
 * opencaching.com.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 18.04.13
 */
public class ComOpencachingReader implements JSONReader, GPXReader {
  /* Constructors */
  /* Methods */
  /* Getter and Setter */

  @Override
  public DTOGeocache readGPX(String gpxString) throws IllegalFormatException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public DTOGeocache readJSON(String jsonString) throws IllegalFormatException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}

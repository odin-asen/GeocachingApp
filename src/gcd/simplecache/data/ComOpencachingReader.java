package gcd.simplecache.data;

import android.util.Log;
import gcd.simplecache.dto.geocache.DTOCacheOwner;
import gcd.simplecache.dto.geocache.DTOGeocache;
import gcd.simplecache.dto.geocache.DTOLocation;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.IllegalFormatException;
import java.util.List;

/**
 * An object of this file can read files that are created from
 * opencaching.com.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 18.04.13
 */
public class ComOpencachingReader implements JSONReader, GPXReader {
  /* json attribute keys */
  private static final String ATTR_DESCRIPTION = "description";
  private static final String ATTR_DIFFICULTY = "difficulty";
  private static final String ATTR_HINT = "hint";
  private static final String ATTR_CACHE_ID= "oxcode";
  private static final String OBJECT_LOCATION = "location";
  private static final String ATTR_LATITUDE = "lat";
  private static final String ATTR_LONGITUDE = "lon";
  private static final String ATTR_NAME = "name";
  private static final String OBJECT_OWNER = "hidden_by";
  private static final String ATTR_OWNER_ID = "id";
  private static final String ATTR_SIZE = "size";
  private static final String ATTR_TERRAIN = "terrain";
  private static final String ATTR_TYPE = "type";

  /* Constructors */
  /* Methods */
  @Override
  public DTOGeocache readGPX(String gpxString) throws IllegalFormatException {
    DTOGeocache cache = new DTOGeocache();
    return cache;
  }

  @Override
  public DTOGeocache readJSON(String jsonString) throws IllegalFormatException {
    final DTOGeocache dtoCache = new DTOGeocache();

    try {
      JSONObject jsonCache = new JSONObject(jsonString);
      fillDTOCache(dtoCache, jsonCache);
    } catch (Exception e) {
      Log.e(ComOpencachingReader.class.getName(), "Could not read json object");
    }

    return dtoCache;
  }

  @Override
  public List<DTOGeocache> readManyJSON(String jsonString) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  /* fill a DTOGeocache object with data from a json file */
  private void fillDTOCache(DTOGeocache dtoCache, JSONObject jsonCache) throws JSONException {
    dtoCache.description = jsonCache.getString(ATTR_DESCRIPTION);
    dtoCache.difficulty = (float) jsonCache.getDouble(ATTR_DIFFICULTY);
    dtoCache.hint = jsonCache.getString(ATTR_HINT);
    dtoCache.id = jsonCache.getString(ATTR_CACHE_ID);

    JSONObject dummy = jsonCache.getJSONObject(OBJECT_LOCATION);
    dtoCache.location = new DTOLocation(dummy.getDouble(ATTR_LATITUDE),
        dummy.getDouble(ATTR_LONGITUDE));

    dtoCache.name = jsonCache.getString(ATTR_NAME);

    dummy = jsonCache.getJSONObject(OBJECT_OWNER);
    dtoCache.owner = new DTOCacheOwner(dummy.getString(ATTR_OWNER_ID),
        dummy.getString(ATTR_NAME));

    dtoCache.size = (float) jsonCache.getDouble(ATTR_SIZE);
    dtoCache.terrain = (float) jsonCache.getDouble(ATTR_TERRAIN);
    dtoCache.type = jsonCache.getString(ATTR_TYPE);
  }

  /* Getter and Setter */
}

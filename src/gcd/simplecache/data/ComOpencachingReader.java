package gcd.simplecache.data;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import gcd.simplecache.dto.geocache.DTOGeocache;
import gcd.simplecache.dto.geocache.DTOLocation;

import java.util.IllegalFormatException;

/**
 * An object of this file can read files that are created from
 * opencaching.com.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 18.04.13
 */
public class ComOpencachingReader implements JSONReader, GPXReader {

  private static final String STRING_GEOCACHE = "geocache";

  /* Constructors */
  /* Methods */
  @Override
  public DTOGeocache readGPX(String gpxString) throws IllegalFormatException {
    DTOGeocache cache = new DTOGeocache();
    return cache;
  }

  @Override
  public DTOGeocache readJSON(String jsonString) throws IllegalFormatException {
    XStream xStream = initJSONXStream();
    if(!hasGeocacheTag(jsonString))
      jsonString = "{"+ STRING_GEOCACHE +":"+jsonString+"}";
    return (DTOGeocache) xStream.fromXML(jsonString);
  }

  /* Test for a geocache json tag at the beginning of the string */
  private boolean hasGeocacheTag(String string) {
    /* Validates the start beginning with a geocache tag */
    String geocacheTagRegex = "\\A(\\s*\\{\\s*geocache:)\\S*";
    return string.matches(geocacheTagRegex);
  }

  private XStream initJSONXStream() {
    XStream xStream = new IgnoringXStream(new JettisonMappedXmlDriver());
    xStream.alias(STRING_GEOCACHE, DTOGeocache.class);
    xStream.alias("location", DTOLocation.class);
    xStream.aliasField("oxcode", DTOGeocache.class, "id");
    xStream.aliasField("hidden_by", DTOGeocache.class, "owner");
    xStream.aliasField("lat", DTOLocation.class, "latitude");
    xStream.aliasField("lon", DTOLocation.class, "longitude");
    xStream.setMode(XStream.NO_REFERENCES);
    return xStream;
  }

  private XStream initGPXXStream() {
    XStream xStream = new IgnoringXStream();
    return xStream;
  }

  /* Getter and Setter */

  /* Inner classes */
  /**
   * Create an XStream object that accepts unknown attributes
   * that are not specified in the data model.
   */
  private class IgnoringXStream extends XStream {
    public IgnoringXStream() {
      super();
    }

    private IgnoringXStream(HierarchicalStreamDriver hierarchicalStreamDriver) {
      super(hierarchicalStreamDriver);
    }

    protected MapperWrapper wrapMapper(MapperWrapper next) {
      return new MapperWrapper(next) {
        @Override
        public boolean shouldSerializeMember(Class definedIn,
                                             String fieldName) {
          if (definedIn == Object.class) {
            return false;
          }
          return super.shouldSerializeMember(definedIn, fieldName);
        }
      };
    }
  }
}

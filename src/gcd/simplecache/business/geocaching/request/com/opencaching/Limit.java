package gcd.simplecache.business.geocaching.request.com.opencaching;

/**
 * limit=limit

 Sets the maximum number of geocaches that will be returned. Can be between 1 and 5000. Defaults to 100 if no limit is specified.
 hard_limit=true/false

 hard_limit = true:
 If more geocaches fit the criteria than what can be returned before reaching the the limit, then an error (HTTP error 413) is returned instead of any geocaches.

 hard_limit = false or not specified:
 If more geocaches fit the criteria than what can be returned before reaching the limit, then the closest geocaches to the center of the query are returned.

 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 16.04.13
 */
public class Limit {
  /* Constructors */
  /* Methods */
  /* Getter and Setter */
}

package net.umask.jmx2statsd.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.umask.jmx2statsd.ObjectNameFilter;

/**
 * User: JoGeraerts
 * Date: 28/03/2014
 * Time: 10:31
 */
public class EhCacheFilter extends DelegatingFilter {
    private final static List<ObjectNameFilter> FILTERS = new ArrayList<ObjectNameFilter>();

    static {
        FILTERS.add(new SimpleDomainFilter("net.sf.ehcache"));
        FILTERS.add(new KeyPropertyValuesFilter("type", Collections.singleton("CacheStatistics")));
    }

    public EhCacheFilter() {
        super(new AndFilter(FILTERS));
    }


}

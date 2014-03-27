package net.umask.jmx2statsd.filters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.umask.jmx2statsd.ObjectNameFilter;

/**
 * User: JoGeraerts
 * Date: 1/07/13
 * Time: 21:46
 */
public class CamelFilter extends DelegatingFilter {
    private static List<ObjectNameFilter> FILTERS = new ArrayList<ObjectNameFilter>();

    static {
        Set<String> values = new HashSet<String>();
        values.add("routes");
        values.add("context");
        FILTERS.add(new SimpleDomainFilter("org.apache.camel"));
        FILTERS.add(new KeyPropertyValuesFilter("type", values));
    }

    public CamelFilter() {
        super(new AndFilter(FILTERS));
    }
}

package net.umask.jmx2statsd.filters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.umask.jmx2statsd.ObjectNameFilter;

/**
 * User: JoGeraerts
 * Date: 1/07/13
 * Time: 21:25
 */
public class CatalinaFilter extends DelegatingFilter {
    private static List<ObjectNameFilter> FILTERS = new ArrayList<ObjectNameFilter>();

    static {
        Set<String> values = new HashSet<String>();
        values.add("GlobalRequestProcessor");
        values.add("Executor");
        values.add("Manager");
        FILTERS.add(new SimpleDomainFilter("Catalina"));
        FILTERS.add(new KeyPropertyValuesFilter("type", values));
    }

    public CatalinaFilter() {
        super(new AndFilter(FILTERS));

    }
}

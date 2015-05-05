package net.umask.jmx2statsd.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.umask.jmx2statsd.ObjectNameFilter;

/**
 * User: JoGeraerts
 * Date: 1/07/13
 * Time: 21:46
 */
public class ActiveMQFilter extends DelegatingFilter {
    private static List<ObjectNameFilter> FILTERS = new ArrayList<ObjectNameFilter>();

    static {
        Set<String> values = new HashSet<String>();
        values.add("Topic");
        values.add("Queue");
        FILTERS.add(new SimpleDomainFilter("org.apache.activemq"));
        FILTERS.add(
                new OrFilter(
                        Arrays.asList(
                                new KeyPropertyValuesFilter("Type", values),
                                new KeyPropertyValuesFilter("destinationType", values))
                )
        );
        FILTERS.add(
                new NotFilter(
                        new KeyPropertyValuesFilter("endpoint", Collections.singleton("Consumer"))
                )
        );
    }

    public ActiveMQFilter() {
        super(new AndFilter(FILTERS));
    }
}

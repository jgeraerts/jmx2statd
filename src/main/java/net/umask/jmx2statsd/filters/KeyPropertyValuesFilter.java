package net.umask.jmx2statsd.filters;

import javax.management.ObjectName;
import java.util.Set;

import net.umask.jmx2statsd.ObjectNameFilter;

/**
 * User: JoGeraerts
 * Date: 1/07/13
 * Time: 21:45
 */
public class KeyPropertyValuesFilter implements ObjectNameFilter {
    private final String key;
    private final Set<String> values;

    public KeyPropertyValuesFilter(String key, Set<String> values) {

        this.key = key;
        this.values = values;
    }

    @Override
    public boolean accept(ObjectName objectName) {
        return values.contains(objectName.getKeyPropertyList().get(key));
    }
}

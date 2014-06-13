package net.umask.jmx2statsd.filters;

import javax.management.ObjectName;

import net.umask.jmx2statsd.ObjectNameFilter;

/**
 * User: JoGeraerts
 * Date: 13/06/2014
 * Time: 12:09
 */
public class OrFilter implements ObjectNameFilter {

    private final Iterable<? extends ObjectNameFilter> filters;

    public OrFilter(Iterable<? extends ObjectNameFilter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean accept(ObjectName objectName) {
        for (ObjectNameFilter filter : filters) {
            if (filter.accept(objectName)) {
                return true;
            }
        }
        return false;
    }
}

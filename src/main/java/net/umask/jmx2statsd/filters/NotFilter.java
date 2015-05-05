package net.umask.jmx2statsd.filters;

import javax.management.ObjectName;

import net.umask.jmx2statsd.ObjectNameFilter;

/**
 * User: JoGeraerts
 * Date: 5/05/2015
 * Time: 10:51
 */
public final class NotFilter implements ObjectNameFilter {

    private final ObjectNameFilter filter;

    public NotFilter(ObjectNameFilter filter) {
        this.filter = filter;
    }

    @Override
    public boolean accept(ObjectName objectName) {
        return !filter.accept(objectName);
    }
}

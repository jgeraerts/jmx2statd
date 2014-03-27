package net.umask.jmx2statsd.filters;

import javax.management.ObjectName;

import net.umask.jmx2statsd.ObjectNameFilter;

/**
 * User: JoGeraerts
 * Date: 1/07/13
 * Time: 21:37
 */
public class DelegatingFilter implements ObjectNameFilter {
    private final ObjectNameFilter delegate;

    public DelegatingFilter(ObjectNameFilter delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean accept(ObjectName objectName) {
        return delegate.accept(objectName);
    }
}

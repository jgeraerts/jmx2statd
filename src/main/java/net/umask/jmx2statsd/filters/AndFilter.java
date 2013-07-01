package net.umask.jmx2statsd.filters;

import javax.management.ObjectName;

import net.umask.jmx2statsd.ObjectNameFilter;

/**
 * User: JoGeraerts
 * Date: 1/07/13
 * Time: 21:38
 */
public class AndFilter implements ObjectNameFilter {
    private final Iterable<? extends ObjectNameFilter> filters;

    public AndFilter(Iterable<? extends ObjectNameFilter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean accept(ObjectName objectName) {
        for(ObjectNameFilter filter: filters){
            final boolean result = filter.accept(objectName);
            if(!result){
                return false;
            }
        }
        return true;
    }
}

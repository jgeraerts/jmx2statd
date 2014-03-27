package net.umask.jmx2statsd.filters;

import javax.management.ObjectName;

import net.umask.jmx2statsd.ObjectNameFilter;

/**
 * User: JoGeraerts
 * Date: 1/07/13
 * Time: 21:33
 */
public class SimpleDomainFilter implements ObjectNameFilter {
    private final String domainName;

    public SimpleDomainFilter(String domainName) {
        this.domainName = domainName;
    }

    @Override
    public boolean accept(ObjectName objectName) {
        return this.domainName.equals(objectName.getDomain());
    }
}

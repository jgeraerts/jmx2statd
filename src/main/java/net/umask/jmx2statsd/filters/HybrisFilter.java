package net.umask.jmx2statsd.filters;

import javax.management.ObjectName;

import net.umask.jmx2statsd.ObjectNameFilter;

/**
 * User: JoGeraerts
 * Date: 1/07/13
 * Time: 21:24
 */
public class HybrisFilter extends SimpleDomainFilter {
    public HybrisFilter() {
        super("hybris");
    }
}

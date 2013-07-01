package net.umask.jmx2statsd.filters;

import javax.management.ObjectName;

import net.umask.jmx2statsd.ObjectNameFilter;


public class JavaLangFilter extends SimpleDomainFilter{
    public JavaLangFilter(){
        super("java.lang");
    }
}

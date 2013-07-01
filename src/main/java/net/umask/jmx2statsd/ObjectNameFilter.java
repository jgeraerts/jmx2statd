package net.umask.jmx2statsd;

import javax.management.ObjectName;

/**
 * User: JoGeraerts
 * Date: 1/07/13
 * Time: 18:04
 */
public interface ObjectNameFilter  {

    boolean accept(ObjectName objectName);

}

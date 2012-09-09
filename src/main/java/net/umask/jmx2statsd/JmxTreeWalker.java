/*
 * Copyright (c) 2012, Jo Geraerts
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.umask.jmx2statsd;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class JmxTreeWalker {
    private MBeanServerConnection mbeanServer;
    private List<MetricListener> listeners = new ArrayList<MetricListener>();

    public JmxTreeWalker(MBeanServerConnection platformMBeanServer) {

        this.mbeanServer = platformMBeanServer;
    }

    public void walk() {
        try {
            emit(walkObjectNames(mbeanServer.queryNames(null, null)));

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }


    private List<Metric> walkObjectNames(Set<ObjectName> objectNames) throws InstanceNotFoundException, IOException, ReflectionException, IntrospectionException, AttributeNotFoundException, MBeanException {
        List<Metric> metrics = new LinkedList<Metric>();
        for (ObjectName n : objectNames) {
            walkAttributes(n, mbeanServer.getMBeanInfo(n).getAttributes(), metrics);
        }
        return metrics;
    }

    private void walkAttributes(ObjectName n, MBeanAttributeInfo[] attributes, List<Metric> metrics) {
        for (MBeanAttributeInfo a : attributes) {
            try {
                Object value = mbeanServer.getAttribute(n, a.getName());
                if (value instanceof Number) {
                    metrics.add(new Metric(n, a.getName(), null, (Number) value));
                } else if (value instanceof CompositeData) {
                    writeCompositeData(n, a, (CompositeData) value, metrics);
                }
            } catch (Exception e) {
                //e.printStackTrace(System.err);
            }
        }
    }

    private void writeCompositeData(ObjectName n, MBeanAttributeInfo a, CompositeData value, List<Metric> metrics) {
        Set<String> keys = value.getCompositeType().keySet();
        for (String key : keys) {
            Object o = value.get(key);
            if (o instanceof Number) {
                metrics.add(new Metric(n, a.getName(), key, (Number) o));
            }
        }
    }

    private void emit(List<Metric> metrics) {
        for (MetricListener listener : listeners) {
            listener.emit(metrics);
        }
    }


    public void addMetricListener(MetricListener metricListener) {
        listeners.add(metricListener);
    }
}

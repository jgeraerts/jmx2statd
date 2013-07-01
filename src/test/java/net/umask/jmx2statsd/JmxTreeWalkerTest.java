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

import org.junit.Test;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Collections;

import net.umask.jmx2statsd.filters.JavaLangFilter;

/**
 * @author Jo Geraerts
 */
public class JmxTreeWalkerTest {


    @Test
    public void testWalkTree() throws InterruptedException {
        JmxTreeWalker jmxTreeWalker = new JmxTreeWalker(ManagementFactory.getPlatformMBeanServer());
        long start = System.currentTimeMillis();
        jmxTreeWalker.addMetricListener(new ConsoleMetricListener());
        jmxTreeWalker.walk();
        long stop = System.currentTimeMillis();
        System.out.println(stop - start);

    }

    @Test
    public void testWalkTreeStatsd() throws InterruptedException, InvalidConfigurationException, IOException {
        JmxTreeWalker jmxTreeWalker = new JmxTreeWalker(ManagementFactory.getPlatformMBeanServer());
        jmxTreeWalker.setObjectNameFilters(Collections.<ObjectNameFilter>singletonList(new JavaLangFilter()));
        long start = System.currentTimeMillis();
        jmxTreeWalker.addMetricListener(new StatsdMetricListener(Config.loadFromProperties(this.getClass().getClassLoader().getResourceAsStream("jmx2statsd.properties"))));
        jmxTreeWalker.walk();
        long stop = System.currentTimeMillis();
        System.out.println(stop - start);

    }


}

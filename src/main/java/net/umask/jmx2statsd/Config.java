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

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class Config {
    final private InetAddress address;
    final private int port;
    private String applicationName;
    private List<ObjectNameFilter> filters;
    private long interval = 10000;

    public Config(InetAddress address, int port, String applicationName, List<ObjectNameFilter> filters) {
        this.address = address;
        this.port = port;
        this.applicationName = applicationName;
        this.filters = filters;
    }

    public InetAddress getHost() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public List<ObjectNameFilter> getFilters() {
        return filters;
    }

    public String getApplicationName() {
        return this.applicationName;
    }


    public static Config loadFromProperties(InputStream resourceAsStream) throws IOException, InvalidConfigurationException {
        final Properties props = new Properties();
        props.load(resourceAsStream);
        ConfigBuilder builder = new ConfigBuilder();
        builder.setHost(InetAddress.getByName(props.getProperty("statsd.host")));
        builder.setPort(Integer.parseInt(props.getProperty("statsd.port")));
        builder.setApplicationName(props.getProperty("applicationName"));

        for(int i = 0; ; i++){
            String clazzName=props.getProperty("filter."+i);
            if(clazzName == null){
                break;
            }
            try {
                Class<?> clazz = Class.forName(clazzName);
                ObjectNameFilter onf = (ObjectNameFilter) clazz.newInstance();
                builder.addFilter(onf);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return builder.build();
    }

    public long getInterval() {
        return interval;
    }


    private static class ConfigBuilder {

        private InetAddress address;
        private int port;
        private String applicationName;
        private List<ObjectNameFilter> filters = new ArrayList<ObjectNameFilter>();

        public Config build() throws InvalidConfigurationException {
            if (address == null) {
                throw new InvalidConfigurationException("host not set");
            }
            if (port == 0) {
                throw new InvalidConfigurationException("port not set");
            }
            if (applicationName == null){
                throw new InvalidConfigurationException("applicationName not set");
            }

            return new Config(address, port,applicationName,filters);

        }

        public void setHost(InetAddress address) {

            this.address = address;
        }

        public void setPort(int port) {

            this.port = port;
        }

        public void setApplicationName(String applicationName) {
            this.applicationName=applicationName;
        }

        public void addFilter(ObjectNameFilter onf) {
            this.filters.add(onf);
        }
    }
}

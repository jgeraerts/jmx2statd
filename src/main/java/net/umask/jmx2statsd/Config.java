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

import java.net.InetAddress;
import java.net.UnknownHostException;


public class Config {
    final private InetAddress address;
    final private int port;
    private String applicationName;
    private long interval = 10000;

    public Config(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public InetAddress getHost() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public static Config parseArgs(String args) throws InvalidConfigurationException {
        ConfigBuilder builder = new ConfigBuilder();
        String[] parts = args.split(";");
        for (String part : parts) {
            parsePart(part, builder);
        }
        return builder.build();
    }

    private static void parsePart(String part, ConfigBuilder builder) throws InvalidConfigurationException {
        if (isKeyValue(part)) {
            String key = getKey(part);
            String value = getValue(part);
            parseKeyValue(key, value, builder);
        }
    }

    private static void parseKeyValue(String key, String value, ConfigBuilder builder) throws InvalidConfigurationException {
        if ("host".equals(key)) {
            parseHost(value, builder);
        } else if ("port".equals(key)) {
            parsePort(value, builder);
        } else {
            throw new InvalidConfigurationException("unknown key " + key);
        }
    }

    private static void parsePort(String value, ConfigBuilder builder) throws InvalidConfigurationException {
        try {
            int port = Integer.parseInt(value);
            if (port > 65535) {
                throw new InvalidConfigurationException("port number too high");
            }
            builder.setPort(port);
        } catch (NumberFormatException e) {
            throw new InvalidConfigurationException("cannot parse integer " + value, e);
        }
    }

    private static void parseHost(String value, ConfigBuilder builder) throws InvalidConfigurationException {
        try {
            InetAddress address = InetAddress.getByName(value);
            builder.setHost(address);
        } catch (UnknownHostException e) {
            throw new InvalidConfigurationException("Host not known", e);
        }
    }

    private static String getValue(String part) {
        return part.substring(part.indexOf('=') + 1);
    }

    private static String getKey(String part) {
        return part.substring(0, part.indexOf('='));
    }

    private static boolean isKeyValue(String part) {
        if (part == null) {
            return false;
        }
        int idx = part.indexOf('=');
        return idx > 0 && part.length() > idx + 1;
    }

    public String getApplicationName() {
        return this.applicationName;
    }

    public long getInterval() {
        return interval;
    }


    private static class ConfigBuilder {

        private InetAddress address;
        private int port;

        public Config build() throws InvalidConfigurationException {
            if (address == null) {
                throw new InvalidConfigurationException("host not set");
            }
            if (port == 0) {
                throw new InvalidConfigurationException("port not set");
            }
            Config config = new Config(address, port);
            try {
                config.applicationName = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException ignored) {

            }
            return config;

        }

        public void setHost(InetAddress address) {

            this.address = address;
        }

        public void setPort(int port) {

            this.port = port;
        }
    }
}

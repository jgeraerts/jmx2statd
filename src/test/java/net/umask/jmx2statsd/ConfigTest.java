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

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: JoGeraerts
 * Date: 9/09/12
 * Time: 11:07
 */
public class ConfigTest {

    @Test
    public void parseArgsTestValid() throws InvalidConfigurationException {
        Config c = Config.parseArgs("host=127.0.0.1;port=8125");
        assertEquals("127.0.0.1", c.getHost().getHostAddress());
        assertEquals(8125, c.getPort());
    }

    @Test(expected = InvalidConfigurationException.class)
    public void parseArgsNoPort() throws InvalidConfigurationException {
        Config.parseArgs("host=127.0.0.1");
    }

    @Test(expected = InvalidConfigurationException.class)
    public void parseArgsNoHost() throws InvalidConfigurationException {
        Config.parseArgs("port=8125");
    }

    @Test(expected = InvalidConfigurationException.class)
    public void parseArgsInvalidHostAddress() throws InvalidConfigurationException {
        Config.parseArgs("host=1111.111.123.2,port=8125");
    }

    @Test(expected = InvalidConfigurationException.class)
    public void parseArgsInvalidHostNotFound() throws InvalidConfigurationException {
        Config.parseArgs("host=non.existing.host.local,port=8125");
    }


}

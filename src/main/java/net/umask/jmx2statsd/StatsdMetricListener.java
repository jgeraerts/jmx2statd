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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Locale;


public class StatsdMetricListener implements MetricListener {
    public static final int BUFFERSIZE = 1024;
    final StatdNamingStrategy statdNamingStrategy;
    final private InetSocketAddress _address;
    final private DatagramChannel _channel;

    public StatsdMetricListener(Config config) throws IOException {
        statdNamingStrategy = new DefaultStatdNamingStrategy(config.getApplicationName());
        _address = new InetSocketAddress(config.getHost(), config.getPort());
        _channel = DatagramChannel.open();
    }


    @Override
    public void emit(Iterable<Metric> metrics) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream(BUFFERSIZE);

            int bytesWritten = 0;
            for (Metric metric : metrics) {
                String statdLine = String.format(String.format(Locale.ENGLISH, "%s:%s|g",
                        statdNamingStrategy.name(metric.getObjectName(), metric.getAttributeName(), metric.getCompositeBeanKey()),
                        metric.getValue()));
                byte[] bytes = statdLine.getBytes();
                if (bytes.length > (BUFFERSIZE - bytesWritten + 1)) {
                    flush(os.toByteArray());
                    os.reset();
                    bytesWritten = 0;
                }
                if (bytesWritten > 0) {
                    os.write('\n');
                    bytesWritten++;
                }
                os.write(bytes);
                bytesWritten += bytes.length;
            }
            flush(os.toByteArray());
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void flush(byte[] bytes) {
        try {
            _channel.send(ByteBuffer.wrap(bytes), _address);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}

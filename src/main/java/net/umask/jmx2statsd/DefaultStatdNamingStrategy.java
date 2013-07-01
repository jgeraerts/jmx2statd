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

import javax.management.ObjectName;
import java.util.Hashtable;


public class DefaultStatdNamingStrategy implements StatdNamingStrategy {
    final private String applicationName;


    public DefaultStatdNamingStrategy(final String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public String name(ObjectName objectName, String attributeName, String compositeBeanKey) {
        return applicationName + "." + canonicalize(objectName) + "." + attributeName + (compositeBeanKey != null ? "." + compositeBeanKey : "");
    }

    private String canonicalize(ObjectName objectName) {
        final StringBuilder builder = new StringBuilder(1024);
        builder.append(objectName.getDomain());

        String keyPropertyList = objectName.getKeyPropertyListString();
        String[] keyPropertyParts= keyPropertyList.split("\\,");
        for(String part: keyPropertyParts){
            builder.append('.').append(part.substring(part.indexOf('=')+1));
        }
        return builder.toString()
                        .replaceAll(",", "_")
                        .replaceAll("=", "_")
                        .replaceAll("/", "_")
                        .replaceAll(":", "_")
                        .replaceAll("\"","")
                        .replaceAll(" ", "");
    }
}

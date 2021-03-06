/**
 * Copyright 2016 Yahoo Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yahoo.pulsar.broker.auth;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.Set;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

import org.testng.annotations.Test;

import com.google.common.collect.Sets;
import com.yahoo.pulsar.broker.ServiceConfiguration;
import com.yahoo.pulsar.broker.authentication.AuthenticationDataSource;
import com.yahoo.pulsar.broker.authentication.AuthenticationProvider;
import com.yahoo.pulsar.broker.authentication.AuthenticationService;

public class AuthenticationServiceTest {

    private static final String s_authentication_success = "authenticated";

    @Test
    public void testAuthentication() throws Exception {
        ServiceConfiguration config = new ServiceConfiguration();
        Set<String> providersClassNames = Sets.newHashSet(MockAuthenticationProvider.class.getName());
        config.setAuthenticationProviders(providersClassNames);
        config.setAuthenticationEnabled(true);
        AuthenticationService service = new AuthenticationService(config);
        String result = service.authenticate(null, "auth");
        assertEquals(result, s_authentication_success);
    }

    @Test
    public void testAuthenticationHttp() throws Exception {
        ServiceConfiguration config = new ServiceConfiguration();
        Set<String> providersClassNames = Sets.newHashSet(MockAuthenticationProvider.class.getName());
        config.setAuthenticationProviders(providersClassNames);
        config.setAuthenticationEnabled(true);
        AuthenticationService service = new AuthenticationService(config);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("192.168.1.1");
        when(request.getRemotePort()).thenReturn(8080);
        when(request.getHeader(anyString())).thenReturn("data");
        String result = service.authenticateHttpRequest(request);
        assertEquals(result, s_authentication_success);
    }

    public static class MockAuthenticationProvider implements AuthenticationProvider {

        @Override
        public void close() throws IOException {
        }

        @Override
        public void initialize(ServiceConfiguration config) throws IOException {
        }

        @Override
        public String getAuthMethodName() {
            return "auth";
        }

        @Override
        public String authenticate(AuthenticationDataSource authData) throws AuthenticationException {
            return s_authentication_success;
        }
    }
}

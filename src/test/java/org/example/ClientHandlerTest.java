package org.example;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ClientHandlerTest {
    @Test
    void handlesAbortedConnectionWithoutThrowingException() {
        Socket abortedSocket = new Socket() {
            @Override
            public java.net.SocketAddress getRemoteSocketAddress() {
                return new InetSocketAddress("localhost", 5000);
            }

            @Override
            public InputStream getInputStream() throws IOException {
                throw new IOException("simulated aborted connection");
            }
        };

        assertDoesNotThrow(() -> new ClientHandler(abortedSocket).run());
    }
}

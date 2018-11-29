package server;

import java.io.InputStream;
import java.io.OutputStream;

public interface ClientConnection {
	void close();

	Boolean isClosed();

	InputStream getInputStream();

	OutputStream getOutputStream();
}

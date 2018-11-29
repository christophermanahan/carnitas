package server;

import java.io.InputStream;
import java.io.OutputStream;

public interface ClientSocket {
	void close();

	Boolean isClosed();

	InputStream getInputStream();

	OutputStream getOutputStream();
}

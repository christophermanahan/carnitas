package server;

public interface IClient {
	Boolean connected();

	String receiveFrom();

	void sendTo(String data);
}

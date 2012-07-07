package ece454p1;

public interface MessageListener {
	public void onConnect();
	public void onMessageResponse(String response);
	public void onDisconnected();
}

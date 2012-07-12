package ece454p1;

import java.net.Socket;

public class ClientPeer extends Peer implements MessageListener{

	private ClientWorker worker;
	private Object lock;
	private String response;
	private boolean isConnected;
	private boolean isSending;
	
	public ClientPeer(String hostname, String port){
		this.hostname = hostname;
		this.port = port;
		this.lock = new Object();
		this.isConnected = false;
		this.isSending = false;

		// create a new thread
		worker = new ClientWorker(hostname, port);
		worker.setListener(this);
	}
	
	public boolean isSending() {
		return isSending;
	}
	
	public Socket getSocket() {
		return worker.getsocket();
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
	public String sendMessage(String message, boolean block) {
		synchronized (lock) {
			isSending = true;
			worker.sendMessage(message);
			try {
				if (!this.isConnected) return null;
				if (block) lock.wait();
			} catch (InterruptedException e) {
				System.out.println(e);
			}
			return response;
		}
	}
	
	public String sendFile(String fullRelativePath, String absolutePath, boolean block) {
		synchronized (lock) {
			isSending = true;
			worker.sendFile(fullRelativePath, absolutePath);
			try {
				if (block) lock.wait();
			} catch (InterruptedException e) {
				System.out.println(e);
			}
			return response;
		}
	}
	
	public void kill() {
		worker.kill();
	}
	
	@Override
	public int insert(String filename) {
		return 0;
	}

	@Override
	public int query(Status status) {
		
		return 0;
	}

	@Override
	public int join() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int leave() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onMessageResponse(String response) {
		synchronized (lock) {
			this.response = response;
			isSending = false;
			lock.notify();
		}
		// notify peer manager to do another scan
		PeerManager.getManager().poke();
	}

	@Override
	public void onConnect() {
		this.isConnected = true;
	}

	@Override
	public int insert(String filename, int id, String fullRelativePath) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onDisconnected() {
		this.currentState = State.disconnected;
		this.isConnected = false;
		// notify any one that is waiting
		synchronized (lock) {
			isSending = false;
			lock.notify();
		}
	}

}

package ece454p1;

import java.util.ArrayList;

public class ConcretePeers extends Peers {
	String withoutHost = null;
	String withoutPort = null;
	
	public class HostAndPort {
		public String hostname;
		public String port;
		
		public HostAndPort(String hostname, String port) {
			this.hostname = hostname;
			this.port = port;
		}
	}

	public int initialize(String withoutHost, String withoutPort) {
		this.withoutHost = withoutHost;
		this.withoutPort = withoutPort;
		
		String response = HttpRequest.postServer(Config.SERVER_PEER_STATUS, null);
		if (response == null) return ReturnCodes.ERR_OK;
		
		PeerListPostObject peerlist = (PeerListPostObject)FileUtils.mapToObject(response, PeerListPostObject.class);
		
		if (peerlist == null) return ReturnCodes.ERR_OK;
		
		for (PeerPostObject obj : peerlist.peerList) {
			if (!withoutHost.equals(obj.hostname) && !withoutPort.equals(obj.port)) {
				peers.add(new ClientPeer(obj.hostname, obj.port));
				numPeers++;
			}
		}
				
		return ReturnCodes.ERR_OK;
	}
	
	public void checkForNewPeers() {
		String response = HttpRequest.postServer(Config.SERVER_PEER_STATUS, null);
		if (response == null) return;
		
		PeerListPostObject peerlist = (PeerListPostObject)FileUtils.mapToObject(response, PeerListPostObject.class);
		if (peerlist == null) return;
		
		ArrayList<HostAndPort> toAdd = new ArrayList<HostAndPort>();
		for (PeerPostObject obj : peerlist.peerList) {
			if (!withoutHost.equals(obj.hostname) || !withoutPort.equals(obj.port)) {
				toAdd.add(new HostAndPort(obj.hostname, obj.port));
			}
		}
		if (toAdd.size() != 0) {
			// check to see if there is any new peers to be connected
			ClientPeer client = null;
			HostAndPort hostPort = null;
			for (int i=0; i<peers.size(); i++) {
				for (int j=toAdd.size()-1; j >= 0; j--) {
					client = (ClientPeer)peers.get(i);
					hostPort = toAdd.get(j);
					if (client.hostname.equals(hostPort.hostname) && client.port.equals(hostPort.port)) {
						toAdd.remove(j);
						break;
					}
				}
			}
			// update the new peers
			for (int i=0; i<toAdd.size(); i++) {
				peers.add(new ClientPeer(toAdd.get(i).hostname, toAdd.get(i).port));
				numPeers++;
			}
		}
	}

	@Override
	public Peer getPeer(int i) {
		if (peers == null || i < 0 || i > peers.size()) return null;
		return peers.get(i);
	}

	@Override
	public void removePeer(String hostname, String port) {
		for (int i=0; i<numPeers; i++) {
			if (hostname == peers.get(i).hostname && port == peers.get(i).port) {
				peers.remove(i);
				numPeers--;
				break;
			}
		}
	}

	@Override
	public int initialize(String peersFile) {
		// TODO Auto-generated method stub
		return 0;
	}

}

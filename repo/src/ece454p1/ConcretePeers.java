package ece454p1;
import java.io.*;


public class ConcretePeers extends Peers {

	public int initialize(String peersFile, String withoutHost, String withoutPort) {
		
		BufferedReader br = null; 
		try {
			br = new BufferedReader(new FileReader(peersFile));
			String server;
			while((server = br.readLine()) != null){
				String[] tokens = server.split(" ");
				if (!withoutHost.equals(tokens[0]) || !withoutPort.equals(tokens[1])) {
					peers.add(new ClientPeer(tokens[0], tokens[1]));
					numPeers++;
				}
			}
			return ReturnCodes.ERR_OK;
		} catch (FileNotFoundException e1) {
			System.out.println("Cannot Find PeersFile");
			return ReturnCodes.ERR_NO_PEERS_FOUND;
		} catch (IOException e) {
			System.out.println("cannot Read PeersFile");
			return ReturnCodes.ERR_NO_PEERS_FOUND;
		} finally {
			if (br != null){
				try {
					br.close();
				} catch (IOException e) {
					System.out.println("cannot Close PeersFile");
				}
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

package ece454p1;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;

import org.codehaus.jackson.map.ObjectMapper;

import ece454p1.Action.ActionType;

public class ServerPeer extends Peer{
	
	private ServerSocket socket;
	private ArrayList<ServerWorker> clients;
	private Thread t = null;
	private boolean isStopped;
	
	public ServerPeer(String hostname, String port){
		this.hostname = hostname;
		this.port = port;
		this.clients = new ArrayList<ServerWorker>();
		this.isStopped = false;
		
		//TODO: should check if hostname matches current host
		
		// start socket
		socket = null;
		try {
			socket = new ServerSocket(Integer.parseInt(port));
			System.out.println("Server Connected");
			System.out.println("Localport: "+socket.getLocalPort());
		} catch (Exception e) {
			System.out.println(e);
			try {
				if (socket != null) socket.close();
			}
			catch (Exception ex) {
				// do nothig
				System.out.println(ex);
			}
		}
	}
	
	private synchronized void startPeers() {
		// initialise all the peers
		ConcretePeers peers = new ConcretePeers();
		if(peers.initialize(this.hostname, this.port) != ReturnCodes.ERR_OK){
			return;
		}
		this.peers = peers;
	}
	
	private synchronized void killPeers() {
		ClientPeer peer = null;
		for (Peer temp : peers.peers) {
			peer = (ClientPeer)temp;
			peer.kill();
		}
		this.peers = null;
	}
	
	public synchronized ConcretePeers getPeers() {
		return (ConcretePeers) peers;
	}
	public void setPeers(Peers peers) {
		this.peers = peers;
	}

	public synchronized void removeWorker(Socket socket) {
		for (int i=0; i<clients.size(); i++) {
			if (clients.get(i).getSocket() == socket) {
				clients.remove(i);
			}
		}
	}
	
	public synchronized boolean checkStop() {
		return isStopped;
	}
	
	public synchronized void listen() {
		final ServerPeer peer = this;
		t = new Thread(new Runnable() {
			public void run() {
				// start accepting new requests on a new thread
				Socket remote = null;
				while (true) {
					try {
						remote = socket.accept();
						if (checkStop()) return;
						clients.add(new ServerWorker(remote, peer, peer.currentState == State.connected));
						System.out.println("New Client");
						System.out.println("RemotePort: "+remote.getPort());
						System.out.println("LocalPort: "+remote.getLocalPort());
					} catch (Exception e) {
						// do nothing
					}
				}

			}
		});
		t.start();
	}
	
	public synchronized boolean stop() {
		if (this.currentState != State.disconnected) {
			for (ServerWorker worker : clients) {
				worker.stop();
			}
			this.isStopped = true;
			this.currentState = State.disconnected;
			this.clients = null;
			killPeers();
			return true;
		} else {
			return false;
		}
	}
	
	public synchronized boolean start() {
		if (this.currentState != State.connected) {
			this.currentState = State.connected;
			
			// start server
			this.clients = new ArrayList<ServerWorker>();
			this.isStopped = false;
			if (t == null || !t.isAlive()) listen();
			
			// start peer
			startPeers();
			
			return true;
		} else {
			return false;
		}
	}
	
	public String onClientRequest(String request, File file) {
		ObjectMapper oMapper = new ObjectMapper();
		Action action;

		if (request != null) {
			try{
				action = oMapper.readValue(request, Action.class);
			}catch (Exception e){
				// regular message cannot be parsed to action class
				System.out.println("Incoming Message: "+request);
				return null;
			}
			
			// special message, parse it
			if (action.type == Action.ActionType.fileInfo){
				return retrieveFileInfo();
			} else if (action.type == Action.ActionType.deleteFile) {
				FileUtils.deleteTorrentAndFiles(action.filePath);
			}
		}
		else if (file != null) {
			PeerManager.getManager().poke();
			// if we got a file, return FileInfo
			return retrieveFileInfo();
		}
		return null;
	}
	
	public String retrieveFileInfo(){
		ObjectMapper oMapper = new ObjectMapper();
		ArrayList<FileInfo> returnList = new ArrayList<FileInfo>();
		String result = null;
		
		File folder = new File(Config.SHARE_PATH);
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles == null) return null;
		
		for(int i=0; i<listOfFiles.length; i++){
			if (FileUtils.isHeaderFile(listOfFiles[i])){
				HeaderFile headFile = FileUtils.readHeaderFile(listOfFiles[i]);
				FileInfo returnFile = new FileInfo();
				returnFile.headFile = headFile;
				returnList.add(returnFile);
			}
		}
		try {
			Files resultFile = new Files();
			resultFile.filesList = returnList;
			result = oMapper.writeValueAsString(resultFile);
		} catch (Exception e) {
			System.out.println("retrieve file info");
			System.out.println(e);
		}
		return result;
	}
	
	@Override
	public int insert(String filename, int id, String fullRelativePath) {
		if (fullRelativePath.startsWith("/")) {
			fullRelativePath = fullRelativePath.substring(1);
		}
		String absolutePath = Config.SHARE_PATH + fullRelativePath;
		File srcFile = new File(filename);
		File destFile = new File(absolutePath);
		destFile.getParentFile().mkdirs();
		if (destFile.exists()){
			System.out.println("This file name already exsists");
			return ReturnCodes.ERR_FILE_EXISTS;
		}else{
			try {
				InputStream in = new FileInputStream(srcFile);
				OutputStream out = new FileOutputStream(destFile);
				byte [] buff = new byte[1024];
				int length;
				while((length = in.read(buff)) > 0){
					out.write(buff, 0, length);
				}
				in.close();
				out.close();
			} catch (Exception e) {
				System.out.println(e);
				return -1;
			}
			
			//PeerManager.getManager().notifyNewFile(destFile);		
			ArrayList<File> files = FileUtils.divideFiles(destFile);
			FileUtils.generateHeaderFile(destFile, files, id);
			PeerManager.getManager().poke();
			return 0;
		}
	}
	
	public void broadCastAction(Object message) {
		broadCastMessage(FileUtils.mapToJSON(message));
	}
	
	public void broadCastMessage(String message) {
		if (this.peers != null) {
			for (int i=0; i<this.peers.numPeers; i++) {
				ClientPeer peer = ((ClientPeer)this.peers.getPeer(i));
				if (peer.isConnected()) {
					peer.sendMessage(message, false);	
				}
			}
		} else {
			System.out.println("Warning: you have to join first before you can send messages");
		}
	}

	@Override
	public int query(Status status) {
		((ConcreteStatus)status).clear();
		if (peers == null) {
			return ReturnCodes.ERR_PEER_NOT_FOUND;
		}
		
		ObjectMapper oMapper = new ObjectMapper();
		Action action = new Action();
		action.type = ActionType.fileInfo;
		String response;
		Files files;
		HashSet<Integer> fileSet = new HashSet<Integer>();

		for(int i=0; i<peers.numPeers; i++){
			ClientPeer peer = (ClientPeer)peers.getPeer(i);
			
			if (!((ClientPeer)peer).isConnected()) continue;
			
			try {
				response = ((ClientPeer) peer).sendMessage(oMapper.writeValueAsString(action), true);
				if (response == null || response.equals("null")) continue;
				
				files = oMapper.readValue(response, Files.class);
				
				ArrayList<HeaderFile> clientheaders = new ArrayList<HeaderFile>();
				for(int j = 0; j<files.filesList.size();j++){
					clientheaders.add(files.filesList.get(j).headFile.copy());
					
					int fileId = files.filesList.get(j).headFile.fileId;
					fileSet.add(fileId);
					status.globalHeader[fileId] = FileUtils.updateGlobalHeader(
							status.globalHeader[fileId], files.filesList.get(j).headFile, status);
				}
				status.headerfiles.put(peer.getSocket(), clientheaders);
			} catch (Exception e) {
				System.out.println("query mapper");
				System.out.println(e);
			}
		}
		
		//Handle local stats
		File file = new File(Config.SHARE_PATH);
		File [] fileList = file.listFiles();

		for (int i=0; i< fileList.length; i++){
			HeaderFile headFile = FileUtils.readHeaderFile(fileList[i]);	
			if(headFile != null){
				fileSet.add(headFile.fileId);
				status.globalHeader[headFile.fileId] = FileUtils.updateGlobalHeader(
						status.globalHeader[headFile.fileId], headFile, status);
				status.local[headFile.fileId] = FileUtils.chunkPercentage(headFile.localChunksPresent);
				
			}
		}
		
		for(int i=0; i<status.globalHeader.length;i++){
			if (status.globalHeader[i] != null){
				float result = FileUtils.chunkPercentage(status.globalHeader[i].localChunksPresent);
				status.system[i] = result;
			}
		}
		
		status.numFiles = fileSet.size();
		
		for (int i=0; i< status.chunkReplication.size(); i++){
			int numberOfChunks = status.chunkReplication.get(i).size();
			if (numberOfChunks<1){
				continue;
			}else{
				int chunkSum = status.chunkReplication.get(i).get(0);
				int minValue = status.chunkReplication.get(i).get(0);
				for(int j=1; j< status.chunkReplication.get(i).size(); j++){
					if(status.chunkReplication.get(i).get(j)< minValue){
						minValue = status.chunkReplication.get(i).get(j);
					}
					chunkSum = chunkSum + status.chunkReplication.get(i).get(j);
				}
				status.leastReplication[i] = minValue;
				status.weightedLeastReplication[i] = (float)chunkSum/(float)status.globalHeader[i].localChunksPresent.length;
			}
		}

		
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
	public int insert(String filename) {
		// TODO Auto-generated method stub
		return 0;
	}
}

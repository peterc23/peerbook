package ece454p1;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PeerManager {
	private final static long timeout = 1000; // time for each scan
	private static PeerManager manager = null;
	
	private Thread t;
	private ServerPeer peer = null;
	private Object goLock = null;
	private Object workLock = null;
	private boolean isWorking = false;
	
	private HashSet<ChunkInfo> receivingChunk;
	
	private PeerManager() {
		goLock = new Object();
		workLock = new Object();
		isWorking = false;
		receivingChunk = new HashSet<ChunkInfo>();
		
		t = new Thread(new Runnable() {
			public void run() {
				runInBackground();
			}
		});
		t.start();
	}
	
	public boolean receiveChunk(String chunkFile) {
		ChunkInfo info = getChunkInfoFromFileName(chunkFile);
		if (info == null) return false;
		return this.receiveChunk(info.headId, info.chunk);
	}
	
	public boolean finishChunk(String chunkFile) {
		ChunkInfo info = getChunkInfoFromFileName(chunkFile);
		if (info == null) return false;
		return this.finishChunk(info.headId, info.chunk);
	}
	
	public synchronized boolean receiveChunk(int headerId, int chunk) {
		return receivingChunk.add(new ChunkInfo(headerId, chunk));
	}
	
	public synchronized boolean finishChunk(int headerId, int chunk) {
		return receivingChunk.remove(new ChunkInfo(headerId, chunk));
	}
	
	public synchronized boolean isReceivingChunk(int headerId, int chunk) {
		return receivingChunk.contains(new ChunkInfo(headerId, chunk));
	}
	
	public void poke() {
		synchronized (goLock) {
			goLock.notify();
		}
	}
	
	public void start() {
		synchronized (workLock) {
			isWorking = true;
			workLock.notify();
		}
	}
	
	public void stop() {
		synchronized (workLock) {
			isWorking = false;
		}
	}
	
	public void checkIsWorking() {
		synchronized (workLock) {
			if (!isWorking) {
				try {
					workLock.wait();
				} catch (InterruptedException e) {
					System.out.println(e);
				}
			}
		}
	}
	
	public void runInBackground() {
		Status status = new ConcreteStatus();
		while (true) {
			// work lock
			checkIsWorking();
			
			// poll to check new peers to be added
			peer.getPeers().checkForNewPeers();
			
			ArrayList<HeaderFile> myheaders = FileUtils.getHeaderFiles();
			if (myheaders != null && myheaders.size() > 0) {
				
				// combine files
				for(int i=0; i< myheaders.size(); i++){
					int chunkCount = myheaders.get(i).localChunksPresent.length;
					int count = 0;
					for(int j=0; j<chunkCount; j++){
						if(myheaders.get(i).localChunksPresent[j] == true){
							count++;
						}
					}
					String filename = FileUtils.getFileNameFromId(myheaders.get(i).fileId);
					if(filename != null){
						File destFile = new File(filename);
						if (count == chunkCount && !destFile.exists()){
	
							ArrayList<File> fileList = new ArrayList<File>();
	 						for(int j=1; j< chunkCount+1; j++){
								File file = new File(filename+".part"+j);
								fileList.add(file);
							}
							FileUtils.combineFiles(fileList, filename);
						}
					}
				}
				
				String toSync = HttpRequest.postServer(Config.SERVER_SYNC_STATUS, null);
				if ("notok".equals(toSync)) {
					// skip sync, and wait for the next cycle
				} else {
					// for sending
					peer.query(status);
					
					// check which part files the client doesn't have
					// get all the chunks
					HashMap<String, Boolean> chunkMap = new HashMap<String, Boolean>();
					HashMap<Integer, Boolean> headerMap = new HashMap<Integer, Boolean>();
					for (HeaderFile head : myheaders) {
						for (int i=0; i<head.localChunksPresent.length; i++) {
							if (head.localChunksPresent[i]) {
								ChunkInfo info = new ChunkInfo();
								info.chunk = i;
								info.headId = head.fileId;
								chunkMap.put(info.toString(), false);
							}
						}
						headerMap.put(head.fileId, false);
					}
				
					
					ClientPeer client = null;
					ArrayList<HeaderFile> tempHead = null;
					ChunkInfo tempInfo = null;
					int leastReplication = -1;
					int tempReplication = -1;
					boolean headFlag = false;
					if (this.peer.getPeers() == null) {
						// probably this peer has been disconnected
						stop();
						continue;
					}
					for (Peer temppeer : this.peer.peers.peers) {
						client = (ClientPeer)temppeer;
						
						// if we are already sending to this peer or not connected to it, don't send
						if (!client.isConnected() || client.isSending()) continue;
						
						// set matching ones to true
						tempHead = status.headerfiles.get(client.getSocket());
						if(tempHead == null || tempHead.size() <= 0) { 
							// just send the first header file in my computer if client doesn't have any
							client.sendFile(FileUtils.getHeadPathFromId(myheaders.get(0).fileId), false);
							continue;
						} else {
							// check which header files the client doesn't have
							headFlag = false;
							for (HeaderFile eachHead : tempHead){
								headerMap.put(eachHead.fileId, true);
							}
							for (Integer fileId : headerMap.keySet()){
								if(!headerMap.get(fileId).booleanValue()) {
									headFlag = true;
									client.sendFile(FileUtils.getHeadPathFromId(fileId), false);
								}
							}
							if(headFlag == true) continue;
							
							// rest all to false
							for (String info : chunkMap.keySet()) {
								chunkMap.put(info, false);
							}
							
							for (HeaderFile eachHead : tempHead) {
								for (int i=0; i<eachHead.localChunksPresent.length; i++) {
									if (eachHead.localChunksPresent[i]) {
										tempInfo = new ChunkInfo();
										tempInfo.headId = eachHead.fileId;
										tempInfo.chunk = i;
										chunkMap.put(tempInfo.toString(), true);
									}
								}
							}
							// get least replication for don't exist chunks
							ChunkInfo info = new ChunkInfo();
							leastReplication = -1;
							tempReplication = -1;
							String resultInfo = null;
							for (String infoString : chunkMap.keySet()) {
								info.fromString(infoString);
								if (!chunkMap.get(infoString).booleanValue()) {
									tempReplication = status.chunkReplication.get(info.headId).get(info.chunk);
									if (leastReplication == -1 || tempReplication < leastReplication) {
										leastReplication = tempReplication;
										resultInfo = infoString;
									}
								}
							}
							// send least replicated file
							if (resultInfo != null) {
								info.fromString(resultInfo);
								client.sendFile(FileUtils.getChunkPathFromId(info.headId, info.chunk+1), false);
							}
						}
					}
				}
			}
			
			// wait for next scan
			synchronized (goLock) {
				try {
					goLock.wait(timeout);
				} catch (InterruptedException e) {
					System.out.println(e);
				}
			}
		}
	}
	
	public void setPeer(ServerPeer peer) {
		this.peer = peer;
	}
	
	public static PeerManager getManager() {
		if (manager == null) {
			manager = new PeerManager();
		}
		return manager;
	}
	

	public static ChunkInfo getChunkInfoFromFileName(String chunkFile) {
		if (chunkFile == null || chunkFile.isEmpty()) return null;
		
		// get filename and chunk count
		int dot = chunkFile.lastIndexOf(".");
		String filename = chunkFile.substring(0, dot);
		int chunk = Integer.parseInt(chunkFile.substring(dot+5));
		
		// get header id
		StringBuilder path = new StringBuilder();
		path.append(Config.SHARE_PATH);
		path.append(filename);
		path.append(Config.HEADER_FILE_EXT);
		HeaderFile header = FileUtils.readHeaderFile(new File(path.toString()));
		int headerid = header.fileId;
		
		return new ChunkInfo(headerid, chunk);
	}
}

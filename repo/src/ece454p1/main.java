package ece454p1;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class main{
	
	public static void main(String[] args) {
		if (args.length <= 1) {
			System.out.println("Need to pass hostname and port as arguments");
			return;
		}
		
		String host = args[0];
		String port = args[1];
		
		// start server
		ServerPeer serverPeer = new ServerPeer(host, port);
		//serverPeer.start(); // NEED TO REMOVE THIS
		
		// start peer manager
		PeerManager.getManager().setPeer(serverPeer);
		//PeerManager.getManager().start();
		
		// start reading from command prompt
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = null;
		ConcreteStatus status = new ConcreteStatus();
		PeerPostObject peerPost = null;
		
		String fileRelativePath = null;
		String fileOpen = null;
		StringBuffer fileContent = null;
		
		while(true) {
			try{
				System.out.print("> ");
				input = br.readLine();
				System.out.println("Message: " + input);
				if(input != null && !input.equals("")){
					if (input.startsWith("open")) {
						String [] file = input.split(" ");
						if (file.length != 2) {
							System.out.println("Warning: you must specify the file name");
						} else if (!file[1].endsWith(".txt")) {
							System.out.println("Warning: you can only edit a .txt file");
						} else {
							fileRelativePath = file[1];
							if (fileRelativePath.startsWith("/")) {
								fileRelativePath = fileRelativePath.substring(1);
							}
							fileOpen = Config.SHARE_PATH + file[1];
							fileContent = new StringBuffer(FileUtils.readFile(fileOpen));
							System.out.println("File opened: " + fileOpen);
						}
					} else if (input.equals("close")) {
						if (fileOpen == null) {
							System.out.println("Warning: you cannot close a file that hasn't been opened");
						} else {
							int tryCount = 0;
							while (tryCount < Config.SYNC_TRY_COUNT) {
								HttpRequest.postServer(Config.SERVER_STOP_SYNC, null);
								if ("notok".equals(HttpRequest.postServer(Config.SERVER_SYNC_STATUS, null))) {
									// tell other peers to delete this file
									serverPeer.broadCastAction(new Action(Action.ActionType.deleteFile, fileRelativePath));
									
									// update server checksum
									HeaderFile torrent = FileUtils.readHeaderFile(new File(fileOpen+Config.HEADER_FILE_EXT));
									FilePostObject postInfo = new FilePostObject(torrent.fileId, MD5Checksum.getMD5Checksum(fileOpen));
									HttpRequest.postServer(Config.SERVER_CHECKSUM, postInfo);
									
									// update this file
									File oldFile = new File(fileOpen);
									if (oldFile.delete()) {
										// write string to file
										FileUtils.writeFile(fileOpen, fileContent.toString());
										
										// delete the chunks and update header file
										FileUtils.deleteChuckFiles(fileOpen);
										ArrayList<File> files = FileUtils.divideFiles(new File(fileOpen));
										FileUtils.updateHeaderFileForWrite(fileRelativePath, torrent, files);
										
										// reset 
										fileRelativePath = null;
										fileOpen = null;
										fileContent = null;
										
										System.out.println("File closed: " + fileOpen);
										break;
									}
								}
								tryCount++;
							}
							HttpRequest.postServer(Config.SERVER_START_SYNC, null);
						}
					} else if (input.startsWith("read")) {
						if (fileContent == null) {
							System.out.println("Warning: you must first open the file to read");
						} else {
							String [] readArgs = input.split(" ");
							if (readArgs.length < 2) {
								System.out.println("Warning: you must specify the indexes for reading");
							} else if (readArgs.length == 2) {
								System.out.println(fileContent.substring(Integer.parseInt(readArgs[1])));
							} else if (readArgs.length == 3) {
								System.out.println(fileContent.substring(Integer.parseInt(readArgs[1]), Integer.parseInt(readArgs[2])));
							} else {
								System.out.println("Warning: too many arguments");
							}
						}
					} else if (input.startsWith("write")) {
						if (fileContent == null) {
							System.out.println("Warning: you must first open the file to write");
						} else {
							String [] readArgs = input.split(" ");
							if (readArgs.length != 3) {
								System.out.println("Warning: you must specify the index and string for writing");
							} else {
								if (Integer.parseInt(readArgs[1]) > fileContent.length()) {
									int diff = Integer.parseInt(readArgs[1]) - fileContent.length();
									for (int i=0; i<diff; i++) {
										fileContent.append(" ");
									}
									fileContent.append(readArgs[2]);
								} else {
									fileContent.insert(Integer.parseInt(readArgs[1]), readArgs[2]);
								}
							}
						}
					} else if(input.equalsIgnoreCase("query")){
						serverPeer.query(status);
					} else if (input.equalsIgnoreCase("join")) {
						serverPeer.start(); 
						PeerManager.getManager().start();
						peerPost = new PeerPostObject(serverPeer.hostname, serverPeer.port);
						peerPost = (PeerPostObject)FileUtils.mapToObject(HttpRequest.postServer(Config.SERVER_PEER_JOIN, peerPost), PeerPostObject.class);
					} else if (input.equalsIgnoreCase("leave")) {
						if (peerPost == null) {
							System.out.println("Warning: you cannot leave before joining");
						} else {
							HttpRequest.postServer(Config.SERVER_PEER_LEAVE, peerPost);
							System.exit(0);
							//PeerManager.getManager().stop();
							//serverPeer.stop(); 
						}
					} else if (input.startsWith("insert")) {
						if (serverPeer.query(status) == ReturnCodes.ERR_NO_PEERS_FOUND) {
							System.out.println("You must join first before querying status");
						} else { 
							String [] file = input.split(" ");
							serverPeer.insert(file[1], status);
						}
						//TODO: NEED TO UPDATE HEADER FILE ID RIGHT BEFORE SENDING IT OUT
						
					} else if (input.startsWith("file")) {
						ArrayList<File> flist = new ArrayList<File>();
						for (int i=1; i<=9; i++){
							File ofile = new File("./bitTorrent/abc.jpg.part"+i);
							flist.add(ofile);
						}
						FileUtils.combineFiles(flist, "./bitTorrent/jeffrey.jpg");

					} else {
						serverPeer.broadCastMessage(input);
					}
				}
			}catch(IOException e){
				System.out.println("Error processing input");
				System.exit(1);
			}
		}
	}

}

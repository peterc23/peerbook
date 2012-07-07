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
		serverPeer.start(); // NEED TO REMOVE THIS
		
		// start peer manager
		PeerManager.getManager().setPeer(serverPeer);
		PeerManager.getManager().start();
		
		// start reading from command prompt
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = null;
		ConcreteStatus status = new ConcreteStatus();
		while(true) {
			try{
				System.out.print("> ");
				input = br.readLine();
				System.out.println("Message: " + input);
				if(input != null && !input.equals("")){
					if(input.equalsIgnoreCase("query")){
						serverPeer.query(status);
					} else if (input.equalsIgnoreCase("join")) {
						PeerManager.getManager().start();
						serverPeer.start(); 
					} else if (input.equalsIgnoreCase("leave")) {
						System.exit(0);
						//PeerManager.getManager().stop();
						//serverPeer.stop(); 
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
						if (serverPeer.peers != null) {
							for (int i=0; i<serverPeer.peers.numPeers; i++) {
								ClientPeer peer = ((ClientPeer)serverPeer.peers.getPeer(i));
								if (peer.isConnected()) {
									peer.sendMessage(input, false);	
								}
							}
						} else {
							System.out.println("you have to join first before you can send messages");
						}
					}
				}
			}catch(IOException e){
				System.out.println("Error processing input");
				System.exit(1);
			}
		}
	}

}

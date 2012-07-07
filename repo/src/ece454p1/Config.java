package ece454p1;

public class Config {

	public static final int CHUNK_SIZE = 65536;
	public static final int MAX_PEERS = 6;

	// Cheesy, but allows us to do a simple Status class
	public static final int MAX_FILES = 100;
	
	public static final String SHARE_PATH = "./bitTorrent/";
	
	public static final String HEADER_FILE_EXT = ".torrent";

	public	final static String PEERS_FILE = "./peersFile.txt";

}

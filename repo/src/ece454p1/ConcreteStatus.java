package ece454p1;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ConcreteStatus extends Status {

	public ConcreteStatus(){
		this.numFiles = 0;
		this.local = new float[Config.MAX_FILES+1];
		this.system = new float[Config.MAX_FILES+1];
		this.leastReplication = new int[Config.MAX_FILES+1];
		this.chunkReplication = new ArrayList<ArrayList<Integer>>();
		for(int i=0; i<Config.MAX_FILES+1; i++){
			ArrayList<Integer> newList = new ArrayList<Integer>();
			this.chunkReplication.add(newList);
		}
		this.weightedLeastReplication = new float[Config.MAX_FILES+1];
		this.globalHeader = new HeaderFile[Config.MAX_FILES+1];
		this.headerfiles = new HashMap<Socket, ArrayList<HeaderFile>>();
	}
	
	public void clear(){
		this.numFiles = 0;
		this.local = new float[Config.MAX_FILES+1];
		this.system = new float[Config.MAX_FILES+1];
		this.leastReplication = new int[Config.MAX_FILES+1];
		this.chunkReplication = new ArrayList<ArrayList<Integer>>();
		for(int i=0; i<Config.MAX_FILES+1; i++){
			ArrayList<Integer> newList = new ArrayList<Integer>();
			this.chunkReplication.add(newList);
		}
		this.weightedLeastReplication = new float[Config.MAX_FILES+1];
		this.globalHeader = new HeaderFile[Config.MAX_FILES+1];
		
	}
	@Override
	public int numberOfFiles() {
		return this.numFiles;
	}

	@Override
	public float fractionPresentLocally(int fileNumber) {
		if(!isFilePresent(fileNumber)) return -1;
		return this.local[fileNumber];
	}

	@Override
	public float fractionPresent(int fileNumber) {
		if(!isFilePresent(fileNumber)) return -1;
		return this.system[fileNumber];
	}

	@Override
	public int minimumReplicationLevel(int fileNumber) {
		if(!isFilePresent(fileNumber)) return -1;
		return this.leastReplication[fileNumber];
	}

	@Override
	public float averageReplicationLevel(int fileNumber) {
		if(!isFilePresent(fileNumber)) return -1;
		return this.weightedLeastReplication[fileNumber];
	}
	
	public boolean isFilePresent(int fileNumber){
		if (this.globalHeader[fileNumber] == null){
			return false;
		}else{
			return true;
		}
	}

}

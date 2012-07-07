package ece454p1;

public class ChunkInfo {
	public int headId;
	public int chunk;
	
	public ChunkInfo() {
		this.chunk = 0;
		this.headId = 0;
	}
	
	public ChunkInfo(int headId, int chunk) {
		this.headId = headId;
		this.chunk = chunk;
	}
	
	public boolean equals(ChunkInfo info) {
		return (info.headId == headId) && (info.chunk == chunk);
	}
	
	public String toString() {
		return Integer.toString(headId)+","+Integer.toString(chunk);
	}
	
	public void fromString(String value) {
		String[] splitted = value.split(",");
		this.headId = Integer.parseInt(splitted[0]);
		this.chunk = Integer.parseInt(splitted[1]);
	}
}

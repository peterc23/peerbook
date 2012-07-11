package ece454p1;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="objectType")
public class FilePostObject {
	
	@JsonProperty
	public int id;

	@JsonProperty
	public String timestamp;
	
	@JsonProperty
	public String checksum;
	
	@JsonProperty
	public String editing;
	
	@JsonProperty
	public String path;
	
	public FilePostObject() {
		
	}
	public FilePostObject(int id, String timestamp, String checksum, String editing, String path) {
		this.id = id;
		this.timestamp = timestamp;
		this.checksum = checksum;
		this.editing = editing;
		this.path = path;
	}
	
	public FilePostObject(int id, String checksum) {
		this.id = id;
		this.timestamp = null;
		this.checksum = checksum;
		this.editing = null;
		this.path = null;
	}
	
	public FilePostObject(String checksum, String path) {
		this.id = -1;
		this.timestamp = null;
		this.checksum = checksum;
		this.path = path;
		this.editing = null;
	}
}

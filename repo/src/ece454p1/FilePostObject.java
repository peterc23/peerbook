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
	public boolean editing;
	
	public FilePostObject(int id, String checksum) {
		this.id = id;
		this.timestamp = null;
		this.checksum = checksum;
		this.editing = false;
	}
	
}

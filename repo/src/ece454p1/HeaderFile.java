package ece454p1;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="objectType")
public class HeaderFile {

	@JsonProperty
	public int fileId;
	
	@JsonProperty
	public boolean [] localChunksPresent;
	
	@JsonProperty 
	public String fullRelativePath;
	
	public HeaderFile copy() {
		HeaderFile file = new HeaderFile();
		file.fileId = this.fileId;
		file.localChunksPresent = this.localChunksPresent;
		file.fullRelativePath = this.fullRelativePath;
		return file;
	}
	
}

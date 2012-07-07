package ece454p1;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="objectType")
public class FileInfo {
/*
	@JsonProperty
	public String fileName;
	
	@JsonProperty
	public String extension;
	
	@JsonProperty
	public long size;
	*/
	@JsonProperty
	public HeaderFile headFile;
}

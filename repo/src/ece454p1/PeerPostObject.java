package ece454p1;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import ece454p1.Action.ActionType;


@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="objectType")
public class PeerPostObject {

	@JsonProperty
	public String id;
	
	@JsonProperty
	public String hostname;
	
	@JsonProperty
	public String port;
	
	@JsonProperty
	public String status;
	
	public PeerPostObject() {
		id = null;
		hostname = null;
		port = null;
		status = null;
	}
	
	public PeerPostObject(String hostname, String port) {
		this.id = null;
		this.hostname = hostname;
		this.port = port;
		this.status = null;
	}
}

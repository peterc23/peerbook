package ece454p1;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="objectType")
public class PeerListPostObject {
	
	@JsonProperty
	public PeerPostObject[] peerList;
}

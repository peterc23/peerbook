package ece454p1;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="objectType")
public class Action {

	public enum ActionType {fileInfo, deleteFile};

	@JsonProperty
	public ActionType type;
	
	@JsonProperty
	public String filePath;
	
	public Action() {
		type = ActionType.fileInfo;
		filePath = null;
	}
	
	public Action(ActionType type, String filePath) {
		this.type = type; 
		this.filePath = filePath;
	}
}

package sharedResources;

public class SymbolTableEntry {
	private String token;
	private String type;
	private String location;
	
	public SymbolTableEntry(String token){
		this.token = token;
	}
	
	public SymbolTableEntry(String token, String type){
		this.token = token;
		this.type = type;
	}
	
	public SymbolTableEntry(String token, String type, String location){
		this.token = token;
		this.type = type;
		this.location = location;
	}
	
	/** Getters and Setters **/
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}

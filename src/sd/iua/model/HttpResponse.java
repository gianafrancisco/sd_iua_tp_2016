package sd.iua.model;

public interface HttpResponse {
	
	
	public String getStatus404();
	public String getStatus405();
	public String getStatus500();
	public String getStatus503();
	
	public void addHeader(String name, String value);
	public String getHeaders();
	
	public String getResponseHeaderOK();
}

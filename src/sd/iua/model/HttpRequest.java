package sd.iua.model;

public interface HttpRequest {

	public String getPlainMessage();

	public String getPath();

	public String getQuery();

	public String getVerb();
	
	public boolean isVerbAllowed();

	public String getRequestMimeType();

}

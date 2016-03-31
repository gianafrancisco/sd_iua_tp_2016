class HttpResponse {
public:
	 virtual String getStatus404() = 0;
	 virtual String getStatus405() = 0;
	 virtual String getStatus500() = 0;
	 virtual String getStatus503() = 0;

	 virtual void addHeader(String name, String value) = 0;
	 virtual String getHeaders() = 0;
	
	 virtual String getResponseHeaderOK() = 0;
};

class Header {
private:
  String attr;
  String value;
public:
  Header(String attr, String value){
    this->attr = attr;
    this->value = value;
  };
  String toString() {
         return attr+": "+value+"\r\n";
    };
};

class HttpResponseImp: public HttpResponse {
private:
	Header *header[10];
	int count = 0;
	String headerToString(){
		String s = "";
		for(int i = 0; i<10;i++){
			s+=header[i]->toString();
		}
		return s;
	}
public:
	 String getStatus404(){
	 	return String("HTTP/1.1 404 Not Found\r\n");
	 };
	 String getStatus405(){
	 	return String("HTTP/1.1 405 Method Not Allowed\r\n");
	 };
	 String getStatus500(){
	 	return String("HTTP/1.1 500 Internal Server Error\r\n");
	 };
	 String getStatus503(){
	 	return String("HTTP/1.1 503 Service Unavailable\r\n");
	 };

	 void addHeader(String name, String value){
	 	header[count++] = new Header(name, value);
	 };
	 String getHeaders(){
	 	return headerToString();
	 };
	
	 String getResponseHeaderOK(){
	 	return String("HTTP/1.1 200 OK\r\n"+headerToString()+"\r\n");
	 };

};



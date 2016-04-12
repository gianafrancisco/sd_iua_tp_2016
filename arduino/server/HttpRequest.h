#include "Properties.h"
#include <EthernetClient.h>

class HttpRequest {
public:
	virtual String getPlainMessage() = 0; 
	virtual String getPath() = 0;
	virtual String getQuery() = 0;
	virtual String getVerb() = 0;
	virtual bool isVerbAllowed() = 0;
	virtual String getRequestMimeType() = 0;
	
};


class HttpRequestImp: public HttpRequest {
private:
	String line;
	String message;
    Properties prop;
public:

	HttpRequestImp(EthernetClient *client, Properties prop){
		this->prop = prop;
		while(client->available()){
			message += client->read();
		}
	}

	String getPlainMessage(){
		return message;
	}; 
	String getPath(){
		int begin = ((line.indexOf("//") == -1)?line.indexOf("/"):line.indexOf("/",line.indexOf("//") + 2));
        int end = (line.indexOf("?", begin) == -1)?line.indexOf(" ", begin):line.indexOf("?", begin);
        return line.substring(begin, end);
	};
	String getQuery(){
        int begin = line.indexOf("?");
        if(begin == -1) return "";
        int end = (line.indexOf(" ", begin) == -1)?begin:line.indexOf(" ", begin);
        return line.substring(begin + 1, end);
	};
	String getVerb(){
        String verb = line.substring(0,line.indexOf(" "));
        return verb;
	};
	bool isVerbAllowed(){
        if(getVerb() == "GET") return true;
        if(getVerb() == "POST") return true;
        return false;
	};
	String getRequestMimeType(){
        String path = getPath();
        String ext = path.substring(path.lastIndexOf("."), path.length());
        return prop.getProperty(ext);
	};
};

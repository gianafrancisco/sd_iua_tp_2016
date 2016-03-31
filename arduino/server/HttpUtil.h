#include <SD.h>
class HttpUtil {
public:
	virtual File* readFile(String virtualPath) = 0;
	virtual bool fileExists(String virtualPath) = 0;
};


class HttpUtilImp: public HttpUtil {
private:
	String path = "site/";
  File f;
public:
	File* readFile(String virtualPath){
		String file = path + virtualPath;
    	f = SD.open(file);
		return &f;
	};
	bool fileExists(String virtualPath){
		String file = path + virtualPath;
		return SD.exists(file);
	};	
};

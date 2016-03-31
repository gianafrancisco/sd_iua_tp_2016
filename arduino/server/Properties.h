#include "Attributes.h"
#ifndef __PROPERTIES__
#define __PROPERTIES__
class Properties {
private:
	Attributes *prop[15];
  int count;
public:
	Properties(){

		prop[0] = new Attributes(".jpg","image/jpeg");count++;
		prop[1] = new Attributes(".png","image/png");count++; 
		prop[2] = new Attributes(".html","text/html");count++;
		prop[3] = new Attributes(".html","text/html");count++;
		prop[4] = new Attributes(".css","text/css");count++;
		prop[5] = new Attributes(".js","application/javascript");count++;

	};
  void add(String name, String value){
    prop[count++] = new Attributes(name, value);
  };
	String getProperty(String ext){
		for(int i = 0; i < count; i++){
			if(prop[i]->key() == ext) return prop[i]->val();
		}
	};
};
#endif

#ifndef __ATTRIBUTES__
#define __ATTRIBUTES__
class Attributes {
private:
  String attr;
  String value;
public:
  Attributes(String attr, String value){
    this->attr = attr;
    this->value = value;
  };
  String toString() {
         return attr+": "+value+"\r\n";
  };
  String val(){
  	return value;
  };
  String key(){
  	return attr;
  };
};
#endif

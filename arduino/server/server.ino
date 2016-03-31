#include "Properties.h"
#include "AtiendeCliente.h"

#include <SPI.h>
#include <Ethernet.h>
#include <SD.h>
// network configuration.  gateway and subnet are optional.

 // the media access control (ethernet hardware) address for the shield:
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };  
//the IP address for the shield:
byte ip[] = { 192, 168, 100, 30 };    
// the router's gateway address:
byte gateway[] = { 192, 168, 100, 254 };
// the subnet:
byte subnet[] = { 255, 255, 255, 0 };

Properties prop;
EthernetServer server = EthernetServer(80);
AtiendeCliente *ac;

void setup() {
  // put your setup code here, to run once:
  Ethernet.begin(mac, ip, gateway, subnet);
  SD.begin();
  prop.add("httpserver.name","Super Server");
  prop.add("httpserver.maxconn","10");
  prop.add("web.site.folder","site/");
  prop.add("httpserver.port","8080");
  prop.add("admin.port","8081");
}

void loop() {
  // put your main code here, to run repeatedly:
  EthernetClient ec = server.available();
  if(ec == true)
    ac = new AtiendeCliente(&ec, prop);
}

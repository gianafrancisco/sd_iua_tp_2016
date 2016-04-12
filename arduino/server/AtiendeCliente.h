#include "Properties.h"
#include "HttpRequest.h"
#include "HttpResponse.h"
#include "HttpUtil.h"
#include <Ethernet.h>
#include <SD.h>


class AtiendeCliente {
	private:
		HttpResponse *response;
		HttpRequest *request;
		HttpUtil *util;
		EthernetClient *client;
		Properties prop;
  public:
		AtiendeCliente(EthernetClient *client, Properties prop){
			this->client = client;
			this->prop = prop;
			this->request = new HttpRequestImp(client, prop);
			this->util = new HttpUtilImp();
			this->response = new HttpResponseImp();
		};
	    void run(){
	          if(util->fileExists(request->getPath()) == true){
	          	client->print(response->getStatus404());
	          }else if(request->isVerbAllowed() != true){
				client->print(response->getStatus405());
	          }else {
				File *body = util->readFile(request->getPath());
				if(body != false){
					response->addHeader("Server", prop.getProperty("httpserver.name"));
					response->addHeader("Content-Type", request->getRequestMimeType());
					response->addHeader("Content-Length:", String(body->size()));

					client->print(response->getResponseHeaderOK());
					while(body->available())
						client->print(body->read());
					body->close();					
				}else{
					client->print(response->getStatus500());
				}
	          }
	    };
};

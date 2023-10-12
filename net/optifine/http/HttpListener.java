package net.optifine.http;

public interface HttpListener {
  void finished(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse);
  
  void failed(HttpRequest paramHttpRequest, Exception paramException);
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\http\HttpListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
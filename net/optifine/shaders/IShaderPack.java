package net.optifine.shaders;

import java.io.InputStream;

public interface IShaderPack {
  String getName();
  
  InputStream getResourceAsStream(String paramString);
  
  boolean hasDirectory(String paramString);
  
  void close();
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\shaders\IShaderPack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.client.renderer.texture;

import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;
import net.optifine.shaders.MultiTexID;

public interface ITextureObject {
  void setBlurMipmap(boolean paramBoolean1, boolean paramBoolean2);
  
  void restoreLastBlurMipmap();
  
  void loadTexture(IResourceManager paramIResourceManager) throws IOException;
  
  int getGlTextureId();
  
  MultiTexID getMultiTexID();
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\texture\ITextureObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
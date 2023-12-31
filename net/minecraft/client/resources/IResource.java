package net.minecraft.client.resources;

import java.io.InputStream;
import net.minecraft.util.ResourceLocation;

public interface IResource {
  ResourceLocation getResourceLocation();
  
  InputStream getInputStream();
  
  boolean hasMetadata();
  
  <T extends net.minecraft.client.resources.data.IMetadataSection> T getMetadata(String paramString);
  
  String getResourcePackName();
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\resources\IResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
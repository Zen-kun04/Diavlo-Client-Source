package net.minecraft.client.resources.data;

import com.google.gson.JsonDeserializer;

public interface IMetadataSectionSerializer<T extends IMetadataSection> extends JsonDeserializer<T> {
  String getSectionName();
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\resources\data\IMetadataSectionSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
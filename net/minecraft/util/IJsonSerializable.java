package net.minecraft.util;

import com.google.gson.JsonElement;

public interface IJsonSerializable {
  void fromJson(JsonElement paramJsonElement);
  
  JsonElement getSerializableElement();
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\IJsonSerializable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
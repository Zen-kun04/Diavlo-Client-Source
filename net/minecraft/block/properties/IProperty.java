package net.minecraft.block.properties;

import java.util.Collection;

public interface IProperty<T extends Comparable<T>> {
  String getName();
  
  Collection<T> getAllowedValues();
  
  Class<T> getValueClass();
  
  String getName(T paramT);
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\properties\IProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
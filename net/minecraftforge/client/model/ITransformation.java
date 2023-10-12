package net.minecraftforge.client.model;

import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Matrix4f;

public interface ITransformation {
  Matrix4f getMatrix();
  
  EnumFacing rotate(EnumFacing paramEnumFacing);
  
  int rotate(EnumFacing paramEnumFacing, int paramInt);
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraftforge\client\model\ITransformation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
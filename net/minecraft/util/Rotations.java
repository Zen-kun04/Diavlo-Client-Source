/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.nbt.NBTBase;
/*    */ import net.minecraft.nbt.NBTTagFloat;
/*    */ import net.minecraft.nbt.NBTTagList;
/*    */ 
/*    */ public class Rotations
/*    */ {
/*    */   protected final float x;
/*    */   protected final float y;
/*    */   protected final float z;
/*    */   
/*    */   public Rotations(float x, float y, float z) {
/* 14 */     this.x = x;
/* 15 */     this.y = y;
/* 16 */     this.z = z;
/*    */   }
/*    */ 
/*    */   
/*    */   public Rotations(NBTTagList nbt) {
/* 21 */     this.x = nbt.getFloatAt(0);
/* 22 */     this.y = nbt.getFloatAt(1);
/* 23 */     this.z = nbt.getFloatAt(2);
/*    */   }
/*    */ 
/*    */   
/*    */   public NBTTagList writeToNBT() {
/* 28 */     NBTTagList nbttaglist = new NBTTagList();
/* 29 */     nbttaglist.appendTag((NBTBase)new NBTTagFloat(this.x));
/* 30 */     nbttaglist.appendTag((NBTBase)new NBTTagFloat(this.y));
/* 31 */     nbttaglist.appendTag((NBTBase)new NBTTagFloat(this.z));
/* 32 */     return nbttaglist;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 37 */     if (!(p_equals_1_ instanceof Rotations))
/*    */     {
/* 39 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 43 */     Rotations rotations = (Rotations)p_equals_1_;
/* 44 */     return (this.x == rotations.x && this.y == rotations.y && this.z == rotations.z);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public float getX() {
/* 50 */     return this.x;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getY() {
/* 55 */     return this.y;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getZ() {
/* 60 */     return this.z;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\Rotations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
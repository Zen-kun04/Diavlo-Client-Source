/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public abstract class NBTBase
/*    */ {
/*  9 */   public static final String[] NBT_TYPES = new String[] { "END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]" };
/*    */ 
/*    */   
/*    */   abstract void write(DataOutput paramDataOutput) throws IOException;
/*    */   
/*    */   abstract void read(DataInput paramDataInput, int paramInt, NBTSizeTracker paramNBTSizeTracker) throws IOException;
/*    */   
/*    */   public abstract String toString();
/*    */   
/*    */   public abstract byte getId();
/*    */   
/*    */   protected static NBTBase createNewByType(byte id) {
/* 21 */     switch (id) {
/*    */       
/*    */       case 0:
/* 24 */         return new NBTTagEnd();
/*    */       
/*    */       case 1:
/* 27 */         return new NBTTagByte();
/*    */       
/*    */       case 2:
/* 30 */         return new NBTTagShort();
/*    */       
/*    */       case 3:
/* 33 */         return new NBTTagInt();
/*    */       
/*    */       case 4:
/* 36 */         return new NBTTagLong();
/*    */       
/*    */       case 5:
/* 39 */         return new NBTTagFloat();
/*    */       
/*    */       case 6:
/* 42 */         return new NBTTagDouble();
/*    */       
/*    */       case 7:
/* 45 */         return new NBTTagByteArray();
/*    */       
/*    */       case 8:
/* 48 */         return new NBTTagString();
/*    */       
/*    */       case 9:
/* 51 */         return new NBTTagList();
/*    */       
/*    */       case 10:
/* 54 */         return new NBTTagCompound();
/*    */       
/*    */       case 11:
/* 57 */         return new NBTTagIntArray();
/*    */     } 
/*    */     
/* 60 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract NBTBase copy();
/*    */ 
/*    */   
/*    */   public boolean hasNoTags() {
/* 68 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 73 */     if (!(p_equals_1_ instanceof NBTBase))
/*    */     {
/* 75 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 79 */     NBTBase nbtbase = (NBTBase)p_equals_1_;
/* 80 */     return (getId() == nbtbase.getId());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 86 */     return getId();
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getString() {
/* 91 */     return toString();
/*    */   }
/*    */   
/*    */   public static abstract class NBTPrimitive extends NBTBase {
/*    */     public abstract long getLong();
/*    */     
/*    */     public abstract int getInt();
/*    */     
/*    */     public abstract short getShort();
/*    */     
/*    */     public abstract byte getByte();
/*    */     
/*    */     public abstract double getDouble();
/*    */     
/*    */     public abstract float getFloat();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\nbt\NBTBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
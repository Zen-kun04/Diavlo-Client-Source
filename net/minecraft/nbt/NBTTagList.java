/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class NBTTagList
/*     */   extends NBTBase
/*     */ {
/*  14 */   private static final Logger LOGGER = LogManager.getLogger();
/*  15 */   private List<NBTBase> tagList = Lists.newArrayList();
/*  16 */   private byte tagType = 0;
/*     */ 
/*     */   
/*     */   void write(DataOutput output) throws IOException {
/*  20 */     if (!this.tagList.isEmpty()) {
/*     */       
/*  22 */       this.tagType = ((NBTBase)this.tagList.get(0)).getId();
/*     */     }
/*     */     else {
/*     */       
/*  26 */       this.tagType = 0;
/*     */     } 
/*     */     
/*  29 */     output.writeByte(this.tagType);
/*  30 */     output.writeInt(this.tagList.size());
/*     */     
/*  32 */     for (int i = 0; i < this.tagList.size(); i++)
/*     */     {
/*  34 */       ((NBTBase)this.tagList.get(i)).write(output);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
/*  40 */     sizeTracker.read(296L);
/*     */     
/*  42 */     if (depth > 512)
/*     */     {
/*  44 */       throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
/*     */     }
/*     */ 
/*     */     
/*  48 */     this.tagType = input.readByte();
/*  49 */     int i = input.readInt();
/*     */     
/*  51 */     if (this.tagType == 0 && i > 0)
/*     */     {
/*  53 */       throw new RuntimeException("Missing type on ListTag");
/*     */     }
/*     */ 
/*     */     
/*  57 */     sizeTracker.read(32L * i);
/*  58 */     this.tagList = Lists.newArrayListWithCapacity(i);
/*     */     
/*  60 */     for (int j = 0; j < i; j++) {
/*     */       
/*  62 */       NBTBase nbtbase = NBTBase.createNewByType(this.tagType);
/*  63 */       nbtbase.read(input, depth + 1, sizeTracker);
/*  64 */       this.tagList.add(nbtbase);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  72 */     return 9;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  77 */     StringBuilder stringbuilder = new StringBuilder("[");
/*     */     
/*  79 */     for (int i = 0; i < this.tagList.size(); i++) {
/*     */       
/*  81 */       if (i != 0)
/*     */       {
/*  83 */         stringbuilder.append(',');
/*     */       }
/*     */       
/*  86 */       stringbuilder.append(i).append(':').append(this.tagList.get(i));
/*     */     } 
/*     */     
/*  89 */     return stringbuilder.append(']').toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void appendTag(NBTBase nbt) {
/*  94 */     if (nbt.getId() == 0) {
/*     */       
/*  96 */       LOGGER.warn("Invalid TagEnd added to ListTag");
/*     */     }
/*     */     else {
/*     */       
/* 100 */       if (this.tagType == 0) {
/*     */         
/* 102 */         this.tagType = nbt.getId();
/*     */       }
/* 104 */       else if (this.tagType != nbt.getId()) {
/*     */         
/* 106 */         LOGGER.warn("Adding mismatching tag types to tag list");
/*     */         
/*     */         return;
/*     */       } 
/* 110 */       this.tagList.add(nbt);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(int idx, NBTBase nbt) {
/* 116 */     if (nbt.getId() == 0) {
/*     */       
/* 118 */       LOGGER.warn("Invalid TagEnd added to ListTag");
/*     */     }
/* 120 */     else if (idx >= 0 && idx < this.tagList.size()) {
/*     */       
/* 122 */       if (this.tagType == 0) {
/*     */         
/* 124 */         this.tagType = nbt.getId();
/*     */       }
/* 126 */       else if (this.tagType != nbt.getId()) {
/*     */         
/* 128 */         LOGGER.warn("Adding mismatching tag types to tag list");
/*     */         
/*     */         return;
/*     */       } 
/* 132 */       this.tagList.set(idx, nbt);
/*     */     }
/*     */     else {
/*     */       
/* 136 */       LOGGER.warn("index out of bounds to set tag in tag list");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTBase removeTag(int i) {
/* 142 */     return this.tagList.remove(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNoTags() {
/* 147 */     return this.tagList.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound getCompoundTagAt(int i) {
/* 152 */     if (i >= 0 && i < this.tagList.size()) {
/*     */       
/* 154 */       NBTBase nbtbase = this.tagList.get(i);
/* 155 */       return (nbtbase.getId() == 10) ? (NBTTagCompound)nbtbase : new NBTTagCompound();
/*     */     } 
/*     */ 
/*     */     
/* 159 */     return new NBTTagCompound();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getIntArrayAt(int i) {
/* 165 */     if (i >= 0 && i < this.tagList.size()) {
/*     */       
/* 167 */       NBTBase nbtbase = this.tagList.get(i);
/* 168 */       return (nbtbase.getId() == 11) ? ((NBTTagIntArray)nbtbase).getIntArray() : new int[0];
/*     */     } 
/*     */ 
/*     */     
/* 172 */     return new int[0];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDoubleAt(int i) {
/* 178 */     if (i >= 0 && i < this.tagList.size()) {
/*     */       
/* 180 */       NBTBase nbtbase = this.tagList.get(i);
/* 181 */       return (nbtbase.getId() == 6) ? ((NBTTagDouble)nbtbase).getDouble() : 0.0D;
/*     */     } 
/*     */ 
/*     */     
/* 185 */     return 0.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getFloatAt(int i) {
/* 191 */     if (i >= 0 && i < this.tagList.size()) {
/*     */       
/* 193 */       NBTBase nbtbase = this.tagList.get(i);
/* 194 */       return (nbtbase.getId() == 5) ? ((NBTTagFloat)nbtbase).getFloat() : 0.0F;
/*     */     } 
/*     */ 
/*     */     
/* 198 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStringTagAt(int i) {
/* 204 */     if (i >= 0 && i < this.tagList.size()) {
/*     */       
/* 206 */       NBTBase nbtbase = this.tagList.get(i);
/* 207 */       return (nbtbase.getId() == 8) ? nbtbase.getString() : nbtbase.toString();
/*     */     } 
/*     */ 
/*     */     
/* 211 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTBase get(int idx) {
/* 217 */     return (idx >= 0 && idx < this.tagList.size()) ? this.tagList.get(idx) : new NBTTagEnd();
/*     */   }
/*     */ 
/*     */   
/*     */   public int tagCount() {
/* 222 */     return this.tagList.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTBase copy() {
/* 227 */     NBTTagList nbttaglist = new NBTTagList();
/* 228 */     nbttaglist.tagType = this.tagType;
/*     */     
/* 230 */     for (NBTBase nbtbase : this.tagList) {
/*     */       
/* 232 */       NBTBase nbtbase1 = nbtbase.copy();
/* 233 */       nbttaglist.tagList.add(nbtbase1);
/*     */     } 
/*     */     
/* 236 */     return nbttaglist;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object p_equals_1_) {
/* 241 */     if (super.equals(p_equals_1_)) {
/*     */       
/* 243 */       NBTTagList nbttaglist = (NBTTagList)p_equals_1_;
/*     */       
/* 245 */       if (this.tagType == nbttaglist.tagType)
/*     */       {
/* 247 */         return this.tagList.equals(nbttaglist.tagList);
/*     */       }
/*     */     } 
/*     */     
/* 251 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 256 */     return super.hashCode() ^ this.tagList.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTagType() {
/* 261 */     return this.tagType;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\nbt\NBTTagList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
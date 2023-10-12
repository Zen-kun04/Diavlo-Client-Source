/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.util.ReportedException;
/*     */ 
/*     */ 
/*     */ public class NBTTagCompound
/*     */   extends NBTBase
/*     */ {
/*  18 */   private Map<String, NBTBase> tagMap = Maps.newHashMap();
/*     */ 
/*     */   
/*     */   void write(DataOutput output) throws IOException {
/*  22 */     for (String s : this.tagMap.keySet()) {
/*     */       
/*  24 */       NBTBase nbtbase = this.tagMap.get(s);
/*  25 */       writeEntry(s, nbtbase, output);
/*     */     } 
/*     */     
/*  28 */     output.writeByte(0);
/*     */   }
/*     */ 
/*     */   
/*     */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
/*  33 */     sizeTracker.read(384L);
/*     */     
/*  35 */     if (depth > 512)
/*     */     {
/*  37 */       throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
/*     */     }
/*     */ 
/*     */     
/*  41 */     this.tagMap.clear();
/*     */     
/*     */     byte b0;
/*  44 */     while ((b0 = readType(input, sizeTracker)) != 0) {
/*     */       
/*  46 */       String s = readKey(input, sizeTracker);
/*  47 */       sizeTracker.read((224 + 16 * s.length()));
/*  48 */       NBTBase nbtbase = readNBT(b0, s, input, depth + 1, sizeTracker);
/*     */       
/*  50 */       if (this.tagMap.put(s, nbtbase) != null)
/*     */       {
/*  52 */         sizeTracker.read(288L);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getKeySet() {
/*  60 */     return this.tagMap.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  65 */     return 10;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTag(String key, NBTBase value) {
/*  70 */     this.tagMap.put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setByte(String key, byte value) {
/*  75 */     this.tagMap.put(key, new NBTTagByte(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setShort(String key, short value) {
/*  80 */     this.tagMap.put(key, new NBTTagShort(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInteger(String key, int value) {
/*  85 */     this.tagMap.put(key, new NBTTagInt(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLong(String key, long value) {
/*  90 */     this.tagMap.put(key, new NBTTagLong(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFloat(String key, float value) {
/*  95 */     this.tagMap.put(key, new NBTTagFloat(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDouble(String key, double value) {
/* 100 */     this.tagMap.put(key, new NBTTagDouble(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setString(String key, String value) {
/* 105 */     this.tagMap.put(key, new NBTTagString(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setByteArray(String key, byte[] value) {
/* 110 */     this.tagMap.put(key, new NBTTagByteArray(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIntArray(String key, int[] value) {
/* 115 */     this.tagMap.put(key, new NBTTagIntArray(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBoolean(String key, boolean value) {
/* 120 */     setByte(key, (byte)(value ? 1 : 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTBase getTag(String key) {
/* 125 */     return this.tagMap.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getTagId(String key) {
/* 130 */     NBTBase nbtbase = this.tagMap.get(key);
/* 131 */     return (nbtbase != null) ? nbtbase.getId() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasKey(String key) {
/* 136 */     return this.tagMap.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasKey(String key, int type) {
/* 141 */     int i = getTagId(key);
/*     */     
/* 143 */     if (i == type)
/*     */     {
/* 145 */       return true;
/*     */     }
/* 147 */     if (type != 99) {
/*     */       
/* 149 */       if (i > 0);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 154 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 158 */     return (i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getByte(String key) {
/*     */     try {
/* 166 */       return !hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getByte();
/*     */     }
/* 168 */     catch (ClassCastException var3) {
/*     */       
/* 170 */       return 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public short getShort(String key) {
/*     */     try {
/* 178 */       return !hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getShort();
/*     */     }
/* 180 */     catch (ClassCastException var3) {
/*     */       
/* 182 */       return 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInteger(String key) {
/*     */     try {
/* 190 */       return !hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getInt();
/*     */     }
/* 192 */     catch (ClassCastException var3) {
/*     */       
/* 194 */       return 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLong(String key) {
/*     */     try {
/* 202 */       return !hasKey(key, 99) ? 0L : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getLong();
/*     */     }
/* 204 */     catch (ClassCastException var3) {
/*     */       
/* 206 */       return 0L;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getFloat(String key) {
/*     */     try {
/* 214 */       return !hasKey(key, 99) ? 0.0F : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getFloat();
/*     */     }
/* 216 */     catch (ClassCastException var3) {
/*     */       
/* 218 */       return 0.0F;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDouble(String key) {
/*     */     try {
/* 226 */       return !hasKey(key, 99) ? 0.0D : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getDouble();
/*     */     }
/* 228 */     catch (ClassCastException var3) {
/*     */       
/* 230 */       return 0.0D;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString(String key) {
/*     */     try {
/* 238 */       return !hasKey(key, 8) ? "" : ((NBTBase)this.tagMap.get(key)).getString();
/*     */     }
/* 240 */     catch (ClassCastException var3) {
/*     */       
/* 242 */       return "";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getByteArray(String key) {
/*     */     try {
/* 250 */       return !hasKey(key, 7) ? new byte[0] : ((NBTTagByteArray)this.tagMap.get(key)).getByteArray();
/*     */     }
/* 252 */     catch (ClassCastException classcastexception) {
/*     */       
/* 254 */       throw new ReportedException(createCrashReport(key, 7, classcastexception));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getIntArray(String key) {
/*     */     try {
/* 262 */       return !hasKey(key, 11) ? new int[0] : ((NBTTagIntArray)this.tagMap.get(key)).getIntArray();
/*     */     }
/* 264 */     catch (ClassCastException classcastexception) {
/*     */       
/* 266 */       throw new ReportedException(createCrashReport(key, 11, classcastexception));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTTagCompound getCompoundTag(String key) {
/*     */     try {
/* 274 */       return !hasKey(key, 10) ? new NBTTagCompound() : (NBTTagCompound)this.tagMap.get(key);
/*     */     }
/* 276 */     catch (ClassCastException classcastexception) {
/*     */       
/* 278 */       throw new ReportedException(createCrashReport(key, 10, classcastexception));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTTagList getTagList(String key, int type) {
/*     */     try {
/* 286 */       if (getTagId(key) != 9)
/*     */       {
/* 288 */         return new NBTTagList();
/*     */       }
/*     */ 
/*     */       
/* 292 */       NBTTagList nbttaglist = (NBTTagList)this.tagMap.get(key);
/* 293 */       return (nbttaglist.tagCount() > 0 && nbttaglist.getTagType() != type) ? new NBTTagList() : nbttaglist;
/*     */     
/*     */     }
/* 296 */     catch (ClassCastException classcastexception) {
/*     */       
/* 298 */       throw new ReportedException(createCrashReport(key, 9, classcastexception));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBoolean(String key) {
/* 304 */     return (getByte(key) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeTag(String key) {
/* 309 */     this.tagMap.remove(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 314 */     StringBuilder stringbuilder = new StringBuilder("{");
/*     */     
/* 316 */     for (Map.Entry<String, NBTBase> entry : this.tagMap.entrySet()) {
/*     */       
/* 318 */       if (stringbuilder.length() != 1)
/*     */       {
/* 320 */         stringbuilder.append(',');
/*     */       }
/*     */       
/* 323 */       stringbuilder.append(entry.getKey()).append(':').append(entry.getValue());
/*     */     } 
/*     */     
/* 326 */     return stringbuilder.append('}').toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNoTags() {
/* 331 */     return this.tagMap.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   private CrashReport createCrashReport(final String key, final int expectedType, ClassCastException ex) {
/* 336 */     CrashReport crashreport = CrashReport.makeCrashReport(ex, "Reading NBT data");
/* 337 */     CrashReportCategory crashreportcategory = crashreport.makeCategoryDepth("Corrupt NBT tag", 1);
/* 338 */     crashreportcategory.addCrashSectionCallable("Tag type found", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 342 */             return NBTBase.NBT_TYPES[((NBTBase)NBTTagCompound.this.tagMap.get(key)).getId()];
/*     */           }
/*     */         });
/* 345 */     crashreportcategory.addCrashSectionCallable("Tag type expected", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 349 */             return NBTBase.NBT_TYPES[expectedType];
/*     */           }
/*     */         });
/* 352 */     crashreportcategory.addCrashSection("Tag name", key);
/* 353 */     return crashreport;
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTBase copy() {
/* 358 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*     */     
/* 360 */     for (String s : this.tagMap.keySet())
/*     */     {
/* 362 */       nbttagcompound.setTag(s, ((NBTBase)this.tagMap.get(s)).copy());
/*     */     }
/*     */     
/* 365 */     return nbttagcompound;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object p_equals_1_) {
/* 370 */     if (super.equals(p_equals_1_)) {
/*     */       
/* 372 */       NBTTagCompound nbttagcompound = (NBTTagCompound)p_equals_1_;
/* 373 */       return this.tagMap.entrySet().equals(nbttagcompound.tagMap.entrySet());
/*     */     } 
/*     */ 
/*     */     
/* 377 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 383 */     return super.hashCode() ^ this.tagMap.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeEntry(String name, NBTBase data, DataOutput output) throws IOException {
/* 388 */     output.writeByte(data.getId());
/*     */     
/* 390 */     if (data.getId() != 0) {
/*     */       
/* 392 */       output.writeUTF(name);
/* 393 */       data.write(output);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte readType(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
/* 399 */     return input.readByte();
/*     */   }
/*     */ 
/*     */   
/*     */   private static String readKey(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
/* 404 */     return input.readUTF();
/*     */   }
/*     */ 
/*     */   
/*     */   static NBTBase readNBT(byte id, String key, DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
/* 409 */     NBTBase nbtbase = NBTBase.createNewByType(id);
/*     */ 
/*     */     
/*     */     try {
/* 413 */       assert nbtbase != null;
/* 414 */       nbtbase.read(input, depth, sizeTracker);
/* 415 */       return nbtbase;
/*     */     }
/* 417 */     catch (IOException ioexception) {
/*     */       
/* 419 */       CrashReport crashreport = CrashReport.makeCrashReport(ioexception, "Loading NBT data");
/* 420 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("NBT Tag");
/* 421 */       crashreportcategory.addCrashSection("Tag name", key);
/* 422 */       crashreportcategory.addCrashSection("Tag type", Byte.valueOf(id));
/* 423 */       throw new ReportedException(crashreport);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void merge(NBTTagCompound other) {
/* 429 */     for (String s : other.tagMap.keySet()) {
/*     */       
/* 431 */       NBTBase nbtbase = other.tagMap.get(s);
/*     */       
/* 433 */       if (nbtbase.getId() == 10) {
/*     */         
/* 435 */         if (hasKey(s, 10)) {
/*     */           
/* 437 */           NBTTagCompound nbttagcompound = getCompoundTag(s);
/* 438 */           nbttagcompound.merge((NBTTagCompound)nbtbase);
/*     */           
/*     */           continue;
/*     */         } 
/* 442 */         setTag(s, nbtbase.copy());
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 447 */       setTag(s, nbtbase.copy());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\nbt\NBTTagCompound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
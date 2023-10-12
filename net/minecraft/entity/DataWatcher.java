/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.util.Rotations;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import org.apache.commons.lang3.ObjectUtils;
/*     */ 
/*     */ public class DataWatcher
/*     */ {
/*     */   private final Entity owner;
/*     */   private boolean isBlank = true;
/*  24 */   private static final Map<Class<?>, Integer> dataTypes = Maps.newHashMap();
/*  25 */   private final Map<Integer, WatchableObject> watchedObjects = Maps.newHashMap();
/*     */   private boolean objectChanged;
/*  27 */   private ReadWriteLock lock = new ReentrantReadWriteLock();
/*  28 */   public BiomeGenBase spawnBiome = BiomeGenBase.plains;
/*  29 */   public BlockPos spawnPosition = BlockPos.ORIGIN;
/*     */ 
/*     */   
/*     */   public DataWatcher(Entity owner) {
/*  33 */     this.owner = owner;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> void addObject(int id, T object) {
/*  38 */     Integer integer = dataTypes.get(object.getClass());
/*     */     
/*  40 */     if (integer == null)
/*     */     {
/*  42 */       throw new IllegalArgumentException("Unknown data type: " + object.getClass());
/*     */     }
/*  44 */     if (id > 31)
/*     */     {
/*  46 */       throw new IllegalArgumentException("Data value id is too big with " + id + "! (Max is " + '\037' + ")");
/*     */     }
/*  48 */     if (this.watchedObjects.containsKey(Integer.valueOf(id)))
/*     */     {
/*  50 */       throw new IllegalArgumentException("Duplicate id value for " + id + "!");
/*     */     }
/*     */ 
/*     */     
/*  54 */     WatchableObject datawatcher$watchableobject = new WatchableObject(integer.intValue(), id, object);
/*  55 */     this.lock.writeLock().lock();
/*  56 */     this.watchedObjects.put(Integer.valueOf(id), datawatcher$watchableobject);
/*  57 */     this.lock.writeLock().unlock();
/*  58 */     this.isBlank = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addObjectByDataType(int id, int type) {
/*  64 */     WatchableObject datawatcher$watchableobject = new WatchableObject(type, id, null);
/*  65 */     this.lock.writeLock().lock();
/*  66 */     this.watchedObjects.put(Integer.valueOf(id), datawatcher$watchableobject);
/*  67 */     this.lock.writeLock().unlock();
/*  68 */     this.isBlank = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getWatchableObjectByte(int id) {
/*  73 */     return ((Byte)getWatchedObject(id).getObject()).byteValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public short getWatchableObjectShort(int id) {
/*  78 */     return ((Short)getWatchedObject(id).getObject()).shortValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWatchableObjectInt(int id) {
/*  83 */     return ((Integer)getWatchedObject(id).getObject()).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWatchableObjectFloat(int id) {
/*  88 */     return ((Float)getWatchedObject(id).getObject()).floatValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getWatchableObjectString(int id) {
/*  93 */     return (String)getWatchedObject(id).getObject();
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getWatchableObjectItemStack(int id) {
/*  98 */     return (ItemStack)getWatchedObject(id).getObject();
/*     */   }
/*     */   
/*     */   private WatchableObject getWatchedObject(int id) {
/*     */     WatchableObject datawatcher$watchableobject;
/* 103 */     this.lock.readLock().lock();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 108 */       datawatcher$watchableobject = this.watchedObjects.get(Integer.valueOf(id));
/*     */     }
/* 110 */     catch (Throwable throwable) {
/*     */       
/* 112 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting synched entity data");
/* 113 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Synched entity data");
/* 114 */       crashreportcategory.addCrashSection("Data ID", Integer.valueOf(id));
/* 115 */       throw new ReportedException(crashreport);
/*     */     } 
/*     */     
/* 118 */     this.lock.readLock().unlock();
/* 119 */     return datawatcher$watchableobject;
/*     */   }
/*     */ 
/*     */   
/*     */   public Rotations getWatchableObjectRotations(int id) {
/* 124 */     return (Rotations)getWatchedObject(id).getObject();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> void updateObject(int id, T newData) {
/* 129 */     WatchableObject datawatcher$watchableobject = getWatchedObject(id);
/*     */     
/* 131 */     if (ObjectUtils.notEqual(newData, datawatcher$watchableobject.getObject())) {
/*     */       
/* 133 */       datawatcher$watchableobject.setObject(newData);
/* 134 */       this.owner.onDataWatcherUpdate(id);
/* 135 */       datawatcher$watchableobject.setWatched(true);
/* 136 */       this.objectChanged = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setObjectWatched(int id) {
/* 142 */     (getWatchedObject(id)).watched = true;
/* 143 */     this.objectChanged = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasObjectChanged() {
/* 148 */     return this.objectChanged;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void writeWatchedListToPacketBuffer(List<WatchableObject> objectsList, PacketBuffer buffer) throws IOException {
/* 153 */     if (objectsList != null)
/*     */     {
/* 155 */       for (WatchableObject datawatcher$watchableobject : objectsList)
/*     */       {
/* 157 */         writeWatchableObjectToPacketBuffer(buffer, datawatcher$watchableobject);
/*     */       }
/*     */     }
/*     */     
/* 161 */     buffer.writeByte(127);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<WatchableObject> getChanged() {
/* 166 */     List<WatchableObject> list = null;
/*     */     
/* 168 */     if (this.objectChanged) {
/*     */       
/* 170 */       this.lock.readLock().lock();
/*     */       
/* 172 */       for (WatchableObject datawatcher$watchableobject : this.watchedObjects.values()) {
/*     */         
/* 174 */         if (datawatcher$watchableobject.isWatched()) {
/*     */           
/* 176 */           datawatcher$watchableobject.setWatched(false);
/*     */           
/* 178 */           if (list == null)
/*     */           {
/* 180 */             list = Lists.newArrayList();
/*     */           }
/*     */           
/* 183 */           list.add(datawatcher$watchableobject);
/*     */         } 
/*     */       } 
/*     */       
/* 187 */       this.lock.readLock().unlock();
/*     */     } 
/*     */     
/* 190 */     this.objectChanged = false;
/* 191 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(PacketBuffer buffer) throws IOException {
/* 196 */     this.lock.readLock().lock();
/*     */     
/* 198 */     for (WatchableObject datawatcher$watchableobject : this.watchedObjects.values())
/*     */     {
/* 200 */       writeWatchableObjectToPacketBuffer(buffer, datawatcher$watchableobject);
/*     */     }
/*     */     
/* 203 */     this.lock.readLock().unlock();
/* 204 */     buffer.writeByte(127);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<WatchableObject> getAllWatched() {
/* 209 */     List<WatchableObject> list = null;
/* 210 */     this.lock.readLock().lock();
/*     */     
/* 212 */     for (WatchableObject datawatcher$watchableobject : this.watchedObjects.values()) {
/*     */       
/* 214 */       if (list == null)
/*     */       {
/* 216 */         list = Lists.newArrayList();
/*     */       }
/*     */       
/* 219 */       list.add(datawatcher$watchableobject);
/*     */     } 
/*     */     
/* 222 */     this.lock.readLock().unlock();
/* 223 */     return list;
/*     */   } private static void writeWatchableObjectToPacketBuffer(PacketBuffer buffer, WatchableObject object) throws IOException {
/*     */     ItemStack itemstack;
/*     */     BlockPos blockpos;
/*     */     Rotations rotations;
/* 228 */     int i = (object.getObjectType() << 5 | object.getDataValueId() & 0x1F) & 0xFF;
/* 229 */     buffer.writeByte(i);
/*     */     
/* 231 */     switch (object.getObjectType()) {
/*     */       
/*     */       case 0:
/* 234 */         buffer.writeByte(((Byte)object.getObject()).byteValue());
/*     */         break;
/*     */       
/*     */       case 1:
/* 238 */         buffer.writeShort(((Short)object.getObject()).shortValue());
/*     */         break;
/*     */       
/*     */       case 2:
/* 242 */         buffer.writeInt(((Integer)object.getObject()).intValue());
/*     */         break;
/*     */       
/*     */       case 3:
/* 246 */         buffer.writeFloat(((Float)object.getObject()).floatValue());
/*     */         break;
/*     */       
/*     */       case 4:
/* 250 */         buffer.writeString((String)object.getObject());
/*     */         break;
/*     */       
/*     */       case 5:
/* 254 */         itemstack = (ItemStack)object.getObject();
/* 255 */         buffer.writeItemStackToBuffer(itemstack);
/*     */         break;
/*     */       
/*     */       case 6:
/* 259 */         blockpos = (BlockPos)object.getObject();
/* 260 */         buffer.writeInt(blockpos.getX());
/* 261 */         buffer.writeInt(blockpos.getY());
/* 262 */         buffer.writeInt(blockpos.getZ());
/*     */         break;
/*     */       
/*     */       case 7:
/* 266 */         rotations = (Rotations)object.getObject();
/* 267 */         buffer.writeFloat(rotations.getX());
/* 268 */         buffer.writeFloat(rotations.getY());
/* 269 */         buffer.writeFloat(rotations.getZ());
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static List<WatchableObject> readWatchedListFromPacketBuffer(PacketBuffer buffer) throws IOException {
/* 275 */     List<WatchableObject> list = null;
/*     */     
/* 277 */     for (int i = buffer.readByte(); i != 127; i = buffer.readByte()) {
/*     */       int l, i1, j1; float f, f1, f2;
/* 279 */       if (list == null)
/*     */       {
/* 281 */         list = Lists.newArrayList();
/*     */       }
/*     */       
/* 284 */       int j = (i & 0xE0) >> 5;
/* 285 */       int k = i & 0x1F;
/* 286 */       WatchableObject datawatcher$watchableobject = null;
/*     */       
/* 288 */       switch (j) {
/*     */         
/*     */         case 0:
/* 291 */           datawatcher$watchableobject = new WatchableObject(j, k, Byte.valueOf(buffer.readByte()));
/*     */           break;
/*     */         
/*     */         case 1:
/* 295 */           datawatcher$watchableobject = new WatchableObject(j, k, Short.valueOf(buffer.readShort()));
/*     */           break;
/*     */         
/*     */         case 2:
/* 299 */           datawatcher$watchableobject = new WatchableObject(j, k, Integer.valueOf(buffer.readInt()));
/*     */           break;
/*     */         
/*     */         case 3:
/* 303 */           datawatcher$watchableobject = new WatchableObject(j, k, Float.valueOf(buffer.readFloat()));
/*     */           break;
/*     */         
/*     */         case 4:
/* 307 */           datawatcher$watchableobject = new WatchableObject(j, k, buffer.readStringFromBuffer(32767));
/*     */           break;
/*     */         
/*     */         case 5:
/* 311 */           datawatcher$watchableobject = new WatchableObject(j, k, buffer.readItemStackFromBuffer());
/*     */           break;
/*     */         
/*     */         case 6:
/* 315 */           l = buffer.readInt();
/* 316 */           i1 = buffer.readInt();
/* 317 */           j1 = buffer.readInt();
/* 318 */           datawatcher$watchableobject = new WatchableObject(j, k, new BlockPos(l, i1, j1));
/*     */           break;
/*     */         
/*     */         case 7:
/* 322 */           f = buffer.readFloat();
/* 323 */           f1 = buffer.readFloat();
/* 324 */           f2 = buffer.readFloat();
/* 325 */           datawatcher$watchableobject = new WatchableObject(j, k, new Rotations(f, f1, f2));
/*     */           break;
/*     */       } 
/* 328 */       list.add(datawatcher$watchableobject);
/*     */     } 
/*     */     
/* 331 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateWatchedObjectsFromList(List<WatchableObject> p_75687_1_) {
/* 336 */     this.lock.writeLock().lock();
/*     */     
/* 338 */     for (WatchableObject datawatcher$watchableobject : p_75687_1_) {
/*     */       
/* 340 */       WatchableObject datawatcher$watchableobject1 = this.watchedObjects.get(Integer.valueOf(datawatcher$watchableobject.getDataValueId()));
/*     */       
/* 342 */       if (datawatcher$watchableobject1 != null) {
/*     */         
/* 344 */         datawatcher$watchableobject1.setObject(datawatcher$watchableobject.getObject());
/* 345 */         this.owner.onDataWatcherUpdate(datawatcher$watchableobject.getDataValueId());
/*     */       } 
/*     */     } 
/*     */     
/* 349 */     this.lock.writeLock().unlock();
/* 350 */     this.objectChanged = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getIsBlank() {
/* 355 */     return this.isBlank;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_111144_e() {
/* 360 */     this.objectChanged = false;
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 365 */     dataTypes.put(Byte.class, Integer.valueOf(0));
/* 366 */     dataTypes.put(Short.class, Integer.valueOf(1));
/* 367 */     dataTypes.put(Integer.class, Integer.valueOf(2));
/* 368 */     dataTypes.put(Float.class, Integer.valueOf(3));
/* 369 */     dataTypes.put(String.class, Integer.valueOf(4));
/* 370 */     dataTypes.put(ItemStack.class, Integer.valueOf(5));
/* 371 */     dataTypes.put(BlockPos.class, Integer.valueOf(6));
/* 372 */     dataTypes.put(Rotations.class, Integer.valueOf(7));
/*     */   }
/*     */ 
/*     */   
/*     */   public static class WatchableObject
/*     */   {
/*     */     private final int objectType;
/*     */     private final int dataValueId;
/*     */     private Object watchedObject;
/*     */     private boolean watched;
/*     */     
/*     */     public WatchableObject(int type, int id, Object object) {
/* 384 */       this.dataValueId = id;
/* 385 */       this.watchedObject = object;
/* 386 */       this.objectType = type;
/* 387 */       this.watched = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getDataValueId() {
/* 392 */       return this.dataValueId;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setObject(Object object) {
/* 397 */       this.watchedObject = object;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getObject() {
/* 402 */       return this.watchedObject;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getObjectType() {
/* 407 */       return this.objectType;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWatched() {
/* 412 */       return this.watched;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setWatched(boolean watched) {
/* 417 */       this.watched = watched;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\DataWatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
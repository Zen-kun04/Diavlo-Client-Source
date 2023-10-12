/*     */ package com.viaversion.viaversion.api.minecraft.chunks;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.IntArrayList;
/*     */ import com.viaversion.viaversion.libs.fastutil.ints.IntList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DataPaletteImpl
/*     */   implements DataPalette
/*     */ {
/*     */   private static final int DEFAULT_INITIAL_SIZE = 16;
/*     */   private final IntList palette;
/*     */   private final Int2IntMap inversePalette;
/*     */   private final int sizeBits;
/*     */   private ChunkData values;
/*     */   
/*     */   public DataPaletteImpl(int valuesLength) {
/*  40 */     this(valuesLength, 16);
/*     */   }
/*     */   
/*     */   public DataPaletteImpl(int valuesLength, int initialSize) {
/*  44 */     this.values = new EmptyChunkData(valuesLength);
/*  45 */     this.sizeBits = Integer.numberOfTrailingZeros(valuesLength) / 3;
/*     */     
/*  47 */     this.palette = (IntList)new IntArrayList(initialSize);
/*     */     
/*  49 */     this.inversePalette = (Int2IntMap)new Int2IntOpenHashMap((int)(initialSize * 0.75F));
/*  50 */     this.inversePalette.defaultReturnValue(-1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int index(int x, int y, int z) {
/*  55 */     return (y << this.sizeBits | z) << this.sizeBits | x;
/*     */   }
/*     */ 
/*     */   
/*     */   public int idAt(int sectionCoordinate) {
/*  60 */     int index = this.values.get(sectionCoordinate);
/*  61 */     return this.palette.getInt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIdAt(int sectionCoordinate, int id) {
/*  66 */     int index = this.inversePalette.get(id);
/*  67 */     if (index == -1) {
/*  68 */       index = this.palette.size();
/*  69 */       this.palette.add(id);
/*  70 */       this.inversePalette.put(id, index);
/*     */     } 
/*     */     
/*  73 */     this.values.set(sectionCoordinate, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int paletteIndexAt(int packedCoordinate) {
/*  78 */     return this.values.get(packedCoordinate);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPaletteIndexAt(int sectionCoordinate, int index) {
/*  83 */     this.values.set(sectionCoordinate, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  88 */     return this.palette.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public int idByIndex(int index) {
/*  93 */     return this.palette.getInt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIdByIndex(int index, int id) {
/*  98 */     int oldId = this.palette.set(index, id);
/*  99 */     if (oldId == id)
/*     */       return; 
/* 101 */     this.inversePalette.put(id, index);
/* 102 */     if (this.inversePalette.get(oldId) == index) {
/* 103 */       this.inversePalette.remove(oldId);
/* 104 */       for (int i = 0; i < this.palette.size(); i++) {
/* 105 */         if (this.palette.getInt(i) == oldId) {
/* 106 */           this.inversePalette.put(oldId, i);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void replaceId(int oldId, int newId) {
/* 115 */     int index = this.inversePalette.remove(oldId);
/* 116 */     if (index == -1)
/*     */       return; 
/* 118 */     this.inversePalette.put(newId, index);
/* 119 */     for (int i = 0; i < this.palette.size(); i++) {
/* 120 */       if (this.palette.getInt(i) == oldId) {
/* 121 */         this.palette.set(i, newId);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addId(int id) {
/* 128 */     this.inversePalette.put(id, this.palette.size());
/* 129 */     this.palette.add(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 134 */     this.palette.clear();
/* 135 */     this.inversePalette.clear();
/*     */   }
/*     */   
/*     */   static interface ChunkData {
/*     */     int get(int param1Int);
/*     */     
/*     */     void set(int param1Int1, int param1Int2);
/*     */   }
/*     */   
/*     */   private class EmptyChunkData implements ChunkData {
/*     */     private final int size;
/*     */     
/*     */     public EmptyChunkData(int size) {
/* 148 */       this.size = size;
/*     */     }
/*     */ 
/*     */     
/*     */     public int get(int idx) {
/* 153 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(int idx, int val) {
/* 158 */       if (val != 0) {
/* 159 */         DataPaletteImpl.this.values = new DataPaletteImpl.ByteChunkData(this.size);
/* 160 */         DataPaletteImpl.this.values.set(idx, val);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class ByteChunkData implements ChunkData {
/*     */     private final byte[] data;
/*     */     
/*     */     public ByteChunkData(int size) {
/* 169 */       this.data = new byte[size];
/*     */     }
/*     */ 
/*     */     
/*     */     public int get(int idx) {
/* 174 */       return this.data[idx] & 0xFF;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void set(int idx, int val) {
/* 180 */       if (val > 255) {
/* 181 */         DataPaletteImpl.this.values = new DataPaletteImpl.ShortChunkData(this.data);
/* 182 */         DataPaletteImpl.this.values.set(idx, val);
/*     */         return;
/*     */       } 
/* 185 */       this.data[idx] = (byte)val;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ShortChunkData implements ChunkData {
/*     */     private final short[] data;
/*     */     
/*     */     public ShortChunkData(byte[] data) {
/* 193 */       this.data = new short[data.length];
/* 194 */       for (int i = 0; i < data.length; i++) {
/* 195 */         this.data[i] = (short)(data[i] & 0xFF);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public int get(int idx) {
/* 201 */       return this.data[idx];
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(int idx, int val) {
/* 206 */       this.data[idx] = (short)val;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\minecraft\chunks\DataPaletteImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
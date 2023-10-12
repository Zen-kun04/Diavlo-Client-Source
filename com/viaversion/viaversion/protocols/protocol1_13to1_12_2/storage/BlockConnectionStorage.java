/*     */ package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage;
/*     */ 
/*     */ import com.google.common.collect.EvictingQueue;
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.connection.StorableObject;
/*     */ import com.viaversion.viaversion.api.minecraft.Position;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
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
/*     */ public class BlockConnectionStorage
/*     */   implements StorableObject
/*     */ {
/*     */   private static Constructor<?> fastUtilLongObjectHashMap;
/*  36 */   private final Map<Long, SectionData> blockStorage = createLongObjectMap();
/*     */   
/*  38 */   private final Queue<Position> modified = (Queue<Position>)EvictingQueue.create(5);
/*     */ 
/*     */   
/*  41 */   private long lastIndex = -1L;
/*     */   
/*     */   private SectionData lastSection;
/*     */   
/*     */   static {
/*     */     try {
/*  47 */       String className = "it" + ".unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap";
/*  48 */       fastUtilLongObjectHashMap = Class.forName(className).getConstructor(new Class[0]);
/*  49 */       Via.getPlatform().getLogger().info("Using FastUtil Long2ObjectOpenHashMap for block connections");
/*  50 */     } catch (ClassNotFoundException|NoSuchMethodException classNotFoundException) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public static void init() {}
/*     */ 
/*     */   
/*     */   public void store(int x, int y, int z, int blockState) {
/*  58 */     long index = getChunkSectionIndex(x, y, z);
/*  59 */     SectionData section = getSection(index);
/*  60 */     if (section == null) {
/*  61 */       if (blockState == 0) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*  66 */       this.blockStorage.put(Long.valueOf(index), section = new SectionData());
/*  67 */       this.lastSection = section;
/*  68 */       this.lastIndex = index;
/*     */     } 
/*     */     
/*  71 */     section.setBlockAt(x, y, z, blockState);
/*     */   }
/*     */   
/*     */   public int get(int x, int y, int z) {
/*  75 */     long pair = getChunkSectionIndex(x, y, z);
/*  76 */     SectionData section = getSection(pair);
/*  77 */     if (section == null) {
/*  78 */       return 0;
/*     */     }
/*     */     
/*  81 */     return section.blockAt(x, y, z);
/*     */   }
/*     */   
/*     */   public void remove(int x, int y, int z) {
/*  85 */     long index = getChunkSectionIndex(x, y, z);
/*  86 */     SectionData section = getSection(index);
/*  87 */     if (section == null) {
/*     */       return;
/*     */     }
/*     */     
/*  91 */     section.setBlockAt(x, y, z, 0);
/*     */     
/*  93 */     if (section.nonEmptyBlocks() == 0) {
/*  94 */       removeSection(index);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void markModified(Position pos) {
/* 100 */     if (!this.modified.contains(pos)) {
/* 101 */       this.modified.add(pos);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean recentlyModified(Position pos) {
/* 106 */     for (Position p : this.modified) {
/* 107 */       if (Math.abs(pos.x() - p.x()) + Math.abs(pos.y() - p.y()) + Math.abs(pos.z() - p.z()) <= 2) {
/* 108 */         return true;
/*     */       }
/*     */     } 
/* 111 */     return false;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 115 */     this.blockStorage.clear();
/* 116 */     this.lastSection = null;
/* 117 */     this.lastIndex = -1L;
/* 118 */     this.modified.clear();
/*     */   }
/*     */   
/*     */   public void unloadChunk(int x, int z) {
/* 122 */     for (int y = 0; y < 16; y++) {
/* 123 */       unloadSection(x, y, z);
/*     */     }
/*     */   }
/*     */   
/*     */   public void unloadSection(int x, int y, int z) {
/* 128 */     removeSection(getChunkSectionIndex(x << 4, y << 4, z << 4));
/*     */   }
/*     */   
/*     */   private SectionData getSection(long index) {
/* 132 */     if (this.lastIndex == index) {
/* 133 */       return this.lastSection;
/*     */     }
/* 135 */     this.lastIndex = index;
/* 136 */     return this.lastSection = this.blockStorage.get(Long.valueOf(index));
/*     */   }
/*     */   
/*     */   private void removeSection(long index) {
/* 140 */     this.blockStorage.remove(Long.valueOf(index));
/* 141 */     if (this.lastIndex == index) {
/* 142 */       this.lastIndex = -1L;
/* 143 */       this.lastSection = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static long getChunkSectionIndex(int x, int y, int z) {
/* 148 */     return ((x >> 4) & 0x3FFFFFFL) << 38L | ((y >> 4) & 0xFFFL) << 26L | (z >> 4) & 0x3FFFFFFL;
/*     */   }
/*     */   
/*     */   private <T> Map<Long, T> createLongObjectMap() {
/* 152 */     if (fastUtilLongObjectHashMap != null) {
/*     */       
/*     */       try {
/* 155 */         return (Map<Long, T>)fastUtilLongObjectHashMap.newInstance(new Object[0]);
/* 156 */       } catch (IllegalAccessException|InstantiationException|java.lang.reflect.InvocationTargetException e) {
/* 157 */         e.printStackTrace();
/*     */       } 
/*     */     }
/* 160 */     return new HashMap<>();
/*     */   }
/*     */   
/*     */   private static final class SectionData {
/* 164 */     private final short[] blockStates = new short[4096];
/*     */     private short nonEmptyBlocks;
/*     */     
/*     */     public int blockAt(int x, int y, int z) {
/* 168 */       return this.blockStates[encodeBlockPos(x, y, z)];
/*     */     }
/*     */     
/*     */     public void setBlockAt(int x, int y, int z, int blockState) {
/* 172 */       int index = encodeBlockPos(x, y, z);
/* 173 */       if (blockState == this.blockStates[index]) {
/*     */         return;
/*     */       }
/*     */       
/* 177 */       this.blockStates[index] = (short)blockState;
/* 178 */       if (blockState == 0) {
/* 179 */         this.nonEmptyBlocks = (short)(this.nonEmptyBlocks - 1);
/*     */       } else {
/* 181 */         this.nonEmptyBlocks = (short)(this.nonEmptyBlocks + 1);
/*     */       } 
/*     */     }
/*     */     
/*     */     public short nonEmptyBlocks() {
/* 186 */       return this.nonEmptyBlocks;
/*     */     }
/*     */     
/*     */     private static int encodeBlockPos(int x, int y, int z) {
/* 190 */       return (y & 0xF) << 8 | (x & 0xF) << 4 | z & 0xF;
/*     */     }
/*     */     
/*     */     private SectionData() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\protocols\protocol1_13to1_12_2\storage\BlockConnectionStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
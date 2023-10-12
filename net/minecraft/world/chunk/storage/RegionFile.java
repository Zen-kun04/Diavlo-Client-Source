/*     */ package net.minecraft.world.chunk.storage;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.util.List;
/*     */ import java.util.zip.DeflaterOutputStream;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.InflaterInputStream;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ 
/*     */ public class RegionFile
/*     */ {
/*  20 */   private static final byte[] emptySector = new byte[4096];
/*     */   private final File fileName;
/*     */   private RandomAccessFile dataFile;
/*  23 */   private final int[] offsets = new int[1024];
/*  24 */   private final int[] chunkTimestamps = new int[1024];
/*     */   
/*     */   private List<Boolean> sectorFree;
/*     */   private int sizeDelta;
/*     */   private long lastModified;
/*     */   
/*     */   public RegionFile(File fileNameIn) {
/*  31 */     this.fileName = fileNameIn;
/*  32 */     this.sizeDelta = 0;
/*     */ 
/*     */     
/*     */     try {
/*  36 */       if (fileNameIn.exists())
/*     */       {
/*  38 */         this.lastModified = fileNameIn.lastModified();
/*     */       }
/*     */       
/*  41 */       this.dataFile = new RandomAccessFile(fileNameIn, "rw");
/*     */       
/*  43 */       if (this.dataFile.length() < 4096L) {
/*     */         
/*  45 */         for (int i = 0; i < 1024; i++)
/*     */         {
/*  47 */           this.dataFile.writeInt(0);
/*     */         }
/*     */         
/*  50 */         for (int i1 = 0; i1 < 1024; i1++)
/*     */         {
/*  52 */           this.dataFile.writeInt(0);
/*     */         }
/*     */         
/*  55 */         this.sizeDelta += 8192;
/*     */       } 
/*     */       
/*  58 */       if ((this.dataFile.length() & 0xFFFL) != 0L)
/*     */       {
/*  60 */         for (int j1 = 0; j1 < (this.dataFile.length() & 0xFFFL); j1++)
/*     */         {
/*  62 */           this.dataFile.write(0);
/*     */         }
/*     */       }
/*     */       
/*  66 */       int k1 = (int)this.dataFile.length() / 4096;
/*  67 */       this.sectorFree = Lists.newArrayListWithCapacity(k1);
/*     */       
/*  69 */       for (int j = 0; j < k1; j++)
/*     */       {
/*  71 */         this.sectorFree.add(Boolean.valueOf(true));
/*     */       }
/*     */       
/*  74 */       this.sectorFree.set(0, Boolean.valueOf(false));
/*  75 */       this.sectorFree.set(1, Boolean.valueOf(false));
/*  76 */       this.dataFile.seek(0L);
/*     */       
/*  78 */       for (int l1 = 0; l1 < 1024; l1++) {
/*     */         
/*  80 */         int k = this.dataFile.readInt();
/*  81 */         this.offsets[l1] = k;
/*     */         
/*  83 */         if (k != 0 && (k >> 8) + (k & 0xFF) <= this.sectorFree.size())
/*     */         {
/*  85 */           for (int l = 0; l < (k & 0xFF); l++)
/*     */           {
/*  87 */             this.sectorFree.set((k >> 8) + l, Boolean.valueOf(false));
/*     */           }
/*     */         }
/*     */       } 
/*     */       
/*  92 */       for (int i2 = 0; i2 < 1024; i2++)
/*     */       {
/*  94 */         int j2 = this.dataFile.readInt();
/*  95 */         this.chunkTimestamps[i2] = j2;
/*     */       }
/*     */     
/*  98 */     } catch (IOException ioexception) {
/*     */       
/* 100 */       ioexception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized DataInputStream getChunkDataInputStream(int x, int z) {
/* 106 */     if (outOfBounds(x, z))
/*     */     {
/* 108 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 114 */       int i = getOffset(x, z);
/*     */       
/* 116 */       if (i == 0)
/*     */       {
/* 118 */         return null;
/*     */       }
/*     */ 
/*     */       
/* 122 */       int j = i >> 8;
/* 123 */       int k = i & 0xFF;
/*     */       
/* 125 */       if (j + k > this.sectorFree.size())
/*     */       {
/* 127 */         return null;
/*     */       }
/*     */ 
/*     */       
/* 131 */       this.dataFile.seek((j * 4096));
/* 132 */       int l = this.dataFile.readInt();
/*     */       
/* 134 */       if (l > 4096 * k)
/*     */       {
/* 136 */         return null;
/*     */       }
/* 138 */       if (l <= 0)
/*     */       {
/* 140 */         return null;
/*     */       }
/*     */ 
/*     */       
/* 144 */       byte b0 = this.dataFile.readByte();
/*     */       
/* 146 */       if (b0 == 1) {
/*     */         
/* 148 */         byte[] abyte1 = new byte[l - 1];
/* 149 */         this.dataFile.read(abyte1);
/* 150 */         return new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(abyte1))));
/*     */       } 
/* 152 */       if (b0 == 2) {
/*     */         
/* 154 */         byte[] abyte = new byte[l - 1];
/* 155 */         this.dataFile.read(abyte);
/* 156 */         return new DataInputStream(new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(abyte))));
/*     */       } 
/*     */ 
/*     */       
/* 160 */       return null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 166 */     catch (IOException var9) {
/*     */       
/* 168 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DataOutputStream getChunkDataOutputStream(int x, int z) {
/* 175 */     return outOfBounds(x, z) ? null : new DataOutputStream(new DeflaterOutputStream(new ChunkBuffer(x, z)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void write(int x, int z, byte[] data, int length) {
/*     */     try {
/* 182 */       int i = getOffset(x, z);
/* 183 */       int j = i >> 8;
/* 184 */       int k = i & 0xFF;
/* 185 */       int l = (length + 5) / 4096 + 1;
/*     */       
/* 187 */       if (l >= 256) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 192 */       if (j != 0 && k == l) {
/*     */         
/* 194 */         write(j, data, length);
/*     */       }
/*     */       else {
/*     */         
/* 198 */         for (int i1 = 0; i1 < k; i1++)
/*     */         {
/* 200 */           this.sectorFree.set(j + i1, Boolean.valueOf(true));
/*     */         }
/*     */         
/* 203 */         int l1 = this.sectorFree.indexOf(Boolean.valueOf(true));
/* 204 */         int j1 = 0;
/*     */         
/* 206 */         if (l1 != -1)
/*     */         {
/* 208 */           for (int k1 = l1; k1 < this.sectorFree.size(); k1++) {
/*     */             
/* 210 */             if (j1 != 0) {
/*     */               
/* 212 */               if (((Boolean)this.sectorFree.get(k1)).booleanValue())
/*     */               {
/* 214 */                 j1++;
/*     */               }
/*     */               else
/*     */               {
/* 218 */                 j1 = 0;
/*     */               }
/*     */             
/* 221 */             } else if (((Boolean)this.sectorFree.get(k1)).booleanValue()) {
/*     */               
/* 223 */               l1 = k1;
/* 224 */               j1 = 1;
/*     */             } 
/*     */             
/* 227 */             if (j1 >= l) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/* 234 */         if (j1 >= l) {
/*     */           
/* 236 */           j = l1;
/* 237 */           setOffset(x, z, l1 << 8 | l);
/*     */           
/* 239 */           for (int j2 = 0; j2 < l; j2++)
/*     */           {
/* 241 */             this.sectorFree.set(j + j2, Boolean.valueOf(false));
/*     */           }
/*     */           
/* 244 */           write(j, data, length);
/*     */         }
/*     */         else {
/*     */           
/* 248 */           this.dataFile.seek(this.dataFile.length());
/* 249 */           j = this.sectorFree.size();
/*     */           
/* 251 */           for (int i2 = 0; i2 < l; i2++) {
/*     */             
/* 253 */             this.dataFile.write(emptySector);
/* 254 */             this.sectorFree.add(Boolean.valueOf(false));
/*     */           } 
/*     */           
/* 257 */           this.sizeDelta += 4096 * l;
/* 258 */           write(j, data, length);
/* 259 */           setOffset(x, z, j << 8 | l);
/*     */         } 
/*     */       } 
/*     */       
/* 263 */       setChunkTimestamp(x, z, (int)(MinecraftServer.getCurrentTimeMillis() / 1000L));
/*     */     }
/* 265 */     catch (IOException ioexception) {
/*     */       
/* 267 */       ioexception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void write(int sectorNumber, byte[] data, int length) throws IOException {
/* 273 */     this.dataFile.seek((sectorNumber * 4096));
/* 274 */     this.dataFile.writeInt(length + 1);
/* 275 */     this.dataFile.writeByte(2);
/* 276 */     this.dataFile.write(data, 0, length);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean outOfBounds(int x, int z) {
/* 281 */     return (x < 0 || x >= 32 || z < 0 || z >= 32);
/*     */   }
/*     */ 
/*     */   
/*     */   private int getOffset(int x, int z) {
/* 286 */     return this.offsets[x + z * 32];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChunkSaved(int x, int z) {
/* 291 */     return (getOffset(x, z) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setOffset(int x, int z, int offset) throws IOException {
/* 296 */     this.offsets[x + z * 32] = offset;
/* 297 */     this.dataFile.seek(((x + z * 32) * 4));
/* 298 */     this.dataFile.writeInt(offset);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setChunkTimestamp(int x, int z, int timestamp) throws IOException {
/* 303 */     this.chunkTimestamps[x + z * 32] = timestamp;
/* 304 */     this.dataFile.seek((4096 + (x + z * 32) * 4));
/* 305 */     this.dataFile.writeInt(timestamp);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 310 */     if (this.dataFile != null)
/*     */     {
/* 312 */       this.dataFile.close();
/*     */     }
/*     */   }
/*     */   
/*     */   class ChunkBuffer
/*     */     extends ByteArrayOutputStream
/*     */   {
/*     */     private int chunkX;
/*     */     private int chunkZ;
/*     */     
/*     */     public ChunkBuffer(int x, int z) {
/* 323 */       super(8096);
/* 324 */       this.chunkX = x;
/* 325 */       this.chunkZ = z;
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 330 */       RegionFile.this.write(this.chunkX, this.chunkZ, this.buf, this.count);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\chunk\storage\RegionFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
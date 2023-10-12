/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.network.INetHandler;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ 
/*     */ 
/*     */ public class S26PacketMapChunkBulk
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private int[] xPositions;
/*     */   private int[] zPositions;
/*     */   private S21PacketChunkData.Extracted[] chunksData;
/*     */   private boolean isOverworld;
/*     */   
/*     */   public S26PacketMapChunkBulk() {}
/*     */   
/*     */   public S26PacketMapChunkBulk(List<Chunk> chunks) {
/*  23 */     int i = chunks.size();
/*  24 */     this.xPositions = new int[i];
/*  25 */     this.zPositions = new int[i];
/*  26 */     this.chunksData = new S21PacketChunkData.Extracted[i];
/*  27 */     this.isOverworld = !(((Chunk)chunks.get(0)).getWorld()).provider.getHasNoSky();
/*     */     
/*  29 */     for (int j = 0; j < i; j++) {
/*     */       
/*  31 */       Chunk chunk = chunks.get(j);
/*  32 */       S21PacketChunkData.Extracted s21packetchunkdata$extracted = S21PacketChunkData.getExtractedData(chunk, true, this.isOverworld, 65535);
/*  33 */       this.xPositions[j] = chunk.xPosition;
/*  34 */       this.zPositions[j] = chunk.zPosition;
/*  35 */       this.chunksData[j] = s21packetchunkdata$extracted;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readPacketData(PacketBuffer buf) throws IOException {
/*  41 */     this.isOverworld = buf.readBoolean();
/*  42 */     int i = buf.readVarIntFromBuffer();
/*  43 */     this.xPositions = new int[i];
/*  44 */     this.zPositions = new int[i];
/*  45 */     this.chunksData = new S21PacketChunkData.Extracted[i];
/*     */     
/*  47 */     for (int j = 0; j < i; j++) {
/*     */       
/*  49 */       this.xPositions[j] = buf.readInt();
/*  50 */       this.zPositions[j] = buf.readInt();
/*  51 */       this.chunksData[j] = new S21PacketChunkData.Extracted();
/*  52 */       (this.chunksData[j]).dataSize = buf.readShort() & 0xFFFF;
/*  53 */       (this.chunksData[j]).data = new byte[S21PacketChunkData.func_180737_a(Integer.bitCount((this.chunksData[j]).dataSize), this.isOverworld, true)];
/*     */     } 
/*     */     
/*  56 */     for (int k = 0; k < i; k++)
/*     */     {
/*  58 */       buf.readBytes((this.chunksData[k]).data);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void writePacketData(PacketBuffer buf) throws IOException {
/*  64 */     buf.writeBoolean(this.isOverworld);
/*  65 */     buf.writeVarIntToBuffer(this.chunksData.length);
/*     */     
/*  67 */     for (int i = 0; i < this.xPositions.length; i++) {
/*     */       
/*  69 */       buf.writeInt(this.xPositions[i]);
/*  70 */       buf.writeInt(this.zPositions[i]);
/*  71 */       buf.writeShort((short)((this.chunksData[i]).dataSize & 0xFFFF));
/*     */     } 
/*     */     
/*  74 */     for (int j = 0; j < this.xPositions.length; j++)
/*     */     {
/*  76 */       buf.writeBytes((this.chunksData[j]).data);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void processPacket(INetHandlerPlayClient handler) {
/*  82 */     handler.handleMapChunkBulk(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getChunkX(int p_149255_1_) {
/*  87 */     return this.xPositions[p_149255_1_];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getChunkZ(int p_149253_1_) {
/*  92 */     return this.zPositions[p_149253_1_];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getChunkCount() {
/*  97 */     return this.xPositions.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getChunkBytes(int p_149256_1_) {
/* 102 */     return (this.chunksData[p_149256_1_]).data;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getChunkSize(int p_179754_1_) {
/* 107 */     return (this.chunksData[p_179754_1_]).dataSize;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S26PacketMapChunkBulk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
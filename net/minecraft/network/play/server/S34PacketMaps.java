/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import net.minecraft.network.INetHandler;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.util.Vec4b;
/*     */ import net.minecraft.world.storage.MapData;
/*     */ 
/*     */ 
/*     */ public class S34PacketMaps
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private int mapId;
/*     */   private byte mapScale;
/*     */   private Vec4b[] mapVisiblePlayersVec4b;
/*     */   private int mapMinX;
/*     */   private int mapMinY;
/*     */   private int mapMaxX;
/*     */   private int mapMaxY;
/*     */   private byte[] mapDataBytes;
/*     */   
/*     */   public S34PacketMaps() {}
/*     */   
/*     */   public S34PacketMaps(int mapIdIn, byte scale, Collection<Vec4b> visiblePlayers, byte[] colors, int minX, int minY, int maxX, int maxY) {
/*  28 */     this.mapId = mapIdIn;
/*  29 */     this.mapScale = scale;
/*  30 */     this.mapVisiblePlayersVec4b = visiblePlayers.<Vec4b>toArray(new Vec4b[visiblePlayers.size()]);
/*  31 */     this.mapMinX = minX;
/*  32 */     this.mapMinY = minY;
/*  33 */     this.mapMaxX = maxX;
/*  34 */     this.mapMaxY = maxY;
/*  35 */     this.mapDataBytes = new byte[maxX * maxY];
/*     */     
/*  37 */     for (int i = 0; i < maxX; i++) {
/*     */       
/*  39 */       for (int j = 0; j < maxY; j++)
/*     */       {
/*  41 */         this.mapDataBytes[i + j * maxX] = colors[minX + i + (minY + j) * 128];
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readPacketData(PacketBuffer buf) throws IOException {
/*  48 */     this.mapId = buf.readVarIntFromBuffer();
/*  49 */     this.mapScale = buf.readByte();
/*  50 */     this.mapVisiblePlayersVec4b = new Vec4b[buf.readVarIntFromBuffer()];
/*     */     
/*  52 */     for (int i = 0; i < this.mapVisiblePlayersVec4b.length; i++) {
/*     */       
/*  54 */       short short1 = (short)buf.readByte();
/*  55 */       this.mapVisiblePlayersVec4b[i] = new Vec4b((byte)(short1 >> 4 & 0xF), buf.readByte(), buf.readByte(), (byte)(short1 & 0xF));
/*     */     } 
/*     */     
/*  58 */     this.mapMaxX = buf.readUnsignedByte();
/*     */     
/*  60 */     if (this.mapMaxX > 0) {
/*     */       
/*  62 */       this.mapMaxY = buf.readUnsignedByte();
/*  63 */       this.mapMinX = buf.readUnsignedByte();
/*  64 */       this.mapMinY = buf.readUnsignedByte();
/*  65 */       this.mapDataBytes = buf.readByteArray();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writePacketData(PacketBuffer buf) throws IOException {
/*  71 */     buf.writeVarIntToBuffer(this.mapId);
/*  72 */     buf.writeByte(this.mapScale);
/*  73 */     buf.writeVarIntToBuffer(this.mapVisiblePlayersVec4b.length);
/*     */     
/*  75 */     for (Vec4b vec4b : this.mapVisiblePlayersVec4b) {
/*     */       
/*  77 */       buf.writeByte((vec4b.func_176110_a() & 0xF) << 4 | vec4b.func_176111_d() & 0xF);
/*  78 */       buf.writeByte(vec4b.func_176112_b());
/*  79 */       buf.writeByte(vec4b.func_176113_c());
/*     */     } 
/*     */     
/*  82 */     buf.writeByte(this.mapMaxX);
/*     */     
/*  84 */     if (this.mapMaxX > 0) {
/*     */       
/*  86 */       buf.writeByte(this.mapMaxY);
/*  87 */       buf.writeByte(this.mapMinX);
/*  88 */       buf.writeByte(this.mapMinY);
/*  89 */       buf.writeByteArray(this.mapDataBytes);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void processPacket(INetHandlerPlayClient handler) {
/*  95 */     handler.handleMaps(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMapId() {
/* 100 */     return this.mapId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMapdataTo(MapData mapdataIn) {
/* 105 */     mapdataIn.scale = this.mapScale;
/* 106 */     mapdataIn.mapDecorations.clear();
/*     */     
/* 108 */     for (int i = 0; i < this.mapVisiblePlayersVec4b.length; i++) {
/*     */       
/* 110 */       Vec4b vec4b = this.mapVisiblePlayersVec4b[i];
/* 111 */       mapdataIn.mapDecorations.put("icon-" + i, vec4b);
/*     */     } 
/*     */     
/* 114 */     for (int j = 0; j < this.mapMaxX; j++) {
/*     */       
/* 116 */       for (int k = 0; k < this.mapMaxY; k++)
/*     */       {
/* 118 */         mapdataIn.colors[this.mapMinX + j + (this.mapMinY + k) * 128] = this.mapDataBytes[j + k * this.mapMaxX];
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S34PacketMaps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
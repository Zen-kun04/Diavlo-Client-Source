/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.network.INetHandler;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ 
/*     */ public class S27PacketExplosion
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private double posX;
/*     */   private double posY;
/*     */   private double posZ;
/*     */   private float strength;
/*     */   private List<BlockPos> affectedBlockPositions;
/*     */   private float field_149152_f;
/*     */   private float field_149153_g;
/*     */   private float field_149159_h;
/*     */   
/*     */   public S27PacketExplosion() {}
/*     */   
/*     */   public S27PacketExplosion(double p_i45193_1_, double y, double z, float strengthIn, List<BlockPos> affectedBlocksIn, Vec3 p_i45193_9_) {
/*  29 */     this.posX = p_i45193_1_;
/*  30 */     this.posY = y;
/*  31 */     this.posZ = z;
/*  32 */     this.strength = strengthIn;
/*  33 */     this.affectedBlockPositions = Lists.newArrayList(affectedBlocksIn);
/*     */     
/*  35 */     if (p_i45193_9_ != null) {
/*     */       
/*  37 */       this.field_149152_f = (float)p_i45193_9_.xCoord;
/*  38 */       this.field_149153_g = (float)p_i45193_9_.yCoord;
/*  39 */       this.field_149159_h = (float)p_i45193_9_.zCoord;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readPacketData(PacketBuffer buf) throws IOException {
/*  45 */     this.posX = buf.readFloat();
/*  46 */     this.posY = buf.readFloat();
/*  47 */     this.posZ = buf.readFloat();
/*  48 */     this.strength = buf.readFloat();
/*  49 */     int i = buf.readInt();
/*  50 */     this.affectedBlockPositions = Lists.newArrayListWithCapacity(i);
/*  51 */     int j = (int)this.posX;
/*  52 */     int k = (int)this.posY;
/*  53 */     int l = (int)this.posZ;
/*     */     
/*  55 */     for (int i1 = 0; i1 < i; i1++) {
/*     */       
/*  57 */       int j1 = buf.readByte() + j;
/*  58 */       int k1 = buf.readByte() + k;
/*  59 */       int l1 = buf.readByte() + l;
/*  60 */       this.affectedBlockPositions.add(new BlockPos(j1, k1, l1));
/*     */     } 
/*     */     
/*  63 */     this.field_149152_f = buf.readFloat();
/*  64 */     this.field_149153_g = buf.readFloat();
/*  65 */     this.field_149159_h = buf.readFloat();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writePacketData(PacketBuffer buf) throws IOException {
/*  70 */     buf.writeFloat((float)this.posX);
/*  71 */     buf.writeFloat((float)this.posY);
/*  72 */     buf.writeFloat((float)this.posZ);
/*  73 */     buf.writeFloat(this.strength);
/*  74 */     buf.writeInt(this.affectedBlockPositions.size());
/*  75 */     int i = (int)this.posX;
/*  76 */     int j = (int)this.posY;
/*  77 */     int k = (int)this.posZ;
/*     */     
/*  79 */     for (BlockPos blockpos : this.affectedBlockPositions) {
/*     */       
/*  81 */       int l = blockpos.getX() - i;
/*  82 */       int i1 = blockpos.getY() - j;
/*  83 */       int j1 = blockpos.getZ() - k;
/*  84 */       buf.writeByte(l);
/*  85 */       buf.writeByte(i1);
/*  86 */       buf.writeByte(j1);
/*     */     } 
/*     */     
/*  89 */     buf.writeFloat(this.field_149152_f);
/*  90 */     buf.writeFloat(this.field_149153_g);
/*  91 */     buf.writeFloat(this.field_149159_h);
/*     */   }
/*     */ 
/*     */   
/*     */   public void processPacket(INetHandlerPlayClient handler) {
/*  96 */     handler.handleExplosion(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public float func_149149_c() {
/* 101 */     return this.field_149152_f;
/*     */   }
/*     */ 
/*     */   
/*     */   public float func_149144_d() {
/* 106 */     return this.field_149153_g;
/*     */   }
/*     */ 
/*     */   
/*     */   public float func_149147_e() {
/* 111 */     return this.field_149159_h;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getX() {
/* 116 */     return this.posX;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getY() {
/* 121 */     return this.posY;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getZ() {
/* 126 */     return this.posZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getStrength() {
/* 131 */     return this.strength;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BlockPos> getAffectedBlockPositions() {
/* 136 */     return this.affectedBlockPositions;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S27PacketExplosion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
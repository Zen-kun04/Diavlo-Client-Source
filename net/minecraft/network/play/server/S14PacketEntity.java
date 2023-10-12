/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.INetHandler;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ public class S14PacketEntity
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   protected int entityId;
/*     */   protected byte posX;
/*     */   protected byte posY;
/*     */   protected byte posZ;
/*     */   protected byte yaw;
/*     */   protected byte pitch;
/*     */   protected boolean onGround;
/*     */   protected boolean field_149069_g;
/*     */   
/*     */   public S14PacketEntity() {}
/*     */   
/*     */   public S14PacketEntity(int entityIdIn) {
/*  27 */     this.entityId = entityIdIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readPacketData(PacketBuffer buf) throws IOException {
/*  32 */     this.entityId = buf.readVarIntFromBuffer();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writePacketData(PacketBuffer buf) throws IOException {
/*  37 */     buf.writeVarIntToBuffer(this.entityId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void processPacket(INetHandlerPlayClient handler) {
/*  42 */     handler.handleEntityMovement(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  47 */     return "Entity_" + super.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getEntity(World worldIn) {
/*  52 */     return worldIn.getEntityByID(this.entityId);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte func_149062_c() {
/*  57 */     return this.posX;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte func_149061_d() {
/*  62 */     return this.posY;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte func_149064_e() {
/*  67 */     return this.posZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte func_149066_f() {
/*  72 */     return this.yaw;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte func_149063_g() {
/*  77 */     return this.pitch;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_149060_h() {
/*  82 */     return this.field_149069_g;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getOnGround() {
/*  87 */     return this.onGround;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class S15PacketEntityRelMove
/*     */     extends S14PacketEntity
/*     */   {
/*     */     public S15PacketEntityRelMove() {}
/*     */ 
/*     */     
/*     */     public S15PacketEntityRelMove(int entityIdIn, byte x, byte y, byte z, boolean onGroundIn) {
/*  98 */       super(entityIdIn);
/*  99 */       this.posX = x;
/* 100 */       this.posY = y;
/* 101 */       this.posZ = z;
/* 102 */       this.onGround = onGroundIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public void readPacketData(PacketBuffer buf) throws IOException {
/* 107 */       super.readPacketData(buf);
/* 108 */       this.posX = buf.readByte();
/* 109 */       this.posY = buf.readByte();
/* 110 */       this.posZ = buf.readByte();
/* 111 */       this.onGround = buf.readBoolean();
/*     */     }
/*     */ 
/*     */     
/*     */     public void writePacketData(PacketBuffer buf) throws IOException {
/* 116 */       super.writePacketData(buf);
/* 117 */       buf.writeByte(this.posX);
/* 118 */       buf.writeByte(this.posY);
/* 119 */       buf.writeByte(this.posZ);
/* 120 */       buf.writeBoolean(this.onGround);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class S16PacketEntityLook
/*     */     extends S14PacketEntity
/*     */   {
/*     */     public S16PacketEntityLook() {
/* 128 */       this.field_149069_g = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public S16PacketEntityLook(int entityIdIn, byte yawIn, byte pitchIn, boolean onGroundIn) {
/* 133 */       super(entityIdIn);
/* 134 */       this.yaw = yawIn;
/* 135 */       this.pitch = pitchIn;
/* 136 */       this.field_149069_g = true;
/* 137 */       this.onGround = onGroundIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public void readPacketData(PacketBuffer buf) throws IOException {
/* 142 */       super.readPacketData(buf);
/* 143 */       this.yaw = buf.readByte();
/* 144 */       this.pitch = buf.readByte();
/* 145 */       this.onGround = buf.readBoolean();
/*     */     }
/*     */ 
/*     */     
/*     */     public void writePacketData(PacketBuffer buf) throws IOException {
/* 150 */       super.writePacketData(buf);
/* 151 */       buf.writeByte(this.yaw);
/* 152 */       buf.writeByte(this.pitch);
/* 153 */       buf.writeBoolean(this.onGround);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class S17PacketEntityLookMove
/*     */     extends S14PacketEntity
/*     */   {
/*     */     public S17PacketEntityLookMove() {
/* 161 */       this.field_149069_g = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public S17PacketEntityLookMove(int p_i45973_1_, byte p_i45973_2_, byte p_i45973_3_, byte p_i45973_4_, byte p_i45973_5_, byte p_i45973_6_, boolean p_i45973_7_) {
/* 166 */       super(p_i45973_1_);
/* 167 */       this.posX = p_i45973_2_;
/* 168 */       this.posY = p_i45973_3_;
/* 169 */       this.posZ = p_i45973_4_;
/* 170 */       this.yaw = p_i45973_5_;
/* 171 */       this.pitch = p_i45973_6_;
/* 172 */       this.onGround = p_i45973_7_;
/* 173 */       this.field_149069_g = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void readPacketData(PacketBuffer buf) throws IOException {
/* 178 */       super.readPacketData(buf);
/* 179 */       this.posX = buf.readByte();
/* 180 */       this.posY = buf.readByte();
/* 181 */       this.posZ = buf.readByte();
/* 182 */       this.yaw = buf.readByte();
/* 183 */       this.pitch = buf.readByte();
/* 184 */       this.onGround = buf.readBoolean();
/*     */     }
/*     */ 
/*     */     
/*     */     public void writePacketData(PacketBuffer buf) throws IOException {
/* 189 */       super.writePacketData(buf);
/* 190 */       buf.writeByte(this.posX);
/* 191 */       buf.writeByte(this.posY);
/* 192 */       buf.writeByte(this.posZ);
/* 193 */       buf.writeByte(this.yaw);
/* 194 */       buf.writeByte(this.pitch);
/* 195 */       buf.writeBoolean(this.onGround);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S14PacketEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
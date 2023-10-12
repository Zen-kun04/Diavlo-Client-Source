/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.network.INetHandler;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ 
/*     */ 
/*     */ public class S2APacketParticles
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private EnumParticleTypes particleType;
/*     */   private float xCoord;
/*     */   private float yCoord;
/*     */   private float zCoord;
/*     */   private float xOffset;
/*     */   private float yOffset;
/*     */   private float zOffset;
/*     */   private float particleSpeed;
/*     */   private int particleCount;
/*     */   private boolean longDistance;
/*     */   private int[] particleArguments;
/*     */   
/*     */   public S2APacketParticles() {}
/*     */   
/*     */   public S2APacketParticles(EnumParticleTypes particleTypeIn, boolean longDistanceIn, float x, float y, float z, float xOffsetIn, float yOffset, float zOffset, float particleSpeedIn, int particleCountIn, int... particleArgumentsIn) {
/*  29 */     this.particleType = particleTypeIn;
/*  30 */     this.longDistance = longDistanceIn;
/*  31 */     this.xCoord = x;
/*  32 */     this.yCoord = y;
/*  33 */     this.zCoord = z;
/*  34 */     this.xOffset = xOffsetIn;
/*  35 */     this.yOffset = yOffset;
/*  36 */     this.zOffset = zOffset;
/*  37 */     this.particleSpeed = particleSpeedIn;
/*  38 */     this.particleCount = particleCountIn;
/*  39 */     this.particleArguments = particleArgumentsIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readPacketData(PacketBuffer buf) throws IOException {
/*  44 */     this.particleType = EnumParticleTypes.getParticleFromId(buf.readInt());
/*     */     
/*  46 */     if (this.particleType == null)
/*     */     {
/*  48 */       this.particleType = EnumParticleTypes.BARRIER;
/*     */     }
/*     */     
/*  51 */     this.longDistance = buf.readBoolean();
/*  52 */     this.xCoord = buf.readFloat();
/*  53 */     this.yCoord = buf.readFloat();
/*  54 */     this.zCoord = buf.readFloat();
/*  55 */     this.xOffset = buf.readFloat();
/*  56 */     this.yOffset = buf.readFloat();
/*  57 */     this.zOffset = buf.readFloat();
/*  58 */     this.particleSpeed = buf.readFloat();
/*  59 */     this.particleCount = buf.readInt();
/*  60 */     int i = this.particleType.getArgumentCount();
/*  61 */     this.particleArguments = new int[i];
/*     */     
/*  63 */     for (int j = 0; j < i; j++)
/*     */     {
/*  65 */       this.particleArguments[j] = buf.readVarIntFromBuffer();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void writePacketData(PacketBuffer buf) throws IOException {
/*  71 */     buf.writeInt(this.particleType.getParticleID());
/*  72 */     buf.writeBoolean(this.longDistance);
/*  73 */     buf.writeFloat(this.xCoord);
/*  74 */     buf.writeFloat(this.yCoord);
/*  75 */     buf.writeFloat(this.zCoord);
/*  76 */     buf.writeFloat(this.xOffset);
/*  77 */     buf.writeFloat(this.yOffset);
/*  78 */     buf.writeFloat(this.zOffset);
/*  79 */     buf.writeFloat(this.particleSpeed);
/*  80 */     buf.writeInt(this.particleCount);
/*  81 */     int i = this.particleType.getArgumentCount();
/*     */     
/*  83 */     for (int j = 0; j < i; j++)
/*     */     {
/*  85 */       buf.writeVarIntToBuffer(this.particleArguments[j]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumParticleTypes getParticleType() {
/*  91 */     return this.particleType;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLongDistance() {
/*  96 */     return this.longDistance;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getXCoordinate() {
/* 101 */     return this.xCoord;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getYCoordinate() {
/* 106 */     return this.yCoord;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getZCoordinate() {
/* 111 */     return this.zCoord;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getXOffset() {
/* 116 */     return this.xOffset;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getYOffset() {
/* 121 */     return this.yOffset;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getZOffset() {
/* 126 */     return this.zOffset;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getParticleSpeed() {
/* 131 */     return this.particleSpeed;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getParticleCount() {
/* 136 */     return this.particleCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getParticleArgs() {
/* 141 */     return this.particleArguments;
/*     */   }
/*     */ 
/*     */   
/*     */   public void processPacket(INetHandlerPlayClient handler) {
/* 146 */     handler.handleParticles(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S2APacketParticles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
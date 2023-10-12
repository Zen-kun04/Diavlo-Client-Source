/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.network.INetHandler;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ 
/*     */ 
/*     */ public class S08PacketPlayerPosLook
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private double x;
/*     */   private double y;
/*     */   private double z;
/*     */   private float yaw;
/*     */   private float pitch;
/*     */   private Set<EnumFlags> field_179835_f;
/*     */   
/*     */   public S08PacketPlayerPosLook() {}
/*     */   
/*     */   public S08PacketPlayerPosLook(double xIn, double yIn, double zIn, float yawIn, float pitchIn, Set<EnumFlags> p_i45993_9_) {
/*  25 */     this.x = xIn;
/*  26 */     this.y = yIn;
/*  27 */     this.z = zIn;
/*  28 */     this.yaw = yawIn;
/*  29 */     this.pitch = pitchIn;
/*  30 */     this.field_179835_f = p_i45993_9_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readPacketData(PacketBuffer buf) throws IOException {
/*  35 */     this.x = buf.readDouble();
/*  36 */     this.y = buf.readDouble();
/*  37 */     this.z = buf.readDouble();
/*  38 */     this.yaw = buf.readFloat();
/*  39 */     this.pitch = buf.readFloat();
/*  40 */     this.field_179835_f = EnumFlags.func_180053_a(buf.readUnsignedByte());
/*     */   }
/*     */ 
/*     */   
/*     */   public void writePacketData(PacketBuffer buf) throws IOException {
/*  45 */     buf.writeDouble(this.x);
/*  46 */     buf.writeDouble(this.y);
/*  47 */     buf.writeDouble(this.z);
/*  48 */     buf.writeFloat(this.yaw);
/*  49 */     buf.writeFloat(this.pitch);
/*  50 */     buf.writeByte(EnumFlags.func_180056_a(this.field_179835_f));
/*     */   }
/*     */ 
/*     */   
/*     */   public void processPacket(INetHandlerPlayClient handler) {
/*  55 */     handler.handlePlayerPosLook(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getX() {
/*  60 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getY() {
/*  65 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getZ() {
/*  70 */     return this.z;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getYaw() {
/*  75 */     return this.yaw;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPitch() {
/*  80 */     return this.pitch;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<EnumFlags> func_179834_f() {
/*  85 */     return this.field_179835_f;
/*     */   }
/*     */   
/*     */   public enum EnumFlags
/*     */   {
/*  90 */     X(0),
/*  91 */     Y(1),
/*  92 */     Z(2),
/*  93 */     Y_ROT(3),
/*  94 */     X_ROT(4);
/*     */     
/*     */     private int field_180058_f;
/*     */ 
/*     */     
/*     */     EnumFlags(int p_i45992_3_) {
/* 100 */       this.field_180058_f = p_i45992_3_;
/*     */     }
/*     */ 
/*     */     
/*     */     private int func_180055_a() {
/* 105 */       return 1 << this.field_180058_f;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean func_180054_b(int p_180054_1_) {
/* 110 */       return ((p_180054_1_ & func_180055_a()) == func_180055_a());
/*     */     }
/*     */ 
/*     */     
/*     */     public static Set<EnumFlags> func_180053_a(int p_180053_0_) {
/* 115 */       Set<EnumFlags> set = EnumSet.noneOf(EnumFlags.class);
/*     */       
/* 117 */       for (EnumFlags s08packetplayerposlook$enumflags : values()) {
/*     */         
/* 119 */         if (s08packetplayerposlook$enumflags.func_180054_b(p_180053_0_))
/*     */         {
/* 121 */           set.add(s08packetplayerposlook$enumflags);
/*     */         }
/*     */       } 
/*     */       
/* 125 */       return set;
/*     */     }
/*     */ 
/*     */     
/*     */     public static int func_180056_a(Set<EnumFlags> p_180056_0_) {
/* 130 */       int i = 0;
/*     */       
/* 132 */       for (EnumFlags s08packetplayerposlook$enumflags : p_180056_0_)
/*     */       {
/* 134 */         i |= s08packetplayerposlook$enumflags.func_180055_a();
/*     */       }
/*     */       
/* 137 */       return i;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S08PacketPlayerPosLook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
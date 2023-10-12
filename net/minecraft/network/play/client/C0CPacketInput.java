/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ public class C0CPacketInput
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private float strafeSpeed;
/*    */   private float forwardSpeed;
/*    */   private boolean jumping;
/*    */   private boolean sneaking;
/*    */   
/*    */   public C0CPacketInput() {}
/*    */   
/*    */   public C0CPacketInput(float strafeSpeed, float forwardSpeed, boolean jumping, boolean sneaking) {
/* 21 */     this.strafeSpeed = strafeSpeed;
/* 22 */     this.forwardSpeed = forwardSpeed;
/* 23 */     this.jumping = jumping;
/* 24 */     this.sneaking = sneaking;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 29 */     this.strafeSpeed = buf.readFloat();
/* 30 */     this.forwardSpeed = buf.readFloat();
/* 31 */     byte b0 = buf.readByte();
/* 32 */     this.jumping = ((b0 & 0x1) > 0);
/* 33 */     this.sneaking = ((b0 & 0x2) > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 38 */     buf.writeFloat(this.strafeSpeed);
/* 39 */     buf.writeFloat(this.forwardSpeed);
/* 40 */     byte b0 = 0;
/*    */     
/* 42 */     if (this.jumping)
/*    */     {
/* 44 */       b0 = (byte)(b0 | 0x1);
/*    */     }
/*    */     
/* 47 */     if (this.sneaking)
/*    */     {
/* 49 */       b0 = (byte)(b0 | 0x2);
/*    */     }
/*    */     
/* 52 */     buf.writeByte(b0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayServer handler) {
/* 57 */     handler.processInput(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public float getStrafeSpeed() {
/* 62 */     return this.strafeSpeed;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getForwardSpeed() {
/* 67 */     return this.forwardSpeed;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isJumping() {
/* 72 */     return this.jumping;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSneaking() {
/* 77 */     return this.sneaking;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\client\C0CPacketInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
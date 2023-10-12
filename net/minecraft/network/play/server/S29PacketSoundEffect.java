/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class S29PacketSoundEffect implements Packet<INetHandlerPlayClient> {
/*    */   private String soundName;
/*    */   private int posX;
/* 14 */   private int posY = Integer.MAX_VALUE;
/*    */ 
/*    */   
/*    */   private int posZ;
/*    */   
/*    */   private float soundVolume;
/*    */   
/*    */   private int soundPitch;
/*    */ 
/*    */   
/*    */   public S29PacketSoundEffect(String soundNameIn, double soundX, double soundY, double soundZ, float volume, float pitch) {
/* 25 */     Validate.notNull(soundNameIn, "name", new Object[0]);
/* 26 */     this.soundName = soundNameIn;
/* 27 */     this.posX = (int)(soundX * 8.0D);
/* 28 */     this.posY = (int)(soundY * 8.0D);
/* 29 */     this.posZ = (int)(soundZ * 8.0D);
/* 30 */     this.soundVolume = volume;
/* 31 */     this.soundPitch = (int)(pitch * 63.0F);
/* 32 */     pitch = MathHelper.clamp_float(pitch, 0.0F, 255.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 37 */     this.soundName = buf.readStringFromBuffer(256);
/* 38 */     this.posX = buf.readInt();
/* 39 */     this.posY = buf.readInt();
/* 40 */     this.posZ = buf.readInt();
/* 41 */     this.soundVolume = buf.readFloat();
/* 42 */     this.soundPitch = buf.readUnsignedByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 47 */     buf.writeString(this.soundName);
/* 48 */     buf.writeInt(this.posX);
/* 49 */     buf.writeInt(this.posY);
/* 50 */     buf.writeInt(this.posZ);
/* 51 */     buf.writeFloat(this.soundVolume);
/* 52 */     buf.writeByte(this.soundPitch);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSoundName() {
/* 57 */     return this.soundName;
/*    */   }
/*    */ 
/*    */   
/*    */   public double getX() {
/* 62 */     return (this.posX / 8.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public double getY() {
/* 67 */     return (this.posY / 8.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public double getZ() {
/* 72 */     return (this.posZ / 8.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public float getVolume() {
/* 77 */     return this.soundVolume;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getPitch() {
/* 82 */     return this.soundPitch / 63.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 87 */     handler.handleSoundEffect(this);
/*    */   }
/*    */   
/*    */   public S29PacketSoundEffect() {}
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S29PacketSoundEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
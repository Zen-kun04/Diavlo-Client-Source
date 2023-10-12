/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ public class C15PacketClientSettings
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private String lang;
/*    */   private int view;
/*    */   private EntityPlayer.EnumChatVisibility chatVisibility;
/*    */   private boolean enableColors;
/*    */   private int modelPartFlags;
/*    */   
/*    */   public C15PacketClientSettings() {}
/*    */   
/*    */   public C15PacketClientSettings(String langIn, int viewIn, EntityPlayer.EnumChatVisibility chatVisibilityIn, boolean enableColorsIn, int modelPartFlagsIn) {
/* 23 */     this.lang = langIn;
/* 24 */     this.view = viewIn;
/* 25 */     this.chatVisibility = chatVisibilityIn;
/* 26 */     this.enableColors = enableColorsIn;
/* 27 */     this.modelPartFlags = modelPartFlagsIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 32 */     this.lang = buf.readStringFromBuffer(7);
/* 33 */     this.view = buf.readByte();
/* 34 */     this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(buf.readByte());
/* 35 */     this.enableColors = buf.readBoolean();
/* 36 */     this.modelPartFlags = buf.readUnsignedByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 41 */     buf.writeString(this.lang);
/* 42 */     buf.writeByte(this.view);
/* 43 */     buf.writeByte(this.chatVisibility.getChatVisibility());
/* 44 */     buf.writeBoolean(this.enableColors);
/* 45 */     buf.writeByte(this.modelPartFlags);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayServer handler) {
/* 50 */     handler.processClientSettings(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLang() {
/* 55 */     return this.lang;
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityPlayer.EnumChatVisibility getChatVisibility() {
/* 60 */     return this.chatVisibility;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isColorsEnabled() {
/* 65 */     return this.enableColors;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getModelPartFlags() {
/* 70 */     return this.modelPartFlags;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\client\C15PacketClientSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
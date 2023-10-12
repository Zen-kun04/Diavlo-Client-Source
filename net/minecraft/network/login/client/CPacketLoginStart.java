/*    */ package net.minecraft.network.login.client;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.login.INetHandlerLoginServer;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CPacketLoginStart
/*    */   implements Packet<INetHandlerLoginServer>
/*    */ {
/*    */   private GameProfile profile;
/*    */   
/*    */   public CPacketLoginStart() {}
/*    */   
/*    */   public CPacketLoginStart(GameProfile profileIn) {
/* 20 */     this.profile = profileIn;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 26 */     this.profile = new GameProfile(null, buf.readStringFromBuffer(16));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 32 */     buf.writeString(this.profile.getName());
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerLoginServer handler) {
/* 37 */     handler.processLoginStart(new C00PacketLoginStart(getProfile()));
/*    */   }
/*    */ 
/*    */   
/*    */   public GameProfile getProfile() {
/* 42 */     return this.profile;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\login\client\CPacketLoginStart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
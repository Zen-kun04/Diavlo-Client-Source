/*    */ package net.minecraft.server.integrated;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.net.SocketAddress;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.management.ServerConfigurationManager;
/*    */ 
/*    */ public class IntegratedPlayerList
/*    */   extends ServerConfigurationManager {
/*    */   private NBTTagCompound hostPlayerData;
/*    */   
/*    */   public IntegratedPlayerList(IntegratedServer server) {
/* 15 */     super(server);
/* 16 */     setViewDistance(10);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void writePlayerData(EntityPlayerMP playerIn) {
/* 21 */     if (playerIn.getName().equals(getServerInstance().getServerOwner())) {
/*    */       
/* 23 */       this.hostPlayerData = new NBTTagCompound();
/* 24 */       playerIn.writeToNBT(this.hostPlayerData);
/*    */     } 
/*    */     
/* 27 */     super.writePlayerData(playerIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public String allowUserToConnect(SocketAddress address, GameProfile profile) {
/* 32 */     return (profile.getName().equalsIgnoreCase(getServerInstance().getServerOwner()) && getPlayerByUsername(profile.getName()) != null) ? "That name is already taken." : super.allowUserToConnect(address, profile);
/*    */   }
/*    */ 
/*    */   
/*    */   public IntegratedServer getServerInstance() {
/* 37 */     return (IntegratedServer)super.getServerInstance();
/*    */   }
/*    */ 
/*    */   
/*    */   public NBTTagCompound getHostPlayerData() {
/* 42 */     return this.hostPlayerData;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\server\integrated\IntegratedPlayerList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
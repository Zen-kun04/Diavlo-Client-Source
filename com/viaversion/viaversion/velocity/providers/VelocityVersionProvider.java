/*    */ package com.viaversion.viaversion.velocity.providers;
/*    */ 
/*    */ import com.velocitypowered.api.network.ProtocolVersion;
/*    */ import com.velocitypowered.api.proxy.ServerConnection;
/*    */ import com.viaversion.viaversion.VelocityPlugin;
/*    */ import com.viaversion.viaversion.api.Via;
/*    */ import com.viaversion.viaversion.api.connection.UserConnection;
/*    */ import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
/*    */ import com.viaversion.viaversion.protocols.base.BaseVersionProvider;
/*    */ import com.viaversion.viaversion.velocity.platform.VelocityViaInjector;
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Arrays;
/*    */ import java.util.stream.IntStream;
/*    */ import org.jetbrains.annotations.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VelocityVersionProvider
/*    */   extends BaseVersionProvider
/*    */ {
/* 34 */   private static final Method GET_ASSOCIATION = getAssociationMethod();
/*    */   @Nullable
/*    */   private static Method getAssociationMethod() {
/*    */     try {
/* 38 */       return Class.forName("com.velocitypowered.proxy.connection.MinecraftConnection").getMethod("getAssociation", new Class[0]);
/* 39 */     } catch (NoSuchMethodException|ClassNotFoundException e) {
/* 40 */       e.printStackTrace();
/* 41 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int getClosestServerProtocol(UserConnection user) throws Exception {
/* 47 */     return user.isClientSide() ? getBackProtocol(user) : getFrontProtocol(user);
/*    */   }
/*    */ 
/*    */   
/*    */   private int getBackProtocol(UserConnection user) throws Exception {
/* 52 */     ChannelHandler mcHandler = user.getChannel().pipeline().get("handler");
/* 53 */     ServerConnection serverConnection = (ServerConnection)GET_ASSOCIATION.invoke(mcHandler, new Object[0]);
/* 54 */     return Via.proxyPlatform().protocolDetectorService().serverProtocolVersion(serverConnection.getServerInfo().getName());
/*    */   }
/*    */   
/*    */   private int getFrontProtocol(UserConnection user) throws Exception {
/* 58 */     int playerVersion = user.getProtocolInfo().getProtocolVersion();
/*    */ 
/*    */     
/* 61 */     IntStream versions = ProtocolVersion.SUPPORTED_VERSIONS.stream().mapToInt(ProtocolVersion::getProtocol);
/*    */ 
/*    */     
/* 64 */     if (VelocityViaInjector.GET_PLAYER_INFO_FORWARDING_MODE != null && ((Enum)VelocityViaInjector.GET_PLAYER_INFO_FORWARDING_MODE
/* 65 */       .invoke(VelocityPlugin.PROXY.getConfiguration(), new Object[0]))
/* 66 */       .name().equals("MODERN")) {
/* 67 */       versions = versions.filter(ver -> (ver >= ProtocolVersion.v1_13.getVersion()));
/*    */     }
/* 69 */     int[] compatibleProtocols = versions.toArray();
/*    */     
/* 71 */     if (Arrays.binarySearch(compatibleProtocols, playerVersion) >= 0)
/*    */     {
/* 73 */       return playerVersion;
/*    */     }
/*    */     
/* 76 */     if (playerVersion < compatibleProtocols[0])
/*    */     {
/* 78 */       return compatibleProtocols[0];
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 85 */     for (int i = compatibleProtocols.length - 1; i >= 0; i--) {
/* 86 */       int protocol = compatibleProtocols[i];
/* 87 */       if (playerVersion > protocol && ProtocolVersion.isRegistered(protocol)) {
/* 88 */         return protocol;
/*    */       }
/*    */     } 
/*    */     
/* 92 */     Via.getPlatform().getLogger().severe("Panic, no protocol id found for " + playerVersion);
/* 93 */     return playerVersion;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\velocity\providers\VelocityVersionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
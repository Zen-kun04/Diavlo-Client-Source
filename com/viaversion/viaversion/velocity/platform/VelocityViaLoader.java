/*    */ package com.viaversion.viaversion.velocity.platform;
/*    */ 
/*    */ import com.velocitypowered.api.plugin.PluginContainer;
/*    */ import com.viaversion.viaversion.VelocityPlugin;
/*    */ import com.viaversion.viaversion.api.Via;
/*    */ import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
/*    */ import com.viaversion.viaversion.api.platform.providers.Provider;
/*    */ import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
/*    */ import com.viaversion.viaversion.api.protocol.version.VersionProvider;
/*    */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.BossBarProvider;
/*    */ import com.viaversion.viaversion.velocity.listeners.UpdateListener;
/*    */ import com.viaversion.viaversion.velocity.providers.VelocityBossBarProvider;
/*    */ import com.viaversion.viaversion.velocity.providers.VelocityVersionProvider;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VelocityViaLoader
/*    */   implements ViaPlatformLoader
/*    */ {
/*    */   public void load() {
/* 36 */     Object plugin = VelocityPlugin.PROXY.getPluginManager().getPlugin("viaversion").flatMap(PluginContainer::getInstance).get();
/*    */     
/* 38 */     if (Via.getAPI().getServerVersion().lowestSupportedVersion() < ProtocolVersion.v1_9.getVersion()) {
/* 39 */       Via.getManager().getProviders().use(BossBarProvider.class, (Provider)new VelocityBossBarProvider());
/*    */     }
/*    */     
/* 42 */     Via.getManager().getProviders().use(VersionProvider.class, (Provider)new VelocityVersionProvider());
/*    */ 
/*    */ 
/*    */     
/* 46 */     VelocityPlugin.PROXY.getEventManager().register(plugin, new UpdateListener());
/*    */     
/* 48 */     int pingInterval = ((VelocityViaConfig)Via.getPlatform().getConf()).getVelocityPingInterval();
/* 49 */     if (pingInterval > 0)
/* 50 */       Via.getPlatform().runRepeatingAsync(() -> Via.proxyPlatform().protocolDetectorService().probeAllServers(), pingInterval * 20L); 
/*    */   }
/*    */   
/*    */   public void unload() {}
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\velocity\platform\VelocityViaLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
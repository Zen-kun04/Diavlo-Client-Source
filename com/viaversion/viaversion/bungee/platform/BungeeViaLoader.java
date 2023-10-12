/*    */ package com.viaversion.viaversion.bungee.platform;
/*    */ 
/*    */ import com.viaversion.viaversion.BungeePlugin;
/*    */ import com.viaversion.viaversion.api.Via;
/*    */ import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
/*    */ import com.viaversion.viaversion.api.platform.providers.Provider;
/*    */ import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
/*    */ import com.viaversion.viaversion.api.protocol.version.VersionProvider;
/*    */ import com.viaversion.viaversion.bungee.handlers.BungeeServerHandler;
/*    */ import com.viaversion.viaversion.bungee.listeners.ElytraPatch;
/*    */ import com.viaversion.viaversion.bungee.listeners.UpdateListener;
/*    */ import com.viaversion.viaversion.bungee.providers.BungeeBossBarProvider;
/*    */ import com.viaversion.viaversion.bungee.providers.BungeeEntityIdProvider;
/*    */ import com.viaversion.viaversion.bungee.providers.BungeeMainHandProvider;
/*    */ import com.viaversion.viaversion.bungee.providers.BungeeVersionProvider;
/*    */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.BossBarProvider;
/*    */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.EntityIdProvider;
/*    */ import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MainHandProvider;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import net.md_5.bungee.api.ProxyServer;
/*    */ import net.md_5.bungee.api.plugin.Listener;
/*    */ import net.md_5.bungee.api.plugin.Plugin;
/*    */ import net.md_5.bungee.api.scheduler.ScheduledTask;
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
/*    */ public class BungeeViaLoader
/*    */   implements ViaPlatformLoader
/*    */ {
/* 43 */   private final Set<Listener> listeners = new HashSet<>();
/* 44 */   private final Set<ScheduledTask> tasks = new HashSet<>();
/*    */   private final BungeePlugin plugin;
/*    */   
/*    */   public BungeeViaLoader(BungeePlugin plugin) {
/* 48 */     this.plugin = plugin;
/*    */   }
/*    */   
/*    */   private void registerListener(Listener listener) {
/* 52 */     this.listeners.add(listener);
/* 53 */     ProxyServer.getInstance().getPluginManager().registerListener((Plugin)this.plugin, listener);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void load() {
/* 59 */     registerListener((Listener)this.plugin);
/* 60 */     registerListener((Listener)new UpdateListener());
/* 61 */     registerListener((Listener)new BungeeServerHandler());
/*    */     
/* 63 */     if (Via.getAPI().getServerVersion().lowestSupportedVersion() < ProtocolVersion.v1_9.getVersion()) {
/* 64 */       registerListener((Listener)new ElytraPatch());
/*    */     }
/*    */ 
/*    */     
/* 68 */     Via.getManager().getProviders().use(VersionProvider.class, (Provider)new BungeeVersionProvider());
/* 69 */     Via.getManager().getProviders().use(EntityIdProvider.class, (Provider)new BungeeEntityIdProvider());
/*    */     
/* 71 */     if (Via.getAPI().getServerVersion().lowestSupportedVersion() < ProtocolVersion.v1_9.getVersion()) {
/* 72 */       Via.getManager().getProviders().use(BossBarProvider.class, (Provider)new BungeeBossBarProvider());
/* 73 */       Via.getManager().getProviders().use(MainHandProvider.class, (Provider)new BungeeMainHandProvider());
/*    */     } 
/*    */     
/* 76 */     if (this.plugin.getConf().getBungeePingInterval() > 0) {
/* 77 */       this.tasks.add(this.plugin.getProxy().getScheduler().schedule((Plugin)this.plugin, () -> Via.proxyPlatform().protocolDetectorService().probeAllServers(), 0L, this.plugin
/*    */ 
/*    */             
/* 80 */             .getConf().getBungeePingInterval(), TimeUnit.SECONDS));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void unload() {
/* 88 */     for (Listener listener : this.listeners) {
/* 89 */       ProxyServer.getInstance().getPluginManager().unregisterListener(listener);
/*    */     }
/* 91 */     this.listeners.clear();
/* 92 */     for (ScheduledTask task : this.tasks) {
/* 93 */       task.cancel();
/*    */     }
/* 95 */     this.tasks.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\bungee\platform\BungeeViaLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
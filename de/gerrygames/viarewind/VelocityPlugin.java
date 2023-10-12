/*    */ package de.gerrygames.viarewind;
/*    */ 
/*    */ import com.google.inject.Inject;
/*    */ import com.velocitypowered.api.event.PostOrder;
/*    */ import com.velocitypowered.api.event.Subscribe;
/*    */ import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
/*    */ import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
/*    */ import com.velocitypowered.api.plugin.Dependency;
/*    */ import com.velocitypowered.api.plugin.Plugin;
/*    */ import com.velocitypowered.api.plugin.annotation.DataDirectory;
/*    */ import com.viaversion.viaversion.velocity.util.LoggerWrapper;
/*    */ import de.gerrygames.viarewind.api.ViaRewindConfig;
/*    */ import de.gerrygames.viarewind.api.ViaRewindConfigImpl;
/*    */ import de.gerrygames.viarewind.api.ViaRewindPlatform;
/*    */ import java.nio.file.Path;
/*    */ import java.util.logging.Logger;
/*    */ import org.slf4j.Logger;
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
/*    */ @Plugin(id = "viarewind", name = "ViaRewind", version = "2.0.4-SNAPSHOT", authors = {"Gerrygames"}, dependencies = {@Dependency(id = "viaversion"), @Dependency(id = "viabackwards", optional = true)}, url = "https://viaversion.com/rewind")
/*    */ public class VelocityPlugin
/*    */   implements ViaRewindPlatform
/*    */ {
/*    */   private Logger logger;
/*    */   @Inject
/*    */   private Logger loggerSlf4j;
/*    */   @Inject
/*    */   @DataDirectory
/*    */   private Path configDir;
/*    */   private ViaRewindConfigImpl conf;
/*    */   
/*    */   @Subscribe(order = PostOrder.LATE)
/*    */   public void onProxyStart(ProxyInitializeEvent e) {
/* 42 */     this.logger = (Logger)new LoggerWrapper(this.loggerSlf4j);
/*    */     
/* 44 */     this.conf = new ViaRewindConfigImpl(this.configDir.resolve("config.yml").toFile());
/* 45 */     this.conf.reloadConfig();
/* 46 */     init((ViaRewindConfig)this.conf);
/*    */   }
/*    */   
/*    */   @Subscribe
/*    */   public void onReload(ProxyReloadEvent e) {
/* 51 */     this.conf.reloadConfig();
/*    */   }
/*    */   
/*    */   public Logger getLogger() {
/* 55 */     return this.logger;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\de\gerrygames\viarewind\VelocityPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
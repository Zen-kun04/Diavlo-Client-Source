/*    */ package de.gerrygames.viarewind;
/*    */ 
/*    */ import com.google.inject.Inject;
/*    */ import com.viaversion.viaversion.sponge.util.LoggerWrapper;
/*    */ import de.gerrygames.viarewind.api.ViaRewindConfig;
/*    */ import de.gerrygames.viarewind.api.ViaRewindConfigImpl;
/*    */ import de.gerrygames.viarewind.api.ViaRewindPlatform;
/*    */ import java.nio.file.Path;
/*    */ import java.util.logging.Logger;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.spongepowered.api.config.ConfigDir;
/*    */ import org.spongepowered.api.event.Listener;
/*    */ import org.spongepowered.api.event.Order;
/*    */ import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
/*    */ import org.spongepowered.api.event.lifecycle.RefreshGameEvent;
/*    */ import org.spongepowered.plugin.builtin.jvm.Plugin;
/*    */ 
/*    */ @Plugin("viarewind")
/*    */ public class SpongePlugin
/*    */   implements ViaRewindPlatform
/*    */ {
/*    */   private Logger logger;
/*    */   @Inject
/*    */   private Logger loggerSlf4j;
/*    */   @Inject
/*    */   @ConfigDir(sharedRoot = false)
/*    */   private Path configDir;
/*    */   private ViaRewindConfigImpl conf;
/*    */   
/*    */   @Listener(order = Order.LATE)
/*    */   public void loadPlugin(ConstructPluginEvent e) {
/* 32 */     this.logger = (Logger)new LoggerWrapper(this.loggerSlf4j);
/*    */     
/* 34 */     this.conf = new ViaRewindConfigImpl(this.configDir.resolve("config.yml").toFile());
/* 35 */     this.conf.reloadConfig();
/* 36 */     init((ViaRewindConfig)this.conf);
/*    */   }
/*    */   
/*    */   @Listener
/*    */   public void reload(RefreshGameEvent e) {
/* 41 */     this.conf.reloadConfig();
/*    */   }
/*    */   
/*    */   public Logger getLogger() {
/* 45 */     return this.logger;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\de\gerrygames\viarewind\SpongePlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
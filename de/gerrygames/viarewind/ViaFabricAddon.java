/*    */ package de.gerrygames.viarewind;
/*    */ 
/*    */ import de.gerrygames.viarewind.api.ViaRewindConfig;
/*    */ import de.gerrygames.viarewind.api.ViaRewindConfigImpl;
/*    */ import de.gerrygames.viarewind.api.ViaRewindPlatform;
/*    */ import de.gerrygames.viarewind.fabric.util.LoggerWrapper;
/*    */ import java.util.logging.Logger;
/*    */ import net.fabricmc.loader.api.FabricLoader;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ 
/*    */ public class ViaFabricAddon implements ViaRewindPlatform, Runnable {
/* 12 */   private final Logger logger = (Logger)new LoggerWrapper(LogManager.getLogger("ViaRewind"));
/*    */ 
/*    */   
/*    */   public void run() {
/* 16 */     ViaRewindConfigImpl conf = new ViaRewindConfigImpl(FabricLoader.getInstance().getConfigDirectory().toPath().resolve("ViaRewind").resolve("config.yml").toFile());
/* 17 */     conf.reloadConfig();
/* 18 */     init((ViaRewindConfig)conf);
/*    */   }
/*    */   
/*    */   public Logger getLogger() {
/* 22 */     return this.logger;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\de\gerrygames\viarewind\ViaFabricAddon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
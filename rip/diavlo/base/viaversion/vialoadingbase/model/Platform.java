/*    */ package rip.diavlo.base.viaversion.vialoadingbase.model;
/*    */ 
/*    */ import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.BooleanSupplier;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.logging.Logger;
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
/*    */ public class Platform
/*    */ {
/* 29 */   public static int COUNT = 0;
/* 30 */   public static final List<ProtocolVersion> TEMP_INPUT_PROTOCOLS = new ArrayList<>();
/*    */   
/*    */   private final String name;
/*    */   private final BooleanSupplier load;
/*    */   private final Runnable executor;
/*    */   private final Consumer<List<ProtocolVersion>> versionCallback;
/*    */   
/*    */   public Platform(String name, BooleanSupplier load, Runnable executor) {
/* 38 */     this(name, load, executor, null);
/*    */   }
/*    */   
/*    */   public Platform(String name, BooleanSupplier load, Runnable executor, Consumer<List<ProtocolVersion>> versionCallback) {
/* 42 */     this.name = name;
/* 43 */     this.load = load;
/* 44 */     this.executor = executor;
/* 45 */     this.versionCallback = versionCallback;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 49 */     return this.name;
/*    */   }
/*    */   
/*    */   public void createProtocolPath() {
/* 53 */     if (this.versionCallback != null) {
/* 54 */       this.versionCallback.accept(TEMP_INPUT_PROTOCOLS);
/*    */     }
/*    */   }
/*    */   
/*    */   public void build(Logger logger) {
/* 59 */     if (this.load.getAsBoolean()) {
/*    */       try {
/* 61 */         this.executor.run();
/* 62 */         logger.info("Loaded Platform " + this.name);
/* 63 */         COUNT++;
/* 64 */       } catch (Throwable t) {
/* 65 */         logger.severe("An error occurred while loading Platform " + this.name + ":");
/* 66 */         t.printStackTrace();
/*    */       } 
/*    */       return;
/*    */     } 
/* 70 */     logger.severe("Platform " + this.name + " is not present");
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\viaversion\vialoadingbase\model\Platform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
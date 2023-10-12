/*    */ package rip.diavlo.base.modules.render;
/*    */ 
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.events.player.UpdateEvent;
/*    */ import rip.diavlo.base.utils.MinecraftUtil;
/*    */ 
/*    */ public class FullBright
/*    */   extends Module {
/*    */   private float oldBrightness;
/*    */   
/*    */   public FullBright() {
/* 14 */     super("Fullbright", 49, Category.RENDER);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 19 */     super.onEnable();
/* 20 */     this.oldBrightness = MinecraftUtil.mc.gameSettings.gammaSetting;
/*    */   }
/*    */   
/*    */   @Subscribe
/*    */   public void onUpdate(UpdateEvent event) {
/* 25 */     MinecraftUtil.mc.gameSettings.gammaSetting = 10.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 30 */     super.onDisable();
/* 31 */     MinecraftUtil.mc.gameSettings.gammaSetting = this.oldBrightness;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSuffix() {
/* 36 */     return "";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\render\FullBright.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
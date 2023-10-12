/*    */ package rip.diavlo.base.modules.movement;
/*    */ 
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.api.value.impl.ModeValue;
/*    */ import rip.diavlo.base.events.player.UpdateEvent;
/*    */ 
/*    */ 
/*    */ public class Sprint
/*    */   extends Module
/*    */ {
/* 13 */   public ModeValue sempre = new ModeValue("Sprint constante", this, new String[] { "No", "Si" });
/*    */   
/*    */   public Sprint() {
/* 16 */     super("Sprint", 44, Category.MOVEMENT);
/* 17 */     getValues().add(this.sempre);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 22 */     super.onEnable();
/*    */   }
/*    */   
/*    */   @Subscribe
/*    */   public void onUpdate(UpdateEvent event) {
/* 27 */     if (mc.thePlayer.moveForward > 0.0F || this.sempre.get() == "Si") {
/* 28 */       mc.thePlayer.setSprinting(true);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 34 */     super.onDisable();
/* 35 */     mc.thePlayer.setSprinting(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSuffix() {
/* 40 */     return (this.sempre.get() == "Si") ? "Si" : "No";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\movement\Sprint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
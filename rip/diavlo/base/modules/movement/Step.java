/*    */ package rip.diavlo.base.modules.movement;
/*    */ 
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.api.value.impl.ModeValue;
/*    */ import rip.diavlo.base.events.player.UpdateEvent;
/*    */ 
/*    */ public class Step
/*    */   extends Module
/*    */ {
/* 12 */   public final ModeValue height = new ModeValue("Speed", this, new String[] { "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5", "5.5", "6", "6.5", "7", "7.5", "8", "8.5", "9", "9.5", "10" });
/*    */   
/*    */   public Step() {
/* 15 */     super("Step", 0, Category.MOVEMENT);
/* 16 */     getValues().add(this.height);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSuffix() {
/* 21 */     return (String)this.height.get();
/*    */   }
/*    */   
/*    */   @Subscribe
/*    */   public void onUpdate(UpdateEvent event) {
/* 26 */     mc.thePlayer.stepHeight = Float.parseFloat((String)this.height.get());
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 31 */     super.onDisable();
/* 32 */     mc.thePlayer.stepHeight = 0.6F;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\movement\Step.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
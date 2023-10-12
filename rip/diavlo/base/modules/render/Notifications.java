/*    */ package rip.diavlo.base.modules.render;
/*    */ 
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.events.render.RenderGuiEvent;
/*    */ import rip.diavlo.base.ui.Notification;
/*    */ 
/*    */ public class Notifications
/*    */   extends Module {
/*    */   public static Notification getCurrentNotification() {
/* 13 */     return currentNotification;
/*    */   }
/*    */   private static Notification currentNotification;
/*    */   
/*    */   public void addNotification(Notification n) {
/* 18 */     currentNotification = n;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Subscribe
/*    */   public void onRenderGui(RenderGuiEvent event) {
/* 25 */     if (currentNotification != null)
/*    */     {
/* 27 */       if (!currentNotification.hasFinished) {
/*    */         
/* 29 */         currentNotification.drawNotification();
/*    */       }
/*    */       else {
/*    */         
/* 33 */         currentNotification = null;
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   private static Minecraft mc = Minecraft.getMinecraft();
/*    */   
/*    */   public Notifications() {
/* 44 */     super("Notification", 0, Category.RENDER);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\render\Notifications.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
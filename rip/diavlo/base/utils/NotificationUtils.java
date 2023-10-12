/*    */ package rip.diavlo.base.utils;
/*    */ 
/*    */ import rip.diavlo.base.Client;
/*    */ import rip.diavlo.base.modules.render.Notifications;
/*    */ import rip.diavlo.base.ui.Notification;
/*    */ 
/*    */ 
/*    */ public class NotificationUtils
/*    */ {
/*    */   public static void drawNotification(String s) {
/* 11 */     ((Notifications)Client.getInstance().getModuleManager().get(Notifications.class)).addNotification(new Notification(s));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\NotificationUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
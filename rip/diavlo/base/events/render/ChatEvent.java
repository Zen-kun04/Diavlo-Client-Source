/*    */ package rip.diavlo.base.events.render;
/*    */ 
/*    */ import rip.diavlo.base.api.event.Event;
/*    */ 
/*    */ public class ChatEvent extends Event {
/*    */   public ChatEvent(String message) {
/*  7 */     this.message = message;
/*    */   }
/*    */   private String message;
/*    */   public String getMessage() {
/* 11 */     return this.message;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\events\render\ChatEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
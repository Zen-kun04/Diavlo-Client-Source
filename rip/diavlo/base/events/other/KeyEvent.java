/*    */ package rip.diavlo.base.events.other;
/*    */ 
/*    */ import rip.diavlo.base.api.event.Event;
/*    */ 
/*    */ public class KeyEvent
/*    */   extends Event {
/*    */   public KeyEvent(int key) {
/*  8 */     this.key = key;
/*    */   } private final int key;
/*    */   public int getKey() {
/* 11 */     return this.key;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\events\other\KeyEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
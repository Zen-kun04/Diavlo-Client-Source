/*    */ package org.yaml.snakeyaml.events;
/*    */ 
/*    */ import org.yaml.snakeyaml.error.Mark;
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
/*    */ public final class SequenceEndEvent
/*    */   extends CollectionEndEvent
/*    */ {
/*    */   public SequenceEndEvent(Mark startMark, Mark endMark) {
/* 26 */     super(startMark, endMark);
/*    */   }
/*    */ 
/*    */   
/*    */   public Event.ID getEventId() {
/* 31 */     return Event.ID.SequenceEnd;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\org\yaml\snakeyaml\events\SequenceEndEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
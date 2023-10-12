/*    */ package org.yaml.snakeyaml.constructor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DuplicateKeyException
/*    */   extends ConstructorException
/*    */ {
/*    */   protected DuplicateKeyException(Mark contextMark, Object key, Mark problemMark) {
/* 31 */     super("while constructing a mapping", contextMark, "found duplicate key " + key, problemMark);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\org\yaml\snakeyaml\constructor\DuplicateKeyException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package org.yaml.snakeyaml.tokens;
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
/*    */ public final class ValueToken
/*    */   extends Token
/*    */ {
/*    */   public ValueToken(Mark startMark, Mark endMark) {
/* 21 */     super(startMark, endMark);
/*    */   }
/*    */ 
/*    */   
/*    */   public Token.ID getTokenId() {
/* 26 */     return Token.ID.Value;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\org\yaml\snakeyaml\tokens\ValueToken.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
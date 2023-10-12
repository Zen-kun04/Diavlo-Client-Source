/*    */ package net.optifine.expr;
/*    */ 
/*    */ 
/*    */ public class Token
/*    */ {
/*    */   private TokenType type;
/*    */   private String text;
/*    */   
/*    */   public Token(TokenType type, String text) {
/* 10 */     this.type = type;
/* 11 */     this.text = text;
/*    */   }
/*    */ 
/*    */   
/*    */   public TokenType getType() {
/* 16 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getText() {
/* 21 */     return this.text;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 26 */     return this.text;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\expr\Token.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
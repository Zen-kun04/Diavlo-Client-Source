/*    */ package net.optifine.expr;
/*    */ 
/*    */ public class Parameters
/*    */   implements IParameters
/*    */ {
/*    */   private ExpressionType[] parameterTypes;
/*    */   
/*    */   public Parameters(ExpressionType[] parameterTypes) {
/*  9 */     this.parameterTypes = parameterTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public ExpressionType[] getParameterTypes(IExpression[] params) {
/* 14 */     return this.parameterTypes;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\expr\Parameters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
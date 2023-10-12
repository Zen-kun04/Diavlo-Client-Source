/*    */ package net.optifine.entity.model.anim;
/*    */ 
/*    */ import net.minecraft.client.model.ModelRenderer;
/*    */ import net.optifine.expr.ExpressionType;
/*    */ import net.optifine.expr.IExpressionFloat;
/*    */ 
/*    */ public class ModelVariableFloat
/*    */   implements IExpressionFloat
/*    */ {
/*    */   private String name;
/*    */   private ModelRenderer modelRenderer;
/*    */   private ModelVariableType enumModelVariable;
/*    */   
/*    */   public ModelVariableFloat(String name, ModelRenderer modelRenderer, ModelVariableType enumModelVariable) {
/* 15 */     this.name = name;
/* 16 */     this.modelRenderer = modelRenderer;
/* 17 */     this.enumModelVariable = enumModelVariable;
/*    */   }
/*    */ 
/*    */   
/*    */   public ExpressionType getExpressionType() {
/* 22 */     return ExpressionType.FLOAT;
/*    */   }
/*    */ 
/*    */   
/*    */   public float eval() {
/* 27 */     return getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public float getValue() {
/* 32 */     return this.enumModelVariable.getFloat(this.modelRenderer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(float value) {
/* 37 */     this.enumModelVariable.setFloat(this.modelRenderer, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 42 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\entity\model\anim\ModelVariableFloat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.optifine.shaders.uniform;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.optifine.expr.IExpression;
/*    */ import net.optifine.expr.IExpressionCached;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CustomUniforms
/*    */ {
/*    */   private CustomUniform[] uniforms;
/*    */   private IExpressionCached[] expressionsCached;
/*    */   
/*    */   public CustomUniforms(CustomUniform[] uniforms, Map<String, IExpression> mapExpressions) {
/* 17 */     this.uniforms = uniforms;
/* 18 */     List<IExpressionCached> list = new ArrayList<>();
/*    */     
/* 20 */     for (String s : mapExpressions.keySet()) {
/*    */       
/* 22 */       IExpression iexpression = mapExpressions.get(s);
/*    */       
/* 24 */       if (iexpression instanceof IExpressionCached) {
/*    */         
/* 26 */         IExpressionCached iexpressioncached = (IExpressionCached)iexpression;
/* 27 */         list.add(iexpressioncached);
/*    */       } 
/*    */     } 
/*    */     
/* 31 */     this.expressionsCached = list.<IExpressionCached>toArray(new IExpressionCached[list.size()]);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setProgram(int program) {
/* 36 */     for (int i = 0; i < this.uniforms.length; i++) {
/*    */       
/* 38 */       CustomUniform customuniform = this.uniforms[i];
/* 39 */       customuniform.setProgram(program);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 45 */     resetCache();
/*    */     
/* 47 */     for (int i = 0; i < this.uniforms.length; i++) {
/*    */       
/* 49 */       CustomUniform customuniform = this.uniforms[i];
/* 50 */       customuniform.update();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private void resetCache() {
/* 56 */     for (int i = 0; i < this.expressionsCached.length; i++) {
/*    */       
/* 58 */       IExpressionCached iexpressioncached = this.expressionsCached[i];
/* 59 */       iexpressioncached.reset();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void reset() {
/* 65 */     for (int i = 0; i < this.uniforms.length; i++) {
/*    */       
/* 67 */       CustomUniform customuniform = this.uniforms[i];
/* 68 */       customuniform.reset();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\shader\\uniform\CustomUniforms.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
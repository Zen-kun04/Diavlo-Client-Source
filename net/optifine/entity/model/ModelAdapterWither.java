/*    */ package net.optifine.entity.model;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.model.ModelRenderer;
/*    */ import net.minecraft.client.model.ModelWither;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.client.renderer.entity.RenderWither;
/*    */ import net.minecraft.entity.boss.EntityWither;
/*    */ import net.minecraft.src.Config;
/*    */ import net.optifine.reflect.Reflector;
/*    */ 
/*    */ public class ModelAdapterWither
/*    */   extends ModelAdapter
/*    */ {
/*    */   public ModelAdapterWither() {
/* 17 */     super(EntityWither.class, "wither", 0.5F);
/*    */   }
/*    */ 
/*    */   
/*    */   public ModelBase makeModel() {
/* 22 */     return (ModelBase)new ModelWither(0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
/* 27 */     if (!(model instanceof ModelWither))
/*    */     {
/* 29 */       return null;
/*    */     }
/*    */ 
/*    */     
/* 33 */     ModelWither modelwither = (ModelWither)model;
/* 34 */     String s = "body";
/*    */     
/* 36 */     if (modelPart.startsWith(s)) {
/*    */       
/* 38 */       ModelRenderer[] amodelrenderer1 = (ModelRenderer[])Reflector.getFieldValue(modelwither, Reflector.ModelWither_bodyParts);
/*    */       
/* 40 */       if (amodelrenderer1 == null)
/*    */       {
/* 42 */         return null;
/*    */       }
/*    */ 
/*    */       
/* 46 */       String s3 = modelPart.substring(s.length());
/* 47 */       int j = Config.parseInt(s3, -1);
/* 48 */       j--; return (
/* 49 */         j >= 0 && j < amodelrenderer1.length) ? amodelrenderer1[j] : null;
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 54 */     String s1 = "head";
/*    */     
/* 56 */     if (modelPart.startsWith(s1)) {
/*    */       
/* 58 */       ModelRenderer[] amodelrenderer = (ModelRenderer[])Reflector.getFieldValue(modelwither, Reflector.ModelWither_heads);
/*    */       
/* 60 */       if (amodelrenderer == null)
/*    */       {
/* 62 */         return null;
/*    */       }
/*    */ 
/*    */       
/* 66 */       String s2 = modelPart.substring(s1.length());
/* 67 */       int i = Config.parseInt(s2, -1);
/* 68 */       i--; return (
/* 69 */         i >= 0 && i < amodelrenderer.length) ? amodelrenderer[i] : null;
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 74 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] getModelRendererNames() {
/* 82 */     return new String[] { "body1", "body2", "body3", "head1", "head2", "head3" };
/*    */   }
/*    */ 
/*    */   
/*    */   public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
/* 87 */     RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
/* 88 */     RenderWither renderwither = new RenderWither(rendermanager);
/* 89 */     renderwither.mainModel = modelBase;
/* 90 */     renderwither.shadowSize = shadowSize;
/* 91 */     return (IEntityRenderer)renderwither;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\entity\model\ModelAdapterWither.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.passive.EntityRabbit;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderRabbit extends RenderLiving<EntityRabbit> {
/* 10 */   private static final ResourceLocation BROWN = new ResourceLocation("textures/entity/rabbit/brown.png");
/* 11 */   private static final ResourceLocation WHITE = new ResourceLocation("textures/entity/rabbit/white.png");
/* 12 */   private static final ResourceLocation BLACK = new ResourceLocation("textures/entity/rabbit/black.png");
/* 13 */   private static final ResourceLocation GOLD = new ResourceLocation("textures/entity/rabbit/gold.png");
/* 14 */   private static final ResourceLocation SALT = new ResourceLocation("textures/entity/rabbit/salt.png");
/* 15 */   private static final ResourceLocation WHITE_SPLOTCHED = new ResourceLocation("textures/entity/rabbit/white_splotched.png");
/* 16 */   private static final ResourceLocation TOAST = new ResourceLocation("textures/entity/rabbit/toast.png");
/* 17 */   private static final ResourceLocation CAERBANNOG = new ResourceLocation("textures/entity/rabbit/caerbannog.png");
/*    */ 
/*    */   
/*    */   public RenderRabbit(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
/* 21 */     super(renderManagerIn, modelBaseIn, shadowSizeIn);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityRabbit entity) {
/* 26 */     String s = EnumChatFormatting.getTextWithoutFormattingCodes(entity.getName());
/*    */     
/* 28 */     if (s != null && s.equals("Toast"))
/*    */     {
/* 30 */       return TOAST;
/*    */     }
/*    */ 
/*    */     
/* 34 */     switch (entity.getRabbitType()) {
/*    */ 
/*    */       
/*    */       default:
/* 38 */         return BROWN;
/*    */       
/*    */       case 1:
/* 41 */         return WHITE;
/*    */       
/*    */       case 2:
/* 44 */         return BLACK;
/*    */       
/*    */       case 3:
/* 47 */         return WHITE_SPLOTCHED;
/*    */       
/*    */       case 4:
/* 50 */         return GOLD;
/*    */       
/*    */       case 5:
/* 53 */         return SALT;
/*    */       case 99:
/*    */         break;
/* 56 */     }  return CAERBANNOG;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderRabbit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.optifine.player;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.client.model.ModelBiped;
/*    */ import net.minecraft.client.model.ModelPlayer;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.RenderPlayer;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerRenderer;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.src.Config;
/*    */ 
/*    */ public class PlayerItemsLayer
/*    */   implements LayerRenderer {
/* 16 */   private RenderPlayer renderPlayer = null;
/*    */ 
/*    */   
/*    */   public PlayerItemsLayer(RenderPlayer renderPlayer) {
/* 20 */     this.renderPlayer = renderPlayer;
/*    */   }
/*    */ 
/*    */   
/*    */   public void doRenderLayer(EntityLivingBase entityLiving, float limbSwing, float limbSwingAmount, float partialTicks, float ticksExisted, float headYaw, float rotationPitch, float scale) {
/* 25 */     renderEquippedItems(entityLiving, scale, partialTicks);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderEquippedItems(EntityLivingBase entityLiving, float scale, float partialTicks) {
/* 30 */     if (Config.isShowCapes())
/*    */     {
/* 32 */       if (!entityLiving.isInvisible())
/*    */       {
/* 34 */         if (entityLiving instanceof AbstractClientPlayer) {
/*    */           
/* 36 */           AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entityLiving;
/* 37 */           GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 38 */           GlStateManager.disableRescaleNormal();
/* 39 */           GlStateManager.enableCull();
/* 40 */           ModelPlayer modelPlayer = this.renderPlayer.getMainModel();
/* 41 */           PlayerConfigurations.renderPlayerItems((ModelBiped)modelPlayer, abstractclientplayer, scale, partialTicks);
/* 42 */           GlStateManager.disableCull();
/*    */         } 
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldCombineTextures() {
/* 50 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void register(Map renderPlayerMap) {
/* 55 */     Set set = renderPlayerMap.keySet();
/* 56 */     boolean flag = false;
/*    */     
/* 58 */     for (Object object : set) {
/*    */       
/* 60 */       Object object1 = renderPlayerMap.get(object);
/*    */       
/* 62 */       if (object1 instanceof RenderPlayer) {
/*    */         
/* 64 */         RenderPlayer renderplayer = (RenderPlayer)object1;
/* 65 */         renderplayer.addLayer(new PlayerItemsLayer(renderplayer));
/* 66 */         flag = true;
/*    */       } 
/*    */     } 
/*    */     
/* 70 */     if (!flag)
/*    */     {
/* 72 */       Config.warn("PlayerItemsLayer not registered");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\player\PlayerItemsLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
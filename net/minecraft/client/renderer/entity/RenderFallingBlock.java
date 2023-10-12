/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.BlockRendererDispatcher;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.texture.TextureMap;
/*    */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*    */ import net.minecraft.client.resources.model.IBakedModel;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityFallingBlock;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class RenderFallingBlock extends Render<EntityFallingBlock> {
/*    */   public RenderFallingBlock(RenderManager renderManagerIn) {
/* 22 */     super(renderManagerIn);
/* 23 */     this.shadowSize = 0.5F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void doRender(EntityFallingBlock entity, double x, double y, double z, float entityYaw, float partialTicks) {
/* 28 */     if (entity.getBlock() != null) {
/*    */       
/* 30 */       bindTexture(TextureMap.locationBlocksTexture);
/* 31 */       IBlockState iblockstate = entity.getBlock();
/* 32 */       Block block = iblockstate.getBlock();
/* 33 */       BlockPos blockpos = new BlockPos((Entity)entity);
/* 34 */       World world = entity.getWorldObj();
/*    */       
/* 36 */       if (iblockstate != world.getBlockState(blockpos) && block.getRenderType() != -1)
/*    */       {
/* 38 */         if (block.getRenderType() == 3) {
/*    */           
/* 40 */           GlStateManager.pushMatrix();
/* 41 */           GlStateManager.translate((float)x, (float)y, (float)z);
/* 42 */           GlStateManager.disableLighting();
/* 43 */           Tessellator tessellator = Tessellator.getInstance();
/* 44 */           WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 45 */           worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
/* 46 */           int i = blockpos.getX();
/* 47 */           int j = blockpos.getY();
/* 48 */           int k = blockpos.getZ();
/* 49 */           worldrenderer.setTranslation((-i - 0.5F), -j, (-k - 0.5F));
/* 50 */           BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
/* 51 */           IBakedModel ibakedmodel = blockrendererdispatcher.getModelFromBlockState(iblockstate, (IBlockAccess)world, (BlockPos)null);
/* 52 */           blockrendererdispatcher.getBlockModelRenderer().renderModel((IBlockAccess)world, ibakedmodel, iblockstate, blockpos, worldrenderer, false);
/* 53 */           worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
/* 54 */           tessellator.draw();
/* 55 */           GlStateManager.enableLighting();
/* 56 */           GlStateManager.popMatrix();
/* 57 */           super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */         } 
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityFallingBlock entity) {
/* 65 */     return TextureMap.locationBlocksTexture;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderFallingBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
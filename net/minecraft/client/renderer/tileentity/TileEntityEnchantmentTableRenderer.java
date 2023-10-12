/*    */ package net.minecraft.client.renderer.tileentity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelBook;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.tileentity.TileEntityEnchantmentTable;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class TileEntityEnchantmentTableRenderer extends TileEntitySpecialRenderer<TileEntityEnchantmentTable> {
/* 12 */   private static final ResourceLocation TEXTURE_BOOK = new ResourceLocation("textures/entity/enchanting_table_book.png");
/* 13 */   private ModelBook field_147541_c = new ModelBook();
/*    */ 
/*    */   
/*    */   public void renderTileEntityAt(TileEntityEnchantmentTable te, double x, double y, double z, float partialTicks, int destroyStage) {
/* 17 */     GlStateManager.pushMatrix();
/* 18 */     GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F, (float)z + 0.5F);
/* 19 */     float f = te.tickCount + partialTicks;
/* 20 */     GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(f * 0.1F) * 0.01F, 0.0F);
/*    */     
/*    */     float f1;
/* 23 */     for (f1 = te.bookRotation - te.bookRotationPrev; f1 >= 3.1415927F; f1 -= 6.2831855F);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 28 */     while (f1 < -3.1415927F)
/*    */     {
/* 30 */       f1 += 6.2831855F;
/*    */     }
/*    */     
/* 33 */     float f2 = te.bookRotationPrev + f1 * partialTicks;
/* 34 */     GlStateManager.rotate(-f2 * 180.0F / 3.1415927F, 0.0F, 1.0F, 0.0F);
/* 35 */     GlStateManager.rotate(80.0F, 0.0F, 0.0F, 1.0F);
/* 36 */     bindTexture(TEXTURE_BOOK);
/* 37 */     float f3 = te.pageFlipPrev + (te.pageFlip - te.pageFlipPrev) * partialTicks + 0.25F;
/* 38 */     float f4 = te.pageFlipPrev + (te.pageFlip - te.pageFlipPrev) * partialTicks + 0.75F;
/* 39 */     f3 = (f3 - MathHelper.truncateDoubleToInt(f3)) * 1.6F - 0.3F;
/* 40 */     f4 = (f4 - MathHelper.truncateDoubleToInt(f4)) * 1.6F - 0.3F;
/*    */     
/* 42 */     if (f3 < 0.0F)
/*    */     {
/* 44 */       f3 = 0.0F;
/*    */     }
/*    */     
/* 47 */     if (f4 < 0.0F)
/*    */     {
/* 49 */       f4 = 0.0F;
/*    */     }
/*    */     
/* 52 */     if (f3 > 1.0F)
/*    */     {
/* 54 */       f3 = 1.0F;
/*    */     }
/*    */     
/* 57 */     if (f4 > 1.0F)
/*    */     {
/* 59 */       f4 = 1.0F;
/*    */     }
/*    */     
/* 62 */     float f5 = te.bookSpreadPrev + (te.bookSpread - te.bookSpreadPrev) * partialTicks;
/* 63 */     GlStateManager.enableCull();
/* 64 */     this.field_147541_c.render((Entity)null, f, f3, f4, f5, 0.0F, 0.0625F);
/* 65 */     GlStateManager.popMatrix();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\tileentity\TileEntityEnchantmentTableRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
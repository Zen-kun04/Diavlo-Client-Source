/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.effect.EntityLightningBolt;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class RenderLightningBolt
/*     */   extends Render<EntityLightningBolt> {
/*     */   public RenderLightningBolt(RenderManager renderManagerIn) {
/*  15 */     super(renderManagerIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void doRender(EntityLightningBolt entity, double x, double y, double z, float entityYaw, float partialTicks) {
/*  20 */     Tessellator tessellator = Tessellator.getInstance();
/*  21 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  22 */     GlStateManager.disableTexture2D();
/*  23 */     GlStateManager.disableLighting();
/*  24 */     GlStateManager.enableBlend();
/*  25 */     GlStateManager.blendFunc(770, 1);
/*  26 */     double[] adouble = new double[8];
/*  27 */     double[] adouble1 = new double[8];
/*  28 */     double d0 = 0.0D;
/*  29 */     double d1 = 0.0D;
/*  30 */     Random random = new Random(entity.boltVertex);
/*     */     
/*  32 */     for (int i = 7; i >= 0; i--) {
/*     */       
/*  34 */       adouble[i] = d0;
/*  35 */       adouble1[i] = d1;
/*  36 */       d0 += (random.nextInt(11) - 5);
/*  37 */       d1 += (random.nextInt(11) - 5);
/*     */     } 
/*     */     
/*  40 */     for (int k1 = 0; k1 < 4; k1++) {
/*     */       
/*  42 */       Random random1 = new Random(entity.boltVertex);
/*     */       
/*  44 */       for (int j = 0; j < 3; j++) {
/*     */         
/*  46 */         int k = 7;
/*  47 */         int l = 0;
/*     */         
/*  49 */         if (j > 0)
/*     */         {
/*  51 */           k = 7 - j;
/*     */         }
/*     */         
/*  54 */         if (j > 0)
/*     */         {
/*  56 */           l = k - 2;
/*     */         }
/*     */         
/*  59 */         double d2 = adouble[k] - d0;
/*  60 */         double d3 = adouble1[k] - d1;
/*     */         
/*  62 */         for (int i1 = k; i1 >= l; i1--) {
/*     */           
/*  64 */           double d4 = d2;
/*  65 */           double d5 = d3;
/*     */           
/*  67 */           if (j == 0) {
/*     */             
/*  69 */             d2 += (random1.nextInt(11) - 5);
/*  70 */             d3 += (random1.nextInt(11) - 5);
/*     */           }
/*     */           else {
/*     */             
/*  74 */             d2 += (random1.nextInt(31) - 15);
/*  75 */             d3 += (random1.nextInt(31) - 15);
/*     */           } 
/*     */           
/*  78 */           worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
/*  79 */           float f = 0.5F;
/*  80 */           float f1 = 0.45F;
/*  81 */           float f2 = 0.45F;
/*  82 */           float f3 = 0.5F;
/*  83 */           double d6 = 0.1D + k1 * 0.2D;
/*     */           
/*  85 */           if (j == 0)
/*     */           {
/*  87 */             d6 *= i1 * 0.1D + 1.0D;
/*     */           }
/*     */           
/*  90 */           double d7 = 0.1D + k1 * 0.2D;
/*     */           
/*  92 */           if (j == 0)
/*     */           {
/*  94 */             d7 *= (i1 - 1) * 0.1D + 1.0D;
/*     */           }
/*     */           
/*  97 */           for (int j1 = 0; j1 < 5; j1++) {
/*     */             
/*  99 */             double d8 = x + 0.5D - d6;
/* 100 */             double d9 = z + 0.5D - d6;
/*     */             
/* 102 */             if (j1 == 1 || j1 == 2)
/*     */             {
/* 104 */               d8 += d6 * 2.0D;
/*     */             }
/*     */             
/* 107 */             if (j1 == 2 || j1 == 3)
/*     */             {
/* 109 */               d9 += d6 * 2.0D;
/*     */             }
/*     */             
/* 112 */             double d10 = x + 0.5D - d7;
/* 113 */             double d11 = z + 0.5D - d7;
/*     */             
/* 115 */             if (j1 == 1 || j1 == 2)
/*     */             {
/* 117 */               d10 += d7 * 2.0D;
/*     */             }
/*     */             
/* 120 */             if (j1 == 2 || j1 == 3)
/*     */             {
/* 122 */               d11 += d7 * 2.0D;
/*     */             }
/*     */             
/* 125 */             worldrenderer.pos(d10 + d2, y + (i1 * 16), d11 + d3).color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
/* 126 */             worldrenderer.pos(d8 + d4, y + ((i1 + 1) * 16), d9 + d5).color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
/*     */           } 
/*     */           
/* 129 */           tessellator.draw();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 134 */     GlStateManager.disableBlend();
/* 135 */     GlStateManager.enableLighting();
/* 136 */     GlStateManager.enableTexture2D();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ResourceLocation getEntityTexture(EntityLightningBolt entity) {
/* 141 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderLightningBolt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
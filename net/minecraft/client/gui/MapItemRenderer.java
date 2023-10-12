/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec4b;
/*     */ import net.minecraft.world.storage.MapData;
/*     */ 
/*     */ public class MapItemRenderer
/*     */ {
/*  18 */   private static final ResourceLocation mapIcons = new ResourceLocation("textures/map/map_icons.png");
/*     */   private final TextureManager textureManager;
/*  20 */   private final Map<String, Instance> loadedMaps = Maps.newHashMap();
/*     */ 
/*     */   
/*     */   public MapItemRenderer(TextureManager textureManagerIn) {
/*  24 */     this.textureManager = textureManagerIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateMapTexture(MapData mapdataIn) {
/*  29 */     getMapRendererInstance(mapdataIn).updateMapTexture();
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderMap(MapData mapdataIn, boolean p_148250_2_) {
/*  34 */     getMapRendererInstance(mapdataIn).render(p_148250_2_);
/*     */   }
/*     */ 
/*     */   
/*     */   private Instance getMapRendererInstance(MapData mapdataIn) {
/*  39 */     Instance mapitemrenderer$instance = this.loadedMaps.get(mapdataIn.mapName);
/*     */     
/*  41 */     if (mapitemrenderer$instance == null) {
/*     */       
/*  43 */       mapitemrenderer$instance = new Instance(mapdataIn);
/*  44 */       this.loadedMaps.put(mapdataIn.mapName, mapitemrenderer$instance);
/*     */     } 
/*     */     
/*  47 */     return mapitemrenderer$instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearLoadedMaps() {
/*  52 */     for (Instance mapitemrenderer$instance : this.loadedMaps.values())
/*     */     {
/*  54 */       this.textureManager.deleteTexture(mapitemrenderer$instance.location);
/*     */     }
/*     */     
/*  57 */     this.loadedMaps.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   class Instance
/*     */   {
/*     */     private final MapData mapData;
/*     */     private final DynamicTexture mapTexture;
/*     */     private final ResourceLocation location;
/*     */     private final int[] mapTextureData;
/*     */     
/*     */     private Instance(MapData mapdataIn) {
/*  69 */       this.mapData = mapdataIn;
/*  70 */       this.mapTexture = new DynamicTexture(128, 128);
/*  71 */       this.mapTextureData = this.mapTexture.getTextureData();
/*  72 */       this.location = MapItemRenderer.this.textureManager.getDynamicTextureLocation("map/" + mapdataIn.mapName, this.mapTexture);
/*     */       
/*  74 */       for (int i = 0; i < this.mapTextureData.length; i++)
/*     */       {
/*  76 */         this.mapTextureData[i] = 0;
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     private void updateMapTexture() {
/*  82 */       for (int i = 0; i < 16384; i++) {
/*     */         
/*  84 */         int j = this.mapData.colors[i] & 0xFF;
/*     */         
/*  86 */         if (j / 4 == 0) {
/*     */           
/*  88 */           this.mapTextureData[i] = (i + i / 128 & 0x1) * 8 + 16 << 24;
/*     */         }
/*     */         else {
/*     */           
/*  92 */           this.mapTextureData[i] = MapColor.mapColorArray[j / 4].getMapColor(j & 0x3);
/*     */         } 
/*     */       } 
/*     */       
/*  96 */       this.mapTexture.updateDynamicTexture();
/*     */     }
/*     */ 
/*     */     
/*     */     private void render(boolean noOverlayRendering) {
/* 101 */       int i = 0;
/* 102 */       int j = 0;
/* 103 */       Tessellator tessellator = Tessellator.getInstance();
/* 104 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 105 */       float f = 0.0F;
/* 106 */       MapItemRenderer.this.textureManager.bindTexture(this.location);
/* 107 */       GlStateManager.enableBlend();
/* 108 */       GlStateManager.tryBlendFuncSeparate(1, 771, 0, 1);
/* 109 */       GlStateManager.disableAlpha();
/* 110 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 111 */       worldrenderer.pos(((i + 0) + f), ((j + 128) - f), -0.009999999776482582D).tex(0.0D, 1.0D).endVertex();
/* 112 */       worldrenderer.pos(((i + 128) - f), ((j + 128) - f), -0.009999999776482582D).tex(1.0D, 1.0D).endVertex();
/* 113 */       worldrenderer.pos(((i + 128) - f), ((j + 0) + f), -0.009999999776482582D).tex(1.0D, 0.0D).endVertex();
/* 114 */       worldrenderer.pos(((i + 0) + f), ((j + 0) + f), -0.009999999776482582D).tex(0.0D, 0.0D).endVertex();
/* 115 */       tessellator.draw();
/* 116 */       GlStateManager.enableAlpha();
/* 117 */       GlStateManager.disableBlend();
/* 118 */       MapItemRenderer.this.textureManager.bindTexture(MapItemRenderer.mapIcons);
/* 119 */       int k = 0;
/*     */       
/* 121 */       for (Vec4b vec4b : this.mapData.mapDecorations.values()) {
/*     */         
/* 123 */         if (!noOverlayRendering || vec4b.func_176110_a() == 1) {
/*     */           
/* 125 */           GlStateManager.pushMatrix();
/* 126 */           GlStateManager.translate(i + vec4b.func_176112_b() / 2.0F + 64.0F, j + vec4b.func_176113_c() / 2.0F + 64.0F, -0.02F);
/* 127 */           GlStateManager.rotate((vec4b.func_176111_d() * 360) / 16.0F, 0.0F, 0.0F, 1.0F);
/* 128 */           GlStateManager.scale(4.0F, 4.0F, 3.0F);
/* 129 */           GlStateManager.translate(-0.125F, 0.125F, 0.0F);
/* 130 */           byte b0 = vec4b.func_176110_a();
/* 131 */           float f1 = (b0 % 4 + 0) / 4.0F;
/* 132 */           float f2 = (b0 / 4 + 0) / 4.0F;
/* 133 */           float f3 = (b0 % 4 + 1) / 4.0F;
/* 134 */           float f4 = (b0 / 4 + 1) / 4.0F;
/* 135 */           worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 136 */           float f5 = -0.001F;
/* 137 */           worldrenderer.pos(-1.0D, 1.0D, (k * -0.001F)).tex(f1, f2).endVertex();
/* 138 */           worldrenderer.pos(1.0D, 1.0D, (k * -0.001F)).tex(f3, f2).endVertex();
/* 139 */           worldrenderer.pos(1.0D, -1.0D, (k * -0.001F)).tex(f3, f4).endVertex();
/* 140 */           worldrenderer.pos(-1.0D, -1.0D, (k * -0.001F)).tex(f1, f4).endVertex();
/* 141 */           tessellator.draw();
/* 142 */           GlStateManager.popMatrix();
/* 143 */           k++;
/*     */         } 
/*     */       } 
/*     */       
/* 147 */       GlStateManager.pushMatrix();
/* 148 */       GlStateManager.translate(0.0F, 0.0F, -0.04F);
/* 149 */       GlStateManager.scale(1.0F, 1.0F, 1.0F);
/* 150 */       GlStateManager.popMatrix();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\MapItemRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
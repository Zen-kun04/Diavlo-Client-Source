/*     */ package net.minecraft.client.renderer.tileentity;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.renderer.BlockRendererDispatcher;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
/*     */ import net.minecraft.client.renderer.entity.Render;
/*     */ import net.minecraft.client.renderer.entity.RenderItem;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureCompass;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.resources.model.IBakedModel;
/*     */ import net.minecraft.client.resources.model.ModelManager;
/*     */ import net.minecraft.client.resources.model.ModelResourceLocation;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.storage.MapData;
/*     */ import net.optifine.reflect.Reflector;
/*     */ import net.optifine.shaders.Shaders;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class RenderItemFrame
/*     */   extends Render<EntityItemFrame>
/*     */ {
/*  40 */   private static final ResourceLocation mapBackgroundTextures = new ResourceLocation("textures/map/map_background.png");
/*  41 */   private final Minecraft mc = Minecraft.getMinecraft();
/*  42 */   private final ModelResourceLocation itemFrameModel = new ModelResourceLocation("item_frame", "normal");
/*  43 */   private final ModelResourceLocation mapModel = new ModelResourceLocation("item_frame", "map");
/*     */   private RenderItem itemRenderer;
/*  45 */   private static double itemRenderDistanceSq = 4096.0D;
/*     */ 
/*     */   
/*     */   public RenderItemFrame(RenderManager renderManagerIn, RenderItem itemRendererIn) {
/*  49 */     super(renderManagerIn);
/*  50 */     this.itemRenderer = itemRendererIn;
/*     */   }
/*     */   
/*     */   public void doRender(EntityItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks) {
/*     */     IBakedModel ibakedmodel;
/*  55 */     GlStateManager.pushMatrix();
/*  56 */     BlockPos blockpos = entity.getHangingPosition();
/*  57 */     double d0 = blockpos.getX() - entity.posX + x;
/*  58 */     double d1 = blockpos.getY() - entity.posY + y;
/*  59 */     double d2 = blockpos.getZ() - entity.posZ + z;
/*  60 */     GlStateManager.translate(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
/*  61 */     GlStateManager.rotate(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
/*  62 */     this.renderManager.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
/*  63 */     BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
/*  64 */     ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
/*     */ 
/*     */     
/*  67 */     if (entity.getDisplayedItem() != null && entity.getDisplayedItem().getItem() == Items.filled_map) {
/*     */       
/*  69 */       ibakedmodel = modelmanager.getModel(this.mapModel);
/*     */     }
/*     */     else {
/*     */       
/*  73 */       ibakedmodel = modelmanager.getModel(this.itemFrameModel);
/*     */     } 
/*     */     
/*  76 */     GlStateManager.pushMatrix();
/*  77 */     GlStateManager.translate(-0.5F, -0.5F, -0.5F);
/*  78 */     blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(ibakedmodel, 1.0F, 1.0F, 1.0F, 1.0F);
/*  79 */     GlStateManager.popMatrix();
/*  80 */     GlStateManager.translate(0.0F, 0.0F, 0.4375F);
/*  81 */     renderItem(entity);
/*  82 */     GlStateManager.popMatrix();
/*  83 */     renderName(entity, x + (entity.facingDirection.getFrontOffsetX() * 0.3F), y - 0.25D, z + (entity.facingDirection.getFrontOffsetZ() * 0.3F));
/*     */   }
/*     */ 
/*     */   
/*     */   protected ResourceLocation getEntityTexture(EntityItemFrame entity) {
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderItem(EntityItemFrame itemFrame) {
/*  93 */     ItemStack itemstack = itemFrame.getDisplayedItem();
/*     */     
/*  95 */     if (itemstack != null) {
/*     */       
/*  97 */       if (!isRenderItem(itemFrame)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 102 */       if (!Config.zoomMode) {
/*     */         
/* 104 */         EntityPlayerSP entityPlayerSP = this.mc.thePlayer;
/* 105 */         double d0 = itemFrame.getDistanceSq(((Entity)entityPlayerSP).posX, ((Entity)entityPlayerSP).posY, ((Entity)entityPlayerSP).posZ);
/*     */         
/* 107 */         if (d0 > 4096.0D) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 113 */       EntityItem entityitem = new EntityItem(itemFrame.worldObj, 0.0D, 0.0D, 0.0D, itemstack);
/* 114 */       Item item = entityitem.getEntityItem().getItem();
/* 115 */       (entityitem.getEntityItem()).stackSize = 1;
/* 116 */       entityitem.hoverStart = 0.0F;
/* 117 */       GlStateManager.pushMatrix();
/* 118 */       GlStateManager.disableLighting();
/* 119 */       int i = itemFrame.getRotation();
/*     */       
/* 121 */       if (item instanceof net.minecraft.item.ItemMap)
/*     */       {
/* 123 */         i = i % 4 * 2;
/*     */       }
/*     */       
/* 126 */       GlStateManager.rotate(i * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);
/*     */       
/* 128 */       if (!Reflector.postForgeBusEvent(Reflector.RenderItemInFrameEvent_Constructor, new Object[] { itemFrame, this }))
/*     */       {
/* 130 */         if (item instanceof net.minecraft.item.ItemMap) {
/*     */           
/* 132 */           this.renderManager.renderEngine.bindTexture(mapBackgroundTextures);
/* 133 */           GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
/* 134 */           float f = 0.0078125F;
/* 135 */           GlStateManager.scale(f, f, f);
/* 136 */           GlStateManager.translate(-64.0F, -64.0F, 0.0F);
/* 137 */           MapData mapdata = Items.filled_map.getMapData(entityitem.getEntityItem(), itemFrame.worldObj);
/* 138 */           GlStateManager.translate(0.0F, 0.0F, -1.0F);
/*     */           
/* 140 */           if (mapdata != null)
/*     */           {
/* 142 */             this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, true);
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 147 */           TextureAtlasSprite textureatlassprite = null;
/*     */           
/* 149 */           if (item == Items.compass) {
/*     */             
/* 151 */             textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite(TextureCompass.locationSprite);
/* 152 */             this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
/*     */             
/* 154 */             if (textureatlassprite instanceof TextureCompass) {
/*     */               
/* 156 */               TextureCompass texturecompass = (TextureCompass)textureatlassprite;
/* 157 */               double d1 = texturecompass.currentAngle;
/* 158 */               double d2 = texturecompass.angleDelta;
/* 159 */               texturecompass.currentAngle = 0.0D;
/* 160 */               texturecompass.angleDelta = 0.0D;
/* 161 */               texturecompass.updateCompass(itemFrame.worldObj, itemFrame.posX, itemFrame.posZ, MathHelper.wrapAngleTo180_float((180 + itemFrame.facingDirection.getHorizontalIndex() * 90)), false, true);
/* 162 */               texturecompass.currentAngle = d1;
/* 163 */               texturecompass.angleDelta = d2;
/*     */             }
/*     */             else {
/*     */               
/* 167 */               textureatlassprite = null;
/*     */             } 
/*     */           } 
/*     */           
/* 171 */           GlStateManager.scale(0.5F, 0.5F, 0.5F);
/*     */           
/* 173 */           if (!this.itemRenderer.shouldRenderItemIn3D(entityitem.getEntityItem()) || item instanceof net.minecraft.item.ItemSkull)
/*     */           {
/* 175 */             GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
/*     */           }
/*     */           
/* 178 */           GlStateManager.pushAttrib();
/* 179 */           RenderHelper.enableStandardItemLighting();
/* 180 */           this.itemRenderer.renderItem(entityitem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);
/* 181 */           RenderHelper.disableStandardItemLighting();
/* 182 */           GlStateManager.popAttrib();
/*     */           
/* 184 */           if (textureatlassprite != null && textureatlassprite.getFrameCount() > 0)
/*     */           {
/* 186 */             textureatlassprite.updateAnimation();
/*     */           }
/*     */         } 
/*     */       }
/* 190 */       GlStateManager.enableLighting();
/* 191 */       GlStateManager.popMatrix();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderName(EntityItemFrame entity, double x, double y, double z) {
/* 197 */     if (Minecraft.isGuiEnabled() && entity.getDisplayedItem() != null && entity.getDisplayedItem().hasDisplayName() && this.renderManager.pointedEntity == entity) {
/*     */       
/* 199 */       float f = 1.6F;
/* 200 */       float f1 = 0.016666668F * f;
/* 201 */       double d0 = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
/* 202 */       float f2 = entity.isSneaking() ? 32.0F : 64.0F;
/*     */       
/* 204 */       if (d0 < (f2 * f2)) {
/*     */         
/* 206 */         String s = entity.getDisplayedItem().getDisplayName();
/*     */         
/* 208 */         if (entity.isSneaking()) {
/*     */           
/* 210 */           FontRenderer fontrenderer = getFontRendererFromRenderManager();
/* 211 */           GlStateManager.pushMatrix();
/* 212 */           GlStateManager.translate((float)x + 0.0F, (float)y + entity.height + 0.5F, (float)z);
/* 213 */           GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/* 214 */           GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
/* 215 */           GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
/* 216 */           GlStateManager.scale(-f1, -f1, f1);
/* 217 */           GlStateManager.disableLighting();
/* 218 */           GlStateManager.translate(0.0F, 0.25F / f1, 0.0F);
/* 219 */           GlStateManager.depthMask(false);
/* 220 */           GlStateManager.enableBlend();
/* 221 */           GlStateManager.blendFunc(770, 771);
/* 222 */           Tessellator tessellator = Tessellator.getInstance();
/* 223 */           WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 224 */           int i = fontrenderer.getStringWidth(s) / 2;
/* 225 */           GlStateManager.disableTexture2D();
/* 226 */           worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 227 */           worldrenderer.pos((-i - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 228 */           worldrenderer.pos((-i - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 229 */           worldrenderer.pos((i + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 230 */           worldrenderer.pos((i + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 231 */           tessellator.draw();
/* 232 */           GlStateManager.enableTexture2D();
/* 233 */           GlStateManager.depthMask(true);
/* 234 */           fontrenderer.drawString(s, (-fontrenderer.getStringWidth(s) / 2), 0.0D, 553648127);
/* 235 */           GlStateManager.enableLighting();
/* 236 */           GlStateManager.disableBlend();
/* 237 */           GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 238 */           GlStateManager.popMatrix();
/*     */         }
/*     */         else {
/*     */           
/* 242 */           renderLivingLabel((Entity)entity, s, x, y, z, 64);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isRenderItem(EntityItemFrame p_isRenderItem_1_) {
/* 250 */     if (Shaders.isShadowPass)
/*     */     {
/* 252 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 256 */     if (!Config.zoomMode) {
/*     */       
/* 258 */       Entity entity = this.mc.getRenderViewEntity();
/* 259 */       double d0 = p_isRenderItem_1_.getDistanceSq(entity.posX, entity.posY, entity.posZ);
/*     */       
/* 261 */       if (d0 > itemRenderDistanceSq)
/*     */       {
/* 263 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 267 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void updateItemRenderDistance() {
/* 273 */     Minecraft minecraft = Config.getMinecraft();
/* 274 */     double d0 = Config.limit(minecraft.gameSettings.fovSetting, 1.0F, 120.0F);
/* 275 */     double d1 = Math.max(6.0D * minecraft.displayHeight / d0, 16.0D);
/* 276 */     itemRenderDistanceSq = d1 * d1;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\tileentity\RenderItemFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
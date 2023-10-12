/*      */ package net.minecraft.client.renderer.entity;
/*      */ 
/*      */ import java.util.List;
/*      */ import java.util.concurrent.Callable;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockDirt;
/*      */ import net.minecraft.block.BlockDoublePlant;
/*      */ import net.minecraft.block.BlockFlower;
/*      */ import net.minecraft.block.BlockHugeMushroom;
/*      */ import net.minecraft.block.BlockPlanks;
/*      */ import net.minecraft.block.BlockPrismarine;
/*      */ import net.minecraft.block.BlockQuartz;
/*      */ import net.minecraft.block.BlockRedSandstone;
/*      */ import net.minecraft.block.BlockSand;
/*      */ import net.minecraft.block.BlockSandStone;
/*      */ import net.minecraft.block.BlockSilverfish;
/*      */ import net.minecraft.block.BlockStone;
/*      */ import net.minecraft.block.BlockStoneBrick;
/*      */ import net.minecraft.block.BlockStoneSlab;
/*      */ import net.minecraft.block.BlockStoneSlabNew;
/*      */ import net.minecraft.block.BlockTallGrass;
/*      */ import net.minecraft.block.BlockWall;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.gui.FontRenderer;
/*      */ import net.minecraft.client.renderer.EntityRenderer;
/*      */ import net.minecraft.client.renderer.GlStateManager;
/*      */ import net.minecraft.client.renderer.ItemMeshDefinition;
/*      */ import net.minecraft.client.renderer.ItemModelMesher;
/*      */ import net.minecraft.client.renderer.OpenGlHelper;
/*      */ import net.minecraft.client.renderer.Tessellator;
/*      */ import net.minecraft.client.renderer.WorldRenderer;
/*      */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*      */ import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
/*      */ import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
/*      */ import net.minecraft.client.renderer.texture.TextureManager;
/*      */ import net.minecraft.client.renderer.texture.TextureMap;
/*      */ import net.minecraft.client.renderer.texture.TextureUtil;
/*      */ import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
/*      */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*      */ import net.minecraft.client.resources.IResourceManager;
/*      */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*      */ import net.minecraft.client.resources.model.IBakedModel;
/*      */ import net.minecraft.client.resources.model.ModelManager;
/*      */ import net.minecraft.client.resources.model.ModelResourceLocation;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.item.EnumDyeColor;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemFishFood;
/*      */ import net.minecraft.item.ItemPotion;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.src.Config;
/*      */ import net.minecraft.util.EnumChatFormatting;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumWorldBlockLayer;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.Vec3i;
/*      */ import net.optifine.CustomColors;
/*      */ import net.optifine.CustomItems;
/*      */ import net.optifine.reflect.Reflector;
/*      */ import net.optifine.reflect.ReflectorForge;
/*      */ import net.optifine.shaders.Shaders;
/*      */ import net.optifine.shaders.ShadersRender;
/*      */ import rip.diavlo.base.modules.render.BigItems;
/*      */ 
/*      */ public class RenderItem
/*      */   implements IResourceManagerReloadListener
/*      */ {
/*   74 */   private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
/*      */   private boolean notRenderingEffectsInGUI = true;
/*      */   public float zLevel;
/*      */   private final ItemModelMesher itemModelMesher;
/*      */   private final TextureManager textureManager;
/*   79 */   private ModelResourceLocation modelLocation = null;
/*      */   private boolean renderItemGui = false;
/*   81 */   public ModelManager modelManager = null;
/*      */   
/*      */   private boolean renderModelHasEmissive = false;
/*      */   private boolean renderModelEmissive = false;
/*      */   
/*      */   public RenderItem(TextureManager textureManager, ModelManager modelManager) {
/*   87 */     this.textureManager = textureManager;
/*   88 */     this.modelManager = modelManager;
/*      */     
/*   90 */     if (Reflector.ItemModelMesherForge_Constructor.exists()) {
/*      */       
/*   92 */       this.itemModelMesher = (ItemModelMesher)Reflector.newInstance(Reflector.ItemModelMesherForge_Constructor, new Object[] { modelManager });
/*      */     }
/*      */     else {
/*      */       
/*   96 */       this.itemModelMesher = new ItemModelMesher(modelManager);
/*      */     } 
/*      */     
/*   99 */     registerItems();
/*      */   }
/*      */ 
/*      */   
/*      */   public void isNotRenderingEffectsInGUI(boolean isNot) {
/*  104 */     this.notRenderingEffectsInGUI = isNot;
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemModelMesher getItemModelMesher() {
/*  109 */     return this.itemModelMesher;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void registerItem(Item itm, int subType, String identifier) {
/*  114 */     this.itemModelMesher.register(itm, subType, new ModelResourceLocation(identifier, "inventory"));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void registerBlock(Block blk, int subType, String identifier) {
/*  119 */     registerItem(Item.getItemFromBlock(blk), subType, identifier);
/*      */   }
/*      */ 
/*      */   
/*      */   private void registerBlock(Block blk, String identifier) {
/*  124 */     registerBlock(blk, 0, identifier);
/*      */   }
/*      */ 
/*      */   
/*      */   private void registerItem(Item itm, String identifier) {
/*  129 */     registerItem(itm, 0, identifier);
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderModel(IBakedModel model, ItemStack stack) {
/*  134 */     renderModel(model, -1, stack);
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderModel(IBakedModel model, int color) {
/*  139 */     renderModel(model, color, (ItemStack)null);
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderModel(IBakedModel model, int color, ItemStack stack) {
/*  144 */     Tessellator tessellator = Tessellator.getInstance();
/*  145 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  146 */     boolean flag = Minecraft.getMinecraft().getTextureMapBlocks().isTextureBound();
/*  147 */     boolean flag1 = (Config.isMultiTexture() && flag);
/*      */     
/*  149 */     if (flag1)
/*      */     {
/*  151 */       worldrenderer.setBlockLayer(EnumWorldBlockLayer.SOLID);
/*      */     }
/*      */     
/*  154 */     worldrenderer.begin(7, DefaultVertexFormats.ITEM);
/*      */     
/*  156 */     for (EnumFacing enumfacing : EnumFacing.VALUES)
/*      */     {
/*  158 */       renderQuads(worldrenderer, model.getFaceQuads(enumfacing), color, stack);
/*      */     }
/*      */     
/*  161 */     renderQuads(worldrenderer, model.getGeneralQuads(), color, stack);
/*  162 */     tessellator.draw();
/*      */     
/*  164 */     if (flag1) {
/*      */       
/*  166 */       worldrenderer.setBlockLayer((EnumWorldBlockLayer)null);
/*  167 */       GlStateManager.bindCurrentTexture();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderItem(ItemStack stack, IBakedModel model) {
/*  173 */     if (stack != null) {
/*      */       
/*  175 */       GlStateManager.pushMatrix();
/*      */       
/*  177 */       if (BigItems.scale) {
/*  178 */         GlStateManager.scale(0.5F, 0.5F, 0.5F);
/*      */       }
/*      */       
/*  181 */       if (model.isBuiltInRenderer()) {
/*      */         
/*  183 */         GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
/*  184 */         GlStateManager.translate(-0.5F, -0.5F, -0.5F);
/*  185 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  186 */         GlStateManager.enableRescaleNormal();
/*  187 */         TileEntityItemStackRenderer.instance.renderByItem(stack);
/*      */       }
/*      */       else {
/*      */         
/*  191 */         GlStateManager.translate(-0.5F, -0.5F, -0.5F);
/*      */         
/*  193 */         if (Config.isCustomItems())
/*      */         {
/*  195 */           model = CustomItems.getCustomItemModel(stack, model, (ResourceLocation)this.modelLocation, false);
/*      */         }
/*      */         
/*  198 */         this.renderModelHasEmissive = false;
/*  199 */         renderModel(model, stack);
/*      */         
/*  201 */         if (this.renderModelHasEmissive) {
/*      */           
/*  203 */           float f = OpenGlHelper.lastBrightnessX;
/*  204 */           float f1 = OpenGlHelper.lastBrightnessY;
/*  205 */           OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, f1);
/*  206 */           this.renderModelEmissive = true;
/*  207 */           renderModel(model, stack);
/*  208 */           this.renderModelEmissive = false;
/*  209 */           OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);
/*      */         } 
/*      */         
/*  212 */         if (stack.hasEffect() && (!Config.isCustomItems() || !CustomItems.renderCustomEffect(this, stack, model)))
/*      */         {
/*  214 */           renderEffect(model);
/*      */         }
/*      */       } 
/*      */       
/*  218 */       GlStateManager.popMatrix();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderEffect(IBakedModel model) {
/*  224 */     if (!Config.isCustomItems() || CustomItems.isUseGlint())
/*      */     {
/*  226 */       if (!Config.isShaders() || !Shaders.isShadowPass) {
/*      */         
/*  228 */         GlStateManager.depthMask(false);
/*  229 */         GlStateManager.depthFunc(514);
/*  230 */         GlStateManager.disableLighting();
/*  231 */         GlStateManager.blendFunc(768, 1);
/*  232 */         this.textureManager.bindTexture(RES_ITEM_GLINT);
/*      */         
/*  234 */         if (Config.isShaders() && !this.renderItemGui)
/*      */         {
/*  236 */           ShadersRender.renderEnchantedGlintBegin();
/*      */         }
/*      */         
/*  239 */         GlStateManager.matrixMode(5890);
/*  240 */         GlStateManager.pushMatrix();
/*  241 */         GlStateManager.scale(8.0F, 8.0F, 8.0F);
/*  242 */         float f = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
/*  243 */         GlStateManager.translate(f, 0.0F, 0.0F);
/*  244 */         GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
/*  245 */         renderModel(model, -8372020);
/*  246 */         GlStateManager.popMatrix();
/*  247 */         GlStateManager.pushMatrix();
/*  248 */         GlStateManager.scale(8.0F, 8.0F, 8.0F);
/*  249 */         float f1 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
/*  250 */         GlStateManager.translate(-f1, 0.0F, 0.0F);
/*  251 */         GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
/*  252 */         renderModel(model, -8372020);
/*  253 */         GlStateManager.popMatrix();
/*  254 */         GlStateManager.matrixMode(5888);
/*  255 */         GlStateManager.blendFunc(770, 771);
/*  256 */         GlStateManager.enableLighting();
/*  257 */         GlStateManager.depthFunc(515);
/*  258 */         GlStateManager.depthMask(true);
/*  259 */         this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
/*      */         
/*  261 */         if (Config.isShaders() && !this.renderItemGui)
/*      */         {
/*  263 */           ShadersRender.renderEnchantedGlintEnd();
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void putQuadNormal(WorldRenderer renderer, BakedQuad quad) {
/*  271 */     Vec3i vec3i = quad.getFace().getDirectionVec();
/*  272 */     renderer.putNormal(vec3i.getX(), vec3i.getY(), vec3i.getZ());
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderQuad(WorldRenderer renderer, BakedQuad quad, int color) {
/*  277 */     if (this.renderModelEmissive) {
/*      */       
/*  279 */       if (quad.getQuadEmissive() == null) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  284 */       quad = quad.getQuadEmissive();
/*      */     }
/*  286 */     else if (quad.getQuadEmissive() != null) {
/*      */       
/*  288 */       this.renderModelHasEmissive = true;
/*      */     } 
/*      */     
/*  291 */     if (renderer.isMultiTexture()) {
/*      */       
/*  293 */       renderer.addVertexData(quad.getVertexDataSingle());
/*      */     }
/*      */     else {
/*      */       
/*  297 */       renderer.addVertexData(quad.getVertexData());
/*      */     } 
/*      */     
/*  300 */     renderer.putSprite(quad.getSprite());
/*      */     
/*  302 */     if (Reflector.IColoredBakedQuad.exists() && Reflector.IColoredBakedQuad.isInstance(quad)) {
/*      */       
/*  304 */       forgeHooksClient_putQuadColor(renderer, quad, color);
/*      */     }
/*      */     else {
/*      */       
/*  308 */       renderer.putColor4(color);
/*      */     } 
/*      */     
/*  311 */     putQuadNormal(renderer, quad);
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderQuads(WorldRenderer renderer, List<BakedQuad> quads, int color, ItemStack stack) {
/*  316 */     boolean flag = (color == -1 && stack != null);
/*  317 */     int i = 0;
/*      */     
/*  319 */     for (int j = quads.size(); i < j; i++) {
/*      */       
/*  321 */       BakedQuad bakedquad = quads.get(i);
/*  322 */       int k = color;
/*      */       
/*  324 */       if (flag && bakedquad.hasTintIndex()) {
/*      */         
/*  326 */         k = stack.getItem().getColorFromItemStack(stack, bakedquad.getTintIndex());
/*      */         
/*  328 */         if (Config.isCustomColors())
/*      */         {
/*  330 */           k = CustomColors.getColorFromItemStack(stack, bakedquad.getTintIndex(), k);
/*      */         }
/*      */         
/*  333 */         if (EntityRenderer.anaglyphEnable)
/*      */         {
/*  335 */           k = TextureUtil.anaglyphColor(k);
/*      */         }
/*      */         
/*  338 */         k |= 0xFF000000;
/*      */       } 
/*      */       
/*  341 */       renderQuad(renderer, bakedquad, k);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean shouldRenderItemIn3D(ItemStack stack) {
/*  347 */     IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
/*  348 */     return (ibakedmodel == null) ? false : ibakedmodel.isGui3d();
/*      */   }
/*      */ 
/*      */   
/*      */   private void preTransform(ItemStack stack) {
/*  353 */     IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
/*  354 */     Item item = stack.getItem();
/*      */     
/*  356 */     if (item != null) {
/*      */       
/*  358 */       boolean flag = ibakedmodel.isGui3d();
/*      */       
/*  360 */       if (!flag)
/*      */       {
/*  362 */         GlStateManager.scale(2.0F, 2.0F, 2.0F);
/*      */       }
/*      */       
/*  365 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderItem(ItemStack stack, ItemCameraTransforms.TransformType cameraTransformType) {
/*  371 */     if (stack != null) {
/*      */       
/*  373 */       IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
/*  374 */       renderItemModelTransform(stack, ibakedmodel, cameraTransformType);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderItemModelForEntity(ItemStack stack, EntityLivingBase entityToRenderFor, ItemCameraTransforms.TransformType cameraTransformType) {
/*  380 */     if (stack != null && entityToRenderFor != null) {
/*      */       
/*  382 */       IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
/*      */       
/*  384 */       if (entityToRenderFor instanceof EntityPlayer) {
/*      */         
/*  386 */         EntityPlayer entityplayer = (EntityPlayer)entityToRenderFor;
/*  387 */         Item item = stack.getItem();
/*  388 */         ModelResourceLocation modelresourcelocation = null;
/*      */         
/*  390 */         if (item == Items.fishing_rod && entityplayer.fishEntity != null) {
/*      */           
/*  392 */           modelresourcelocation = new ModelResourceLocation("fishing_rod_cast", "inventory");
/*      */         }
/*  394 */         else if (item == Items.bow && entityplayer.getItemInUse() != null) {
/*      */           
/*  396 */           int i = stack.getMaxItemUseDuration() - entityplayer.getItemInUseCount();
/*      */           
/*  398 */           if (i >= 18)
/*      */           {
/*  400 */             modelresourcelocation = new ModelResourceLocation("bow_pulling_2", "inventory");
/*      */           }
/*  402 */           else if (i > 13)
/*      */           {
/*  404 */             modelresourcelocation = new ModelResourceLocation("bow_pulling_1", "inventory");
/*      */           }
/*  406 */           else if (i > 0)
/*      */           {
/*  408 */             modelresourcelocation = new ModelResourceLocation("bow_pulling_0", "inventory");
/*      */           }
/*      */         
/*  411 */         } else if (Reflector.ForgeItem_getModel.exists()) {
/*      */           
/*  413 */           modelresourcelocation = (ModelResourceLocation)Reflector.call(item, Reflector.ForgeItem_getModel, new Object[] { stack, entityplayer, Integer.valueOf(entityplayer.getItemInUseCount()) });
/*      */         } 
/*      */         
/*  416 */         if (modelresourcelocation != null) {
/*      */           
/*  418 */           ibakedmodel = this.itemModelMesher.getModelManager().getModel(modelresourcelocation);
/*  419 */           this.modelLocation = modelresourcelocation;
/*      */         } 
/*      */       } 
/*      */       
/*  423 */       renderItemModelTransform(stack, ibakedmodel, cameraTransformType);
/*  424 */       this.modelLocation = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void renderItemModelTransform(ItemStack stack, IBakedModel model, ItemCameraTransforms.TransformType cameraTransformType) {
/*  430 */     this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
/*  431 */     this.textureManager.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
/*  432 */     preTransform(stack);
/*  433 */     GlStateManager.enableRescaleNormal();
/*  434 */     GlStateManager.alphaFunc(516, 0.1F);
/*  435 */     GlStateManager.enableBlend();
/*  436 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  437 */     GlStateManager.pushMatrix();
/*      */     
/*  439 */     if (Reflector.ForgeHooksClient_handleCameraTransforms.exists()) {
/*      */       
/*  441 */       model = (IBakedModel)Reflector.call(Reflector.ForgeHooksClient_handleCameraTransforms, new Object[] { model, cameraTransformType });
/*      */     }
/*      */     else {
/*      */       
/*  445 */       ItemCameraTransforms itemcameratransforms = model.getItemCameraTransforms();
/*  446 */       itemcameratransforms.applyTransform(cameraTransformType);
/*      */       
/*  448 */       if (isThereOneNegativeScale(itemcameratransforms.getTransform(cameraTransformType)))
/*      */       {
/*  450 */         GlStateManager.cullFace(1028);
/*      */       }
/*      */     } 
/*      */     
/*  454 */     renderItem(stack, model);
/*  455 */     GlStateManager.cullFace(1029);
/*  456 */     GlStateManager.popMatrix();
/*  457 */     GlStateManager.disableRescaleNormal();
/*  458 */     GlStateManager.disableBlend();
/*  459 */     this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
/*  460 */     this.textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isThereOneNegativeScale(ItemTransformVec3f itemTranformVec) {
/*  465 */     return ((itemTranformVec.scale.x < 0.0F)) ^ ((itemTranformVec.scale.y < 0.0F)) ^ ((itemTranformVec.scale.z < 0.0F) ? 1 : 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderItemIntoGUI(ItemStack stack, int x, int y) {
/*  470 */     this.renderItemGui = true;
/*  471 */     IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
/*  472 */     GlStateManager.pushMatrix();
/*  473 */     this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
/*  474 */     this.textureManager.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
/*  475 */     GlStateManager.enableRescaleNormal();
/*  476 */     GlStateManager.enableAlpha();
/*  477 */     GlStateManager.alphaFunc(516, 0.1F);
/*  478 */     GlStateManager.enableBlend();
/*  479 */     GlStateManager.blendFunc(770, 771);
/*  480 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  481 */     setupGuiTransform(x, y, ibakedmodel.isGui3d());
/*      */     
/*  483 */     if (Reflector.ForgeHooksClient_handleCameraTransforms.exists()) {
/*      */       
/*  485 */       ibakedmodel = (IBakedModel)Reflector.call(Reflector.ForgeHooksClient_handleCameraTransforms, new Object[] { ibakedmodel, ItemCameraTransforms.TransformType.GUI });
/*      */     }
/*      */     else {
/*      */       
/*  489 */       ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GUI);
/*      */     } 
/*      */     
/*  492 */     renderItem(stack, ibakedmodel);
/*  493 */     GlStateManager.disableAlpha();
/*  494 */     GlStateManager.disableRescaleNormal();
/*  495 */     GlStateManager.disableLighting();
/*  496 */     GlStateManager.popMatrix();
/*  497 */     this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
/*  498 */     this.textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
/*  499 */     this.renderItemGui = false;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d) {
/*  504 */     GlStateManager.translate(xPosition, yPosition, 100.0F + this.zLevel);
/*  505 */     GlStateManager.translate(8.0F, 8.0F, 0.0F);
/*  506 */     GlStateManager.scale(1.0F, 1.0F, -1.0F);
/*  507 */     GlStateManager.scale(0.5F, 0.5F, 0.5F);
/*      */     
/*  509 */     if (isGui3d) {
/*      */       
/*  511 */       GlStateManager.scale(40.0F, 40.0F, 40.0F);
/*  512 */       GlStateManager.rotate(210.0F, 1.0F, 0.0F, 0.0F);
/*  513 */       GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
/*  514 */       GlStateManager.enableLighting();
/*      */     }
/*      */     else {
/*      */       
/*  518 */       GlStateManager.scale(64.0F, 64.0F, 64.0F);
/*  519 */       GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
/*  520 */       GlStateManager.disableLighting();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderItemAndEffectIntoGUI(final ItemStack stack, int xPosition, int yPosition) {
/*  526 */     if (stack != null && stack.getItem() != null) {
/*      */       
/*  528 */       this.zLevel += 50.0F;
/*      */ 
/*      */       
/*      */       try {
/*  532 */         renderItemIntoGUI(stack, xPosition, yPosition);
/*      */       }
/*  534 */       catch (Throwable throwable) {
/*      */         
/*  536 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering item");
/*  537 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
/*  538 */         crashreportcategory.addCrashSectionCallable("Item Type", new Callable<String>()
/*      */             {
/*      */               public String call() throws Exception
/*      */               {
/*  542 */                 return String.valueOf(stack.getItem());
/*      */               }
/*      */             });
/*  545 */         crashreportcategory.addCrashSectionCallable("Item Aux", new Callable<String>()
/*      */             {
/*      */               public String call() throws Exception
/*      */               {
/*  549 */                 return String.valueOf(stack.getMetadata());
/*      */               }
/*      */             });
/*  552 */         crashreportcategory.addCrashSectionCallable("Item NBT", new Callable<String>()
/*      */             {
/*      */               public String call() throws Exception
/*      */               {
/*  556 */                 return String.valueOf(stack.getTagCompound());
/*      */               }
/*      */             });
/*  559 */         crashreportcategory.addCrashSectionCallable("Item Foil", new Callable<String>()
/*      */             {
/*      */               public String call() throws Exception
/*      */               {
/*  563 */                 return String.valueOf(stack.hasEffect());
/*      */               }
/*      */             });
/*  566 */         throw new ReportedException(crashreport);
/*      */       } 
/*      */       
/*  569 */       this.zLevel -= 50.0F;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderItemOverlays(FontRenderer fr, ItemStack stack, int xPosition, int yPosition) {
/*  575 */     renderItemOverlayIntoGUI(fr, stack, xPosition, yPosition, (String)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, String text) {
/*  580 */     if (stack != null) {
/*      */       
/*  582 */       if (stack.stackSize != 1 || text != null) {
/*      */         
/*  584 */         String s = (text == null) ? String.valueOf(stack.stackSize) : text;
/*      */         
/*  586 */         if (text == null && stack.stackSize < 1)
/*      */         {
/*  588 */           s = EnumChatFormatting.RED + String.valueOf(stack.stackSize);
/*      */         }
/*      */         
/*  591 */         GlStateManager.disableLighting();
/*  592 */         GlStateManager.disableDepth();
/*  593 */         GlStateManager.disableBlend();
/*  594 */         fr.drawStringWithShadow(s, (xPosition + 19 - 2 - fr.getStringWidth(s)), (yPosition + 6 + 3), 16777215);
/*  595 */         GlStateManager.enableLighting();
/*  596 */         GlStateManager.enableDepth();
/*  597 */         GlStateManager.enableBlend();
/*      */       } 
/*      */       
/*  600 */       if (ReflectorForge.isItemDamaged(stack)) {
/*      */         
/*  602 */         int j1 = (int)Math.round(13.0D - stack.getItemDamage() * 13.0D / stack.getMaxDamage());
/*  603 */         int i = (int)Math.round(255.0D - stack.getItemDamage() * 255.0D / stack.getMaxDamage());
/*      */         
/*  605 */         if (Reflector.ForgeItem_getDurabilityForDisplay.exists()) {
/*      */           
/*  607 */           double d0 = Reflector.callDouble(stack.getItem(), Reflector.ForgeItem_getDurabilityForDisplay, new Object[] { stack });
/*  608 */           j1 = (int)Math.round(13.0D - d0 * 13.0D);
/*  609 */           i = (int)Math.round(255.0D - d0 * 255.0D);
/*      */         } 
/*      */         
/*  612 */         GlStateManager.disableLighting();
/*  613 */         GlStateManager.disableDepth();
/*  614 */         GlStateManager.disableTexture2D();
/*  615 */         GlStateManager.disableAlpha();
/*  616 */         GlStateManager.disableBlend();
/*  617 */         Tessellator tessellator = Tessellator.getInstance();
/*  618 */         WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  619 */         draw(worldrenderer, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
/*  620 */         draw(worldrenderer, xPosition + 2, yPosition + 13, 12, 1, (255 - i) / 4, 64, 0, 255);
/*  621 */         int j = 255 - i;
/*  622 */         int k = i;
/*  623 */         int l = 0;
/*      */         
/*  625 */         if (Config.isCustomColors()) {
/*      */           
/*  627 */           int i1 = CustomColors.getDurabilityColor(i);
/*      */           
/*  629 */           if (i1 >= 0) {
/*      */             
/*  631 */             j = i1 >> 16 & 0xFF;
/*  632 */             k = i1 >> 8 & 0xFF;
/*  633 */             l = i1 >> 0 & 0xFF;
/*      */           } 
/*      */         } 
/*      */         
/*  637 */         draw(worldrenderer, xPosition + 2, yPosition + 13, j1, 1, j, k, l, 255);
/*  638 */         GlStateManager.enableBlend();
/*  639 */         GlStateManager.enableAlpha();
/*  640 */         GlStateManager.enableTexture2D();
/*  641 */         GlStateManager.enableLighting();
/*  642 */         GlStateManager.enableDepth();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void draw(WorldRenderer renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
/*  649 */     renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/*  650 */     renderer.pos((x + 0), (y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
/*  651 */     renderer.pos((x + 0), (y + height), 0.0D).color(red, green, blue, alpha).endVertex();
/*  652 */     renderer.pos((x + width), (y + height), 0.0D).color(red, green, blue, alpha).endVertex();
/*  653 */     renderer.pos((x + width), (y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
/*  654 */     Tessellator.getInstance().draw();
/*      */   }
/*      */ 
/*      */   
/*      */   private void registerItems() {
/*  659 */     registerBlock(Blocks.anvil, "anvil_intact");
/*  660 */     registerBlock(Blocks.anvil, 1, "anvil_slightly_damaged");
/*  661 */     registerBlock(Blocks.anvil, 2, "anvil_very_damaged");
/*  662 */     registerBlock(Blocks.carpet, EnumDyeColor.BLACK.getMetadata(), "black_carpet");
/*  663 */     registerBlock(Blocks.carpet, EnumDyeColor.BLUE.getMetadata(), "blue_carpet");
/*  664 */     registerBlock(Blocks.carpet, EnumDyeColor.BROWN.getMetadata(), "brown_carpet");
/*  665 */     registerBlock(Blocks.carpet, EnumDyeColor.CYAN.getMetadata(), "cyan_carpet");
/*  666 */     registerBlock(Blocks.carpet, EnumDyeColor.GRAY.getMetadata(), "gray_carpet");
/*  667 */     registerBlock(Blocks.carpet, EnumDyeColor.GREEN.getMetadata(), "green_carpet");
/*  668 */     registerBlock(Blocks.carpet, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_carpet");
/*  669 */     registerBlock(Blocks.carpet, EnumDyeColor.LIME.getMetadata(), "lime_carpet");
/*  670 */     registerBlock(Blocks.carpet, EnumDyeColor.MAGENTA.getMetadata(), "magenta_carpet");
/*  671 */     registerBlock(Blocks.carpet, EnumDyeColor.ORANGE.getMetadata(), "orange_carpet");
/*  672 */     registerBlock(Blocks.carpet, EnumDyeColor.PINK.getMetadata(), "pink_carpet");
/*  673 */     registerBlock(Blocks.carpet, EnumDyeColor.PURPLE.getMetadata(), "purple_carpet");
/*  674 */     registerBlock(Blocks.carpet, EnumDyeColor.RED.getMetadata(), "red_carpet");
/*  675 */     registerBlock(Blocks.carpet, EnumDyeColor.SILVER.getMetadata(), "silver_carpet");
/*  676 */     registerBlock(Blocks.carpet, EnumDyeColor.WHITE.getMetadata(), "white_carpet");
/*  677 */     registerBlock(Blocks.carpet, EnumDyeColor.YELLOW.getMetadata(), "yellow_carpet");
/*  678 */     registerBlock(Blocks.cobblestone_wall, BlockWall.EnumType.MOSSY.getMetadata(), "mossy_cobblestone_wall");
/*  679 */     registerBlock(Blocks.cobblestone_wall, BlockWall.EnumType.NORMAL.getMetadata(), "cobblestone_wall");
/*  680 */     registerBlock(Blocks.dirt, BlockDirt.DirtType.COARSE_DIRT.getMetadata(), "coarse_dirt");
/*  681 */     registerBlock(Blocks.dirt, BlockDirt.DirtType.DIRT.getMetadata(), "dirt");
/*  682 */     registerBlock(Blocks.dirt, BlockDirt.DirtType.PODZOL.getMetadata(), "podzol");
/*  683 */     registerBlock((Block)Blocks.double_plant, BlockDoublePlant.EnumPlantType.FERN.getMeta(), "double_fern");
/*  684 */     registerBlock((Block)Blocks.double_plant, BlockDoublePlant.EnumPlantType.GRASS.getMeta(), "double_grass");
/*  685 */     registerBlock((Block)Blocks.double_plant, BlockDoublePlant.EnumPlantType.PAEONIA.getMeta(), "paeonia");
/*  686 */     registerBlock((Block)Blocks.double_plant, BlockDoublePlant.EnumPlantType.ROSE.getMeta(), "double_rose");
/*  687 */     registerBlock((Block)Blocks.double_plant, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta(), "sunflower");
/*  688 */     registerBlock((Block)Blocks.double_plant, BlockDoublePlant.EnumPlantType.SYRINGA.getMeta(), "syringa");
/*  689 */     registerBlock((Block)Blocks.leaves, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_leaves");
/*  690 */     registerBlock((Block)Blocks.leaves, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_leaves");
/*  691 */     registerBlock((Block)Blocks.leaves, BlockPlanks.EnumType.OAK.getMetadata(), "oak_leaves");
/*  692 */     registerBlock((Block)Blocks.leaves, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_leaves");
/*  693 */     registerBlock((Block)Blocks.leaves2, BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_leaves");
/*  694 */     registerBlock((Block)Blocks.leaves2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_leaves");
/*  695 */     registerBlock(Blocks.log, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_log");
/*  696 */     registerBlock(Blocks.log, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_log");
/*  697 */     registerBlock(Blocks.log, BlockPlanks.EnumType.OAK.getMetadata(), "oak_log");
/*  698 */     registerBlock(Blocks.log, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_log");
/*  699 */     registerBlock(Blocks.log2, BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_log");
/*  700 */     registerBlock(Blocks.log2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_log");
/*  701 */     registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.CHISELED_STONEBRICK.getMetadata(), "chiseled_brick_monster_egg");
/*  702 */     registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.COBBLESTONE.getMetadata(), "cobblestone_monster_egg");
/*  703 */     registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.CRACKED_STONEBRICK.getMetadata(), "cracked_brick_monster_egg");
/*  704 */     registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.MOSSY_STONEBRICK.getMetadata(), "mossy_brick_monster_egg");
/*  705 */     registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.STONE.getMetadata(), "stone_monster_egg");
/*  706 */     registerBlock(Blocks.monster_egg, BlockSilverfish.EnumType.STONEBRICK.getMetadata(), "stone_brick_monster_egg");
/*  707 */     registerBlock(Blocks.planks, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_planks");
/*  708 */     registerBlock(Blocks.planks, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_planks");
/*  709 */     registerBlock(Blocks.planks, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_planks");
/*  710 */     registerBlock(Blocks.planks, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_planks");
/*  711 */     registerBlock(Blocks.planks, BlockPlanks.EnumType.OAK.getMetadata(), "oak_planks");
/*  712 */     registerBlock(Blocks.planks, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_planks");
/*  713 */     registerBlock(Blocks.prismarine, BlockPrismarine.EnumType.BRICKS.getMetadata(), "prismarine_bricks");
/*  714 */     registerBlock(Blocks.prismarine, BlockPrismarine.EnumType.DARK.getMetadata(), "dark_prismarine");
/*  715 */     registerBlock(Blocks.prismarine, BlockPrismarine.EnumType.ROUGH.getMetadata(), "prismarine");
/*  716 */     registerBlock(Blocks.quartz_block, BlockQuartz.EnumType.CHISELED.getMetadata(), "chiseled_quartz_block");
/*  717 */     registerBlock(Blocks.quartz_block, BlockQuartz.EnumType.DEFAULT.getMetadata(), "quartz_block");
/*  718 */     registerBlock(Blocks.quartz_block, BlockQuartz.EnumType.LINES_Y.getMetadata(), "quartz_column");
/*  719 */     registerBlock((Block)Blocks.red_flower, BlockFlower.EnumFlowerType.ALLIUM.getMeta(), "allium");
/*  720 */     registerBlock((Block)Blocks.red_flower, BlockFlower.EnumFlowerType.BLUE_ORCHID.getMeta(), "blue_orchid");
/*  721 */     registerBlock((Block)Blocks.red_flower, BlockFlower.EnumFlowerType.HOUSTONIA.getMeta(), "houstonia");
/*  722 */     registerBlock((Block)Blocks.red_flower, BlockFlower.EnumFlowerType.ORANGE_TULIP.getMeta(), "orange_tulip");
/*  723 */     registerBlock((Block)Blocks.red_flower, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta(), "oxeye_daisy");
/*  724 */     registerBlock((Block)Blocks.red_flower, BlockFlower.EnumFlowerType.PINK_TULIP.getMeta(), "pink_tulip");
/*  725 */     registerBlock((Block)Blocks.red_flower, BlockFlower.EnumFlowerType.POPPY.getMeta(), "poppy");
/*  726 */     registerBlock((Block)Blocks.red_flower, BlockFlower.EnumFlowerType.RED_TULIP.getMeta(), "red_tulip");
/*  727 */     registerBlock((Block)Blocks.red_flower, BlockFlower.EnumFlowerType.WHITE_TULIP.getMeta(), "white_tulip");
/*  728 */     registerBlock((Block)Blocks.sand, BlockSand.EnumType.RED_SAND.getMetadata(), "red_sand");
/*  729 */     registerBlock((Block)Blocks.sand, BlockSand.EnumType.SAND.getMetadata(), "sand");
/*  730 */     registerBlock(Blocks.sandstone, BlockSandStone.EnumType.CHISELED.getMetadata(), "chiseled_sandstone");
/*  731 */     registerBlock(Blocks.sandstone, BlockSandStone.EnumType.DEFAULT.getMetadata(), "sandstone");
/*  732 */     registerBlock(Blocks.sandstone, BlockSandStone.EnumType.SMOOTH.getMetadata(), "smooth_sandstone");
/*  733 */     registerBlock(Blocks.red_sandstone, BlockRedSandstone.EnumType.CHISELED.getMetadata(), "chiseled_red_sandstone");
/*  734 */     registerBlock(Blocks.red_sandstone, BlockRedSandstone.EnumType.DEFAULT.getMetadata(), "red_sandstone");
/*  735 */     registerBlock(Blocks.red_sandstone, BlockRedSandstone.EnumType.SMOOTH.getMetadata(), "smooth_red_sandstone");
/*  736 */     registerBlock(Blocks.sapling, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_sapling");
/*  737 */     registerBlock(Blocks.sapling, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_sapling");
/*  738 */     registerBlock(Blocks.sapling, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_sapling");
/*  739 */     registerBlock(Blocks.sapling, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_sapling");
/*  740 */     registerBlock(Blocks.sapling, BlockPlanks.EnumType.OAK.getMetadata(), "oak_sapling");
/*  741 */     registerBlock(Blocks.sapling, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_sapling");
/*  742 */     registerBlock(Blocks.sponge, 0, "sponge");
/*  743 */     registerBlock(Blocks.sponge, 1, "sponge_wet");
/*  744 */     registerBlock((Block)Blocks.stained_glass, EnumDyeColor.BLACK.getMetadata(), "black_stained_glass");
/*  745 */     registerBlock((Block)Blocks.stained_glass, EnumDyeColor.BLUE.getMetadata(), "blue_stained_glass");
/*  746 */     registerBlock((Block)Blocks.stained_glass, EnumDyeColor.BROWN.getMetadata(), "brown_stained_glass");
/*  747 */     registerBlock((Block)Blocks.stained_glass, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_glass");
/*  748 */     registerBlock((Block)Blocks.stained_glass, EnumDyeColor.GRAY.getMetadata(), "gray_stained_glass");
/*  749 */     registerBlock((Block)Blocks.stained_glass, EnumDyeColor.GREEN.getMetadata(), "green_stained_glass");
/*  750 */     registerBlock((Block)Blocks.stained_glass, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_glass");
/*  751 */     registerBlock((Block)Blocks.stained_glass, EnumDyeColor.LIME.getMetadata(), "lime_stained_glass");
/*  752 */     registerBlock((Block)Blocks.stained_glass, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_glass");
/*  753 */     registerBlock((Block)Blocks.stained_glass, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_glass");
/*  754 */     registerBlock((Block)Blocks.stained_glass, EnumDyeColor.PINK.getMetadata(), "pink_stained_glass");
/*  755 */     registerBlock((Block)Blocks.stained_glass, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_glass");
/*  756 */     registerBlock((Block)Blocks.stained_glass, EnumDyeColor.RED.getMetadata(), "red_stained_glass");
/*  757 */     registerBlock((Block)Blocks.stained_glass, EnumDyeColor.SILVER.getMetadata(), "silver_stained_glass");
/*  758 */     registerBlock((Block)Blocks.stained_glass, EnumDyeColor.WHITE.getMetadata(), "white_stained_glass");
/*  759 */     registerBlock((Block)Blocks.stained_glass, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_glass");
/*  760 */     registerBlock((Block)Blocks.stained_glass_pane, EnumDyeColor.BLACK.getMetadata(), "black_stained_glass_pane");
/*  761 */     registerBlock((Block)Blocks.stained_glass_pane, EnumDyeColor.BLUE.getMetadata(), "blue_stained_glass_pane");
/*  762 */     registerBlock((Block)Blocks.stained_glass_pane, EnumDyeColor.BROWN.getMetadata(), "brown_stained_glass_pane");
/*  763 */     registerBlock((Block)Blocks.stained_glass_pane, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_glass_pane");
/*  764 */     registerBlock((Block)Blocks.stained_glass_pane, EnumDyeColor.GRAY.getMetadata(), "gray_stained_glass_pane");
/*  765 */     registerBlock((Block)Blocks.stained_glass_pane, EnumDyeColor.GREEN.getMetadata(), "green_stained_glass_pane");
/*  766 */     registerBlock((Block)Blocks.stained_glass_pane, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_glass_pane");
/*  767 */     registerBlock((Block)Blocks.stained_glass_pane, EnumDyeColor.LIME.getMetadata(), "lime_stained_glass_pane");
/*  768 */     registerBlock((Block)Blocks.stained_glass_pane, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_glass_pane");
/*  769 */     registerBlock((Block)Blocks.stained_glass_pane, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_glass_pane");
/*  770 */     registerBlock((Block)Blocks.stained_glass_pane, EnumDyeColor.PINK.getMetadata(), "pink_stained_glass_pane");
/*  771 */     registerBlock((Block)Blocks.stained_glass_pane, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_glass_pane");
/*  772 */     registerBlock((Block)Blocks.stained_glass_pane, EnumDyeColor.RED.getMetadata(), "red_stained_glass_pane");
/*  773 */     registerBlock((Block)Blocks.stained_glass_pane, EnumDyeColor.SILVER.getMetadata(), "silver_stained_glass_pane");
/*  774 */     registerBlock((Block)Blocks.stained_glass_pane, EnumDyeColor.WHITE.getMetadata(), "white_stained_glass_pane");
/*  775 */     registerBlock((Block)Blocks.stained_glass_pane, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_glass_pane");
/*  776 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.BLACK.getMetadata(), "black_stained_hardened_clay");
/*  777 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.BLUE.getMetadata(), "blue_stained_hardened_clay");
/*  778 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.BROWN.getMetadata(), "brown_stained_hardened_clay");
/*  779 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_hardened_clay");
/*  780 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.GRAY.getMetadata(), "gray_stained_hardened_clay");
/*  781 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.GREEN.getMetadata(), "green_stained_hardened_clay");
/*  782 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_hardened_clay");
/*  783 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.LIME.getMetadata(), "lime_stained_hardened_clay");
/*  784 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_hardened_clay");
/*  785 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_hardened_clay");
/*  786 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.PINK.getMetadata(), "pink_stained_hardened_clay");
/*  787 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_hardened_clay");
/*  788 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.RED.getMetadata(), "red_stained_hardened_clay");
/*  789 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.SILVER.getMetadata(), "silver_stained_hardened_clay");
/*  790 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.WHITE.getMetadata(), "white_stained_hardened_clay");
/*  791 */     registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_hardened_clay");
/*  792 */     registerBlock(Blocks.stone, BlockStone.EnumType.ANDESITE.getMetadata(), "andesite");
/*  793 */     registerBlock(Blocks.stone, BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata(), "andesite_smooth");
/*  794 */     registerBlock(Blocks.stone, BlockStone.EnumType.DIORITE.getMetadata(), "diorite");
/*  795 */     registerBlock(Blocks.stone, BlockStone.EnumType.DIORITE_SMOOTH.getMetadata(), "diorite_smooth");
/*  796 */     registerBlock(Blocks.stone, BlockStone.EnumType.GRANITE.getMetadata(), "granite");
/*  797 */     registerBlock(Blocks.stone, BlockStone.EnumType.GRANITE_SMOOTH.getMetadata(), "granite_smooth");
/*  798 */     registerBlock(Blocks.stone, BlockStone.EnumType.STONE.getMetadata(), "stone");
/*  799 */     registerBlock(Blocks.stonebrick, BlockStoneBrick.EnumType.CRACKED.getMetadata(), "cracked_stonebrick");
/*  800 */     registerBlock(Blocks.stonebrick, BlockStoneBrick.EnumType.DEFAULT.getMetadata(), "stonebrick");
/*  801 */     registerBlock(Blocks.stonebrick, BlockStoneBrick.EnumType.CHISELED.getMetadata(), "chiseled_stonebrick");
/*  802 */     registerBlock(Blocks.stonebrick, BlockStoneBrick.EnumType.MOSSY.getMetadata(), "mossy_stonebrick");
/*  803 */     registerBlock((Block)Blocks.stone_slab, BlockStoneSlab.EnumType.BRICK.getMetadata(), "brick_slab");
/*  804 */     registerBlock((Block)Blocks.stone_slab, BlockStoneSlab.EnumType.COBBLESTONE.getMetadata(), "cobblestone_slab");
/*  805 */     registerBlock((Block)Blocks.stone_slab, BlockStoneSlab.EnumType.WOOD.getMetadata(), "old_wood_slab");
/*  806 */     registerBlock((Block)Blocks.stone_slab, BlockStoneSlab.EnumType.NETHERBRICK.getMetadata(), "nether_brick_slab");
/*  807 */     registerBlock((Block)Blocks.stone_slab, BlockStoneSlab.EnumType.QUARTZ.getMetadata(), "quartz_slab");
/*  808 */     registerBlock((Block)Blocks.stone_slab, BlockStoneSlab.EnumType.SAND.getMetadata(), "sandstone_slab");
/*  809 */     registerBlock((Block)Blocks.stone_slab, BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata(), "stone_brick_slab");
/*  810 */     registerBlock((Block)Blocks.stone_slab, BlockStoneSlab.EnumType.STONE.getMetadata(), "stone_slab");
/*  811 */     registerBlock((Block)Blocks.stone_slab2, BlockStoneSlabNew.EnumType.RED_SANDSTONE.getMetadata(), "red_sandstone_slab");
/*  812 */     registerBlock((Block)Blocks.tallgrass, BlockTallGrass.EnumType.DEAD_BUSH.getMeta(), "dead_bush");
/*  813 */     registerBlock((Block)Blocks.tallgrass, BlockTallGrass.EnumType.FERN.getMeta(), "fern");
/*  814 */     registerBlock((Block)Blocks.tallgrass, BlockTallGrass.EnumType.GRASS.getMeta(), "tall_grass");
/*  815 */     registerBlock((Block)Blocks.wooden_slab, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_slab");
/*  816 */     registerBlock((Block)Blocks.wooden_slab, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_slab");
/*  817 */     registerBlock((Block)Blocks.wooden_slab, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_slab");
/*  818 */     registerBlock((Block)Blocks.wooden_slab, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_slab");
/*  819 */     registerBlock((Block)Blocks.wooden_slab, BlockPlanks.EnumType.OAK.getMetadata(), "oak_slab");
/*  820 */     registerBlock((Block)Blocks.wooden_slab, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_slab");
/*  821 */     registerBlock(Blocks.wool, EnumDyeColor.BLACK.getMetadata(), "black_wool");
/*  822 */     registerBlock(Blocks.wool, EnumDyeColor.BLUE.getMetadata(), "blue_wool");
/*  823 */     registerBlock(Blocks.wool, EnumDyeColor.BROWN.getMetadata(), "brown_wool");
/*  824 */     registerBlock(Blocks.wool, EnumDyeColor.CYAN.getMetadata(), "cyan_wool");
/*  825 */     registerBlock(Blocks.wool, EnumDyeColor.GRAY.getMetadata(), "gray_wool");
/*  826 */     registerBlock(Blocks.wool, EnumDyeColor.GREEN.getMetadata(), "green_wool");
/*  827 */     registerBlock(Blocks.wool, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_wool");
/*  828 */     registerBlock(Blocks.wool, EnumDyeColor.LIME.getMetadata(), "lime_wool");
/*  829 */     registerBlock(Blocks.wool, EnumDyeColor.MAGENTA.getMetadata(), "magenta_wool");
/*  830 */     registerBlock(Blocks.wool, EnumDyeColor.ORANGE.getMetadata(), "orange_wool");
/*  831 */     registerBlock(Blocks.wool, EnumDyeColor.PINK.getMetadata(), "pink_wool");
/*  832 */     registerBlock(Blocks.wool, EnumDyeColor.PURPLE.getMetadata(), "purple_wool");
/*  833 */     registerBlock(Blocks.wool, EnumDyeColor.RED.getMetadata(), "red_wool");
/*  834 */     registerBlock(Blocks.wool, EnumDyeColor.SILVER.getMetadata(), "silver_wool");
/*  835 */     registerBlock(Blocks.wool, EnumDyeColor.WHITE.getMetadata(), "white_wool");
/*  836 */     registerBlock(Blocks.wool, EnumDyeColor.YELLOW.getMetadata(), "yellow_wool");
/*  837 */     registerBlock(Blocks.acacia_stairs, "acacia_stairs");
/*  838 */     registerBlock(Blocks.activator_rail, "activator_rail");
/*  839 */     registerBlock((Block)Blocks.beacon, "beacon");
/*  840 */     registerBlock(Blocks.bedrock, "bedrock");
/*  841 */     registerBlock(Blocks.birch_stairs, "birch_stairs");
/*  842 */     registerBlock(Blocks.bookshelf, "bookshelf");
/*  843 */     registerBlock(Blocks.brick_block, "brick_block");
/*  844 */     registerBlock(Blocks.brick_block, "brick_block");
/*  845 */     registerBlock(Blocks.brick_stairs, "brick_stairs");
/*  846 */     registerBlock((Block)Blocks.brown_mushroom, "brown_mushroom");
/*  847 */     registerBlock((Block)Blocks.cactus, "cactus");
/*  848 */     registerBlock(Blocks.clay, "clay");
/*  849 */     registerBlock(Blocks.coal_block, "coal_block");
/*  850 */     registerBlock(Blocks.coal_ore, "coal_ore");
/*  851 */     registerBlock(Blocks.cobblestone, "cobblestone");
/*  852 */     registerBlock(Blocks.crafting_table, "crafting_table");
/*  853 */     registerBlock(Blocks.dark_oak_stairs, "dark_oak_stairs");
/*  854 */     registerBlock((Block)Blocks.daylight_detector, "daylight_detector");
/*  855 */     registerBlock((Block)Blocks.deadbush, "dead_bush");
/*  856 */     registerBlock(Blocks.detector_rail, "detector_rail");
/*  857 */     registerBlock(Blocks.diamond_block, "diamond_block");
/*  858 */     registerBlock(Blocks.diamond_ore, "diamond_ore");
/*  859 */     registerBlock(Blocks.dispenser, "dispenser");
/*  860 */     registerBlock(Blocks.dropper, "dropper");
/*  861 */     registerBlock(Blocks.emerald_block, "emerald_block");
/*  862 */     registerBlock(Blocks.emerald_ore, "emerald_ore");
/*  863 */     registerBlock(Blocks.enchanting_table, "enchanting_table");
/*  864 */     registerBlock(Blocks.end_portal_frame, "end_portal_frame");
/*  865 */     registerBlock(Blocks.end_stone, "end_stone");
/*  866 */     registerBlock(Blocks.oak_fence, "oak_fence");
/*  867 */     registerBlock(Blocks.spruce_fence, "spruce_fence");
/*  868 */     registerBlock(Blocks.birch_fence, "birch_fence");
/*  869 */     registerBlock(Blocks.jungle_fence, "jungle_fence");
/*  870 */     registerBlock(Blocks.dark_oak_fence, "dark_oak_fence");
/*  871 */     registerBlock(Blocks.acacia_fence, "acacia_fence");
/*  872 */     registerBlock(Blocks.oak_fence_gate, "oak_fence_gate");
/*  873 */     registerBlock(Blocks.spruce_fence_gate, "spruce_fence_gate");
/*  874 */     registerBlock(Blocks.birch_fence_gate, "birch_fence_gate");
/*  875 */     registerBlock(Blocks.jungle_fence_gate, "jungle_fence_gate");
/*  876 */     registerBlock(Blocks.dark_oak_fence_gate, "dark_oak_fence_gate");
/*  877 */     registerBlock(Blocks.acacia_fence_gate, "acacia_fence_gate");
/*  878 */     registerBlock(Blocks.furnace, "furnace");
/*  879 */     registerBlock(Blocks.glass, "glass");
/*  880 */     registerBlock(Blocks.glass_pane, "glass_pane");
/*  881 */     registerBlock(Blocks.glowstone, "glowstone");
/*  882 */     registerBlock(Blocks.golden_rail, "golden_rail");
/*  883 */     registerBlock(Blocks.gold_block, "gold_block");
/*  884 */     registerBlock(Blocks.gold_ore, "gold_ore");
/*  885 */     registerBlock((Block)Blocks.grass, "grass");
/*  886 */     registerBlock(Blocks.gravel, "gravel");
/*  887 */     registerBlock(Blocks.hardened_clay, "hardened_clay");
/*  888 */     registerBlock(Blocks.hay_block, "hay_block");
/*  889 */     registerBlock(Blocks.heavy_weighted_pressure_plate, "heavy_weighted_pressure_plate");
/*  890 */     registerBlock((Block)Blocks.hopper, "hopper");
/*  891 */     registerBlock(Blocks.ice, "ice");
/*  892 */     registerBlock(Blocks.iron_bars, "iron_bars");
/*  893 */     registerBlock(Blocks.iron_block, "iron_block");
/*  894 */     registerBlock(Blocks.iron_ore, "iron_ore");
/*  895 */     registerBlock(Blocks.iron_trapdoor, "iron_trapdoor");
/*  896 */     registerBlock(Blocks.jukebox, "jukebox");
/*  897 */     registerBlock(Blocks.jungle_stairs, "jungle_stairs");
/*  898 */     registerBlock(Blocks.ladder, "ladder");
/*  899 */     registerBlock(Blocks.lapis_block, "lapis_block");
/*  900 */     registerBlock(Blocks.lapis_ore, "lapis_ore");
/*  901 */     registerBlock(Blocks.lever, "lever");
/*  902 */     registerBlock(Blocks.light_weighted_pressure_plate, "light_weighted_pressure_plate");
/*  903 */     registerBlock(Blocks.lit_pumpkin, "lit_pumpkin");
/*  904 */     registerBlock(Blocks.melon_block, "melon_block");
/*  905 */     registerBlock(Blocks.mossy_cobblestone, "mossy_cobblestone");
/*  906 */     registerBlock((Block)Blocks.mycelium, "mycelium");
/*  907 */     registerBlock(Blocks.netherrack, "netherrack");
/*  908 */     registerBlock(Blocks.nether_brick, "nether_brick");
/*  909 */     registerBlock(Blocks.nether_brick_fence, "nether_brick_fence");
/*  910 */     registerBlock(Blocks.nether_brick_stairs, "nether_brick_stairs");
/*  911 */     registerBlock(Blocks.noteblock, "noteblock");
/*  912 */     registerBlock(Blocks.oak_stairs, "oak_stairs");
/*  913 */     registerBlock(Blocks.obsidian, "obsidian");
/*  914 */     registerBlock(Blocks.packed_ice, "packed_ice");
/*  915 */     registerBlock((Block)Blocks.piston, "piston");
/*  916 */     registerBlock(Blocks.pumpkin, "pumpkin");
/*  917 */     registerBlock(Blocks.quartz_ore, "quartz_ore");
/*  918 */     registerBlock(Blocks.quartz_stairs, "quartz_stairs");
/*  919 */     registerBlock(Blocks.rail, "rail");
/*  920 */     registerBlock(Blocks.redstone_block, "redstone_block");
/*  921 */     registerBlock(Blocks.redstone_lamp, "redstone_lamp");
/*  922 */     registerBlock(Blocks.redstone_ore, "redstone_ore");
/*  923 */     registerBlock(Blocks.redstone_torch, "redstone_torch");
/*  924 */     registerBlock((Block)Blocks.red_mushroom, "red_mushroom");
/*  925 */     registerBlock(Blocks.sandstone_stairs, "sandstone_stairs");
/*  926 */     registerBlock(Blocks.red_sandstone_stairs, "red_sandstone_stairs");
/*  927 */     registerBlock(Blocks.sea_lantern, "sea_lantern");
/*  928 */     registerBlock(Blocks.slime_block, "slime");
/*  929 */     registerBlock(Blocks.snow, "snow");
/*  930 */     registerBlock(Blocks.snow_layer, "snow_layer");
/*  931 */     registerBlock(Blocks.soul_sand, "soul_sand");
/*  932 */     registerBlock(Blocks.spruce_stairs, "spruce_stairs");
/*  933 */     registerBlock((Block)Blocks.sticky_piston, "sticky_piston");
/*  934 */     registerBlock(Blocks.stone_brick_stairs, "stone_brick_stairs");
/*  935 */     registerBlock(Blocks.stone_button, "stone_button");
/*  936 */     registerBlock(Blocks.stone_pressure_plate, "stone_pressure_plate");
/*  937 */     registerBlock(Blocks.stone_stairs, "stone_stairs");
/*  938 */     registerBlock(Blocks.tnt, "tnt");
/*  939 */     registerBlock(Blocks.torch, "torch");
/*  940 */     registerBlock(Blocks.trapdoor, "trapdoor");
/*  941 */     registerBlock((Block)Blocks.tripwire_hook, "tripwire_hook");
/*  942 */     registerBlock(Blocks.vine, "vine");
/*  943 */     registerBlock(Blocks.waterlily, "waterlily");
/*  944 */     registerBlock(Blocks.web, "web");
/*  945 */     registerBlock(Blocks.wooden_button, "wooden_button");
/*  946 */     registerBlock(Blocks.wooden_pressure_plate, "wooden_pressure_plate");
/*  947 */     registerBlock((Block)Blocks.yellow_flower, BlockFlower.EnumFlowerType.DANDELION.getMeta(), "dandelion");
/*  948 */     registerBlock((Block)Blocks.chest, "chest");
/*  949 */     registerBlock(Blocks.trapped_chest, "trapped_chest");
/*  950 */     registerBlock(Blocks.ender_chest, "ender_chest");
/*  951 */     registerItem(Items.iron_shovel, "iron_shovel");
/*  952 */     registerItem(Items.iron_pickaxe, "iron_pickaxe");
/*  953 */     registerItem(Items.iron_axe, "iron_axe");
/*  954 */     registerItem(Items.flint_and_steel, "flint_and_steel");
/*  955 */     registerItem(Items.apple, "apple");
/*  956 */     registerItem((Item)Items.bow, 0, "bow");
/*  957 */     registerItem((Item)Items.bow, 1, "bow_pulling_0");
/*  958 */     registerItem((Item)Items.bow, 2, "bow_pulling_1");
/*  959 */     registerItem((Item)Items.bow, 3, "bow_pulling_2");
/*  960 */     registerItem(Items.arrow, "arrow");
/*  961 */     registerItem(Items.coal, 0, "coal");
/*  962 */     registerItem(Items.coal, 1, "charcoal");
/*  963 */     registerItem(Items.diamond, "diamond");
/*  964 */     registerItem(Items.iron_ingot, "iron_ingot");
/*  965 */     registerItem(Items.gold_ingot, "gold_ingot");
/*  966 */     registerItem(Items.iron_sword, "iron_sword");
/*  967 */     registerItem(Items.wooden_sword, "wooden_sword");
/*  968 */     registerItem(Items.wooden_shovel, "wooden_shovel");
/*  969 */     registerItem(Items.wooden_pickaxe, "wooden_pickaxe");
/*  970 */     registerItem(Items.wooden_axe, "wooden_axe");
/*  971 */     registerItem(Items.stone_sword, "stone_sword");
/*  972 */     registerItem(Items.stone_shovel, "stone_shovel");
/*  973 */     registerItem(Items.stone_pickaxe, "stone_pickaxe");
/*  974 */     registerItem(Items.stone_axe, "stone_axe");
/*  975 */     registerItem(Items.diamond_sword, "diamond_sword");
/*  976 */     registerItem(Items.diamond_shovel, "diamond_shovel");
/*  977 */     registerItem(Items.diamond_pickaxe, "diamond_pickaxe");
/*  978 */     registerItem(Items.diamond_axe, "diamond_axe");
/*  979 */     registerItem(Items.stick, "stick");
/*  980 */     registerItem(Items.bowl, "bowl");
/*  981 */     registerItem(Items.mushroom_stew, "mushroom_stew");
/*  982 */     registerItem(Items.golden_sword, "golden_sword");
/*  983 */     registerItem(Items.golden_shovel, "golden_shovel");
/*  984 */     registerItem(Items.golden_pickaxe, "golden_pickaxe");
/*  985 */     registerItem(Items.golden_axe, "golden_axe");
/*  986 */     registerItem(Items.string, "string");
/*  987 */     registerItem(Items.feather, "feather");
/*  988 */     registerItem(Items.gunpowder, "gunpowder");
/*  989 */     registerItem(Items.wooden_hoe, "wooden_hoe");
/*  990 */     registerItem(Items.stone_hoe, "stone_hoe");
/*  991 */     registerItem(Items.iron_hoe, "iron_hoe");
/*  992 */     registerItem(Items.diamond_hoe, "diamond_hoe");
/*  993 */     registerItem(Items.golden_hoe, "golden_hoe");
/*  994 */     registerItem(Items.wheat_seeds, "wheat_seeds");
/*  995 */     registerItem(Items.wheat, "wheat");
/*  996 */     registerItem(Items.bread, "bread");
/*  997 */     registerItem((Item)Items.leather_helmet, "leather_helmet");
/*  998 */     registerItem((Item)Items.leather_chestplate, "leather_chestplate");
/*  999 */     registerItem((Item)Items.leather_leggings, "leather_leggings");
/* 1000 */     registerItem((Item)Items.leather_boots, "leather_boots");
/* 1001 */     registerItem((Item)Items.chainmail_helmet, "chainmail_helmet");
/* 1002 */     registerItem((Item)Items.chainmail_chestplate, "chainmail_chestplate");
/* 1003 */     registerItem((Item)Items.chainmail_leggings, "chainmail_leggings");
/* 1004 */     registerItem((Item)Items.chainmail_boots, "chainmail_boots");
/* 1005 */     registerItem((Item)Items.iron_helmet, "iron_helmet");
/* 1006 */     registerItem((Item)Items.iron_chestplate, "iron_chestplate");
/* 1007 */     registerItem((Item)Items.iron_leggings, "iron_leggings");
/* 1008 */     registerItem((Item)Items.iron_boots, "iron_boots");
/* 1009 */     registerItem((Item)Items.diamond_helmet, "diamond_helmet");
/* 1010 */     registerItem((Item)Items.diamond_chestplate, "diamond_chestplate");
/* 1011 */     registerItem((Item)Items.diamond_leggings, "diamond_leggings");
/* 1012 */     registerItem((Item)Items.diamond_boots, "diamond_boots");
/* 1013 */     registerItem((Item)Items.golden_helmet, "golden_helmet");
/* 1014 */     registerItem((Item)Items.golden_chestplate, "golden_chestplate");
/* 1015 */     registerItem((Item)Items.golden_leggings, "golden_leggings");
/* 1016 */     registerItem((Item)Items.golden_boots, "golden_boots");
/* 1017 */     registerItem(Items.flint, "flint");
/* 1018 */     registerItem(Items.porkchop, "porkchop");
/* 1019 */     registerItem(Items.cooked_porkchop, "cooked_porkchop");
/* 1020 */     registerItem(Items.painting, "painting");
/* 1021 */     registerItem(Items.golden_apple, "golden_apple");
/* 1022 */     registerItem(Items.golden_apple, 1, "golden_apple");
/* 1023 */     registerItem(Items.sign, "sign");
/* 1024 */     registerItem(Items.oak_door, "oak_door");
/* 1025 */     registerItem(Items.spruce_door, "spruce_door");
/* 1026 */     registerItem(Items.birch_door, "birch_door");
/* 1027 */     registerItem(Items.jungle_door, "jungle_door");
/* 1028 */     registerItem(Items.acacia_door, "acacia_door");
/* 1029 */     registerItem(Items.dark_oak_door, "dark_oak_door");
/* 1030 */     registerItem(Items.bucket, "bucket");
/* 1031 */     registerItem(Items.water_bucket, "water_bucket");
/* 1032 */     registerItem(Items.lava_bucket, "lava_bucket");
/* 1033 */     registerItem(Items.minecart, "minecart");
/* 1034 */     registerItem(Items.saddle, "saddle");
/* 1035 */     registerItem(Items.iron_door, "iron_door");
/* 1036 */     registerItem(Items.redstone, "redstone");
/* 1037 */     registerItem(Items.snowball, "snowball");
/* 1038 */     registerItem(Items.boat, "boat");
/* 1039 */     registerItem(Items.leather, "leather");
/* 1040 */     registerItem(Items.milk_bucket, "milk_bucket");
/* 1041 */     registerItem(Items.brick, "brick");
/* 1042 */     registerItem(Items.clay_ball, "clay_ball");
/* 1043 */     registerItem(Items.reeds, "reeds");
/* 1044 */     registerItem(Items.paper, "paper");
/* 1045 */     registerItem(Items.book, "book");
/* 1046 */     registerItem(Items.slime_ball, "slime_ball");
/* 1047 */     registerItem(Items.chest_minecart, "chest_minecart");
/* 1048 */     registerItem(Items.furnace_minecart, "furnace_minecart");
/* 1049 */     registerItem(Items.egg, "egg");
/* 1050 */     registerItem(Items.compass, "compass");
/* 1051 */     registerItem((Item)Items.fishing_rod, "fishing_rod");
/* 1052 */     registerItem((Item)Items.fishing_rod, 1, "fishing_rod_cast");
/* 1053 */     registerItem(Items.clock, "clock");
/* 1054 */     registerItem(Items.glowstone_dust, "glowstone_dust");
/* 1055 */     registerItem(Items.fish, ItemFishFood.FishType.COD.getMetadata(), "cod");
/* 1056 */     registerItem(Items.fish, ItemFishFood.FishType.SALMON.getMetadata(), "salmon");
/* 1057 */     registerItem(Items.fish, ItemFishFood.FishType.CLOWNFISH.getMetadata(), "clownfish");
/* 1058 */     registerItem(Items.fish, ItemFishFood.FishType.PUFFERFISH.getMetadata(), "pufferfish");
/* 1059 */     registerItem(Items.cooked_fish, ItemFishFood.FishType.COD.getMetadata(), "cooked_cod");
/* 1060 */     registerItem(Items.cooked_fish, ItemFishFood.FishType.SALMON.getMetadata(), "cooked_salmon");
/* 1061 */     registerItem(Items.dye, EnumDyeColor.BLACK.getDyeDamage(), "dye_black");
/* 1062 */     registerItem(Items.dye, EnumDyeColor.RED.getDyeDamage(), "dye_red");
/* 1063 */     registerItem(Items.dye, EnumDyeColor.GREEN.getDyeDamage(), "dye_green");
/* 1064 */     registerItem(Items.dye, EnumDyeColor.BROWN.getDyeDamage(), "dye_brown");
/* 1065 */     registerItem(Items.dye, EnumDyeColor.BLUE.getDyeDamage(), "dye_blue");
/* 1066 */     registerItem(Items.dye, EnumDyeColor.PURPLE.getDyeDamage(), "dye_purple");
/* 1067 */     registerItem(Items.dye, EnumDyeColor.CYAN.getDyeDamage(), "dye_cyan");
/* 1068 */     registerItem(Items.dye, EnumDyeColor.SILVER.getDyeDamage(), "dye_silver");
/* 1069 */     registerItem(Items.dye, EnumDyeColor.GRAY.getDyeDamage(), "dye_gray");
/* 1070 */     registerItem(Items.dye, EnumDyeColor.PINK.getDyeDamage(), "dye_pink");
/* 1071 */     registerItem(Items.dye, EnumDyeColor.LIME.getDyeDamage(), "dye_lime");
/* 1072 */     registerItem(Items.dye, EnumDyeColor.YELLOW.getDyeDamage(), "dye_yellow");
/* 1073 */     registerItem(Items.dye, EnumDyeColor.LIGHT_BLUE.getDyeDamage(), "dye_light_blue");
/* 1074 */     registerItem(Items.dye, EnumDyeColor.MAGENTA.getDyeDamage(), "dye_magenta");
/* 1075 */     registerItem(Items.dye, EnumDyeColor.ORANGE.getDyeDamage(), "dye_orange");
/* 1076 */     registerItem(Items.dye, EnumDyeColor.WHITE.getDyeDamage(), "dye_white");
/* 1077 */     registerItem(Items.bone, "bone");
/* 1078 */     registerItem(Items.sugar, "sugar");
/* 1079 */     registerItem(Items.cake, "cake");
/* 1080 */     registerItem(Items.bed, "bed");
/* 1081 */     registerItem(Items.repeater, "repeater");
/* 1082 */     registerItem(Items.cookie, "cookie");
/* 1083 */     registerItem((Item)Items.shears, "shears");
/* 1084 */     registerItem(Items.melon, "melon");
/* 1085 */     registerItem(Items.pumpkin_seeds, "pumpkin_seeds");
/* 1086 */     registerItem(Items.melon_seeds, "melon_seeds");
/* 1087 */     registerItem(Items.beef, "beef");
/* 1088 */     registerItem(Items.cooked_beef, "cooked_beef");
/* 1089 */     registerItem(Items.chicken, "chicken");
/* 1090 */     registerItem(Items.cooked_chicken, "cooked_chicken");
/* 1091 */     registerItem(Items.rabbit, "rabbit");
/* 1092 */     registerItem(Items.cooked_rabbit, "cooked_rabbit");
/* 1093 */     registerItem(Items.mutton, "mutton");
/* 1094 */     registerItem(Items.cooked_mutton, "cooked_mutton");
/* 1095 */     registerItem(Items.rabbit_foot, "rabbit_foot");
/* 1096 */     registerItem(Items.rabbit_hide, "rabbit_hide");
/* 1097 */     registerItem(Items.rabbit_stew, "rabbit_stew");
/* 1098 */     registerItem(Items.rotten_flesh, "rotten_flesh");
/* 1099 */     registerItem(Items.ender_pearl, "ender_pearl");
/* 1100 */     registerItem(Items.blaze_rod, "blaze_rod");
/* 1101 */     registerItem(Items.ghast_tear, "ghast_tear");
/* 1102 */     registerItem(Items.gold_nugget, "gold_nugget");
/* 1103 */     registerItem(Items.nether_wart, "nether_wart");
/* 1104 */     this.itemModelMesher.register((Item)Items.potionitem, new ItemMeshDefinition()
/*      */         {
/*      */           public ModelResourceLocation getModelLocation(ItemStack stack)
/*      */           {
/* 1108 */             return ItemPotion.isSplash(stack.getMetadata()) ? new ModelResourceLocation("bottle_splash", "inventory") : new ModelResourceLocation("bottle_drinkable", "inventory");
/*      */           }
/*      */         });
/* 1111 */     registerItem(Items.glass_bottle, "glass_bottle");
/* 1112 */     registerItem(Items.spider_eye, "spider_eye");
/* 1113 */     registerItem(Items.fermented_spider_eye, "fermented_spider_eye");
/* 1114 */     registerItem(Items.blaze_powder, "blaze_powder");
/* 1115 */     registerItem(Items.magma_cream, "magma_cream");
/* 1116 */     registerItem(Items.brewing_stand, "brewing_stand");
/* 1117 */     registerItem(Items.cauldron, "cauldron");
/* 1118 */     registerItem(Items.ender_eye, "ender_eye");
/* 1119 */     registerItem(Items.speckled_melon, "speckled_melon");
/* 1120 */     this.itemModelMesher.register(Items.spawn_egg, new ItemMeshDefinition()
/*      */         {
/*      */           public ModelResourceLocation getModelLocation(ItemStack stack)
/*      */           {
/* 1124 */             return new ModelResourceLocation("spawn_egg", "inventory");
/*      */           }
/*      */         });
/* 1127 */     registerItem(Items.experience_bottle, "experience_bottle");
/* 1128 */     registerItem(Items.fire_charge, "fire_charge");
/* 1129 */     registerItem(Items.writable_book, "writable_book");
/* 1130 */     registerItem(Items.emerald, "emerald");
/* 1131 */     registerItem(Items.item_frame, "item_frame");
/* 1132 */     registerItem(Items.flower_pot, "flower_pot");
/* 1133 */     registerItem(Items.carrot, "carrot");
/* 1134 */     registerItem(Items.potato, "potato");
/* 1135 */     registerItem(Items.baked_potato, "baked_potato");
/* 1136 */     registerItem(Items.poisonous_potato, "poisonous_potato");
/* 1137 */     registerItem((Item)Items.map, "map");
/* 1138 */     registerItem(Items.golden_carrot, "golden_carrot");
/* 1139 */     registerItem(Items.skull, 0, "skull_skeleton");
/* 1140 */     registerItem(Items.skull, 1, "skull_wither");
/* 1141 */     registerItem(Items.skull, 2, "skull_zombie");
/* 1142 */     registerItem(Items.skull, 3, "skull_char");
/* 1143 */     registerItem(Items.skull, 4, "skull_creeper");
/* 1144 */     registerItem(Items.carrot_on_a_stick, "carrot_on_a_stick");
/* 1145 */     registerItem(Items.nether_star, "nether_star");
/* 1146 */     registerItem(Items.pumpkin_pie, "pumpkin_pie");
/* 1147 */     registerItem(Items.firework_charge, "firework_charge");
/* 1148 */     registerItem(Items.comparator, "comparator");
/* 1149 */     registerItem(Items.netherbrick, "netherbrick");
/* 1150 */     registerItem(Items.quartz, "quartz");
/* 1151 */     registerItem(Items.tnt_minecart, "tnt_minecart");
/* 1152 */     registerItem(Items.hopper_minecart, "hopper_minecart");
/* 1153 */     registerItem((Item)Items.armor_stand, "armor_stand");
/* 1154 */     registerItem(Items.iron_horse_armor, "iron_horse_armor");
/* 1155 */     registerItem(Items.golden_horse_armor, "golden_horse_armor");
/* 1156 */     registerItem(Items.diamond_horse_armor, "diamond_horse_armor");
/* 1157 */     registerItem(Items.lead, "lead");
/* 1158 */     registerItem(Items.name_tag, "name_tag");
/* 1159 */     this.itemModelMesher.register(Items.banner, new ItemMeshDefinition()
/*      */         {
/*      */           public ModelResourceLocation getModelLocation(ItemStack stack)
/*      */           {
/* 1163 */             return new ModelResourceLocation("banner", "inventory");
/*      */           }
/*      */         });
/* 1166 */     registerItem(Items.record_13, "record_13");
/* 1167 */     registerItem(Items.record_cat, "record_cat");
/* 1168 */     registerItem(Items.record_blocks, "record_blocks");
/* 1169 */     registerItem(Items.record_chirp, "record_chirp");
/* 1170 */     registerItem(Items.record_far, "record_far");
/* 1171 */     registerItem(Items.record_mall, "record_mall");
/* 1172 */     registerItem(Items.record_mellohi, "record_mellohi");
/* 1173 */     registerItem(Items.record_stal, "record_stal");
/* 1174 */     registerItem(Items.record_strad, "record_strad");
/* 1175 */     registerItem(Items.record_ward, "record_ward");
/* 1176 */     registerItem(Items.record_11, "record_11");
/* 1177 */     registerItem(Items.record_wait, "record_wait");
/* 1178 */     registerItem(Items.prismarine_shard, "prismarine_shard");
/* 1179 */     registerItem(Items.prismarine_crystals, "prismarine_crystals");
/* 1180 */     this.itemModelMesher.register((Item)Items.enchanted_book, new ItemMeshDefinition()
/*      */         {
/*      */           public ModelResourceLocation getModelLocation(ItemStack stack)
/*      */           {
/* 1184 */             return new ModelResourceLocation("enchanted_book", "inventory");
/*      */           }
/*      */         });
/* 1187 */     this.itemModelMesher.register((Item)Items.filled_map, new ItemMeshDefinition()
/*      */         {
/*      */           public ModelResourceLocation getModelLocation(ItemStack stack)
/*      */           {
/* 1191 */             return new ModelResourceLocation("filled_map", "inventory");
/*      */           }
/*      */         });
/* 1194 */     registerBlock(Blocks.command_block, "command_block");
/* 1195 */     registerItem(Items.fireworks, "fireworks");
/* 1196 */     registerItem(Items.command_block_minecart, "command_block_minecart");
/* 1197 */     registerBlock(Blocks.barrier, "barrier");
/* 1198 */     registerBlock(Blocks.mob_spawner, "mob_spawner");
/* 1199 */     registerItem(Items.written_book, "written_book");
/* 1200 */     registerBlock(Blocks.brown_mushroom_block, BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "brown_mushroom_block");
/* 1201 */     registerBlock(Blocks.red_mushroom_block, BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "red_mushroom_block");
/* 1202 */     registerBlock(Blocks.dragon_egg, "dragon_egg");
/*      */     
/* 1204 */     if (Reflector.ModelLoader_onRegisterItems.exists())
/*      */     {
/* 1206 */       Reflector.call(Reflector.ModelLoader_onRegisterItems, new Object[] { this.itemModelMesher });
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void onResourceManagerReload(IResourceManager resourceManager) {
/* 1212 */     this.itemModelMesher.rebuildCache();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void forgeHooksClient_putQuadColor(WorldRenderer p_forgeHooksClient_putQuadColor_0_, BakedQuad p_forgeHooksClient_putQuadColor_1_, int p_forgeHooksClient_putQuadColor_2_) {
/* 1217 */     float f = (p_forgeHooksClient_putQuadColor_2_ & 0xFF);
/* 1218 */     float f1 = (p_forgeHooksClient_putQuadColor_2_ >>> 8 & 0xFF);
/* 1219 */     float f2 = (p_forgeHooksClient_putQuadColor_2_ >>> 16 & 0xFF);
/* 1220 */     float f3 = (p_forgeHooksClient_putQuadColor_2_ >>> 24 & 0xFF);
/* 1221 */     int[] aint = p_forgeHooksClient_putQuadColor_1_.getVertexData();
/* 1222 */     int i = aint.length / 4;
/*      */     
/* 1224 */     for (int j = 0; j < 4; j++) {
/*      */       
/* 1226 */       int k = aint[3 + i * j];
/* 1227 */       float f4 = (k & 0xFF);
/* 1228 */       float f5 = (k >>> 8 & 0xFF);
/* 1229 */       float f6 = (k >>> 16 & 0xFF);
/* 1230 */       float f7 = (k >>> 24 & 0xFF);
/* 1231 */       int l = Math.min(255, (int)(f * f4 / 255.0F));
/* 1232 */       int i1 = Math.min(255, (int)(f1 * f5 / 255.0F));
/* 1233 */       int j1 = Math.min(255, (int)(f2 * f6 / 255.0F));
/* 1234 */       int k1 = Math.min(255, (int)(f3 * f7 / 255.0F));
/* 1235 */       p_forgeHooksClient_putQuadColor_0_.putColorRGBA(p_forgeHooksClient_putQuadColor_0_.getColorIndex(4 - j), l, i1, j1, k1);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
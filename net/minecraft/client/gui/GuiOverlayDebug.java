/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.ClientBrandRetriever;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.EnumSkyBlock;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.WorldType;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.optifine.SmartAnimations;
/*     */ import net.optifine.TextureAnimations;
/*     */ import net.optifine.reflect.Reflector;
/*     */ import net.optifine.util.MemoryMonitor;
/*     */ import net.optifine.util.NativeMemory;
/*     */ import org.lwjgl.opengl.Display;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class GuiOverlayDebug extends Gui {
/*     */   private final Minecraft mc;
/*  40 */   private String debugOF = null; private final FontRenderer fontRenderer;
/*  41 */   private List<String> debugInfoLeft = null;
/*  42 */   private List<String> debugInfoRight = null;
/*  43 */   private long updateInfoLeftTimeMs = 0L;
/*  44 */   private long updateInfoRightTimeMs = 0L;
/*     */ 
/*     */   
/*     */   public GuiOverlayDebug(Minecraft mc) {
/*  48 */     this.mc = mc;
/*  49 */     this.fontRenderer = mc.fontRendererObj;
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderDebugInfo(ScaledResolution scaledResolutionIn) {
/*  54 */     this.mc.mcProfiler.startSection("debug");
/*  55 */     GlStateManager.pushMatrix();
/*  56 */     renderDebugInfoLeft();
/*  57 */     renderDebugInfoRight(scaledResolutionIn);
/*  58 */     GlStateManager.popMatrix();
/*     */     
/*  60 */     if (this.mc.gameSettings.showLagometer)
/*     */     {
/*  62 */       renderLagometer();
/*     */     }
/*     */     
/*  65 */     this.mc.mcProfiler.endSection();
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isReducedDebug() {
/*  70 */     return (this.mc.thePlayer.hasReducedDebug() || this.mc.gameSettings.reducedDebugInfo);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderDebugInfoLeft() {
/*  75 */     List<String> list = this.debugInfoLeft;
/*     */     
/*  77 */     if (list == null || System.currentTimeMillis() > this.updateInfoLeftTimeMs) {
/*     */       
/*  79 */       list = call();
/*  80 */       this.debugInfoLeft = list;
/*  81 */       this.updateInfoLeftTimeMs = System.currentTimeMillis() + 100L;
/*     */     } 
/*     */     
/*  84 */     for (int i = 0; i < list.size(); i++) {
/*     */       
/*  86 */       String s = list.get(i);
/*     */       
/*  88 */       if (!Strings.isNullOrEmpty(s)) {
/*     */         
/*  90 */         int j = this.fontRenderer.FONT_HEIGHT;
/*  91 */         int k = this.fontRenderer.getStringWidth(s);
/*  92 */         int l = 2;
/*  93 */         int i1 = 2 + j * i;
/*  94 */         drawRect(1.0D, (i1 - 1), (2 + k + 1), (i1 + j - 1), -1873784752);
/*  95 */         this.fontRenderer.drawString(s, 2.0D, i1, 14737632);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderDebugInfoRight(ScaledResolution scaledRes) {
/* 102 */     List<String> list = this.debugInfoRight;
/*     */     
/* 104 */     if (list == null || System.currentTimeMillis() > this.updateInfoRightTimeMs) {
/*     */       
/* 106 */       list = getDebugInfoRight();
/* 107 */       this.debugInfoRight = list;
/* 108 */       this.updateInfoRightTimeMs = System.currentTimeMillis() + 100L;
/*     */     } 
/*     */     
/* 111 */     for (int i = 0; i < list.size(); i++) {
/*     */       
/* 113 */       String s = list.get(i);
/*     */       
/* 115 */       if (!Strings.isNullOrEmpty(s)) {
/*     */         
/* 117 */         int j = this.fontRenderer.FONT_HEIGHT;
/* 118 */         int k = this.fontRenderer.getStringWidth(s);
/* 119 */         int l = scaledRes.getScaledWidth() - 2 - k;
/* 120 */         int i1 = 2 + j * i;
/* 121 */         drawRect((l - 1), (i1 - 1), (l + k + 1), (i1 + j - 1), -1873784752);
/* 122 */         this.fontRenderer.drawString(s, l, i1, 14737632);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<String> call() {
/* 130 */     BlockPos blockpos = new BlockPos((this.mc.getRenderViewEntity()).posX, (this.mc.getRenderViewEntity().getEntityBoundingBox()).minY, (this.mc.getRenderViewEntity()).posZ);
/*     */     
/* 132 */     if (this.mc.debug != this.debugOF) {
/*     */       
/* 134 */       StringBuffer stringbuffer = new StringBuffer(this.mc.debug);
/* 135 */       int i = Config.getFpsMin();
/* 136 */       int j = this.mc.debug.indexOf(" fps ");
/*     */       
/* 138 */       if (j >= 0)
/*     */       {
/* 140 */         stringbuffer.insert(j, "/" + i);
/*     */       }
/*     */       
/* 143 */       if (Config.isSmoothFps())
/*     */       {
/* 145 */         stringbuffer.append(" sf");
/*     */       }
/*     */       
/* 148 */       if (Config.isFastRender())
/*     */       {
/* 150 */         stringbuffer.append(" fr");
/*     */       }
/*     */       
/* 153 */       if (Config.isAnisotropicFiltering())
/*     */       {
/* 155 */         stringbuffer.append(" af");
/*     */       }
/*     */       
/* 158 */       if (Config.isAntialiasing())
/*     */       {
/* 160 */         stringbuffer.append(" aa");
/*     */       }
/*     */       
/* 163 */       if (Config.isRenderRegions())
/*     */       {
/* 165 */         stringbuffer.append(" reg");
/*     */       }
/*     */       
/* 168 */       if (Config.isShaders())
/*     */       {
/* 170 */         stringbuffer.append(" sh");
/*     */       }
/*     */       
/* 173 */       this.mc.debug = stringbuffer.toString();
/* 174 */       this.debugOF = this.mc.debug;
/*     */     } 
/*     */     
/* 177 */     StringBuilder stringbuilder = new StringBuilder();
/* 178 */     TextureMap texturemap = Config.getTextureMap();
/* 179 */     stringbuilder.append(", A: ");
/*     */     
/* 181 */     if (SmartAnimations.isActive()) {
/*     */       
/* 183 */       stringbuilder.append(texturemap.getCountAnimationsActive() + TextureAnimations.getCountAnimationsActive());
/* 184 */       stringbuilder.append("/");
/*     */     } 
/*     */     
/* 187 */     stringbuilder.append(texturemap.getCountAnimations() + TextureAnimations.getCountAnimations());
/* 188 */     String s1 = stringbuilder.toString();
/*     */     
/* 190 */     if (isReducedDebug())
/*     */     {
/* 192 */       return Lists.newArrayList((Object[])new String[] { "Minecraft 1.8.9 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities() + s1, this.mc.theWorld.getProviderName(), "", String.format("Chunk-relative: %d %d %d", new Object[] { Integer.valueOf(blockpos.getX() & 0xF), Integer.valueOf(blockpos.getY() & 0xF), Integer.valueOf(blockpos.getZ() & 0xF) }) });
/*     */     }
/*     */ 
/*     */     
/* 196 */     Entity entity = this.mc.getRenderViewEntity();
/* 197 */     EnumFacing enumfacing = entity.getHorizontalFacing();
/* 198 */     String s = "Invalid";
/*     */     
/* 200 */     switch (enumfacing) {
/*     */       
/*     */       case NORTH:
/* 203 */         s = "Towards negative Z";
/*     */         break;
/*     */       
/*     */       case SOUTH:
/* 207 */         s = "Towards positive Z";
/*     */         break;
/*     */       
/*     */       case WEST:
/* 211 */         s = "Towards negative X";
/*     */         break;
/*     */       
/*     */       case EAST:
/* 215 */         s = "Towards positive X";
/*     */         break;
/*     */     } 
/* 218 */     List<String> list = Lists.newArrayList((Object[])new String[] { "Minecraft 1.8.9 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities() + s1, this.mc.theWorld.getProviderName(), "", String.format("XYZ: %.3f / %.5f / %.3f", new Object[] { Double.valueOf((this.mc.getRenderViewEntity()).posX), Double.valueOf((this.mc.getRenderViewEntity().getEntityBoundingBox()).minY), Double.valueOf((this.mc.getRenderViewEntity()).posZ) }), String.format("Block: %d %d %d", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) }), String.format("Chunk: %d %d %d in %d %d %d", new Object[] { Integer.valueOf(blockpos.getX() & 0xF), Integer.valueOf(blockpos.getY() & 0xF), Integer.valueOf(blockpos.getZ() & 0xF), Integer.valueOf(blockpos.getX() >> 4), Integer.valueOf(blockpos.getY() >> 4), Integer.valueOf(blockpos.getZ() >> 4) }), String.format("Facing: %s (%s) (%.1f / %.1f)", new Object[] { enumfacing, s, Float.valueOf(MathHelper.wrapAngleTo180_float(entity.rotationYaw)), Float.valueOf(MathHelper.wrapAngleTo180_float(entity.rotationPitch)) }) });
/*     */     
/* 220 */     if (this.mc.theWorld != null && this.mc.theWorld.isBlockLoaded(blockpos)) {
/*     */       
/* 222 */       Chunk chunk = this.mc.theWorld.getChunkFromBlockCoords(blockpos);
/* 223 */       list.add("Biome: " + (chunk.getBiome(blockpos, this.mc.theWorld.getWorldChunkManager())).biomeName);
/* 224 */       list.add("Light: " + chunk.getLightSubtracted(blockpos, 0) + " (" + chunk.getLightFor(EnumSkyBlock.SKY, blockpos) + " sky, " + chunk.getLightFor(EnumSkyBlock.BLOCK, blockpos) + " block)");
/* 225 */       DifficultyInstance difficultyinstance = this.mc.theWorld.getDifficultyForLocation(blockpos);
/*     */       
/* 227 */       if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null) {
/*     */         
/* 229 */         EntityPlayerMP entityplayermp = this.mc.getIntegratedServer().getConfigurationManager().getPlayerByUUID(this.mc.thePlayer.getUniqueID());
/*     */         
/* 231 */         if (entityplayermp != null) {
/*     */           
/* 233 */           DifficultyInstance difficultyinstance1 = this.mc.getIntegratedServer().getDifficultyAsync(entityplayermp.worldObj, new BlockPos((Entity)entityplayermp));
/*     */           
/* 235 */           if (difficultyinstance1 != null)
/*     */           {
/* 237 */             difficultyinstance = difficultyinstance1;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 242 */       list.add(String.format("Local Difficulty: %.2f (Day %d)", new Object[] { Float.valueOf(difficultyinstance.getAdditionalDifficulty()), Long.valueOf(this.mc.theWorld.getWorldTime() / 24000L) }));
/*     */     } 
/*     */     
/* 245 */     if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive())
/*     */     {
/* 247 */       list.add("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName());
/*     */     }
/*     */     
/* 250 */     if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
/*     */       
/* 252 */       BlockPos blockpos1 = this.mc.objectMouseOver.getBlockPos();
/* 253 */       list.add(String.format("Looking at: %d %d %d", new Object[] { Integer.valueOf(blockpos1.getX()), Integer.valueOf(blockpos1.getY()), Integer.valueOf(blockpos1.getZ()) }));
/*     */     } 
/*     */     
/* 256 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<String> getDebugInfoRight() {
/* 262 */     long i = Runtime.getRuntime().maxMemory();
/* 263 */     long j = Runtime.getRuntime().totalMemory();
/* 264 */     long k = Runtime.getRuntime().freeMemory();
/* 265 */     long l = j - k;
/* 266 */     List<String> list = Lists.newArrayList((Object[])new String[] { String.format("Java: %s %dbit", new Object[] { System.getProperty("java.version"), Integer.valueOf(this.mc.isJava64bit() ? 64 : 32) }), String.format("Mem: % 2d%% %03d/%03dMB", new Object[] { Long.valueOf(l * 100L / i), Long.valueOf(bytesToMb(l)), Long.valueOf(bytesToMb(i)) }), String.format("Allocated: % 2d%% %03dMB", new Object[] { Long.valueOf(j * 100L / i), Long.valueOf(bytesToMb(j)) }), "", String.format("CPU: %s", new Object[] { OpenGlHelper.getCpu() }), "", String.format("Display: %dx%d (%s)", new Object[] { Integer.valueOf(Display.getWidth()), Integer.valueOf(Display.getHeight()), GL11.glGetString(7936) }), GL11.glGetString(7937), GL11.glGetString(7938) });
/* 267 */     long i1 = NativeMemory.getBufferAllocated();
/* 268 */     long j1 = NativeMemory.getBufferMaximum();
/* 269 */     String s = "Native: " + bytesToMb(i1) + "/" + bytesToMb(j1) + "MB";
/* 270 */     list.add(4, s);
/* 271 */     list.set(5, "GC: " + MemoryMonitor.getAllocationRateMb() + "MB/s");
/*     */     
/* 273 */     if (Reflector.FMLCommonHandler_getBrandings.exists()) {
/*     */       
/* 275 */       Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
/* 276 */       list.add("");
/* 277 */       list.addAll((Collection<? extends String>)Reflector.call(object, Reflector.FMLCommonHandler_getBrandings, new Object[] { Boolean.valueOf(false) }));
/*     */     } 
/*     */     
/* 280 */     if (isReducedDebug())
/*     */     {
/* 282 */       return list;
/*     */     }
/*     */ 
/*     */     
/* 286 */     if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
/*     */       
/* 288 */       BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
/* 289 */       IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
/*     */       
/* 291 */       if (this.mc.theWorld.getWorldType() != WorldType.DEBUG_WORLD)
/*     */       {
/* 293 */         iblockstate = iblockstate.getBlock().getActualState(iblockstate, (IBlockAccess)this.mc.theWorld, blockpos);
/*     */       }
/*     */       
/* 296 */       list.add("");
/* 297 */       list.add(String.valueOf(Block.blockRegistry.getNameForObject(iblockstate.getBlock())));
/*     */       
/* 299 */       for (UnmodifiableIterator<Map.Entry<IProperty, Comparable>> unmodifiableIterator = iblockstate.getProperties().entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<IProperty, Comparable> entry = unmodifiableIterator.next();
/*     */         
/* 301 */         String s1 = ((Comparable)entry.getValue()).toString();
/*     */         
/* 303 */         if (entry.getValue() == Boolean.TRUE) {
/*     */           
/* 305 */           s1 = EnumChatFormatting.GREEN + s1;
/*     */         }
/* 307 */         else if (entry.getValue() == Boolean.FALSE) {
/*     */           
/* 309 */           s1 = EnumChatFormatting.RED + s1;
/*     */         } 
/*     */         
/* 312 */         list.add(((IProperty)entry.getKey()).getName() + ": " + s1); }
/*     */     
/*     */     } 
/*     */     
/* 316 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderLagometer() {}
/*     */ 
/*     */ 
/*     */   
/*     */   private int getFrameColor(int p_181552_1_, int p_181552_2_, int p_181552_3_, int p_181552_4_) {
/* 326 */     return (p_181552_1_ < p_181552_3_) ? blendColors(-16711936, -256, p_181552_1_ / p_181552_3_) : blendColors(-256, -65536, (p_181552_1_ - p_181552_3_) / (p_181552_4_ - p_181552_3_));
/*     */   }
/*     */ 
/*     */   
/*     */   private int blendColors(int p_181553_1_, int p_181553_2_, float p_181553_3_) {
/* 331 */     int i = p_181553_1_ >> 24 & 0xFF;
/* 332 */     int j = p_181553_1_ >> 16 & 0xFF;
/* 333 */     int k = p_181553_1_ >> 8 & 0xFF;
/* 334 */     int l = p_181553_1_ & 0xFF;
/* 335 */     int i1 = p_181553_2_ >> 24 & 0xFF;
/* 336 */     int j1 = p_181553_2_ >> 16 & 0xFF;
/* 337 */     int k1 = p_181553_2_ >> 8 & 0xFF;
/* 338 */     int l1 = p_181553_2_ & 0xFF;
/* 339 */     int i2 = MathHelper.clamp_int((int)(i + (i1 - i) * p_181553_3_), 0, 255);
/* 340 */     int j2 = MathHelper.clamp_int((int)(j + (j1 - j) * p_181553_3_), 0, 255);
/* 341 */     int k2 = MathHelper.clamp_int((int)(k + (k1 - k) * p_181553_3_), 0, 255);
/* 342 */     int l2 = MathHelper.clamp_int((int)(l + (l1 - l) * p_181553_3_), 0, 255);
/* 343 */     return i2 << 24 | j2 << 16 | k2 << 8 | l2;
/*     */   }
/*     */ 
/*     */   
/*     */   private static long bytesToMb(long bytes) {
/* 348 */     return bytes / 1024L / 1024L;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiOverlayDebug.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
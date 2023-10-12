/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.primitives.Floats;
/*     */ import java.io.IOException;
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.gen.ChunkProviderSettings;
/*     */ 
/*     */ public class GuiCustomizeWorldScreen
/*     */   extends GuiScreen implements GuiSlider.FormatHelper, GuiPageButtonList.GuiResponder {
/*     */   private GuiCreateWorld field_175343_i;
/*  19 */   protected String field_175341_a = "Customize World Settings";
/*  20 */   protected String field_175333_f = "Page 1 of 3";
/*  21 */   protected String field_175335_g = "Basic Settings";
/*  22 */   protected String[] field_175342_h = new String[4]; private GuiPageButtonList field_175349_r;
/*     */   private GuiButton field_175348_s;
/*     */   private GuiButton field_175347_t;
/*     */   private GuiButton field_175346_u;
/*     */   private GuiButton field_175345_v;
/*     */   private GuiButton field_175344_w;
/*     */   private GuiButton field_175352_x;
/*     */   private GuiButton field_175351_y;
/*     */   private GuiButton field_175350_z;
/*     */   private boolean field_175338_A = false;
/*     */   private boolean field_175340_C = false;
/*  33 */   private int field_175339_B = 0;
/*     */   
/*  35 */   private Predicate<String> field_175332_D = new Predicate<String>()
/*     */     {
/*     */       public boolean apply(String p_apply_1_)
/*     */       {
/*  39 */         Float f = Floats.tryParse(p_apply_1_);
/*  40 */         return (p_apply_1_.length() == 0 || (f != null && Floats.isFinite(f.floatValue()) && f.floatValue() >= 0.0F));
/*     */       }
/*     */     };
/*  43 */   private ChunkProviderSettings.Factory field_175334_E = new ChunkProviderSettings.Factory();
/*     */   private ChunkProviderSettings.Factory field_175336_F;
/*  45 */   private Random random = new Random();
/*     */ 
/*     */   
/*     */   public GuiCustomizeWorldScreen(GuiScreen p_i45521_1_, String p_i45521_2_) {
/*  49 */     this.field_175343_i = (GuiCreateWorld)p_i45521_1_;
/*  50 */     func_175324_a(p_i45521_2_);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  55 */     int i = 0;
/*  56 */     int j = 0;
/*     */     
/*  58 */     if (this.field_175349_r != null) {
/*     */       
/*  60 */       i = this.field_175349_r.func_178059_e();
/*  61 */       j = this.field_175349_r.getAmountScrolled();
/*     */     } 
/*     */     
/*  64 */     this.field_175341_a = I18n.format("options.customizeTitle", new Object[0]);
/*  65 */     this.buttonList.clear();
/*  66 */     this.buttonList.add(this.field_175345_v = new GuiButton(302, 20, 5, 80, 20, I18n.format("createWorld.customize.custom.prev", new Object[0])));
/*  67 */     this.buttonList.add(this.field_175344_w = new GuiButton(303, this.width - 100, 5, 80, 20, I18n.format("createWorld.customize.custom.next", new Object[0])));
/*  68 */     this.buttonList.add(this.field_175346_u = new GuiButton(304, this.width / 2 - 187, this.height - 27, 90, 20, I18n.format("createWorld.customize.custom.defaults", new Object[0])));
/*  69 */     this.buttonList.add(this.field_175347_t = new GuiButton(301, this.width / 2 - 92, this.height - 27, 90, 20, I18n.format("createWorld.customize.custom.randomize", new Object[0])));
/*  70 */     this.buttonList.add(this.field_175350_z = new GuiButton(305, this.width / 2 + 3, this.height - 27, 90, 20, I18n.format("createWorld.customize.custom.presets", new Object[0])));
/*  71 */     this.buttonList.add(this.field_175348_s = new GuiButton(300, this.width / 2 + 98, this.height - 27, 90, 20, I18n.format("gui.done", new Object[0])));
/*  72 */     this.field_175346_u.enabled = this.field_175338_A;
/*  73 */     this.field_175352_x = new GuiButton(306, this.width / 2 - 55, 160, 50, 20, I18n.format("gui.yes", new Object[0]));
/*  74 */     this.field_175352_x.visible = false;
/*  75 */     this.buttonList.add(this.field_175352_x);
/*  76 */     this.field_175351_y = new GuiButton(307, this.width / 2 + 5, 160, 50, 20, I18n.format("gui.no", new Object[0]));
/*  77 */     this.field_175351_y.visible = false;
/*  78 */     this.buttonList.add(this.field_175351_y);
/*     */     
/*  80 */     if (this.field_175339_B != 0) {
/*     */       
/*  82 */       this.field_175352_x.visible = true;
/*  83 */       this.field_175351_y.visible = true;
/*     */     } 
/*     */     
/*  86 */     func_175325_f();
/*     */     
/*  88 */     if (i != 0) {
/*     */       
/*  90 */       this.field_175349_r.func_181156_c(i);
/*  91 */       this.field_175349_r.scrollBy(j);
/*  92 */       func_175328_i();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/*  98 */     super.handleMouseInput();
/*  99 */     this.field_175349_r.handleMouseInput();
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_175325_f() {
/* 104 */     GuiPageButtonList.GuiListEntry[] aguipagebuttonlist$guilistentry = { new GuiPageButtonList.GuiSlideEntry(160, I18n.format("createWorld.customize.custom.seaLevel", new Object[0]), true, this, 1.0F, 255.0F, this.field_175336_F.seaLevel), new GuiPageButtonList.GuiButtonEntry(148, I18n.format("createWorld.customize.custom.useCaves", new Object[0]), true, this.field_175336_F.useCaves), new GuiPageButtonList.GuiButtonEntry(150, I18n.format("createWorld.customize.custom.useStrongholds", new Object[0]), true, this.field_175336_F.useStrongholds), new GuiPageButtonList.GuiButtonEntry(151, I18n.format("createWorld.customize.custom.useVillages", new Object[0]), true, this.field_175336_F.useVillages), new GuiPageButtonList.GuiButtonEntry(152, I18n.format("createWorld.customize.custom.useMineShafts", new Object[0]), true, this.field_175336_F.useMineShafts), new GuiPageButtonList.GuiButtonEntry(153, I18n.format("createWorld.customize.custom.useTemples", new Object[0]), true, this.field_175336_F.useTemples), new GuiPageButtonList.GuiButtonEntry(210, I18n.format("createWorld.customize.custom.useMonuments", new Object[0]), true, this.field_175336_F.useMonuments), new GuiPageButtonList.GuiButtonEntry(154, I18n.format("createWorld.customize.custom.useRavines", new Object[0]), true, this.field_175336_F.useRavines), new GuiPageButtonList.GuiButtonEntry(149, I18n.format("createWorld.customize.custom.useDungeons", new Object[0]), true, this.field_175336_F.useDungeons), new GuiPageButtonList.GuiSlideEntry(157, I18n.format("createWorld.customize.custom.dungeonChance", new Object[0]), true, this, 1.0F, 100.0F, this.field_175336_F.dungeonChance), new GuiPageButtonList.GuiButtonEntry(155, I18n.format("createWorld.customize.custom.useWaterLakes", new Object[0]), true, this.field_175336_F.useWaterLakes), new GuiPageButtonList.GuiSlideEntry(158, I18n.format("createWorld.customize.custom.waterLakeChance", new Object[0]), true, this, 1.0F, 100.0F, this.field_175336_F.waterLakeChance), new GuiPageButtonList.GuiButtonEntry(156, I18n.format("createWorld.customize.custom.useLavaLakes", new Object[0]), true, this.field_175336_F.useLavaLakes), new GuiPageButtonList.GuiSlideEntry(159, I18n.format("createWorld.customize.custom.lavaLakeChance", new Object[0]), true, this, 10.0F, 100.0F, this.field_175336_F.lavaLakeChance), new GuiPageButtonList.GuiButtonEntry(161, I18n.format("createWorld.customize.custom.useLavaOceans", new Object[0]), true, this.field_175336_F.useLavaOceans), new GuiPageButtonList.GuiSlideEntry(162, I18n.format("createWorld.customize.custom.fixedBiome", new Object[0]), true, this, -1.0F, 37.0F, this.field_175336_F.fixedBiome), new GuiPageButtonList.GuiSlideEntry(163, I18n.format("createWorld.customize.custom.biomeSize", new Object[0]), true, this, 1.0F, 8.0F, this.field_175336_F.biomeSize), new GuiPageButtonList.GuiSlideEntry(164, I18n.format("createWorld.customize.custom.riverSize", new Object[0]), true, this, 1.0F, 5.0F, this.field_175336_F.riverSize) };
/* 105 */     GuiPageButtonList.GuiListEntry[] aguipagebuttonlist$guilistentry1 = { new GuiPageButtonList.GuiLabelEntry(416, I18n.format("tile.dirt.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(165, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, this.field_175336_F.dirtSize), new GuiPageButtonList.GuiSlideEntry(166, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, this.field_175336_F.dirtCount), new GuiPageButtonList.GuiSlideEntry(167, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.dirtMinHeight), new GuiPageButtonList.GuiSlideEntry(168, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.dirtMaxHeight), new GuiPageButtonList.GuiLabelEntry(417, I18n.format("tile.gravel.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(169, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, this.field_175336_F.gravelSize), new GuiPageButtonList.GuiSlideEntry(170, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, this.field_175336_F.gravelCount), new GuiPageButtonList.GuiSlideEntry(171, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.gravelMinHeight), new GuiPageButtonList.GuiSlideEntry(172, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.gravelMaxHeight), new GuiPageButtonList.GuiLabelEntry(418, I18n.format("tile.stone.granite.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(173, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, this.field_175336_F.graniteSize), new GuiPageButtonList.GuiSlideEntry(174, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, this.field_175336_F.graniteCount), new GuiPageButtonList.GuiSlideEntry(175, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.graniteMinHeight), new GuiPageButtonList.GuiSlideEntry(176, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.graniteMaxHeight), new GuiPageButtonList.GuiLabelEntry(419, I18n.format("tile.stone.diorite.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(177, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, this.field_175336_F.dioriteSize), new GuiPageButtonList.GuiSlideEntry(178, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, this.field_175336_F.dioriteCount), new GuiPageButtonList.GuiSlideEntry(179, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.dioriteMinHeight), new GuiPageButtonList.GuiSlideEntry(180, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.dioriteMaxHeight), new GuiPageButtonList.GuiLabelEntry(420, I18n.format("tile.stone.andesite.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(181, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, this.field_175336_F.andesiteSize), new GuiPageButtonList.GuiSlideEntry(182, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, this.field_175336_F.andesiteCount), new GuiPageButtonList.GuiSlideEntry(183, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.andesiteMinHeight), new GuiPageButtonList.GuiSlideEntry(184, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.andesiteMaxHeight), new GuiPageButtonList.GuiLabelEntry(421, I18n.format("tile.oreCoal.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(185, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, this.field_175336_F.coalSize), new GuiPageButtonList.GuiSlideEntry(186, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, this.field_175336_F.coalCount), new GuiPageButtonList.GuiSlideEntry(187, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.coalMinHeight), new GuiPageButtonList.GuiSlideEntry(189, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.coalMaxHeight), new GuiPageButtonList.GuiLabelEntry(422, I18n.format("tile.oreIron.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(190, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, this.field_175336_F.ironSize), new GuiPageButtonList.GuiSlideEntry(191, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, this.field_175336_F.ironCount), new GuiPageButtonList.GuiSlideEntry(192, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.ironMinHeight), new GuiPageButtonList.GuiSlideEntry(193, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.ironMaxHeight), new GuiPageButtonList.GuiLabelEntry(423, I18n.format("tile.oreGold.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(194, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, this.field_175336_F.goldSize), new GuiPageButtonList.GuiSlideEntry(195, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, this.field_175336_F.goldCount), new GuiPageButtonList.GuiSlideEntry(196, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.goldMinHeight), new GuiPageButtonList.GuiSlideEntry(197, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.goldMaxHeight), new GuiPageButtonList.GuiLabelEntry(424, I18n.format("tile.oreRedstone.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(198, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, this.field_175336_F.redstoneSize), new GuiPageButtonList.GuiSlideEntry(199, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, this.field_175336_F.redstoneCount), new GuiPageButtonList.GuiSlideEntry(200, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.redstoneMinHeight), new GuiPageButtonList.GuiSlideEntry(201, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.redstoneMaxHeight), new GuiPageButtonList.GuiLabelEntry(425, I18n.format("tile.oreDiamond.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(202, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, this.field_175336_F.diamondSize), new GuiPageButtonList.GuiSlideEntry(203, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, this.field_175336_F.diamondCount), new GuiPageButtonList.GuiSlideEntry(204, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.diamondMinHeight), new GuiPageButtonList.GuiSlideEntry(205, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.diamondMaxHeight), new GuiPageButtonList.GuiLabelEntry(426, I18n.format("tile.oreLapis.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(206, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, this.field_175336_F.lapisSize), new GuiPageButtonList.GuiSlideEntry(207, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, this.field_175336_F.lapisCount), new GuiPageButtonList.GuiSlideEntry(208, I18n.format("createWorld.customize.custom.center", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.lapisCenterHeight), new GuiPageButtonList.GuiSlideEntry(209, I18n.format("createWorld.customize.custom.spread", new Object[0]), false, this, 0.0F, 255.0F, this.field_175336_F.lapisSpread) };
/* 106 */     GuiPageButtonList.GuiListEntry[] aguipagebuttonlist$guilistentry2 = { new GuiPageButtonList.GuiSlideEntry(100, I18n.format("createWorld.customize.custom.mainNoiseScaleX", new Object[0]), false, this, 1.0F, 5000.0F, this.field_175336_F.mainNoiseScaleX), new GuiPageButtonList.GuiSlideEntry(101, I18n.format("createWorld.customize.custom.mainNoiseScaleY", new Object[0]), false, this, 1.0F, 5000.0F, this.field_175336_F.mainNoiseScaleY), new GuiPageButtonList.GuiSlideEntry(102, I18n.format("createWorld.customize.custom.mainNoiseScaleZ", new Object[0]), false, this, 1.0F, 5000.0F, this.field_175336_F.mainNoiseScaleZ), new GuiPageButtonList.GuiSlideEntry(103, I18n.format("createWorld.customize.custom.depthNoiseScaleX", new Object[0]), false, this, 1.0F, 2000.0F, this.field_175336_F.depthNoiseScaleX), new GuiPageButtonList.GuiSlideEntry(104, I18n.format("createWorld.customize.custom.depthNoiseScaleZ", new Object[0]), false, this, 1.0F, 2000.0F, this.field_175336_F.depthNoiseScaleZ), new GuiPageButtonList.GuiSlideEntry(105, I18n.format("createWorld.customize.custom.depthNoiseScaleExponent", new Object[0]), false, this, 0.01F, 20.0F, this.field_175336_F.depthNoiseScaleExponent), new GuiPageButtonList.GuiSlideEntry(106, I18n.format("createWorld.customize.custom.baseSize", new Object[0]), false, this, 1.0F, 25.0F, this.field_175336_F.baseSize), new GuiPageButtonList.GuiSlideEntry(107, I18n.format("createWorld.customize.custom.coordinateScale", new Object[0]), false, this, 1.0F, 6000.0F, this.field_175336_F.coordinateScale), new GuiPageButtonList.GuiSlideEntry(108, I18n.format("createWorld.customize.custom.heightScale", new Object[0]), false, this, 1.0F, 6000.0F, this.field_175336_F.heightScale), new GuiPageButtonList.GuiSlideEntry(109, I18n.format("createWorld.customize.custom.stretchY", new Object[0]), false, this, 0.01F, 50.0F, this.field_175336_F.stretchY), new GuiPageButtonList.GuiSlideEntry(110, I18n.format("createWorld.customize.custom.upperLimitScale", new Object[0]), false, this, 1.0F, 5000.0F, this.field_175336_F.upperLimitScale), new GuiPageButtonList.GuiSlideEntry(111, I18n.format("createWorld.customize.custom.lowerLimitScale", new Object[0]), false, this, 1.0F, 5000.0F, this.field_175336_F.lowerLimitScale), new GuiPageButtonList.GuiSlideEntry(112, I18n.format("createWorld.customize.custom.biomeDepthWeight", new Object[0]), false, this, 1.0F, 20.0F, this.field_175336_F.biomeDepthWeight), new GuiPageButtonList.GuiSlideEntry(113, I18n.format("createWorld.customize.custom.biomeDepthOffset", new Object[0]), false, this, 0.0F, 20.0F, this.field_175336_F.biomeDepthOffset), new GuiPageButtonList.GuiSlideEntry(114, I18n.format("createWorld.customize.custom.biomeScaleWeight", new Object[0]), false, this, 1.0F, 20.0F, this.field_175336_F.biomeScaleWeight), new GuiPageButtonList.GuiSlideEntry(115, I18n.format("createWorld.customize.custom.biomeScaleOffset", new Object[0]), false, this, 0.0F, 20.0F, this.field_175336_F.biomeScaleOffset) };
/* 107 */     GuiPageButtonList.GuiListEntry[] aguipagebuttonlist$guilistentry3 = { new GuiPageButtonList.GuiLabelEntry(400, I18n.format("createWorld.customize.custom.mainNoiseScaleX", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(132, String.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.mainNoiseScaleX) }), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(401, I18n.format("createWorld.customize.custom.mainNoiseScaleY", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(133, String.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.mainNoiseScaleY) }), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(402, I18n.format("createWorld.customize.custom.mainNoiseScaleZ", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(134, String.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.mainNoiseScaleZ) }), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(403, I18n.format("createWorld.customize.custom.depthNoiseScaleX", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(135, String.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.depthNoiseScaleX) }), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(404, I18n.format("createWorld.customize.custom.depthNoiseScaleZ", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(136, String.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.depthNoiseScaleZ) }), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(405, I18n.format("createWorld.customize.custom.depthNoiseScaleExponent", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(137, String.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.depthNoiseScaleExponent) }), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(406, I18n.format("createWorld.customize.custom.baseSize", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(138, String.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.baseSize) }), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(407, I18n.format("createWorld.customize.custom.coordinateScale", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(139, String.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.coordinateScale) }), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(408, I18n.format("createWorld.customize.custom.heightScale", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(140, String.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.heightScale) }), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(409, I18n.format("createWorld.customize.custom.stretchY", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(141, String.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.stretchY) }), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(410, I18n.format("createWorld.customize.custom.upperLimitScale", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(142, String.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.upperLimitScale) }), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(411, I18n.format("createWorld.customize.custom.lowerLimitScale", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(143, String.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.lowerLimitScale) }), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(412, I18n.format("createWorld.customize.custom.biomeDepthWeight", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(144, String.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.biomeDepthWeight) }), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(413, I18n.format("createWorld.customize.custom.biomeDepthOffset", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(145, String.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.biomeDepthOffset) }), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(414, I18n.format("createWorld.customize.custom.biomeScaleWeight", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(146, String.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.biomeScaleWeight) }), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(415, I18n.format("createWorld.customize.custom.biomeScaleOffset", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(147, String.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.biomeScaleOffset) }), false, this.field_175332_D) };
/* 108 */     this.field_175349_r = new GuiPageButtonList(this.mc, this.width, this.height, 32, this.height - 32, 25, this, new GuiPageButtonList.GuiListEntry[][] { aguipagebuttonlist$guilistentry, aguipagebuttonlist$guilistentry1, aguipagebuttonlist$guilistentry2, aguipagebuttonlist$guilistentry3 });
/*     */     
/* 110 */     for (int i = 0; i < 4; i++)
/*     */     {
/* 112 */       this.field_175342_h[i] = I18n.format("createWorld.customize.custom.page" + i, new Object[0]);
/*     */     }
/*     */     
/* 115 */     func_175328_i();
/*     */   }
/*     */ 
/*     */   
/*     */   public String func_175323_a() {
/* 120 */     return this.field_175336_F.toString().replace("\n", "");
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_175324_a(String p_175324_1_) {
/* 125 */     if (p_175324_1_ != null && p_175324_1_.length() != 0) {
/*     */       
/* 127 */       this.field_175336_F = ChunkProviderSettings.Factory.jsonToFactory(p_175324_1_);
/*     */     }
/*     */     else {
/*     */       
/* 131 */       this.field_175336_F = new ChunkProviderSettings.Factory();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_175319_a(int p_175319_1_, String p_175319_2_) {
/* 137 */     float f = 0.0F;
/*     */ 
/*     */     
/*     */     try {
/* 141 */       f = Float.parseFloat(p_175319_2_);
/*     */     }
/* 143 */     catch (NumberFormatException numberFormatException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 148 */     float f1 = 0.0F;
/*     */     
/* 150 */     switch (p_175319_1_) {
/*     */       
/*     */       case 132:
/* 153 */         f1 = this.field_175336_F.mainNoiseScaleX = MathHelper.clamp_float(f, 1.0F, 5000.0F);
/*     */         break;
/*     */       
/*     */       case 133:
/* 157 */         f1 = this.field_175336_F.mainNoiseScaleY = MathHelper.clamp_float(f, 1.0F, 5000.0F);
/*     */         break;
/*     */       
/*     */       case 134:
/* 161 */         f1 = this.field_175336_F.mainNoiseScaleZ = MathHelper.clamp_float(f, 1.0F, 5000.0F);
/*     */         break;
/*     */       
/*     */       case 135:
/* 165 */         f1 = this.field_175336_F.depthNoiseScaleX = MathHelper.clamp_float(f, 1.0F, 2000.0F);
/*     */         break;
/*     */       
/*     */       case 136:
/* 169 */         f1 = this.field_175336_F.depthNoiseScaleZ = MathHelper.clamp_float(f, 1.0F, 2000.0F);
/*     */         break;
/*     */       
/*     */       case 137:
/* 173 */         f1 = this.field_175336_F.depthNoiseScaleExponent = MathHelper.clamp_float(f, 0.01F, 20.0F);
/*     */         break;
/*     */       
/*     */       case 138:
/* 177 */         f1 = this.field_175336_F.baseSize = MathHelper.clamp_float(f, 1.0F, 25.0F);
/*     */         break;
/*     */       
/*     */       case 139:
/* 181 */         f1 = this.field_175336_F.coordinateScale = MathHelper.clamp_float(f, 1.0F, 6000.0F);
/*     */         break;
/*     */       
/*     */       case 140:
/* 185 */         f1 = this.field_175336_F.heightScale = MathHelper.clamp_float(f, 1.0F, 6000.0F);
/*     */         break;
/*     */       
/*     */       case 141:
/* 189 */         f1 = this.field_175336_F.stretchY = MathHelper.clamp_float(f, 0.01F, 50.0F);
/*     */         break;
/*     */       
/*     */       case 142:
/* 193 */         f1 = this.field_175336_F.upperLimitScale = MathHelper.clamp_float(f, 1.0F, 5000.0F);
/*     */         break;
/*     */       
/*     */       case 143:
/* 197 */         f1 = this.field_175336_F.lowerLimitScale = MathHelper.clamp_float(f, 1.0F, 5000.0F);
/*     */         break;
/*     */       
/*     */       case 144:
/* 201 */         f1 = this.field_175336_F.biomeDepthWeight = MathHelper.clamp_float(f, 1.0F, 20.0F);
/*     */         break;
/*     */       
/*     */       case 145:
/* 205 */         f1 = this.field_175336_F.biomeDepthOffset = MathHelper.clamp_float(f, 0.0F, 20.0F);
/*     */         break;
/*     */       
/*     */       case 146:
/* 209 */         f1 = this.field_175336_F.biomeScaleWeight = MathHelper.clamp_float(f, 1.0F, 20.0F);
/*     */         break;
/*     */       
/*     */       case 147:
/* 213 */         f1 = this.field_175336_F.biomeScaleOffset = MathHelper.clamp_float(f, 0.0F, 20.0F);
/*     */         break;
/*     */     } 
/* 216 */     if (f1 != f && f != 0.0F)
/*     */     {
/* 218 */       ((GuiTextField)this.field_175349_r.func_178061_c(p_175319_1_)).setText(func_175330_b(p_175319_1_, f1));
/*     */     }
/*     */     
/* 221 */     ((GuiSlider)this.field_175349_r.func_178061_c(p_175319_1_ - 132 + 100)).func_175218_a(f1, false);
/*     */     
/* 223 */     if (!this.field_175336_F.equals(this.field_175334_E))
/*     */     {
/* 225 */       func_181031_a(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_181031_a(boolean p_181031_1_) {
/* 231 */     this.field_175338_A = p_181031_1_;
/* 232 */     this.field_175346_u.enabled = p_181031_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getText(int id, String name, float value) {
/* 237 */     return name + ": " + func_175330_b(id, value);
/*     */   }
/*     */ 
/*     */   
/*     */   private String func_175330_b(int p_175330_1_, float p_175330_2_) {
/* 242 */     switch (p_175330_1_) {
/*     */       
/*     */       case 100:
/*     */       case 101:
/*     */       case 102:
/*     */       case 103:
/*     */       case 104:
/*     */       case 107:
/*     */       case 108:
/*     */       case 110:
/*     */       case 111:
/*     */       case 132:
/*     */       case 133:
/*     */       case 134:
/*     */       case 135:
/*     */       case 136:
/*     */       case 139:
/*     */       case 140:
/*     */       case 142:
/*     */       case 143:
/* 262 */         return String.format("%5.3f", new Object[] { Float.valueOf(p_175330_2_) });
/*     */       
/*     */       case 105:
/*     */       case 106:
/*     */       case 109:
/*     */       case 112:
/*     */       case 113:
/*     */       case 114:
/*     */       case 115:
/*     */       case 137:
/*     */       case 138:
/*     */       case 141:
/*     */       case 144:
/*     */       case 145:
/*     */       case 146:
/*     */       case 147:
/* 278 */         return String.format("%2.3f", new Object[] { Float.valueOf(p_175330_2_) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       default:
/* 311 */         return String.format("%d", new Object[] { Integer.valueOf((int)p_175330_2_) });
/*     */       case 162:
/*     */         break;
/* 314 */     }  if (p_175330_2_ < 0.0F)
/*     */     {
/* 316 */       return I18n.format("gui.all", new Object[0]);
/*     */     }
/* 318 */     if ((int)p_175330_2_ >= BiomeGenBase.hell.biomeID) {
/*     */       
/* 320 */       BiomeGenBase biomegenbase1 = BiomeGenBase.getBiomeGenArray()[(int)p_175330_2_ + 2];
/* 321 */       return (biomegenbase1 != null) ? biomegenbase1.biomeName : "?";
/*     */     } 
/*     */ 
/*     */     
/* 325 */     BiomeGenBase biomegenbase = BiomeGenBase.getBiomeGenArray()[(int)p_175330_2_];
/* 326 */     return (biomegenbase != null) ? biomegenbase.biomeName : "?";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_175321_a(int p_175321_1_, boolean p_175321_2_) {
/* 333 */     switch (p_175321_1_) {
/*     */       
/*     */       case 148:
/* 336 */         this.field_175336_F.useCaves = p_175321_2_;
/*     */         break;
/*     */       
/*     */       case 149:
/* 340 */         this.field_175336_F.useDungeons = p_175321_2_;
/*     */         break;
/*     */       
/*     */       case 150:
/* 344 */         this.field_175336_F.useStrongholds = p_175321_2_;
/*     */         break;
/*     */       
/*     */       case 151:
/* 348 */         this.field_175336_F.useVillages = p_175321_2_;
/*     */         break;
/*     */       
/*     */       case 152:
/* 352 */         this.field_175336_F.useMineShafts = p_175321_2_;
/*     */         break;
/*     */       
/*     */       case 153:
/* 356 */         this.field_175336_F.useTemples = p_175321_2_;
/*     */         break;
/*     */       
/*     */       case 154:
/* 360 */         this.field_175336_F.useRavines = p_175321_2_;
/*     */         break;
/*     */       
/*     */       case 155:
/* 364 */         this.field_175336_F.useWaterLakes = p_175321_2_;
/*     */         break;
/*     */       
/*     */       case 156:
/* 368 */         this.field_175336_F.useLavaLakes = p_175321_2_;
/*     */         break;
/*     */       
/*     */       case 161:
/* 372 */         this.field_175336_F.useLavaOceans = p_175321_2_;
/*     */         break;
/*     */       
/*     */       case 210:
/* 376 */         this.field_175336_F.useMonuments = p_175321_2_;
/*     */         break;
/*     */     } 
/* 379 */     if (!this.field_175336_F.equals(this.field_175334_E))
/*     */     {
/* 381 */       func_181031_a(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTick(int id, float value) {
/* 387 */     switch (id) {
/*     */       
/*     */       case 100:
/* 390 */         this.field_175336_F.mainNoiseScaleX = value;
/*     */         break;
/*     */       
/*     */       case 101:
/* 394 */         this.field_175336_F.mainNoiseScaleY = value;
/*     */         break;
/*     */       
/*     */       case 102:
/* 398 */         this.field_175336_F.mainNoiseScaleZ = value;
/*     */         break;
/*     */       
/*     */       case 103:
/* 402 */         this.field_175336_F.depthNoiseScaleX = value;
/*     */         break;
/*     */       
/*     */       case 104:
/* 406 */         this.field_175336_F.depthNoiseScaleZ = value;
/*     */         break;
/*     */       
/*     */       case 105:
/* 410 */         this.field_175336_F.depthNoiseScaleExponent = value;
/*     */         break;
/*     */       
/*     */       case 106:
/* 414 */         this.field_175336_F.baseSize = value;
/*     */         break;
/*     */       
/*     */       case 107:
/* 418 */         this.field_175336_F.coordinateScale = value;
/*     */         break;
/*     */       
/*     */       case 108:
/* 422 */         this.field_175336_F.heightScale = value;
/*     */         break;
/*     */       
/*     */       case 109:
/* 426 */         this.field_175336_F.stretchY = value;
/*     */         break;
/*     */       
/*     */       case 110:
/* 430 */         this.field_175336_F.upperLimitScale = value;
/*     */         break;
/*     */       
/*     */       case 111:
/* 434 */         this.field_175336_F.lowerLimitScale = value;
/*     */         break;
/*     */       
/*     */       case 112:
/* 438 */         this.field_175336_F.biomeDepthWeight = value;
/*     */         break;
/*     */       
/*     */       case 113:
/* 442 */         this.field_175336_F.biomeDepthOffset = value;
/*     */         break;
/*     */       
/*     */       case 114:
/* 446 */         this.field_175336_F.biomeScaleWeight = value;
/*     */         break;
/*     */       
/*     */       case 115:
/* 450 */         this.field_175336_F.biomeScaleOffset = value;
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 157:
/* 499 */         this.field_175336_F.dungeonChance = (int)value;
/*     */         break;
/*     */       
/*     */       case 158:
/* 503 */         this.field_175336_F.waterLakeChance = (int)value;
/*     */         break;
/*     */       
/*     */       case 159:
/* 507 */         this.field_175336_F.lavaLakeChance = (int)value;
/*     */         break;
/*     */       
/*     */       case 160:
/* 511 */         this.field_175336_F.seaLevel = (int)value;
/*     */         break;
/*     */       
/*     */       case 162:
/* 515 */         this.field_175336_F.fixedBiome = (int)value;
/*     */         break;
/*     */       
/*     */       case 163:
/* 519 */         this.field_175336_F.biomeSize = (int)value;
/*     */         break;
/*     */       
/*     */       case 164:
/* 523 */         this.field_175336_F.riverSize = (int)value;
/*     */         break;
/*     */       
/*     */       case 165:
/* 527 */         this.field_175336_F.dirtSize = (int)value;
/*     */         break;
/*     */       
/*     */       case 166:
/* 531 */         this.field_175336_F.dirtCount = (int)value;
/*     */         break;
/*     */       
/*     */       case 167:
/* 535 */         this.field_175336_F.dirtMinHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 168:
/* 539 */         this.field_175336_F.dirtMaxHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 169:
/* 543 */         this.field_175336_F.gravelSize = (int)value;
/*     */         break;
/*     */       
/*     */       case 170:
/* 547 */         this.field_175336_F.gravelCount = (int)value;
/*     */         break;
/*     */       
/*     */       case 171:
/* 551 */         this.field_175336_F.gravelMinHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 172:
/* 555 */         this.field_175336_F.gravelMaxHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 173:
/* 559 */         this.field_175336_F.graniteSize = (int)value;
/*     */         break;
/*     */       
/*     */       case 174:
/* 563 */         this.field_175336_F.graniteCount = (int)value;
/*     */         break;
/*     */       
/*     */       case 175:
/* 567 */         this.field_175336_F.graniteMinHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 176:
/* 571 */         this.field_175336_F.graniteMaxHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 177:
/* 575 */         this.field_175336_F.dioriteSize = (int)value;
/*     */         break;
/*     */       
/*     */       case 178:
/* 579 */         this.field_175336_F.dioriteCount = (int)value;
/*     */         break;
/*     */       
/*     */       case 179:
/* 583 */         this.field_175336_F.dioriteMinHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 180:
/* 587 */         this.field_175336_F.dioriteMaxHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 181:
/* 591 */         this.field_175336_F.andesiteSize = (int)value;
/*     */         break;
/*     */       
/*     */       case 182:
/* 595 */         this.field_175336_F.andesiteCount = (int)value;
/*     */         break;
/*     */       
/*     */       case 183:
/* 599 */         this.field_175336_F.andesiteMinHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 184:
/* 603 */         this.field_175336_F.andesiteMaxHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 185:
/* 607 */         this.field_175336_F.coalSize = (int)value;
/*     */         break;
/*     */       
/*     */       case 186:
/* 611 */         this.field_175336_F.coalCount = (int)value;
/*     */         break;
/*     */       
/*     */       case 187:
/* 615 */         this.field_175336_F.coalMinHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 189:
/* 619 */         this.field_175336_F.coalMaxHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 190:
/* 623 */         this.field_175336_F.ironSize = (int)value;
/*     */         break;
/*     */       
/*     */       case 191:
/* 627 */         this.field_175336_F.ironCount = (int)value;
/*     */         break;
/*     */       
/*     */       case 192:
/* 631 */         this.field_175336_F.ironMinHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 193:
/* 635 */         this.field_175336_F.ironMaxHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 194:
/* 639 */         this.field_175336_F.goldSize = (int)value;
/*     */         break;
/*     */       
/*     */       case 195:
/* 643 */         this.field_175336_F.goldCount = (int)value;
/*     */         break;
/*     */       
/*     */       case 196:
/* 647 */         this.field_175336_F.goldMinHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 197:
/* 651 */         this.field_175336_F.goldMaxHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 198:
/* 655 */         this.field_175336_F.redstoneSize = (int)value;
/*     */         break;
/*     */       
/*     */       case 199:
/* 659 */         this.field_175336_F.redstoneCount = (int)value;
/*     */         break;
/*     */       
/*     */       case 200:
/* 663 */         this.field_175336_F.redstoneMinHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 201:
/* 667 */         this.field_175336_F.redstoneMaxHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 202:
/* 671 */         this.field_175336_F.diamondSize = (int)value;
/*     */         break;
/*     */       
/*     */       case 203:
/* 675 */         this.field_175336_F.diamondCount = (int)value;
/*     */         break;
/*     */       
/*     */       case 204:
/* 679 */         this.field_175336_F.diamondMinHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 205:
/* 683 */         this.field_175336_F.diamondMaxHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 206:
/* 687 */         this.field_175336_F.lapisSize = (int)value;
/*     */         break;
/*     */       
/*     */       case 207:
/* 691 */         this.field_175336_F.lapisCount = (int)value;
/*     */         break;
/*     */       
/*     */       case 208:
/* 695 */         this.field_175336_F.lapisCenterHeight = (int)value;
/*     */         break;
/*     */       
/*     */       case 209:
/* 699 */         this.field_175336_F.lapisSpread = (int)value;
/*     */         break;
/*     */     } 
/* 702 */     if (id >= 100 && id < 116) {
/*     */       
/* 704 */       Gui gui = this.field_175349_r.func_178061_c(id - 100 + 132);
/*     */       
/* 706 */       if (gui != null)
/*     */       {
/* 708 */         ((GuiTextField)gui).setText(func_175330_b(id, value));
/*     */       }
/*     */     } 
/*     */     
/* 712 */     if (!this.field_175336_F.equals(this.field_175334_E))
/*     */     {
/* 714 */       func_181031_a(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/* 720 */     if (button.enabled) {
/*     */       int i;
/* 722 */       switch (button.id) {
/*     */         
/*     */         case 300:
/* 725 */           this.field_175343_i.chunkProviderSettingsJson = this.field_175336_F.toString();
/* 726 */           this.mc.displayGuiScreen(this.field_175343_i);
/*     */           break;
/*     */         
/*     */         case 301:
/* 730 */           for (i = 0; i < this.field_175349_r.getSize(); i++) {
/*     */             
/* 732 */             GuiPageButtonList.GuiEntry guipagebuttonlist$guientry = this.field_175349_r.getListEntry(i);
/* 733 */             Gui gui = guipagebuttonlist$guientry.func_178022_a();
/*     */             
/* 735 */             if (gui instanceof GuiButton) {
/*     */               
/* 737 */               GuiButton guibutton = (GuiButton)gui;
/*     */               
/* 739 */               if (guibutton instanceof GuiSlider) {
/*     */                 
/* 741 */                 float f = ((GuiSlider)guibutton).func_175217_d() * (0.75F + this.random.nextFloat() * 0.5F) + this.random.nextFloat() * 0.1F - 0.05F;
/* 742 */                 ((GuiSlider)guibutton).func_175219_a(MathHelper.clamp_float(f, 0.0F, 1.0F));
/*     */               }
/* 744 */               else if (guibutton instanceof GuiListButton) {
/*     */                 
/* 746 */                 ((GuiListButton)guibutton).func_175212_b(this.random.nextBoolean());
/*     */               } 
/*     */             } 
/*     */             
/* 750 */             Gui gui1 = guipagebuttonlist$guientry.func_178021_b();
/*     */             
/* 752 */             if (gui1 instanceof GuiButton) {
/*     */               
/* 754 */               GuiButton guibutton1 = (GuiButton)gui1;
/*     */               
/* 756 */               if (guibutton1 instanceof GuiSlider) {
/*     */                 
/* 758 */                 float f1 = ((GuiSlider)guibutton1).func_175217_d() * (0.75F + this.random.nextFloat() * 0.5F) + this.random.nextFloat() * 0.1F - 0.05F;
/* 759 */                 ((GuiSlider)guibutton1).func_175219_a(MathHelper.clamp_float(f1, 0.0F, 1.0F));
/*     */               }
/* 761 */               else if (guibutton1 instanceof GuiListButton) {
/*     */                 
/* 763 */                 ((GuiListButton)guibutton1).func_175212_b(this.random.nextBoolean());
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           return;
/*     */ 
/*     */         
/*     */         case 302:
/* 771 */           this.field_175349_r.func_178071_h();
/* 772 */           func_175328_i();
/*     */           break;
/*     */         
/*     */         case 303:
/* 776 */           this.field_175349_r.func_178064_i();
/* 777 */           func_175328_i();
/*     */           break;
/*     */         
/*     */         case 304:
/* 781 */           if (this.field_175338_A)
/*     */           {
/* 783 */             func_175322_b(304);
/*     */           }
/*     */           break;
/*     */ 
/*     */         
/*     */         case 305:
/* 789 */           this.mc.displayGuiScreen(new GuiScreenCustomizePresets(this));
/*     */           break;
/*     */         
/*     */         case 306:
/* 793 */           func_175331_h();
/*     */           break;
/*     */         
/*     */         case 307:
/* 797 */           this.field_175339_B = 0;
/* 798 */           func_175331_h();
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void func_175326_g() {
/* 805 */     this.field_175336_F.func_177863_a();
/* 806 */     func_175325_f();
/* 807 */     func_181031_a(false);
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_175322_b(int p_175322_1_) {
/* 812 */     this.field_175339_B = p_175322_1_;
/* 813 */     func_175329_a(true);
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_175331_h() throws IOException {
/* 818 */     switch (this.field_175339_B) {
/*     */       
/*     */       case 300:
/* 821 */         actionPerformed((GuiListButton)this.field_175349_r.func_178061_c(300));
/*     */         break;
/*     */       
/*     */       case 304:
/* 825 */         func_175326_g();
/*     */         break;
/*     */     } 
/* 828 */     this.field_175339_B = 0;
/* 829 */     this.field_175340_C = true;
/* 830 */     func_175329_a(false);
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_175329_a(boolean p_175329_1_) {
/* 835 */     this.field_175352_x.visible = p_175329_1_;
/* 836 */     this.field_175351_y.visible = p_175329_1_;
/* 837 */     this.field_175347_t.enabled = !p_175329_1_;
/* 838 */     this.field_175348_s.enabled = !p_175329_1_;
/* 839 */     this.field_175345_v.enabled = !p_175329_1_;
/* 840 */     this.field_175344_w.enabled = !p_175329_1_;
/* 841 */     this.field_175346_u.enabled = (this.field_175338_A && !p_175329_1_);
/* 842 */     this.field_175350_z.enabled = !p_175329_1_;
/* 843 */     this.field_175349_r.func_181155_a(!p_175329_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_175328_i() {
/* 848 */     this.field_175345_v.enabled = (this.field_175349_r.func_178059_e() != 0);
/* 849 */     this.field_175344_w.enabled = (this.field_175349_r.func_178059_e() != this.field_175349_r.func_178057_f() - 1);
/* 850 */     this.field_175333_f = I18n.format("book.pageIndicator", new Object[] { Integer.valueOf(this.field_175349_r.func_178059_e() + 1), Integer.valueOf(this.field_175349_r.func_178057_f()) });
/* 851 */     this.field_175335_g = this.field_175342_h[this.field_175349_r.func_178059_e()];
/* 852 */     this.field_175347_t.enabled = (this.field_175349_r.func_178059_e() != this.field_175349_r.func_178057_f() - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 857 */     super.keyTyped(typedChar, keyCode);
/*     */     
/* 859 */     if (this.field_175339_B == 0) {
/*     */       
/* 861 */       switch (keyCode) {
/*     */         
/*     */         case 200:
/* 864 */           func_175327_a(1.0F);
/*     */           return;
/*     */         
/*     */         case 208:
/* 868 */           func_175327_a(-1.0F);
/*     */           return;
/*     */       } 
/*     */       
/* 872 */       this.field_175349_r.func_178062_a(typedChar, keyCode);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void func_175327_a(float p_175327_1_) {
/* 879 */     Gui gui = this.field_175349_r.func_178056_g();
/*     */     
/* 881 */     if (gui instanceof GuiTextField) {
/*     */       
/* 883 */       float f = p_175327_1_;
/*     */       
/* 885 */       if (GuiScreen.isShiftKeyDown()) {
/*     */         
/* 887 */         f = p_175327_1_ * 0.1F;
/*     */         
/* 889 */         if (GuiScreen.isCtrlKeyDown())
/*     */         {
/* 891 */           f *= 0.1F;
/*     */         }
/*     */       }
/* 894 */       else if (GuiScreen.isCtrlKeyDown()) {
/*     */         
/* 896 */         f = p_175327_1_ * 10.0F;
/*     */         
/* 898 */         if (GuiScreen.isAltKeyDown())
/*     */         {
/* 900 */           f *= 10.0F;
/*     */         }
/*     */       } 
/*     */       
/* 904 */       GuiTextField guitextfield = (GuiTextField)gui;
/* 905 */       Float f1 = Floats.tryParse(guitextfield.getText());
/*     */       
/* 907 */       if (f1 != null) {
/*     */         
/* 909 */         f1 = Float.valueOf(f1.floatValue() + f);
/* 910 */         int i = guitextfield.getId();
/* 911 */         String s = func_175330_b(guitextfield.getId(), f1.floatValue());
/* 912 */         guitextfield.setText(s);
/* 913 */         func_175319_a(i, s);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 920 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     
/* 922 */     if (this.field_175339_B == 0 && !this.field_175340_C)
/*     */     {
/* 924 */       this.field_175349_r.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state) {
/* 930 */     super.mouseReleased(mouseX, mouseY, state);
/*     */     
/* 932 */     if (this.field_175340_C) {
/*     */       
/* 934 */       this.field_175340_C = false;
/*     */     }
/* 936 */     else if (this.field_175339_B == 0) {
/*     */       
/* 938 */       this.field_175349_r.mouseReleased(mouseX, mouseY, state);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 944 */     drawDefaultBackground();
/* 945 */     this.field_175349_r.drawScreen(mouseX, mouseY, partialTicks);
/* 946 */     drawCenteredString(this.fontRendererObj, this.field_175341_a, this.width / 2, 2, 16777215);
/* 947 */     drawCenteredString(this.fontRendererObj, this.field_175333_f, this.width / 2, 12, 16777215);
/* 948 */     drawCenteredString(this.fontRendererObj, this.field_175335_g, this.width / 2, 22, 16777215);
/* 949 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */     
/* 951 */     if (this.field_175339_B != 0) {
/*     */       
/* 953 */       drawRect(0.0D, 0.0D, this.width, this.height, -2147483648);
/* 954 */       this; drawHorizontalLine(this.width / 2 - 91, this.width / 2 + 90, 99, -2039584);
/* 955 */       this; drawHorizontalLine(this.width / 2 - 91, this.width / 2 + 90, 185, -6250336);
/* 956 */       this; drawVerticalLine(this.width / 2 - 91, 99, 185, -2039584);
/* 957 */       this; drawVerticalLine(this.width / 2 + 90, 99, 185, -6250336);
/* 958 */       float f = 85.0F;
/* 959 */       float f1 = 180.0F;
/* 960 */       GlStateManager.disableLighting();
/* 961 */       GlStateManager.disableFog();
/* 962 */       Tessellator tessellator = Tessellator.getInstance();
/* 963 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 964 */       this.mc.getTextureManager().bindTexture(optionsBackground);
/* 965 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 966 */       float f2 = 32.0F;
/* 967 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 968 */       worldrenderer.pos((this.width / 2 - 90), 185.0D, 0.0D).tex(0.0D, 2.65625D).color(64, 64, 64, 64).endVertex();
/* 969 */       worldrenderer.pos((this.width / 2 + 90), 185.0D, 0.0D).tex(5.625D, 2.65625D).color(64, 64, 64, 64).endVertex();
/* 970 */       worldrenderer.pos((this.width / 2 + 90), 100.0D, 0.0D).tex(5.625D, 0.0D).color(64, 64, 64, 64).endVertex();
/* 971 */       worldrenderer.pos((this.width / 2 - 90), 100.0D, 0.0D).tex(0.0D, 0.0D).color(64, 64, 64, 64).endVertex();
/* 972 */       tessellator.draw();
/* 973 */       drawCenteredString(this.fontRendererObj, I18n.format("createWorld.customize.custom.confirmTitle", new Object[0]), this.width / 2, 105, 16777215);
/* 974 */       drawCenteredString(this.fontRendererObj, I18n.format("createWorld.customize.custom.confirm1", new Object[0]), this.width / 2, 125, 16777215);
/* 975 */       drawCenteredString(this.fontRendererObj, I18n.format("createWorld.customize.custom.confirm2", new Object[0]), this.width / 2, 135, 16777215);
/* 976 */       this.field_175352_x.drawButton(this.mc, mouseX, mouseY);
/* 977 */       this.field_175351_y.drawButton(this.mc, mouseX, mouseY);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiCustomizeWorldScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
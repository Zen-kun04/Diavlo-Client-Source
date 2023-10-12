/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockTallGrass;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.gen.FlatGeneratorInfo;
/*     */ import net.minecraft.world.gen.FlatLayerInfo;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class GuiFlatPresets extends GuiScreen {
/*  26 */   private static final List<LayerItem> FLAT_WORLD_PRESETS = Lists.newArrayList();
/*     */   
/*     */   private final GuiCreateFlatWorld parentScreen;
/*     */   private String presetsTitle;
/*     */   private String presetsShare;
/*     */   private String field_146436_r;
/*     */   private ListSlot field_146435_s;
/*     */   private GuiButton field_146434_t;
/*     */   private GuiTextField field_146433_u;
/*     */   
/*     */   public GuiFlatPresets(GuiCreateFlatWorld p_i46318_1_) {
/*  37 */     this.parentScreen = p_i46318_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  42 */     this.buttonList.clear();
/*  43 */     Keyboard.enableRepeatEvents(true);
/*  44 */     this.presetsTitle = I18n.format("createWorld.customize.presets.title", new Object[0]);
/*  45 */     this.presetsShare = I18n.format("createWorld.customize.presets.share", new Object[0]);
/*  46 */     this.field_146436_r = I18n.format("createWorld.customize.presets.list", new Object[0]);
/*  47 */     this.field_146433_u = new GuiTextField(2, this.fontRendererObj, 50, 40, this.width - 100, 20);
/*  48 */     this.field_146435_s = new ListSlot();
/*  49 */     this.field_146433_u.setMaxStringLength(1230);
/*  50 */     this.field_146433_u.setText(this.parentScreen.func_146384_e());
/*  51 */     this.buttonList.add(this.field_146434_t = new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("createWorld.customize.presets.select", new Object[0])));
/*  52 */     this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
/*  53 */     func_146426_g();
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/*  58 */     super.handleMouseInput();
/*  59 */     this.field_146435_s.handleMouseInput();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/*  64 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/*  69 */     this.field_146433_u.mouseClicked(mouseX, mouseY, mouseButton);
/*  70 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/*  75 */     if (!this.field_146433_u.textboxKeyTyped(typedChar, keyCode))
/*     */     {
/*  77 */       super.keyTyped(typedChar, keyCode);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  83 */     if (button.id == 0 && func_146430_p()) {
/*     */       
/*  85 */       this.parentScreen.func_146383_a(this.field_146433_u.getText());
/*  86 */       this.mc.displayGuiScreen(this.parentScreen);
/*     */     }
/*  88 */     else if (button.id == 1) {
/*     */       
/*  90 */       this.mc.displayGuiScreen(this.parentScreen);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  96 */     drawDefaultBackground();
/*  97 */     this.field_146435_s.drawScreen(mouseX, mouseY, partialTicks);
/*  98 */     drawCenteredString(this.fontRendererObj, this.presetsTitle, this.width / 2, 8, 16777215);
/*  99 */     drawString(this.fontRendererObj, this.presetsShare, 50, 30, 10526880);
/* 100 */     drawString(this.fontRendererObj, this.field_146436_r, 50, 70, 10526880);
/* 101 */     this.field_146433_u.drawTextBox();
/* 102 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/* 107 */     this.field_146433_u.updateCursorCounter();
/* 108 */     super.updateScreen();
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_146426_g() {
/* 113 */     boolean flag = func_146430_p();
/* 114 */     this.field_146434_t.enabled = flag;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean func_146430_p() {
/* 119 */     return ((this.field_146435_s.field_148175_k > -1 && this.field_146435_s.field_148175_k < FLAT_WORLD_PRESETS.size()) || this.field_146433_u.getText().length() > 1);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void func_146425_a(String p_146425_0_, Item p_146425_1_, BiomeGenBase p_146425_2_, FlatLayerInfo... p_146425_3_) {
/* 124 */     func_175354_a(p_146425_0_, p_146425_1_, 0, p_146425_2_, (List<String>)null, p_146425_3_);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void func_146421_a(String p_146421_0_, Item p_146421_1_, BiomeGenBase p_146421_2_, List<String> p_146421_3_, FlatLayerInfo... p_146421_4_) {
/* 129 */     func_175354_a(p_146421_0_, p_146421_1_, 0, p_146421_2_, p_146421_3_, p_146421_4_);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void func_175354_a(String p_175354_0_, Item p_175354_1_, int p_175354_2_, BiomeGenBase p_175354_3_, List<String> p_175354_4_, FlatLayerInfo... p_175354_5_) {
/* 134 */     FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();
/*     */     
/* 136 */     for (int i = p_175354_5_.length - 1; i >= 0; i--)
/*     */     {
/* 138 */       flatgeneratorinfo.getFlatLayers().add(p_175354_5_[i]);
/*     */     }
/*     */     
/* 141 */     flatgeneratorinfo.setBiome(p_175354_3_.biomeID);
/* 142 */     flatgeneratorinfo.func_82645_d();
/*     */     
/* 144 */     if (p_175354_4_ != null)
/*     */     {
/* 146 */       for (String s : p_175354_4_)
/*     */       {
/* 148 */         flatgeneratorinfo.getWorldFeatures().put(s, Maps.newHashMap());
/*     */       }
/*     */     }
/*     */     
/* 152 */     FLAT_WORLD_PRESETS.add(new LayerItem(p_175354_1_, p_175354_2_, p_175354_0_, flatgeneratorinfo.toString()));
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 157 */     func_146421_a("Classic Flat", Item.getItemFromBlock((Block)Blocks.grass), BiomeGenBase.plains, Arrays.asList(new String[] { "village" }, ), new FlatLayerInfo[] { new FlatLayerInfo(1, (Block)Blocks.grass), new FlatLayerInfo(2, Blocks.dirt), new FlatLayerInfo(1, Blocks.bedrock) });
/* 158 */     func_146421_a("Tunnelers' Dream", Item.getItemFromBlock(Blocks.stone), BiomeGenBase.extremeHills, Arrays.asList(new String[] { "biome_1", "dungeon", "decoration", "stronghold", "mineshaft" }, ), new FlatLayerInfo[] { new FlatLayerInfo(1, (Block)Blocks.grass), new FlatLayerInfo(5, Blocks.dirt), new FlatLayerInfo(230, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock) });
/* 159 */     func_146421_a("Water World", Items.water_bucket, BiomeGenBase.deepOcean, Arrays.asList(new String[] { "biome_1", "oceanmonument" }, ), new FlatLayerInfo[] { new FlatLayerInfo(90, (Block)Blocks.water), new FlatLayerInfo(5, (Block)Blocks.sand), new FlatLayerInfo(5, Blocks.dirt), new FlatLayerInfo(5, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock) });
/* 160 */     func_175354_a("Overworld", Item.getItemFromBlock((Block)Blocks.tallgrass), BlockTallGrass.EnumType.GRASS.getMeta(), BiomeGenBase.plains, Arrays.asList(new String[] { "village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon", "lake", "lava_lake" }, ), new FlatLayerInfo[] { new FlatLayerInfo(1, (Block)Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(59, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock) });
/* 161 */     func_146421_a("Snowy Kingdom", Item.getItemFromBlock(Blocks.snow_layer), BiomeGenBase.icePlains, Arrays.asList(new String[] { "village", "biome_1" }, ), new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.snow_layer), new FlatLayerInfo(1, (Block)Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(59, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock) });
/* 162 */     func_146421_a("Bottomless Pit", Items.feather, BiomeGenBase.plains, Arrays.asList(new String[] { "village", "biome_1" }, ), new FlatLayerInfo[] { new FlatLayerInfo(1, (Block)Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(2, Blocks.cobblestone) });
/* 163 */     func_146421_a("Desert", Item.getItemFromBlock((Block)Blocks.sand), BiomeGenBase.desert, Arrays.asList(new String[] { "village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon" }, ), new FlatLayerInfo[] { new FlatLayerInfo(8, (Block)Blocks.sand), new FlatLayerInfo(52, Blocks.sandstone), new FlatLayerInfo(3, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock) });
/* 164 */     func_146425_a("Redstone Ready", Items.redstone, BiomeGenBase.desert, new FlatLayerInfo[] { new FlatLayerInfo(52, Blocks.sandstone), new FlatLayerInfo(3, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock) });
/*     */   }
/*     */ 
/*     */   
/*     */   static class LayerItem
/*     */   {
/*     */     public Item field_148234_a;
/*     */     public int field_179037_b;
/*     */     public String field_148232_b;
/*     */     public String field_148233_c;
/*     */     
/*     */     public LayerItem(Item p_i45518_1_, int p_i45518_2_, String p_i45518_3_, String p_i45518_4_) {
/* 176 */       this.field_148234_a = p_i45518_1_;
/* 177 */       this.field_179037_b = p_i45518_2_;
/* 178 */       this.field_148232_b = p_i45518_3_;
/* 179 */       this.field_148233_c = p_i45518_4_;
/*     */     }
/*     */   }
/*     */   
/*     */   class ListSlot
/*     */     extends GuiSlot {
/* 185 */     public int field_148175_k = -1;
/*     */ 
/*     */     
/*     */     public ListSlot() {
/* 189 */       super(GuiFlatPresets.this.mc, GuiFlatPresets.this.width, GuiFlatPresets.this.height, 80, GuiFlatPresets.this.height - 37, 24);
/*     */     }
/*     */ 
/*     */     
/*     */     private void func_178054_a(int p_178054_1_, int p_178054_2_, Item p_178054_3_, int p_178054_4_) {
/* 194 */       func_148173_e(p_178054_1_ + 1, p_178054_2_ + 1);
/* 195 */       GlStateManager.enableRescaleNormal();
/* 196 */       RenderHelper.enableGUIStandardItemLighting();
/* 197 */       GuiFlatPresets.this.itemRender.renderItemIntoGUI(new ItemStack(p_178054_3_, 1, p_178054_4_), p_178054_1_ + 2, p_178054_2_ + 2);
/* 198 */       RenderHelper.disableStandardItemLighting();
/* 199 */       GlStateManager.disableRescaleNormal();
/*     */     }
/*     */ 
/*     */     
/*     */     private void func_148173_e(int p_148173_1_, int p_148173_2_) {
/* 204 */       func_148171_c(p_148173_1_, p_148173_2_, 0, 0);
/*     */     }
/*     */ 
/*     */     
/*     */     private void func_148171_c(int p_148171_1_, int p_148171_2_, int p_148171_3_, int p_148171_4_) {
/* 209 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 210 */       this.mc.getTextureManager().bindTexture(Gui.statIcons);
/* 211 */       float f = 0.0078125F;
/* 212 */       float f1 = 0.0078125F;
/* 213 */       int i = 18;
/* 214 */       int j = 18;
/* 215 */       Tessellator tessellator = Tessellator.getInstance();
/* 216 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 217 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 218 */       worldrenderer.pos((p_148171_1_ + 0), (p_148171_2_ + 18), GuiFlatPresets.this.zLevel).tex(((p_148171_3_ + 0) * 0.0078125F), ((p_148171_4_ + 18) * 0.0078125F)).endVertex();
/* 219 */       worldrenderer.pos((p_148171_1_ + 18), (p_148171_2_ + 18), GuiFlatPresets.this.zLevel).tex(((p_148171_3_ + 18) * 0.0078125F), ((p_148171_4_ + 18) * 0.0078125F)).endVertex();
/* 220 */       worldrenderer.pos((p_148171_1_ + 18), (p_148171_2_ + 0), GuiFlatPresets.this.zLevel).tex(((p_148171_3_ + 18) * 0.0078125F), ((p_148171_4_ + 0) * 0.0078125F)).endVertex();
/* 221 */       worldrenderer.pos((p_148171_1_ + 0), (p_148171_2_ + 0), GuiFlatPresets.this.zLevel).tex(((p_148171_3_ + 0) * 0.0078125F), ((p_148171_4_ + 0) * 0.0078125F)).endVertex();
/* 222 */       tessellator.draw();
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getSize() {
/* 227 */       return GuiFlatPresets.FLAT_WORLD_PRESETS.size();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
/* 232 */       this.field_148175_k = slotIndex;
/* 233 */       GuiFlatPresets.this.func_146426_g();
/* 234 */       GuiFlatPresets.this.field_146433_u.setText((GuiFlatPresets.FLAT_WORLD_PRESETS.get(GuiFlatPresets.this.field_146435_s.field_148175_k)).field_148233_c);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isSelected(int slotIndex) {
/* 239 */       return (slotIndex == this.field_148175_k);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void drawBackground() {}
/*     */ 
/*     */     
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
/* 248 */       GuiFlatPresets.LayerItem guiflatpresets$layeritem = GuiFlatPresets.FLAT_WORLD_PRESETS.get(entryID);
/* 249 */       func_178054_a(p_180791_2_, p_180791_3_, guiflatpresets$layeritem.field_148234_a, guiflatpresets$layeritem.field_179037_b);
/* 250 */       GuiFlatPresets.this.fontRendererObj.drawString(guiflatpresets$layeritem.field_148232_b, (p_180791_2_ + 18 + 5), (p_180791_3_ + 6), 16777215);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiFlatPresets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
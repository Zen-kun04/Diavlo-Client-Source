/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
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
/*     */ import net.minecraft.world.gen.FlatGeneratorInfo;
/*     */ import net.minecraft.world.gen.FlatLayerInfo;
/*     */ 
/*     */ public class GuiCreateFlatWorld
/*     */   extends GuiScreen {
/*     */   private final GuiCreateWorld createWorldGui;
/*  22 */   private FlatGeneratorInfo theFlatGeneratorInfo = FlatGeneratorInfo.getDefaultFlatGenerator();
/*     */   
/*     */   private String flatWorldTitle;
/*     */   private String field_146394_i;
/*     */   private String field_146391_r;
/*     */   private Details createFlatWorldListSlotGui;
/*     */   private GuiButton field_146389_t;
/*     */   private GuiButton field_146388_u;
/*     */   private GuiButton field_146386_v;
/*     */   
/*     */   public GuiCreateFlatWorld(GuiCreateWorld createWorldGuiIn, String p_i1029_2_) {
/*  33 */     this.createWorldGui = createWorldGuiIn;
/*  34 */     func_146383_a(p_i1029_2_);
/*     */   }
/*     */ 
/*     */   
/*     */   public String func_146384_e() {
/*  39 */     return this.theFlatGeneratorInfo.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_146383_a(String p_146383_1_) {
/*  44 */     this.theFlatGeneratorInfo = FlatGeneratorInfo.createFlatGeneratorFromString(p_146383_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  49 */     this.buttonList.clear();
/*  50 */     this.flatWorldTitle = I18n.format("createWorld.customize.flat.title", new Object[0]);
/*  51 */     this.field_146394_i = I18n.format("createWorld.customize.flat.tile", new Object[0]);
/*  52 */     this.field_146391_r = I18n.format("createWorld.customize.flat.height", new Object[0]);
/*  53 */     this.createFlatWorldListSlotGui = new Details();
/*  54 */     this.buttonList.add(this.field_146389_t = new GuiButton(2, this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("createWorld.customize.flat.addLayer", new Object[0]) + " (NYI)"));
/*  55 */     this.buttonList.add(this.field_146388_u = new GuiButton(3, this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("createWorld.customize.flat.editLayer", new Object[0]) + " (NYI)"));
/*  56 */     this.buttonList.add(this.field_146386_v = new GuiButton(4, this.width / 2 - 155, this.height - 52, 150, 20, I18n.format("createWorld.customize.flat.removeLayer", new Object[0])));
/*  57 */     this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("gui.done", new Object[0])));
/*  58 */     this.buttonList.add(new GuiButton(5, this.width / 2 + 5, this.height - 52, 150, 20, I18n.format("createWorld.customize.presets", new Object[0])));
/*  59 */     this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
/*  60 */     this.field_146388_u.visible = false;
/*  61 */     this.theFlatGeneratorInfo.func_82645_d();
/*  62 */     func_146375_g();
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/*  67 */     super.handleMouseInput();
/*  68 */     this.createFlatWorldListSlotGui.handleMouseInput();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  73 */     int i = this.theFlatGeneratorInfo.getFlatLayers().size() - this.createFlatWorldListSlotGui.field_148228_k - 1;
/*     */     
/*  75 */     if (button.id == 1) {
/*     */       
/*  77 */       this.mc.displayGuiScreen(this.createWorldGui);
/*     */     }
/*  79 */     else if (button.id == 0) {
/*     */       
/*  81 */       this.createWorldGui.chunkProviderSettingsJson = func_146384_e();
/*  82 */       this.mc.displayGuiScreen(this.createWorldGui);
/*     */     }
/*  84 */     else if (button.id == 5) {
/*     */       
/*  86 */       this.mc.displayGuiScreen(new GuiFlatPresets(this));
/*     */     }
/*  88 */     else if (button.id == 4 && func_146382_i()) {
/*     */       
/*  90 */       this.theFlatGeneratorInfo.getFlatLayers().remove(i);
/*  91 */       this.createFlatWorldListSlotGui.field_148228_k = Math.min(this.createFlatWorldListSlotGui.field_148228_k, this.theFlatGeneratorInfo.getFlatLayers().size() - 1);
/*     */     } 
/*     */     
/*  94 */     this.theFlatGeneratorInfo.func_82645_d();
/*  95 */     func_146375_g();
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_146375_g() {
/* 100 */     boolean flag = func_146382_i();
/* 101 */     this.field_146386_v.enabled = flag;
/* 102 */     this.field_146388_u.enabled = flag;
/* 103 */     this.field_146388_u.enabled = false;
/* 104 */     this.field_146389_t.enabled = false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean func_146382_i() {
/* 109 */     return (this.createFlatWorldListSlotGui.field_148228_k > -1 && this.createFlatWorldListSlotGui.field_148228_k < this.theFlatGeneratorInfo.getFlatLayers().size());
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 114 */     drawDefaultBackground();
/* 115 */     this.createFlatWorldListSlotGui.drawScreen(mouseX, mouseY, partialTicks);
/* 116 */     drawCenteredString(this.fontRendererObj, this.flatWorldTitle, this.width / 2, 8, 16777215);
/* 117 */     int i = this.width / 2 - 92 - 16;
/* 118 */     drawString(this.fontRendererObj, this.field_146394_i, i, 32, 16777215);
/* 119 */     drawString(this.fontRendererObj, this.field_146391_r, i + 2 + 213 - this.fontRendererObj.getStringWidth(this.field_146391_r), 32, 16777215);
/* 120 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   class Details
/*     */     extends GuiSlot {
/* 125 */     public int field_148228_k = -1;
/*     */ 
/*     */     
/*     */     public Details() {
/* 129 */       super(GuiCreateFlatWorld.this.mc, GuiCreateFlatWorld.this.width, GuiCreateFlatWorld.this.height, 43, GuiCreateFlatWorld.this.height - 60, 24);
/*     */     }
/*     */ 
/*     */     
/*     */     private void func_148225_a(int p_148225_1_, int p_148225_2_, ItemStack p_148225_3_) {
/* 134 */       func_148226_e(p_148225_1_ + 1, p_148225_2_ + 1);
/* 135 */       GlStateManager.enableRescaleNormal();
/*     */       
/* 137 */       if (p_148225_3_ != null && p_148225_3_.getItem() != null) {
/*     */         
/* 139 */         RenderHelper.enableGUIStandardItemLighting();
/* 140 */         GuiCreateFlatWorld.this.itemRender.renderItemIntoGUI(p_148225_3_, p_148225_1_ + 2, p_148225_2_ + 2);
/* 141 */         RenderHelper.disableStandardItemLighting();
/*     */       } 
/*     */       
/* 144 */       GlStateManager.disableRescaleNormal();
/*     */     }
/*     */ 
/*     */     
/*     */     private void func_148226_e(int p_148226_1_, int p_148226_2_) {
/* 149 */       func_148224_c(p_148226_1_, p_148226_2_, 0, 0);
/*     */     }
/*     */ 
/*     */     
/*     */     private void func_148224_c(int p_148224_1_, int p_148224_2_, int p_148224_3_, int p_148224_4_) {
/* 154 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 155 */       this.mc.getTextureManager().bindTexture(Gui.statIcons);
/* 156 */       float f = 0.0078125F;
/* 157 */       float f1 = 0.0078125F;
/* 158 */       int i = 18;
/* 159 */       int j = 18;
/* 160 */       Tessellator tessellator = Tessellator.getInstance();
/* 161 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 162 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 163 */       worldrenderer.pos((p_148224_1_ + 0), (p_148224_2_ + 18), GuiCreateFlatWorld.this.zLevel).tex(((p_148224_3_ + 0) * 0.0078125F), ((p_148224_4_ + 18) * 0.0078125F)).endVertex();
/* 164 */       worldrenderer.pos((p_148224_1_ + 18), (p_148224_2_ + 18), GuiCreateFlatWorld.this.zLevel).tex(((p_148224_3_ + 18) * 0.0078125F), ((p_148224_4_ + 18) * 0.0078125F)).endVertex();
/* 165 */       worldrenderer.pos((p_148224_1_ + 18), (p_148224_2_ + 0), GuiCreateFlatWorld.this.zLevel).tex(((p_148224_3_ + 18) * 0.0078125F), ((p_148224_4_ + 0) * 0.0078125F)).endVertex();
/* 166 */       worldrenderer.pos((p_148224_1_ + 0), (p_148224_2_ + 0), GuiCreateFlatWorld.this.zLevel).tex(((p_148224_3_ + 0) * 0.0078125F), ((p_148224_4_ + 0) * 0.0078125F)).endVertex();
/* 167 */       tessellator.draw();
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getSize() {
/* 172 */       return GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
/* 177 */       this.field_148228_k = slotIndex;
/* 178 */       GuiCreateFlatWorld.this.func_146375_g();
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isSelected(int slotIndex) {
/* 183 */       return (slotIndex == this.field_148228_k);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void drawBackground() {}
/*     */ 
/*     */     
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
/*     */       String s1;
/* 192 */       FlatLayerInfo flatlayerinfo = GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().get(GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size() - entryID - 1);
/* 193 */       IBlockState iblockstate = flatlayerinfo.getLayerMaterial();
/* 194 */       Block block = iblockstate.getBlock();
/* 195 */       Item item = Item.getItemFromBlock(block);
/* 196 */       ItemStack itemstack = (block != Blocks.air && item != null) ? new ItemStack(item, 1, block.getMetaFromState(iblockstate)) : null;
/* 197 */       String s = (itemstack == null) ? "Air" : item.getItemStackDisplayName(itemstack);
/*     */       
/* 199 */       if (item == null) {
/*     */         
/* 201 */         if (block != Blocks.water && block != Blocks.flowing_water) {
/*     */           
/* 203 */           if (block == Blocks.lava || block == Blocks.flowing_lava)
/*     */           {
/* 205 */             item = Items.lava_bucket;
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 210 */           item = Items.water_bucket;
/*     */         } 
/*     */         
/* 213 */         if (item != null) {
/*     */           
/* 215 */           itemstack = new ItemStack(item, 1, block.getMetaFromState(iblockstate));
/* 216 */           s = block.getLocalizedName();
/*     */         } 
/*     */       } 
/*     */       
/* 220 */       func_148225_a(p_180791_2_, p_180791_3_, itemstack);
/* 221 */       GuiCreateFlatWorld.this.fontRendererObj.drawString(s, (p_180791_2_ + 18 + 5), (p_180791_3_ + 3), 16777215);
/*     */ 
/*     */       
/* 224 */       if (entryID == 0) {
/*     */         
/* 226 */         s1 = I18n.format("createWorld.customize.flat.layer.top", new Object[] { Integer.valueOf(flatlayerinfo.getLayerCount()) });
/*     */       }
/* 228 */       else if (entryID == GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size() - 1) {
/*     */         
/* 230 */         s1 = I18n.format("createWorld.customize.flat.layer.bottom", new Object[] { Integer.valueOf(flatlayerinfo.getLayerCount()) });
/*     */       }
/*     */       else {
/*     */         
/* 234 */         s1 = I18n.format("createWorld.customize.flat.layer", new Object[] { Integer.valueOf(flatlayerinfo.getLayerCount()) });
/*     */       } 
/*     */       
/* 237 */       GuiCreateFlatWorld.this.fontRendererObj.drawString(s1, (p_180791_2_ + 2 + 213 - GuiCreateFlatWorld.this.fontRendererObj.getStringWidth(s1)), (p_180791_3_ + 3), 16777215);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getScrollBarX() {
/* 242 */       return this.width - 70;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiCreateFlatWorld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
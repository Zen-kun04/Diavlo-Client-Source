/*     */ package net.minecraft.client.gui;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.client.model.ModelBook;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerEnchantment;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnchantmentNameParts;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.IWorldNameable;
/*     */ import net.minecraft.world.World;
/*     */ import org.lwjgl.util.glu.Project;
/*     */ 
/*     */ public class GuiEnchantment extends GuiContainer {
/*  27 */   private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/enchanting_table.png");
/*  28 */   private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE = new ResourceLocation("textures/entity/enchanting_table_book.png");
/*  29 */   private static final ModelBook MODEL_BOOK = new ModelBook();
/*     */   private final InventoryPlayer playerInventory;
/*  31 */   private Random random = new Random();
/*     */   
/*     */   private ContainerEnchantment container;
/*     */   public int field_147073_u;
/*     */   public float field_147071_v;
/*     */   public float field_147069_w;
/*     */   public float field_147082_x;
/*     */   public float field_147081_y;
/*     */   public float field_147080_z;
/*     */   public float field_147076_A;
/*     */   ItemStack field_147077_B;
/*     */   private final IWorldNameable field_175380_I;
/*     */   
/*     */   public GuiEnchantment(InventoryPlayer inventory, World worldIn, IWorldNameable p_i45502_3_) {
/*  45 */     super((Container)new ContainerEnchantment(inventory, worldIn));
/*  46 */     this.playerInventory = inventory;
/*  47 */     this.container = (ContainerEnchantment)this.inventorySlots;
/*  48 */     this.field_175380_I = p_i45502_3_;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
/*  53 */     this.fontRendererObj.drawString(this.field_175380_I.getDisplayName().getUnformattedText(), 12.0D, 5.0D, 4210752);
/*  54 */     this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0D, (this.ySize - 96 + 2), 4210752);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/*  59 */     super.updateScreen();
/*  60 */     func_147068_g();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/*  65 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*  66 */     int i = (this.width - this.xSize) / 2;
/*  67 */     int j = (this.height - this.ySize) / 2;
/*     */     
/*  69 */     for (int k = 0; k < 3; k++) {
/*     */       
/*  71 */       int l = mouseX - i + 60;
/*  72 */       int i1 = mouseY - j + 14 + 19 * k;
/*     */       
/*  74 */       if (l >= 0 && i1 >= 0 && l < 108 && i1 < 19 && this.container.enchantItem((EntityPlayer)this.mc.thePlayer, k))
/*     */       {
/*  76 */         this.mc.playerController.sendEnchantPacket(this.container.windowId, k);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
/*  83 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  84 */     this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
/*  85 */     int i = (this.width - this.xSize) / 2;
/*  86 */     int j = (this.height - this.ySize) / 2;
/*  87 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/*  88 */     GlStateManager.pushMatrix();
/*  89 */     GlStateManager.matrixMode(5889);
/*  90 */     GlStateManager.pushMatrix();
/*  91 */     GlStateManager.loadIdentity();
/*  92 */     ScaledResolution scaledresolution = new ScaledResolution(this.mc);
/*  93 */     GlStateManager.viewport((scaledresolution.getScaledWidth() - 320) / 2 * scaledresolution.getScaleFactor(), (scaledresolution.getScaledHeight() - 240) / 2 * scaledresolution.getScaleFactor(), 320 * scaledresolution.getScaleFactor(), 240 * scaledresolution.getScaleFactor());
/*  94 */     GlStateManager.translate(-0.34F, 0.23F, 0.0F);
/*  95 */     Project.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);
/*  96 */     float f = 1.0F;
/*  97 */     GlStateManager.matrixMode(5888);
/*  98 */     GlStateManager.loadIdentity();
/*  99 */     RenderHelper.enableStandardItemLighting();
/* 100 */     GlStateManager.translate(0.0F, 3.3F, -16.0F);
/* 101 */     GlStateManager.scale(f, f, f);
/* 102 */     float f1 = 5.0F;
/* 103 */     GlStateManager.scale(f1, f1, f1);
/* 104 */     GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
/* 105 */     this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_BOOK_TEXTURE);
/* 106 */     GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
/* 107 */     float f2 = this.field_147076_A + (this.field_147080_z - this.field_147076_A) * partialTicks;
/* 108 */     GlStateManager.translate((1.0F - f2) * 0.2F, (1.0F - f2) * 0.1F, (1.0F - f2) * 0.25F);
/* 109 */     GlStateManager.rotate(-(1.0F - f2) * 90.0F - 90.0F, 0.0F, 1.0F, 0.0F);
/* 110 */     GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
/* 111 */     float f3 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * partialTicks + 0.25F;
/* 112 */     float f4 = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * partialTicks + 0.75F;
/* 113 */     f3 = (f3 - MathHelper.truncateDoubleToInt(f3)) * 1.6F - 0.3F;
/* 114 */     f4 = (f4 - MathHelper.truncateDoubleToInt(f4)) * 1.6F - 0.3F;
/*     */     
/* 116 */     if (f3 < 0.0F)
/*     */     {
/* 118 */       f3 = 0.0F;
/*     */     }
/*     */     
/* 121 */     if (f4 < 0.0F)
/*     */     {
/* 123 */       f4 = 0.0F;
/*     */     }
/*     */     
/* 126 */     if (f3 > 1.0F)
/*     */     {
/* 128 */       f3 = 1.0F;
/*     */     }
/*     */     
/* 131 */     if (f4 > 1.0F)
/*     */     {
/* 133 */       f4 = 1.0F;
/*     */     }
/*     */     
/* 136 */     GlStateManager.enableRescaleNormal();
/* 137 */     MODEL_BOOK.render((Entity)null, 0.0F, f3, f4, f2, 0.0F, 0.0625F);
/* 138 */     GlStateManager.disableRescaleNormal();
/* 139 */     RenderHelper.disableStandardItemLighting();
/* 140 */     GlStateManager.matrixMode(5889);
/* 141 */     GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
/* 142 */     GlStateManager.popMatrix();
/* 143 */     GlStateManager.matrixMode(5888);
/* 144 */     GlStateManager.popMatrix();
/* 145 */     RenderHelper.disableStandardItemLighting();
/* 146 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 147 */     EnchantmentNameParts.getInstance().reseedRandomGenerator(this.container.xpSeed);
/* 148 */     int k = this.container.getLapisAmount();
/*     */     
/* 150 */     for (int l = 0; l < 3; l++) {
/*     */       
/* 152 */       int i1 = i + 60;
/* 153 */       int j1 = i1 + 20;
/* 154 */       int k1 = 86;
/* 155 */       String s = EnchantmentNameParts.getInstance().generateNewRandomName();
/* 156 */       this.zLevel = 0.0F;
/* 157 */       this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
/* 158 */       int l1 = this.container.enchantLevels[l];
/* 159 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       
/* 161 */       if (l1 == 0) {
/*     */         
/* 163 */         drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 185, 108, 19);
/*     */       }
/*     */       else {
/*     */         
/* 167 */         String s1 = "" + l1;
/* 168 */         FontRenderer fontrenderer = this.mc.standardGalacticFontRenderer;
/* 169 */         int i2 = 6839882;
/*     */         
/* 171 */         if ((k < l + 1 || this.mc.thePlayer.experienceLevel < l1) && !this.mc.thePlayer.capabilities.isCreativeMode) {
/*     */           
/* 173 */           drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 185, 108, 19);
/* 174 */           drawTexturedModalRect(i1 + 1, j + 15 + 19 * l, 16 * l, 239, 16, 16);
/* 175 */           fontrenderer.drawSplitString(s, j1, j + 16 + 19 * l, k1, (i2 & 0xFEFEFE) >> 1);
/* 176 */           i2 = 4226832;
/*     */         }
/*     */         else {
/*     */           
/* 180 */           int j2 = mouseX - i + 60;
/* 181 */           int k2 = mouseY - j + 14 + 19 * l;
/*     */           
/* 183 */           if (j2 >= 0 && k2 >= 0 && j2 < 108 && k2 < 19) {
/*     */             
/* 185 */             drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 204, 108, 19);
/* 186 */             i2 = 16777088;
/*     */           }
/*     */           else {
/*     */             
/* 190 */             drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 166, 108, 19);
/*     */           } 
/*     */           
/* 193 */           drawTexturedModalRect(i1 + 1, j + 15 + 19 * l, 16 * l, 223, 16, 16);
/* 194 */           fontrenderer.drawSplitString(s, j1, j + 16 + 19 * l, k1, i2);
/* 195 */           i2 = 8453920;
/*     */         } 
/*     */         
/* 198 */         fontrenderer = this.mc.fontRendererObj;
/* 199 */         fontrenderer.drawStringWithShadow(s1, (j1 + 86 - fontrenderer.getStringWidth(s1)), (j + 16 + 19 * l + 7), i2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 206 */     super.drawScreen(mouseX, mouseY, partialTicks);
/* 207 */     boolean flag = this.mc.thePlayer.capabilities.isCreativeMode;
/* 208 */     int i = this.container.getLapisAmount();
/*     */     
/* 210 */     for (int j = 0; j < 3; j++) {
/*     */       
/* 212 */       int k = this.container.enchantLevels[j];
/* 213 */       int l = this.container.enchantmentIds[j];
/* 214 */       int i1 = j + 1;
/*     */       
/* 216 */       if (isPointInRegion(60, 14 + 19 * j, 108, 17, mouseX, mouseY) && k > 0 && l >= 0) {
/*     */         
/* 218 */         List<String> list = Lists.newArrayList();
/*     */         
/* 220 */         if (l >= 0 && Enchantment.getEnchantmentById(l & 0xFF) != null) {
/*     */           
/* 222 */           String s = Enchantment.getEnchantmentById(l & 0xFF).getTranslatedName((l & 0xFF00) >> 8);
/* 223 */           list.add(EnumChatFormatting.WHITE.toString() + EnumChatFormatting.ITALIC.toString() + I18n.format("container.enchant.clue", new Object[] { s }));
/*     */         } 
/*     */         
/* 226 */         if (!flag) {
/*     */           
/* 228 */           if (l >= 0)
/*     */           {
/* 230 */             list.add("");
/*     */           }
/*     */           
/* 233 */           if (this.mc.thePlayer.experienceLevel < k) {
/*     */             
/* 235 */             list.add(EnumChatFormatting.RED.toString() + "Level Requirement: " + this.container.enchantLevels[j]);
/*     */           }
/*     */           else {
/*     */             
/* 239 */             String s1 = "";
/*     */             
/* 241 */             if (i1 == 1) {
/*     */               
/* 243 */               s1 = I18n.format("container.enchant.lapis.one", new Object[0]);
/*     */             }
/*     */             else {
/*     */               
/* 247 */               s1 = I18n.format("container.enchant.lapis.many", new Object[] { Integer.valueOf(i1) });
/*     */             } 
/*     */             
/* 250 */             if (i >= i1) {
/*     */               
/* 252 */               list.add(EnumChatFormatting.GRAY.toString() + "" + s1);
/*     */             }
/*     */             else {
/*     */               
/* 256 */               list.add(EnumChatFormatting.RED.toString() + "" + s1);
/*     */             } 
/*     */             
/* 259 */             if (i1 == 1) {
/*     */               
/* 261 */               s1 = I18n.format("container.enchant.level.one", new Object[0]);
/*     */             }
/*     */             else {
/*     */               
/* 265 */               s1 = I18n.format("container.enchant.level.many", new Object[] { Integer.valueOf(i1) });
/*     */             } 
/*     */             
/* 268 */             list.add(EnumChatFormatting.GRAY.toString() + "" + s1);
/*     */           } 
/*     */         } 
/*     */         
/* 272 */         drawHoveringText(list, mouseX, mouseY);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_147068_g() {
/* 280 */     ItemStack itemstack = this.inventorySlots.getSlot(0).getStack();
/*     */     
/* 282 */     if (!ItemStack.areItemStacksEqual(itemstack, this.field_147077_B)) {
/*     */       
/* 284 */       this.field_147077_B = itemstack;
/*     */ 
/*     */       
/*     */       do {
/* 288 */         this.field_147082_x += (this.random.nextInt(4) - this.random.nextInt(4));
/*     */       }
/* 290 */       while (this.field_147071_v <= this.field_147082_x + 1.0F && this.field_147071_v >= this.field_147082_x - 1.0F);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 297 */     this.field_147073_u++;
/* 298 */     this.field_147069_w = this.field_147071_v;
/* 299 */     this.field_147076_A = this.field_147080_z;
/* 300 */     boolean flag = false;
/*     */     
/* 302 */     for (int i = 0; i < 3; i++) {
/*     */       
/* 304 */       if (this.container.enchantLevels[i] != 0)
/*     */       {
/* 306 */         flag = true;
/*     */       }
/*     */     } 
/*     */     
/* 310 */     if (flag) {
/*     */       
/* 312 */       this.field_147080_z += 0.2F;
/*     */     }
/*     */     else {
/*     */       
/* 316 */       this.field_147080_z -= 0.2F;
/*     */     } 
/*     */     
/* 319 */     this.field_147080_z = MathHelper.clamp_float(this.field_147080_z, 0.0F, 1.0F);
/* 320 */     float f1 = (this.field_147082_x - this.field_147071_v) * 0.4F;
/* 321 */     float f = 0.2F;
/* 322 */     f1 = MathHelper.clamp_float(f1, -f, f);
/* 323 */     this.field_147081_y += (f1 - this.field_147081_y) * 0.9F;
/* 324 */     this.field_147071_v += this.field_147081_y;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
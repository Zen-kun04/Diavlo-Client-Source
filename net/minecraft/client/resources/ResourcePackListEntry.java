/*     */ package net.minecraft.client.resources;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.GuiListExtended;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiScreenResourcePacks;
/*     */ import net.minecraft.client.gui.GuiYesNo;
/*     */ import net.minecraft.client.gui.GuiYesNoCallback;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public abstract class ResourcePackListEntry implements GuiListExtended.IGuiListEntry {
/*  17 */   private static final ResourceLocation RESOURCE_PACKS_TEXTURE = new ResourceLocation("textures/gui/resource_packs.png");
/*  18 */   private static final IChatComponent field_183020_d = (IChatComponent)new ChatComponentTranslation("resourcePack.incompatible", new Object[0]);
/*  19 */   private static final IChatComponent field_183021_e = (IChatComponent)new ChatComponentTranslation("resourcePack.incompatible.old", new Object[0]);
/*  20 */   private static final IChatComponent field_183022_f = (IChatComponent)new ChatComponentTranslation("resourcePack.incompatible.new", new Object[0]);
/*     */   
/*     */   protected final Minecraft mc;
/*     */   protected final GuiScreenResourcePacks resourcePacksGUI;
/*     */   
/*     */   public ResourcePackListEntry(GuiScreenResourcePacks resourcePacksGUIIn) {
/*  26 */     this.resourcePacksGUI = resourcePacksGUIIn;
/*  27 */     this.mc = Minecraft.getMinecraft();
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
/*  32 */     int i = func_183019_a();
/*     */     
/*  34 */     if (i != 1) {
/*     */       
/*  36 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  37 */       Gui.drawRect((x - 1), (y - 1), (x + listWidth - 9), (y + slotHeight + 1), -8978432);
/*     */     } 
/*     */     
/*  40 */     func_148313_c();
/*  41 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  42 */     Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
/*  43 */     String s = func_148312_b();
/*  44 */     String s1 = func_148311_a();
/*     */     
/*  46 */     if ((this.mc.gameSettings.touchscreen || isSelected) && func_148310_d()) {
/*     */       
/*  48 */       this.mc.getTextureManager().bindTexture(RESOURCE_PACKS_TEXTURE);
/*  49 */       Gui.drawRect(x, y, (x + 32), (y + 32), -1601138544);
/*  50 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  51 */       int j = mouseX - x;
/*  52 */       int k = mouseY - y;
/*     */       
/*  54 */       if (i < 1) {
/*     */         
/*  56 */         s = field_183020_d.getFormattedText();
/*  57 */         s1 = field_183021_e.getFormattedText();
/*     */       }
/*  59 */       else if (i > 1) {
/*     */         
/*  61 */         s = field_183020_d.getFormattedText();
/*  62 */         s1 = field_183022_f.getFormattedText();
/*     */       } 
/*     */       
/*  65 */       if (func_148309_e()) {
/*     */         
/*  67 */         if (j < 32)
/*     */         {
/*  69 */           Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
/*     */         }
/*     */         else
/*     */         {
/*  73 */           Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/*  78 */         if (func_148308_f())
/*     */         {
/*  80 */           if (j < 16) {
/*     */             
/*  82 */             Gui.drawModalRectWithCustomSizedTexture(x, y, 32.0F, 32.0F, 32, 32, 256.0F, 256.0F);
/*     */           }
/*     */           else {
/*     */             
/*  86 */             Gui.drawModalRectWithCustomSizedTexture(x, y, 32.0F, 0.0F, 32, 32, 256.0F, 256.0F);
/*     */           } 
/*     */         }
/*     */         
/*  90 */         if (func_148314_g())
/*     */         {
/*  92 */           if (j < 32 && j > 16 && k < 16) {
/*     */             
/*  94 */             Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
/*     */           }
/*     */           else {
/*     */             
/*  98 */             Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
/*     */           } 
/*     */         }
/*     */         
/* 102 */         if (func_148307_h())
/*     */         {
/* 104 */           if (j < 32 && j > 16 && k > 16) {
/*     */             
/* 106 */             Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
/*     */           }
/*     */           else {
/*     */             
/* 110 */             Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 116 */     int i1 = this.mc.fontRendererObj.getStringWidth(s);
/*     */     
/* 118 */     if (i1 > 157)
/*     */     {
/* 120 */       s = this.mc.fontRendererObj.trimStringToWidth(s, 157 - this.mc.fontRendererObj.getStringWidth("...")) + "...";
/*     */     }
/*     */     
/* 123 */     this.mc.fontRendererObj.drawStringWithShadow(s, (x + 32 + 2), (y + 1), 16777215);
/* 124 */     List<String> list = this.mc.fontRendererObj.listFormattedStringToWidth(s1, 157);
/*     */     
/* 126 */     for (int l = 0; l < 2 && l < list.size(); l++)
/*     */     {
/* 128 */       this.mc.fontRendererObj.drawStringWithShadow(list.get(l), (x + 32 + 2), (y + 12 + 10 * l), 8421504);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract int func_183019_a();
/*     */   
/*     */   protected abstract String func_148311_a();
/*     */   
/*     */   protected abstract String func_148312_b();
/*     */   
/*     */   protected abstract void func_148313_c();
/*     */   
/*     */   protected boolean func_148310_d() {
/* 142 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean func_148309_e() {
/* 147 */     return !this.resourcePacksGUI.hasResourcePackEntry(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean func_148308_f() {
/* 152 */     return this.resourcePacksGUI.hasResourcePackEntry(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean func_148314_g() {
/* 157 */     List<ResourcePackListEntry> list = this.resourcePacksGUI.getListContaining(this);
/* 158 */     int i = list.indexOf(this);
/* 159 */     return (i > 0 && ((ResourcePackListEntry)list.get(i - 1)).func_148310_d());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean func_148307_h() {
/* 164 */     List<ResourcePackListEntry> list = this.resourcePacksGUI.getListContaining(this);
/* 165 */     int i = list.indexOf(this);
/* 166 */     return (i >= 0 && i < list.size() - 1 && ((ResourcePackListEntry)list.get(i + 1)).func_148310_d());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
/* 171 */     if (func_148310_d() && p_148278_5_ <= 32) {
/*     */       
/* 173 */       if (func_148309_e()) {
/*     */         
/* 175 */         this.resourcePacksGUI.markChanged();
/* 176 */         int j = func_183019_a();
/*     */         
/* 178 */         if (j != 1) {
/*     */           
/* 180 */           String s1 = I18n.format("resourcePack.incompatible.confirm.title", new Object[0]);
/* 181 */           String s = I18n.format("resourcePack.incompatible.confirm." + ((j > 1) ? "new" : "old"), new Object[0]);
/* 182 */           this.mc.displayGuiScreen((GuiScreen)new GuiYesNo(new GuiYesNoCallback()
/*     */                 {
/*     */                   public void confirmClicked(boolean result, int id)
/*     */                   {
/* 186 */                     List<ResourcePackListEntry> list2 = ResourcePackListEntry.this.resourcePacksGUI.getListContaining(ResourcePackListEntry.this);
/* 187 */                     ResourcePackListEntry.this.mc.displayGuiScreen((GuiScreen)ResourcePackListEntry.this.resourcePacksGUI);
/*     */                     
/* 189 */                     if (result)
/*     */                     {
/* 191 */                       list2.remove(ResourcePackListEntry.this);
/* 192 */                       ResourcePackListEntry.this.resourcePacksGUI.getSelectedResourcePacks().add(0, ResourcePackListEntry.this);
/*     */                     }
/*     */                   
/*     */                   }
/*     */                 }s1, s, 0));
/*     */         } else {
/*     */           
/* 199 */           this.resourcePacksGUI.getListContaining(this).remove(this);
/* 200 */           this.resourcePacksGUI.getSelectedResourcePacks().add(0, this);
/*     */         } 
/*     */         
/* 203 */         return true;
/*     */       } 
/*     */       
/* 206 */       if (p_148278_5_ < 16 && func_148308_f()) {
/*     */         
/* 208 */         this.resourcePacksGUI.getListContaining(this).remove(this);
/* 209 */         this.resourcePacksGUI.getAvailableResourcePacks().add(0, this);
/* 210 */         this.resourcePacksGUI.markChanged();
/* 211 */         return true;
/*     */       } 
/*     */       
/* 214 */       if (p_148278_5_ > 16 && p_148278_6_ < 16 && func_148314_g()) {
/*     */         
/* 216 */         List<ResourcePackListEntry> list1 = this.resourcePacksGUI.getListContaining(this);
/* 217 */         int k = list1.indexOf(this);
/* 218 */         list1.remove(this);
/* 219 */         list1.add(k - 1, this);
/* 220 */         this.resourcePacksGUI.markChanged();
/* 221 */         return true;
/*     */       } 
/*     */       
/* 224 */       if (p_148278_5_ > 16 && p_148278_6_ > 16 && func_148307_h()) {
/*     */         
/* 226 */         List<ResourcePackListEntry> list = this.resourcePacksGUI.getListContaining(this);
/* 227 */         int i = list.indexOf(this);
/* 228 */         list.remove(this);
/* 229 */         list.add(i + 1, this);
/* 230 */         this.resourcePacksGUI.markChanged();
/* 231 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 235 */     return false;
/*     */   }
/*     */   
/*     */   public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {}
/*     */   
/*     */   public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\resources\ResourcePackListEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
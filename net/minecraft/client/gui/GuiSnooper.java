/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ 
/*     */ public class GuiSnooper
/*     */   extends GuiScreen {
/*     */   private final GuiScreen field_146608_a;
/*     */   private final GameSettings game_settings_2;
/*  14 */   private final java.util.List<String> field_146604_g = Lists.newArrayList();
/*  15 */   private final java.util.List<String> field_146609_h = Lists.newArrayList();
/*     */   
/*     */   private String field_146610_i;
/*     */   private String[] field_146607_r;
/*     */   private List field_146606_s;
/*     */   private GuiButton field_146605_t;
/*     */   
/*     */   public GuiSnooper(GuiScreen p_i1061_1_, GameSettings p_i1061_2_) {
/*  23 */     this.field_146608_a = p_i1061_1_;
/*  24 */     this.game_settings_2 = p_i1061_2_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  29 */     this.field_146610_i = I18n.format("options.snooper.title", new Object[0]);
/*  30 */     String s = I18n.format("options.snooper.desc", new Object[0]);
/*  31 */     java.util.List<String> list = Lists.newArrayList();
/*     */     
/*  33 */     for (String s1 : this.fontRendererObj.listFormattedStringToWidth(s, this.width - 30))
/*     */     {
/*  35 */       list.add(s1);
/*     */     }
/*     */     
/*  38 */     this.field_146607_r = list.<String>toArray(new String[list.size()]);
/*  39 */     this.field_146604_g.clear();
/*  40 */     this.field_146609_h.clear();
/*  41 */     this.buttonList.add(this.field_146605_t = new GuiButton(1, this.width / 2 - 152, this.height - 30, 150, 20, this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED)));
/*  42 */     this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height - 30, 150, 20, I18n.format("gui.done", new Object[0])));
/*  43 */     boolean flag = (this.mc.getIntegratedServer() != null && this.mc.getIntegratedServer().getPlayerUsageSnooper() != null);
/*     */     
/*  45 */     for (Map.Entry<String, String> entry : (new TreeMap<>(this.mc.getPlayerUsageSnooper().getCurrentStats())).entrySet()) {
/*     */       
/*  47 */       this.field_146604_g.add((flag ? "C " : "") + (String)entry.getKey());
/*  48 */       this.field_146609_h.add(this.fontRendererObj.trimStringToWidth(entry.getValue(), this.width - 220));
/*     */     } 
/*     */     
/*  51 */     if (flag)
/*     */     {
/*  53 */       for (Map.Entry<String, String> entry1 : (new TreeMap<>(this.mc.getIntegratedServer().getPlayerUsageSnooper().getCurrentStats())).entrySet()) {
/*     */         
/*  55 */         this.field_146604_g.add("S " + (String)entry1.getKey());
/*  56 */         this.field_146609_h.add(this.fontRendererObj.trimStringToWidth(entry1.getValue(), this.width - 220));
/*     */       } 
/*     */     }
/*     */     
/*  60 */     this.field_146606_s = new List();
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/*  65 */     super.handleMouseInput();
/*  66 */     this.field_146606_s.handleMouseInput();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  71 */     if (button.enabled) {
/*     */       
/*  73 */       if (button.id == 2) {
/*     */         
/*  75 */         this.game_settings_2.saveOptions();
/*  76 */         this.game_settings_2.saveOptions();
/*  77 */         this.mc.displayGuiScreen(this.field_146608_a);
/*     */       } 
/*     */       
/*  80 */       if (button.id == 1) {
/*     */         
/*  82 */         this.game_settings_2.setOptionValue(GameSettings.Options.SNOOPER_ENABLED, 1);
/*  83 */         this.field_146605_t.displayString = this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  90 */     drawDefaultBackground();
/*  91 */     this.field_146606_s.drawScreen(mouseX, mouseY, partialTicks);
/*  92 */     drawCenteredString(this.fontRendererObj, this.field_146610_i, this.width / 2, 8, 16777215);
/*  93 */     int i = 22;
/*     */     
/*  95 */     for (String s : this.field_146607_r) {
/*     */       
/*  97 */       drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 8421504);
/*  98 */       i += this.fontRendererObj.FONT_HEIGHT;
/*     */     } 
/*     */     
/* 101 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   class List
/*     */     extends GuiSlot
/*     */   {
/*     */     public List() {
/* 108 */       super(GuiSnooper.this.mc, GuiSnooper.this.width, GuiSnooper.this.height, 80, GuiSnooper.this.height - 40, GuiSnooper.this.fontRendererObj.FONT_HEIGHT + 1);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getSize() {
/* 113 */       return GuiSnooper.this.field_146604_g.size();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {}
/*     */ 
/*     */     
/*     */     protected boolean isSelected(int slotIndex) {
/* 122 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void drawBackground() {}
/*     */ 
/*     */     
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
/* 131 */       GuiSnooper.this.fontRendererObj.drawString(GuiSnooper.this.field_146604_g.get(entryID), 10.0D, p_180791_3_, 16777215);
/* 132 */       GuiSnooper.this.fontRendererObj.drawString(GuiSnooper.this.field_146609_h.get(entryID), 230.0D, p_180791_3_, 16777215);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getScrollBarX() {
/* 137 */       return this.width - 10;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiSnooper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
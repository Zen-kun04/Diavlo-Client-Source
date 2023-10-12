/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import net.minecraft.client.AnvilConverterException;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ import net.minecraft.world.storage.ISaveFormat;
/*     */ import net.minecraft.world.storage.ISaveHandler;
/*     */ import net.minecraft.world.storage.SaveFormatComparator;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class GuiSelectWorld
/*     */   extends GuiScreen implements GuiYesNoCallback {
/*  23 */   private static final Logger logger = LogManager.getLogger();
/*  24 */   private final DateFormat field_146633_h = new SimpleDateFormat();
/*     */   protected GuiScreen parentScreen;
/*  26 */   protected String screenTitle = "Select world";
/*     */   private boolean field_146634_i;
/*     */   private int selectedIndex;
/*     */   private java.util.List<SaveFormatComparator> field_146639_s;
/*     */   private List availableWorlds;
/*     */   private String field_146637_u;
/*     */   private String field_146636_v;
/*  33 */   private String[] field_146635_w = new String[4];
/*     */   
/*     */   private boolean confirmingDelete;
/*     */   private GuiButton deleteButton;
/*     */   private GuiButton selectButton;
/*     */   private GuiButton renameButton;
/*     */   private GuiButton recreateButton;
/*     */   
/*     */   public GuiSelectWorld(GuiScreen parentScreenIn) {
/*  42 */     this.parentScreen = parentScreenIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  47 */     this.screenTitle = I18n.format("selectWorld.title", new Object[0]);
/*     */ 
/*     */     
/*     */     try {
/*  51 */       loadLevelList();
/*     */     }
/*  53 */     catch (AnvilConverterException anvilconverterexception) {
/*     */       
/*  55 */       logger.error("Couldn't load level list", (Throwable)anvilconverterexception);
/*  56 */       this.mc.displayGuiScreen(new GuiErrorScreen("Unable to load worlds", anvilconverterexception.getMessage()));
/*     */       
/*     */       return;
/*     */     } 
/*  60 */     this.field_146637_u = I18n.format("selectWorld.world", new Object[0]);
/*  61 */     this.field_146636_v = I18n.format("selectWorld.conversion", new Object[0]);
/*  62 */     this.field_146635_w[WorldSettings.GameType.SURVIVAL.getID()] = I18n.format("gameMode.survival", new Object[0]);
/*  63 */     this.field_146635_w[WorldSettings.GameType.CREATIVE.getID()] = I18n.format("gameMode.creative", new Object[0]);
/*  64 */     this.field_146635_w[WorldSettings.GameType.ADVENTURE.getID()] = I18n.format("gameMode.adventure", new Object[0]);
/*  65 */     this.field_146635_w[WorldSettings.GameType.SPECTATOR.getID()] = I18n.format("gameMode.spectator", new Object[0]);
/*  66 */     this.availableWorlds = new List(this.mc);
/*  67 */     this.availableWorlds.registerScrollButtons(4, 5);
/*  68 */     addWorldSelectionButtons();
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/*  73 */     super.handleMouseInput();
/*  74 */     this.availableWorlds.handleMouseInput();
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadLevelList() throws AnvilConverterException {
/*  79 */     ISaveFormat isaveformat = this.mc.getSaveLoader();
/*  80 */     this.field_146639_s = isaveformat.getSaveList();
/*  81 */     Collections.sort(this.field_146639_s);
/*  82 */     this.selectedIndex = -1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String func_146621_a(int p_146621_1_) {
/*  87 */     return ((SaveFormatComparator)this.field_146639_s.get(p_146621_1_)).getFileName();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String func_146614_d(int p_146614_1_) {
/*  92 */     String s = ((SaveFormatComparator)this.field_146639_s.get(p_146614_1_)).getDisplayName();
/*     */     
/*  94 */     if (StringUtils.isEmpty(s))
/*     */     {
/*  96 */       s = I18n.format("selectWorld.world", new Object[0]) + " " + (p_146614_1_ + 1);
/*     */     }
/*     */     
/*  99 */     return s;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addWorldSelectionButtons() {
/* 104 */     this.buttonList.add(this.selectButton = new GuiButton(1, this.width / 2 - 154, this.height - 52, 150, 20, I18n.format("selectWorld.select", new Object[0])));
/* 105 */     this.buttonList.add(new GuiButton(3, this.width / 2 + 4, this.height - 52, 150, 20, I18n.format("selectWorld.create", new Object[0])));
/* 106 */     this.buttonList.add(this.renameButton = new GuiButton(6, this.width / 2 - 154, this.height - 28, 72, 20, I18n.format("selectWorld.rename", new Object[0])));
/* 107 */     this.buttonList.add(this.deleteButton = new GuiButton(2, this.width / 2 - 76, this.height - 28, 72, 20, I18n.format("selectWorld.delete", new Object[0])));
/* 108 */     this.buttonList.add(this.recreateButton = new GuiButton(7, this.width / 2 + 4, this.height - 28, 72, 20, I18n.format("selectWorld.recreate", new Object[0])));
/* 109 */     this.buttonList.add(new GuiButton(0, this.width / 2 + 82, this.height - 28, 72, 20, I18n.format("gui.cancel", new Object[0])));
/* 110 */     this.selectButton.enabled = false;
/* 111 */     this.deleteButton.enabled = false;
/* 112 */     this.renameButton.enabled = false;
/* 113 */     this.recreateButton.enabled = false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/* 118 */     if (button.enabled)
/*     */     {
/* 120 */       if (button.id == 2) {
/*     */         
/* 122 */         String s = func_146614_d(this.selectedIndex);
/*     */         
/* 124 */         if (s != null)
/*     */         {
/* 126 */           this.confirmingDelete = true;
/* 127 */           GuiYesNo guiyesno = makeDeleteWorldYesNo(this, s, this.selectedIndex);
/* 128 */           this.mc.displayGuiScreen(guiyesno);
/*     */         }
/*     */       
/* 131 */       } else if (button.id == 1) {
/*     */         
/* 133 */         func_146615_e(this.selectedIndex);
/*     */       }
/* 135 */       else if (button.id == 3) {
/*     */         
/* 137 */         this.mc.displayGuiScreen(new GuiCreateWorld(this));
/*     */       }
/* 139 */       else if (button.id == 6) {
/*     */         
/* 141 */         this.mc.displayGuiScreen(new GuiRenameWorld(this, func_146621_a(this.selectedIndex)));
/*     */       }
/* 143 */       else if (button.id == 0) {
/*     */         
/* 145 */         this.mc.displayGuiScreen(this.parentScreen);
/*     */       }
/* 147 */       else if (button.id == 7) {
/*     */         
/* 149 */         GuiCreateWorld guicreateworld = new GuiCreateWorld(this);
/* 150 */         ISaveHandler isavehandler = this.mc.getSaveLoader().getSaveLoader(func_146621_a(this.selectedIndex), false);
/* 151 */         WorldInfo worldinfo = isavehandler.loadWorldInfo();
/* 152 */         isavehandler.flush();
/* 153 */         guicreateworld.recreateFromExistingWorld(worldinfo);
/* 154 */         this.mc.displayGuiScreen(guicreateworld);
/*     */       }
/*     */       else {
/*     */         
/* 158 */         this.availableWorlds.actionPerformed(button);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_146615_e(int p_146615_1_) {
/* 165 */     this.mc.displayGuiScreen((GuiScreen)null);
/*     */     
/* 167 */     if (!this.field_146634_i) {
/*     */       
/* 169 */       this.field_146634_i = true;
/* 170 */       String s = func_146621_a(p_146615_1_);
/*     */       
/* 172 */       if (s == null)
/*     */       {
/* 174 */         s = "World" + p_146615_1_;
/*     */       }
/*     */       
/* 177 */       String s1 = func_146614_d(p_146615_1_);
/*     */       
/* 179 */       if (s1 == null)
/*     */       {
/* 181 */         s1 = "World" + p_146615_1_;
/*     */       }
/*     */       
/* 184 */       if (this.mc.getSaveLoader().canLoadWorld(s))
/*     */       {
/* 186 */         this.mc.launchIntegratedServer(s, s1, (WorldSettings)null);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void confirmClicked(boolean result, int id) {
/* 193 */     if (this.confirmingDelete) {
/*     */       
/* 195 */       this.confirmingDelete = false;
/*     */       
/* 197 */       if (result) {
/*     */         
/* 199 */         ISaveFormat isaveformat = this.mc.getSaveLoader();
/* 200 */         isaveformat.flushCache();
/* 201 */         isaveformat.deleteWorldDirectory(func_146621_a(id));
/*     */ 
/*     */         
/*     */         try {
/* 205 */           loadLevelList();
/*     */         }
/* 207 */         catch (AnvilConverterException anvilconverterexception) {
/*     */           
/* 209 */           logger.error("Couldn't load level list", (Throwable)anvilconverterexception);
/*     */         } 
/*     */       } 
/*     */       
/* 213 */       this.mc.displayGuiScreen(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 219 */     this.availableWorlds.drawScreen(mouseX, mouseY, partialTicks);
/* 220 */     drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 20, 16777215);
/* 221 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   public static GuiYesNo makeDeleteWorldYesNo(GuiYesNoCallback selectWorld, String name, int id) {
/* 226 */     String s = I18n.format("selectWorld.deleteQuestion", new Object[0]);
/* 227 */     String s1 = "'" + name + "' " + I18n.format("selectWorld.deleteWarning", new Object[0]);
/* 228 */     String s2 = I18n.format("selectWorld.deleteButton", new Object[0]);
/* 229 */     String s3 = I18n.format("gui.cancel", new Object[0]);
/* 230 */     GuiYesNo guiyesno = new GuiYesNo(selectWorld, s, s1, s2, s3, id);
/* 231 */     return guiyesno;
/*     */   }
/*     */   
/*     */   class List
/*     */     extends GuiSlot
/*     */   {
/*     */     public List(Minecraft mcIn) {
/* 238 */       super(mcIn, GuiSelectWorld.this.width, GuiSelectWorld.this.height, 32, GuiSelectWorld.this.height - 64, 36);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getSize() {
/* 243 */       return GuiSelectWorld.this.field_146639_s.size();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
/* 248 */       GuiSelectWorld.this.selectedIndex = slotIndex;
/* 249 */       boolean flag = (GuiSelectWorld.this.selectedIndex >= 0 && GuiSelectWorld.this.selectedIndex < getSize());
/* 250 */       GuiSelectWorld.this.selectButton.enabled = flag;
/* 251 */       GuiSelectWorld.this.deleteButton.enabled = flag;
/* 252 */       GuiSelectWorld.this.renameButton.enabled = flag;
/* 253 */       GuiSelectWorld.this.recreateButton.enabled = flag;
/*     */       
/* 255 */       if (isDoubleClick && flag)
/*     */       {
/* 257 */         GuiSelectWorld.this.func_146615_e(slotIndex);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isSelected(int slotIndex) {
/* 263 */       return (slotIndex == GuiSelectWorld.this.selectedIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getContentHeight() {
/* 268 */       return GuiSelectWorld.this.field_146639_s.size() * 36;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void drawBackground() {
/* 273 */       GuiSelectWorld.this.drawDefaultBackground();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
/* 278 */       SaveFormatComparator saveformatcomparator = GuiSelectWorld.this.field_146639_s.get(entryID);
/* 279 */       String s = saveformatcomparator.getDisplayName();
/*     */       
/* 281 */       if (StringUtils.isEmpty(s))
/*     */       {
/* 283 */         s = GuiSelectWorld.this.field_146637_u + " " + (entryID + 1);
/*     */       }
/*     */       
/* 286 */       String s1 = saveformatcomparator.getFileName();
/* 287 */       s1 = s1 + " (" + GuiSelectWorld.this.field_146633_h.format(new Date(saveformatcomparator.getLastTimePlayed()));
/* 288 */       s1 = s1 + ")";
/* 289 */       String s2 = "";
/*     */       
/* 291 */       if (saveformatcomparator.requiresConversion()) {
/*     */         
/* 293 */         s2 = GuiSelectWorld.this.field_146636_v + " " + s2;
/*     */       }
/*     */       else {
/*     */         
/* 297 */         s2 = GuiSelectWorld.this.field_146635_w[saveformatcomparator.getEnumGameType().getID()];
/*     */         
/* 299 */         if (saveformatcomparator.isHardcoreModeEnabled())
/*     */         {
/* 301 */           s2 = EnumChatFormatting.DARK_RED + I18n.format("gameMode.hardcore", new Object[0]) + EnumChatFormatting.RESET;
/*     */         }
/*     */         
/* 304 */         if (saveformatcomparator.getCheatsEnabled())
/*     */         {
/* 306 */           s2 = s2 + ", " + I18n.format("selectWorld.cheats", new Object[0]);
/*     */         }
/*     */       } 
/*     */       
/* 310 */       GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, s, p_180791_2_ + 2, p_180791_3_ + 1, 16777215);
/* 311 */       GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, s1, p_180791_2_ + 2, p_180791_3_ + 12, 8421504);
/* 312 */       GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, s2, p_180791_2_ + 2, p_180791_3_ + 12 + 10, 8421504);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiSelectWorld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
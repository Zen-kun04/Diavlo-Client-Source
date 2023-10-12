/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.resources.ResourcePackListEntry;
/*     */ import net.minecraft.client.resources.ResourcePackListEntryDefault;
/*     */ import net.minecraft.client.resources.ResourcePackListEntryFound;
/*     */ import net.minecraft.client.resources.ResourcePackRepository;
/*     */ import net.minecraft.util.Util;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.Sys;
/*     */ 
/*     */ public class GuiScreenResourcePacks
/*     */   extends GuiScreen {
/*  21 */   private static final Logger logger = LogManager.getLogger();
/*     */   
/*     */   private final GuiScreen parentScreen;
/*     */   private List<ResourcePackListEntry> availableResourcePacks;
/*     */   private List<ResourcePackListEntry> selectedResourcePacks;
/*     */   private GuiResourcePackAvailable availableResourcePacksList;
/*     */   private GuiResourcePackSelected selectedResourcePacksList;
/*     */   private boolean changed = false;
/*     */   
/*     */   public GuiScreenResourcePacks(GuiScreen parentScreenIn) {
/*  31 */     this.parentScreen = parentScreenIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  36 */     this.buttonList.add(new GuiOptionButton(2, this.width / 2 - 154, this.height - 48, I18n.format("resourcePack.openFolder", new Object[0])));
/*  37 */     this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 4, this.height - 48, I18n.format("gui.done", new Object[0])));
/*     */     
/*  39 */     if (!this.changed) {
/*     */       
/*  41 */       this.availableResourcePacks = Lists.newArrayList();
/*  42 */       this.selectedResourcePacks = Lists.newArrayList();
/*  43 */       ResourcePackRepository resourcepackrepository = this.mc.getResourcePackRepository();
/*  44 */       resourcepackrepository.updateRepositoryEntriesAll();
/*  45 */       List<ResourcePackRepository.Entry> list = Lists.newArrayList(resourcepackrepository.getRepositoryEntriesAll());
/*  46 */       list.removeAll(resourcepackrepository.getRepositoryEntries());
/*     */       
/*  48 */       for (ResourcePackRepository.Entry resourcepackrepository$entry : list)
/*     */       {
/*  50 */         this.availableResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry));
/*     */       }
/*     */       
/*  53 */       for (ResourcePackRepository.Entry resourcepackrepository$entry1 : Lists.reverse(resourcepackrepository.getRepositoryEntries()))
/*     */       {
/*  55 */         this.selectedResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry1));
/*     */       }
/*     */       
/*  58 */       this.selectedResourcePacks.add(new ResourcePackListEntryDefault(this));
/*     */     } 
/*     */     
/*  61 */     this.availableResourcePacksList = new GuiResourcePackAvailable(this.mc, 200, this.height, this.availableResourcePacks);
/*  62 */     this.availableResourcePacksList.setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
/*  63 */     this.availableResourcePacksList.registerScrollButtons(7, 8);
/*  64 */     this.selectedResourcePacksList = new GuiResourcePackSelected(this.mc, 200, this.height, this.selectedResourcePacks);
/*  65 */     this.selectedResourcePacksList.setSlotXBoundsFromLeft(this.width / 2 + 4);
/*  66 */     this.selectedResourcePacksList.registerScrollButtons(7, 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/*  71 */     super.handleMouseInput();
/*  72 */     this.selectedResourcePacksList.handleMouseInput();
/*  73 */     this.availableResourcePacksList.handleMouseInput();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasResourcePackEntry(ResourcePackListEntry p_146961_1_) {
/*  78 */     return this.selectedResourcePacks.contains(p_146961_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ResourcePackListEntry> getListContaining(ResourcePackListEntry p_146962_1_) {
/*  83 */     return hasResourcePackEntry(p_146962_1_) ? this.selectedResourcePacks : this.availableResourcePacks;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ResourcePackListEntry> getAvailableResourcePacks() {
/*  88 */     return this.availableResourcePacks;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ResourcePackListEntry> getSelectedResourcePacks() {
/*  93 */     return this.selectedResourcePacks;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  98 */     if (button.enabled)
/*     */     {
/* 100 */       if (button.id == 2) {
/*     */         
/* 102 */         File file1 = this.mc.getResourcePackRepository().getDirResourcepacks();
/* 103 */         String s = file1.getAbsolutePath();
/*     */         
/* 105 */         if (Util.getOSType() == Util.EnumOS.OSX) {
/*     */ 
/*     */           
/*     */           try {
/* 109 */             logger.info(s);
/* 110 */             Runtime.getRuntime().exec(new String[] { "/usr/bin/open", s });
/*     */             
/*     */             return;
/* 113 */           } catch (IOException ioexception1) {
/*     */             
/* 115 */             logger.error("Couldn't open file", ioexception1);
/*     */           }
/*     */         
/* 118 */         } else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
/*     */           
/* 120 */           String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] { s });
/*     */ 
/*     */           
/*     */           try {
/* 124 */             Runtime.getRuntime().exec(s1);
/*     */             
/*     */             return;
/* 127 */           } catch (IOException ioexception) {
/*     */             
/* 129 */             logger.error("Couldn't open file", ioexception);
/*     */           } 
/*     */         } 
/*     */         
/* 133 */         boolean flag = false;
/*     */ 
/*     */         
/*     */         try {
/* 137 */           Class<?> oclass = Class.forName("java.awt.Desktop");
/* 138 */           Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
/* 139 */           oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { file1.toURI() });
/*     */         }
/* 141 */         catch (Throwable throwable) {
/*     */           
/* 143 */           logger.error("Couldn't open link", throwable);
/* 144 */           flag = true;
/*     */         } 
/*     */         
/* 147 */         if (flag)
/*     */         {
/* 149 */           logger.info("Opening via system class!");
/* 150 */           Sys.openURL("file://" + s);
/*     */         }
/*     */       
/* 153 */       } else if (button.id == 1) {
/*     */         
/* 155 */         if (this.changed) {
/*     */           
/* 157 */           List<ResourcePackRepository.Entry> list = Lists.newArrayList();
/*     */           
/* 159 */           for (ResourcePackListEntry resourcepacklistentry : this.selectedResourcePacks) {
/*     */             
/* 161 */             if (resourcepacklistentry instanceof ResourcePackListEntryFound)
/*     */             {
/* 163 */               list.add(((ResourcePackListEntryFound)resourcepacklistentry).func_148318_i());
/*     */             }
/*     */           } 
/*     */           
/* 167 */           Collections.reverse(list);
/* 168 */           this.mc.getResourcePackRepository().setRepositories(list);
/* 169 */           this.mc.gameSettings.resourcePacks.clear();
/* 170 */           this.mc.gameSettings.incompatibleResourcePacks.clear();
/*     */           
/* 172 */           for (ResourcePackRepository.Entry resourcepackrepository$entry : list) {
/*     */             
/* 174 */             this.mc.gameSettings.resourcePacks.add(resourcepackrepository$entry.getResourcePackName());
/*     */             
/* 176 */             if (resourcepackrepository$entry.func_183027_f() != 1)
/*     */             {
/* 178 */               this.mc.gameSettings.incompatibleResourcePacks.add(resourcepackrepository$entry.getResourcePackName());
/*     */             }
/*     */           } 
/*     */           
/* 182 */           this.mc.gameSettings.saveOptions();
/* 183 */           this.mc.refreshResources();
/*     */         } 
/*     */         
/* 186 */         this.mc.displayGuiScreen(this.parentScreen);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 193 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 194 */     this.availableResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
/* 195 */     this.selectedResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state) {
/* 200 */     super.mouseReleased(mouseX, mouseY, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 205 */     drawBackground(0);
/* 206 */     this.availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
/* 207 */     this.selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
/* 208 */     drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.title", new Object[0]), this.width / 2, 16, 16777215);
/* 209 */     drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.folderInfo", new Object[0]), this.width / 2 - 77, this.height - 26, 8421504);
/* 210 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   public void markChanged() {
/* 215 */     this.changed = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiScreenResourcePacks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
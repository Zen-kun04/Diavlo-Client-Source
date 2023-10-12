/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.awt.Color;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.ClipboardOwner;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.StringSelection;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.entity.RenderItem;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.event.ClickEvent;
/*     */ import net.minecraft.event.HoverEvent;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.JsonToNBT;
/*     */ import net.minecraft.nbt.NBTException;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.stats.Achievement;
/*     */ import net.minecraft.stats.StatBase;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public abstract class GuiScreen
/*     */   extends Gui implements GuiYesNoCallback {
/*  46 */   private static final Logger LOGGER = LogManager.getLogger();
/*  47 */   private static final Set<String> PROTOCOLS = Sets.newHashSet((Object[])new String[] { "http", "https" });
/*  48 */   private static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
/*     */   protected Minecraft mc;
/*     */   protected RenderItem itemRender;
/*     */   public int width;
/*     */   public int height;
/*  53 */   protected List<GuiButton> buttonList = Lists.newArrayList();
/*  54 */   protected List<GuiLabel> labelList = Lists.newArrayList();
/*     */   
/*     */   public boolean allowUserInput;
/*     */   protected FontRenderer fontRendererObj;
/*     */   private GuiButton selectedButton;
/*     */   private int eventButton;
/*     */   private long lastMouseEvent;
/*     */   private int touchValue;
/*     */   private URI clickedLinkURI;
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  65 */     for (int i = 0; i < this.buttonList.size(); i++)
/*     */     {
/*  67 */       ((GuiButton)this.buttonList.get(i)).drawButton(this.mc, mouseX, mouseY);
/*     */     }
/*     */     
/*  70 */     for (int j = 0; j < this.labelList.size(); j++)
/*     */     {
/*  72 */       ((GuiLabel)this.labelList.get(j)).drawLabel(this.mc, mouseX, mouseY);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/*  78 */     if (keyCode == 1) {
/*     */       
/*  80 */       this.mc.displayGuiScreen((GuiScreen)null);
/*     */       
/*  82 */       if (this.mc.currentScreen == null)
/*     */       {
/*  84 */         this.mc.setIngameFocus();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getClipboardString() {
/*     */     try {
/*  93 */       Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
/*     */       
/*  95 */       if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
/*     */       {
/*  97 */         return (String)transferable.getTransferData(DataFlavor.stringFlavor);
/*     */       }
/*     */     }
/* 100 */     catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setClipboardString(String copyText) {
/* 110 */     if (!StringUtils.isEmpty(copyText)) {
/*     */       
/*     */       try {
/*     */         
/* 114 */         StringSelection stringselection = new StringSelection(copyText);
/* 115 */         Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, (ClipboardOwner)null);
/*     */       }
/* 117 */       catch (Exception exception) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderToolTip(ItemStack stack, int x, int y) {
/* 126 */     List<String> list = stack.getTooltip((EntityPlayer)this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
/*     */     
/* 128 */     for (int i = 0; i < list.size(); i++) {
/*     */       
/* 130 */       if (i == 0) {
/*     */         
/* 132 */         list.set(i, (stack.getRarity()).rarityColor + (String)list.get(i));
/*     */       }
/*     */       else {
/*     */         
/* 136 */         list.set(i, EnumChatFormatting.GRAY + (String)list.get(i));
/*     */       } 
/*     */     } 
/*     */     
/* 140 */     drawHoveringText(list, x, y);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawCreativeTabHoveringText(String tabName, int mouseX, int mouseY) {
/* 145 */     drawHoveringText(Arrays.asList(new String[] { tabName }, ), mouseX, mouseY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawHoveringText(List<String> textLines, int x, int y) {
/* 150 */     if (!textLines.isEmpty()) {
/*     */       
/* 152 */       GlStateManager.disableRescaleNormal();
/* 153 */       RenderHelper.disableStandardItemLighting();
/* 154 */       GlStateManager.disableLighting();
/* 155 */       GlStateManager.disableDepth();
/* 156 */       int i = 0;
/*     */       
/* 158 */       for (String s : textLines) {
/*     */         
/* 160 */         int j = this.fontRendererObj.getStringWidth(s);
/*     */         
/* 162 */         if (j > i)
/*     */         {
/* 164 */           i = j;
/*     */         }
/*     */       } 
/*     */       
/* 168 */       int l1 = x + 12;
/* 169 */       int i2 = y - 12;
/* 170 */       int k = 8;
/*     */       
/* 172 */       if (textLines.size() > 1)
/*     */       {
/* 174 */         k += 2 + (textLines.size() - 1) * 10;
/*     */       }
/*     */       
/* 177 */       if (l1 + i > this.width)
/*     */       {
/* 179 */         l1 -= 28 + i;
/*     */       }
/*     */       
/* 182 */       if (i2 + k + 6 > this.height)
/*     */       {
/* 184 */         i2 = this.height - k - 6;
/*     */       }
/*     */       
/* 187 */       this.zLevel = 300.0F;
/* 188 */       this.itemRender.zLevel = 300.0F;
/* 189 */       int l = -267386864;
/* 190 */       drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
/* 191 */       drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
/* 192 */       drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
/* 193 */       drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
/* 194 */       drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
/* 195 */       int i1 = 1347420415;
/* 196 */       int j1 = (i1 & 0xFEFEFE) >> 1 | i1 & 0xFF000000;
/* 197 */       drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
/* 198 */       drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
/* 199 */       drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
/* 200 */       drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);
/*     */       
/* 202 */       for (int k1 = 0; k1 < textLines.size(); k1++) {
/*     */         
/* 204 */         String s1 = textLines.get(k1);
/* 205 */         this.fontRendererObj.drawStringWithShadow(s1, l1, i2, -1);
/*     */         
/* 207 */         if (k1 == 0)
/*     */         {
/* 209 */           i2 += 2;
/*     */         }
/*     */         
/* 212 */         i2 += 10;
/*     */       } 
/*     */       
/* 215 */       this.zLevel = 0.0F;
/* 216 */       this.itemRender.zLevel = 0.0F;
/* 217 */       GlStateManager.enableLighting();
/* 218 */       GlStateManager.enableDepth();
/* 219 */       RenderHelper.enableStandardItemLighting();
/* 220 */       GlStateManager.enableRescaleNormal();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleComponentHover(IChatComponent component, int x, int y) {
/* 226 */     if (component != null && component.getChatStyle().getChatHoverEvent() != null) {
/*     */       
/* 228 */       HoverEvent hoverevent = component.getChatStyle().getChatHoverEvent();
/*     */       
/* 230 */       if (hoverevent.getAction() == HoverEvent.Action.SHOW_ITEM) {
/*     */         
/* 232 */         ItemStack itemstack = null;
/*     */ 
/*     */         
/*     */         try {
/* 236 */           NBTTagCompound nBTTagCompound = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
/*     */           
/* 238 */           if (nBTTagCompound instanceof NBTTagCompound)
/*     */           {
/* 240 */             itemstack = ItemStack.loadItemStackFromNBT(nBTTagCompound);
/*     */           }
/*     */         }
/* 243 */         catch (NBTException nBTException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 248 */         if (itemstack != null)
/*     */         {
/* 250 */           renderToolTip(itemstack, x, y);
/*     */         }
/*     */         else
/*     */         {
/* 254 */           drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Item!", x, y);
/*     */         }
/*     */       
/* 257 */       } else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ENTITY) {
/*     */         
/* 259 */         if (this.mc.gameSettings.advancedItemTooltips) {
/*     */           
/*     */           try {
/*     */             
/* 263 */             NBTTagCompound nBTTagCompound = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
/*     */             
/* 265 */             if (nBTTagCompound instanceof NBTTagCompound)
/*     */             {
/* 267 */               List<String> list1 = Lists.newArrayList();
/* 268 */               NBTTagCompound nbttagcompound = nBTTagCompound;
/* 269 */               list1.add(nbttagcompound.getString("name"));
/*     */               
/* 271 */               if (nbttagcompound.hasKey("type", 8)) {
/*     */                 
/* 273 */                 String s = nbttagcompound.getString("type");
/* 274 */                 list1.add("Type: " + s + " (" + EntityList.getIDFromString(s) + ")");
/*     */               } 
/*     */               
/* 277 */               list1.add(nbttagcompound.getString("id"));
/* 278 */               drawHoveringText(list1, x, y);
/*     */             }
/*     */             else
/*     */             {
/* 282 */               drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", x, y);
/*     */             }
/*     */           
/* 285 */           } catch (NBTException var10) {
/*     */             
/* 287 */             drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", x, y);
/*     */           }
/*     */         
/*     */         }
/* 291 */       } else if (hoverevent.getAction() == HoverEvent.Action.SHOW_TEXT) {
/*     */         
/* 293 */         drawHoveringText(NEWLINE_SPLITTER.splitToList(hoverevent.getValue().getFormattedText()), x, y);
/*     */       }
/* 295 */       else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
/*     */         
/* 297 */         StatBase statbase = StatList.getOneShotStat(hoverevent.getValue().getUnformattedText());
/*     */         
/* 299 */         if (statbase != null) {
/*     */           
/* 301 */           IChatComponent ichatcomponent = statbase.getStatName();
/* 302 */           ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("stats.tooltip.type." + (statbase.isAchievement() ? "achievement" : "statistic"), new Object[0]);
/* 303 */           chatComponentTranslation.getChatStyle().setItalic(Boolean.valueOf(true));
/* 304 */           String s1 = (statbase instanceof Achievement) ? ((Achievement)statbase).getDescription() : null;
/* 305 */           List<String> list = Lists.newArrayList((Object[])new String[] { ichatcomponent.getFormattedText(), chatComponentTranslation.getFormattedText() });
/*     */           
/* 307 */           if (s1 != null)
/*     */           {
/* 309 */             list.addAll(this.fontRendererObj.listFormattedStringToWidth(s1, 150));
/*     */           }
/*     */           
/* 312 */           drawHoveringText(list, x, y);
/*     */         }
/*     */         else {
/*     */           
/* 316 */           drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid statistic/achievement!", x, y);
/*     */         } 
/*     */       } 
/*     */       
/* 320 */       GlStateManager.disableLighting();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setText(String newChatText, boolean shouldOverwrite) {}
/*     */ 
/*     */   
/*     */   protected boolean handleComponentClick(IChatComponent component) {
/* 330 */     if (component == null)
/*     */     {
/* 332 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 336 */     ClickEvent clickevent = component.getChatStyle().getChatClickEvent();
/*     */     
/* 338 */     if (isShiftKeyDown()) {
/*     */       
/* 340 */       if (component.getChatStyle().getInsertion() != null)
/*     */       {
/* 342 */         setText(component.getChatStyle().getInsertion(), false);
/*     */       }
/*     */     }
/* 345 */     else if (clickevent != null) {
/*     */       
/* 347 */       if (clickevent.getAction() == ClickEvent.Action.OPEN_URL) {
/*     */         
/* 349 */         if (!this.mc.gameSettings.chatLinks)
/*     */         {
/* 351 */           return false;
/*     */         }
/*     */ 
/*     */         
/*     */         try {
/* 356 */           URI uri = new URI(clickevent.getValue());
/* 357 */           String s = uri.getScheme();
/*     */           
/* 359 */           if (s == null)
/*     */           {
/* 361 */             throw new URISyntaxException(clickevent.getValue(), "Missing protocol");
/*     */           }
/*     */           
/* 364 */           if (!PROTOCOLS.contains(s.toLowerCase()))
/*     */           {
/* 366 */             throw new URISyntaxException(clickevent.getValue(), "Unsupported protocol: " + s.toLowerCase());
/*     */           }
/*     */           
/* 369 */           if (this.mc.gameSettings.chatLinksPrompt)
/*     */           {
/* 371 */             this.clickedLinkURI = uri;
/* 372 */             this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, clickevent.getValue(), 31102009, false));
/*     */           }
/*     */           else
/*     */           {
/* 376 */             openWebLink(uri);
/*     */           }
/*     */         
/* 379 */         } catch (URISyntaxException urisyntaxexception) {
/*     */           
/* 381 */           LOGGER.error("Can't open url for " + clickevent, urisyntaxexception);
/*     */         }
/*     */       
/* 384 */       } else if (clickevent.getAction() == ClickEvent.Action.OPEN_FILE) {
/*     */         
/* 386 */         URI uri1 = (new File(clickevent.getValue())).toURI();
/* 387 */         openWebLink(uri1);
/*     */       }
/* 389 */       else if (clickevent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
/*     */         
/* 391 */         setText(clickevent.getValue(), true);
/*     */       }
/* 393 */       else if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
/*     */         
/* 395 */         sendChatMessage(clickevent.getValue(), false);
/*     */       }
/*     */       else {
/*     */         
/* 399 */         LOGGER.error("Don't know how to handle " + clickevent);
/*     */       } 
/*     */       
/* 402 */       return true;
/*     */     } 
/*     */     
/* 405 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendChatMessage(String msg) {
/* 411 */     sendChatMessage(msg, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendChatMessage(String msg, boolean addToChat) {
/* 416 */     if (addToChat)
/*     */     {
/* 418 */       this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
/*     */     }
/*     */     
/* 421 */     this.mc.thePlayer.sendChatMessage(msg);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 426 */     if (mouseButton == 0)
/*     */     {
/* 428 */       for (int i = 0; i < this.buttonList.size(); i++) {
/*     */         
/* 430 */         GuiButton guibutton = this.buttonList.get(i);
/*     */         
/* 432 */         if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
/*     */           
/* 434 */           this.selectedButton = guibutton;
/* 435 */           guibutton.playPressSound(this.mc.getSoundHandler());
/* 436 */           actionPerformed(guibutton);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state) {
/* 444 */     if (this.selectedButton != null && state == 0) {
/*     */       
/* 446 */       this.selectedButton.mouseReleased(mouseX, mouseY);
/* 447 */       this.selectedButton = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {}
/*     */ 
/*     */   
/*     */   public void setWorldAndResolution(Minecraft mc, int width, int height) {
/* 461 */     this.mc = mc;
/* 462 */     this.itemRender = mc.getRenderItem();
/* 463 */     this.fontRendererObj = mc.fontRendererObj;
/* 464 */     this.width = width;
/* 465 */     this.height = height;
/* 466 */     this.buttonList.clear();
/* 467 */     initGui();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGuiSize(int w, int h) {
/* 472 */     this.width = w;
/* 473 */     this.height = h;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void initGui() {}
/*     */ 
/*     */   
/*     */   public void handleInput() throws IOException {
/* 482 */     if (Mouse.isCreated())
/*     */     {
/* 484 */       while (Mouse.next())
/*     */       {
/* 486 */         handleMouseInput();
/*     */       }
/*     */     }
/*     */     
/* 490 */     if (Keyboard.isCreated())
/*     */     {
/* 492 */       while (Keyboard.next())
/*     */       {
/* 494 */         handleKeyboardInput();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/* 501 */     int i = Mouse.getEventX() * this.width / this.mc.displayWidth;
/* 502 */     int j = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
/* 503 */     int k = Mouse.getEventButton();
/*     */     
/* 505 */     if (Mouse.getEventButtonState()) {
/*     */       
/* 507 */       if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 512 */       this.eventButton = k;
/* 513 */       this.lastMouseEvent = Minecraft.getSystemTime();
/* 514 */       mouseClicked(i, j, this.eventButton);
/*     */     }
/* 516 */     else if (k != -1) {
/*     */       
/* 518 */       if (this.mc.gameSettings.touchscreen && --this.touchValue > 0) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 523 */       this.eventButton = -1;
/* 524 */       mouseReleased(i, j, k);
/*     */     }
/* 526 */     else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
/*     */       
/* 528 */       long l = Minecraft.getSystemTime() - this.lastMouseEvent;
/* 529 */       mouseClickMove(i, j, this.eventButton, l);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleKeyboardInput() throws IOException {
/* 535 */     if (Keyboard.getEventKeyState())
/*     */     {
/* 537 */       keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
/*     */     }
/*     */     
/* 540 */     this.mc.dispatchKeypresses();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateScreen() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {}
/*     */ 
/*     */   
/*     */   public void drawDefaultBackground() {
/* 553 */     drawWorldBackground(0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawWorldBackground(int tint) {
/* 559 */     if (this.mc.theWorld != null) {
/*     */       
/* 561 */       drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
/*     */     }
/*     */     else {
/*     */       
/* 565 */       drawBackground(tint);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean animazione = true;
/*     */   
/*     */   public void drawBackground(int tint) {
/* 573 */     if (!animazione) {
/* 574 */       ScaledResolution sr = new ScaledResolution(this.mc);
/* 575 */       Gui.drawRect(0.0D, 0.0D, sr.getScaledWidth(), sr.getScaledHeight(), (new Color(39, 37, 37)).getRGB());
/*     */       
/*     */       return;
/*     */     } 
/* 579 */     ResourceLocation background = new ResourceLocation("client/fondo_diavlo.jpg");
/* 580 */     this.mc.getTextureManager().bindTexture(background);
/*     */     
/* 582 */     this; drawModalRectWithCustomSizedTexture(0, 0, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doesGuiPauseGame() {
/* 587 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void confirmClicked(boolean result, int id) {
/* 592 */     if (id == 31102009) {
/*     */       
/* 594 */       if (result)
/*     */       {
/* 596 */         openWebLink(this.clickedLinkURI);
/*     */       }
/*     */       
/* 599 */       this.clickedLinkURI = null;
/* 600 */       this.mc.displayGuiScreen(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void openWebLink(URI url) {
/*     */     try {
/* 608 */       Class<?> oclass = Class.forName("java.awt.Desktop");
/* 609 */       Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
/* 610 */       oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { url });
/*     */     }
/* 612 */     catch (Throwable throwable) {
/*     */       
/* 614 */       LOGGER.error("Couldn't open link", throwable);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isCtrlKeyDown() {
/* 620 */     return Minecraft.isRunningOnMac ? ((Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220))) : ((Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isShiftKeyDown() {
/* 625 */     return (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isAltKeyDown() {
/* 630 */     return (Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isKeyComboCtrlX(int keyID) {
/* 635 */     return (keyID == 45 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isKeyComboCtrlV(int keyID) {
/* 640 */     return (keyID == 47 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isKeyComboCtrlC(int keyID) {
/* 645 */     return (keyID == 46 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isKeyComboCtrlA(int keyID) {
/* 650 */     return (keyID == 30 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onResize(Minecraft mcIn, int w, int h) {
/* 655 */     setWorldAndResolution(mcIn, w, h);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
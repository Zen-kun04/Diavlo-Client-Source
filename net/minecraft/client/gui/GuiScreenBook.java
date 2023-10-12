/*     */ package net.minecraft.client.gui;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonParseException;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.event.ClickEvent;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemEditableBook;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.nbt.NBTTagString;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.client.C17PacketCustomPayload;
/*     */ import net.minecraft.util.ChatAllowedCharacters;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class GuiScreenBook extends GuiScreen {
/*  32 */   private static final Logger logger = LogManager.getLogger();
/*  33 */   private static final ResourceLocation bookGuiTextures = new ResourceLocation("textures/gui/book.png");
/*     */   private final EntityPlayer editingPlayer;
/*     */   private final ItemStack bookObj;
/*     */   private final boolean bookIsUnsigned;
/*     */   private boolean bookIsModified;
/*     */   private boolean bookGettingSigned;
/*     */   private int updateCount;
/*  40 */   private int bookImageWidth = 192;
/*  41 */   private int bookImageHeight = 192;
/*  42 */   private int bookTotalPages = 1;
/*     */   private int currPage;
/*     */   private NBTTagList bookPages;
/*  45 */   private String bookTitle = "";
/*     */   private List<IChatComponent> field_175386_A;
/*  47 */   private int field_175387_B = -1;
/*     */   
/*     */   private NextPageButton buttonNextPage;
/*     */   private NextPageButton buttonPreviousPage;
/*     */   private GuiButton buttonDone;
/*     */   private GuiButton buttonSign;
/*     */   private GuiButton buttonFinalize;
/*     */   private GuiButton buttonCancel;
/*     */   
/*     */   public GuiScreenBook(EntityPlayer player, ItemStack book, boolean isUnsigned) {
/*  57 */     this.editingPlayer = player;
/*  58 */     this.bookObj = book;
/*  59 */     this.bookIsUnsigned = isUnsigned;
/*     */     
/*  61 */     if (book.hasTagCompound()) {
/*     */       
/*  63 */       NBTTagCompound nbttagcompound = book.getTagCompound();
/*  64 */       this.bookPages = nbttagcompound.getTagList("pages", 8);
/*     */       
/*  66 */       if (this.bookPages != null) {
/*     */         
/*  68 */         this.bookPages = (NBTTagList)this.bookPages.copy();
/*  69 */         this.bookTotalPages = this.bookPages.tagCount();
/*     */         
/*  71 */         if (this.bookTotalPages < 1)
/*     */         {
/*  73 */           this.bookTotalPages = 1;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  78 */     if (this.bookPages == null && isUnsigned) {
/*     */       
/*  80 */       this.bookPages = new NBTTagList();
/*  81 */       this.bookPages.appendTag((NBTBase)new NBTTagString(""));
/*  82 */       this.bookTotalPages = 1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/*  88 */     super.updateScreen();
/*  89 */     this.updateCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  94 */     this.buttonList.clear();
/*  95 */     Keyboard.enableRepeatEvents(true);
/*     */     
/*  97 */     if (this.bookIsUnsigned) {
/*     */       
/*  99 */       this.buttonList.add(this.buttonSign = new GuiButton(3, this.width / 2 - 100, 4 + this.bookImageHeight, 98, 20, I18n.format("book.signButton", new Object[0])));
/* 100 */       this.buttonList.add(this.buttonDone = new GuiButton(0, this.width / 2 + 2, 4 + this.bookImageHeight, 98, 20, I18n.format("gui.done", new Object[0])));
/* 101 */       this.buttonList.add(this.buttonFinalize = new GuiButton(5, this.width / 2 - 100, 4 + this.bookImageHeight, 98, 20, I18n.format("book.finalizeButton", new Object[0])));
/* 102 */       this.buttonList.add(this.buttonCancel = new GuiButton(4, this.width / 2 + 2, 4 + this.bookImageHeight, 98, 20, I18n.format("gui.cancel", new Object[0])));
/*     */     }
/*     */     else {
/*     */       
/* 106 */       this.buttonList.add(this.buttonDone = new GuiButton(0, this.width / 2 - 100, 4 + this.bookImageHeight, 200, 20, I18n.format("gui.done", new Object[0])));
/*     */     } 
/*     */     
/* 109 */     int i = (this.width - this.bookImageWidth) / 2;
/* 110 */     int j = 2;
/* 111 */     this.buttonList.add(this.buttonNextPage = new NextPageButton(1, i + 120, j + 154, true));
/* 112 */     this.buttonList.add(this.buttonPreviousPage = new NextPageButton(2, i + 38, j + 154, false));
/* 113 */     updateButtons();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/* 118 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateButtons() {
/* 123 */     this.buttonNextPage.visible = (!this.bookGettingSigned && (this.currPage < this.bookTotalPages - 1 || this.bookIsUnsigned));
/* 124 */     this.buttonPreviousPage.visible = (!this.bookGettingSigned && this.currPage > 0);
/* 125 */     this.buttonDone.visible = (!this.bookIsUnsigned || !this.bookGettingSigned);
/*     */     
/* 127 */     if (this.bookIsUnsigned) {
/*     */       
/* 129 */       this.buttonSign.visible = !this.bookGettingSigned;
/* 130 */       this.buttonCancel.visible = this.bookGettingSigned;
/* 131 */       this.buttonFinalize.visible = this.bookGettingSigned;
/* 132 */       this.buttonFinalize.enabled = (this.bookTitle.trim().length() > 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void sendBookToServer(boolean publish) throws IOException {
/* 138 */     if (this.bookIsUnsigned && this.bookIsModified)
/*     */     {
/* 140 */       if (this.bookPages != null) {
/*     */         
/* 142 */         while (this.bookPages.tagCount() > 1) {
/*     */           
/* 144 */           String s = this.bookPages.getStringTagAt(this.bookPages.tagCount() - 1);
/*     */           
/* 146 */           if (s.length() != 0) {
/*     */             break;
/*     */           }
/*     */ 
/*     */           
/* 151 */           this.bookPages.removeTag(this.bookPages.tagCount() - 1);
/*     */         } 
/*     */         
/* 154 */         if (this.bookObj.hasTagCompound()) {
/*     */           
/* 156 */           NBTTagCompound nbttagcompound = this.bookObj.getTagCompound();
/* 157 */           nbttagcompound.setTag("pages", (NBTBase)this.bookPages);
/*     */         }
/*     */         else {
/*     */           
/* 161 */           this.bookObj.setTagInfo("pages", (NBTBase)this.bookPages);
/*     */         } 
/*     */         
/* 164 */         String s2 = "MC|BEdit";
/*     */         
/* 166 */         if (publish) {
/*     */           
/* 168 */           s2 = "MC|BSign";
/* 169 */           this.bookObj.setTagInfo("author", (NBTBase)new NBTTagString(this.editingPlayer.getName()));
/* 170 */           this.bookObj.setTagInfo("title", (NBTBase)new NBTTagString(this.bookTitle.trim()));
/*     */           
/* 172 */           for (int i = 0; i < this.bookPages.tagCount(); i++) {
/*     */             
/* 174 */             String s1 = this.bookPages.getStringTagAt(i);
/* 175 */             ChatComponentText chatComponentText = new ChatComponentText(s1);
/* 176 */             s1 = IChatComponent.Serializer.componentToJson((IChatComponent)chatComponentText);
/* 177 */             this.bookPages.set(i, (NBTBase)new NBTTagString(s1));
/*     */           } 
/*     */           
/* 180 */           this.bookObj.setItem(Items.written_book);
/*     */         } 
/*     */         
/* 183 */         PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
/* 184 */         packetbuffer.writeItemStackToBuffer(this.bookObj);
/* 185 */         this.mc.getNetHandler().addToSendQueue((Packet)new C17PacketCustomPayload(s2, packetbuffer));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/* 192 */     if (button.enabled) {
/*     */       
/* 194 */       if (button.id == 0) {
/*     */         
/* 196 */         this.mc.displayGuiScreen((GuiScreen)null);
/* 197 */         sendBookToServer(false);
/*     */       }
/* 199 */       else if (button.id == 3 && this.bookIsUnsigned) {
/*     */         
/* 201 */         this.bookGettingSigned = true;
/*     */       }
/* 203 */       else if (button.id == 1) {
/*     */         
/* 205 */         if (this.currPage < this.bookTotalPages - 1)
/*     */         {
/* 207 */           this.currPage++;
/*     */         }
/* 209 */         else if (this.bookIsUnsigned)
/*     */         {
/* 211 */           addNewPage();
/*     */           
/* 213 */           if (this.currPage < this.bookTotalPages - 1)
/*     */           {
/* 215 */             this.currPage++;
/*     */           }
/*     */         }
/*     */       
/* 219 */       } else if (button.id == 2) {
/*     */         
/* 221 */         if (this.currPage > 0)
/*     */         {
/* 223 */           this.currPage--;
/*     */         }
/*     */       }
/* 226 */       else if (button.id == 5 && this.bookGettingSigned) {
/*     */         
/* 228 */         sendBookToServer(true);
/* 229 */         this.mc.displayGuiScreen((GuiScreen)null);
/*     */       }
/* 231 */       else if (button.id == 4 && this.bookGettingSigned) {
/*     */         
/* 233 */         this.bookGettingSigned = false;
/*     */       } 
/*     */       
/* 236 */       updateButtons();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void addNewPage() {
/* 242 */     if (this.bookPages != null && this.bookPages.tagCount() < 50) {
/*     */       
/* 244 */       this.bookPages.appendTag((NBTBase)new NBTTagString(""));
/* 245 */       this.bookTotalPages++;
/* 246 */       this.bookIsModified = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 252 */     super.keyTyped(typedChar, keyCode);
/*     */     
/* 254 */     if (this.bookIsUnsigned)
/*     */     {
/* 256 */       if (this.bookGettingSigned) {
/*     */         
/* 258 */         keyTypedInTitle(typedChar, keyCode);
/*     */       }
/*     */       else {
/*     */         
/* 262 */         keyTypedInBook(typedChar, keyCode);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void keyTypedInBook(char typedChar, int keyCode) {
/* 269 */     if (GuiScreen.isKeyComboCtrlV(keyCode)) {
/*     */       
/* 271 */       pageInsertIntoCurrent(GuiScreen.getClipboardString());
/*     */     } else {
/*     */       String s;
/*     */       
/* 275 */       switch (keyCode) {
/*     */         
/*     */         case 14:
/* 278 */           s = pageGetCurrent();
/*     */           
/* 280 */           if (s.length() > 0)
/*     */           {
/* 282 */             pageSetCurrent(s.substring(0, s.length() - 1));
/*     */           }
/*     */           return;
/*     */ 
/*     */         
/*     */         case 28:
/*     */         case 156:
/* 289 */           pageInsertIntoCurrent("\n");
/*     */           return;
/*     */       } 
/*     */       
/* 293 */       if (ChatAllowedCharacters.isAllowedCharacter(typedChar))
/*     */       {
/* 295 */         pageInsertIntoCurrent(Character.toString(typedChar));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void keyTypedInTitle(char p_146460_1_, int p_146460_2_) throws IOException {
/* 303 */     switch (p_146460_2_) {
/*     */       
/*     */       case 14:
/* 306 */         if (!this.bookTitle.isEmpty()) {
/*     */           
/* 308 */           this.bookTitle = this.bookTitle.substring(0, this.bookTitle.length() - 1);
/* 309 */           updateButtons();
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 28:
/*     */       case 156:
/* 316 */         if (!this.bookTitle.isEmpty()) {
/*     */           
/* 318 */           sendBookToServer(true);
/* 319 */           this.mc.displayGuiScreen((GuiScreen)null);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 325 */     if (this.bookTitle.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(p_146460_1_)) {
/*     */       
/* 327 */       this.bookTitle += Character.toString(p_146460_1_);
/* 328 */       updateButtons();
/* 329 */       this.bookIsModified = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String pageGetCurrent() {
/* 336 */     return (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) ? this.bookPages.getStringTagAt(this.currPage) : "";
/*     */   }
/*     */ 
/*     */   
/*     */   private void pageSetCurrent(String p_146457_1_) {
/* 341 */     if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
/*     */       
/* 343 */       this.bookPages.set(this.currPage, (NBTBase)new NBTTagString(p_146457_1_));
/* 344 */       this.bookIsModified = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void pageInsertIntoCurrent(String p_146459_1_) {
/* 350 */     String s = pageGetCurrent();
/* 351 */     String s1 = s + p_146459_1_;
/* 352 */     int i = this.fontRendererObj.splitStringWidth(s1 + "" + EnumChatFormatting.BLACK + "_", 118);
/*     */     
/* 354 */     if (i <= 128 && s1.length() < 256)
/*     */     {
/* 356 */       pageSetCurrent(s1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 362 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 363 */     this.mc.getTextureManager().bindTexture(bookGuiTextures);
/* 364 */     int i = (this.width - this.bookImageWidth) / 2;
/* 365 */     int j = 2;
/* 366 */     drawTexturedModalRect(i, j, 0, 0, this.bookImageWidth, this.bookImageHeight);
/*     */     
/* 368 */     if (this.bookGettingSigned) {
/*     */       
/* 370 */       String s = this.bookTitle;
/*     */       
/* 372 */       if (this.bookIsUnsigned)
/*     */       {
/* 374 */         if (this.updateCount / 6 % 2 == 0) {
/*     */           
/* 376 */           s = s + "" + EnumChatFormatting.BLACK + "_";
/*     */         }
/*     */         else {
/*     */           
/* 380 */           s = s + "" + EnumChatFormatting.GRAY + "_";
/*     */         } 
/*     */       }
/*     */       
/* 384 */       String s1 = I18n.format("book.editTitle", new Object[0]);
/* 385 */       int k = this.fontRendererObj.getStringWidth(s1);
/* 386 */       this.fontRendererObj.drawString(s1, (i + 36 + (116 - k) / 2), (j + 16 + 16), 0);
/* 387 */       int l = this.fontRendererObj.getStringWidth(s);
/* 388 */       this.fontRendererObj.drawString(s, (i + 36 + (116 - l) / 2), (j + 48), 0);
/* 389 */       String s2 = I18n.format("book.byAuthor", new Object[] { this.editingPlayer.getName() });
/* 390 */       int i1 = this.fontRendererObj.getStringWidth(s2);
/* 391 */       this.fontRendererObj.drawString(EnumChatFormatting.DARK_GRAY + s2, (i + 36 + (116 - i1) / 2), (j + 48 + 10), 0);
/* 392 */       String s3 = I18n.format("book.finalizeWarning", new Object[0]);
/* 393 */       this.fontRendererObj.drawSplitString(s3, i + 36, j + 80, 116, 0);
/*     */     }
/*     */     else {
/*     */       
/* 397 */       String s4 = I18n.format("book.pageIndicator", new Object[] { Integer.valueOf(this.currPage + 1), Integer.valueOf(this.bookTotalPages) });
/* 398 */       String s5 = "";
/*     */       
/* 400 */       if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount())
/*     */       {
/* 402 */         s5 = this.bookPages.getStringTagAt(this.currPage);
/*     */       }
/*     */       
/* 405 */       if (this.bookIsUnsigned) {
/*     */         
/* 407 */         if (this.fontRendererObj.getBidiFlag())
/*     */         {
/* 409 */           s5 = s5 + "_";
/*     */         }
/* 411 */         else if (this.updateCount / 6 % 2 == 0)
/*     */         {
/* 413 */           s5 = s5 + "" + EnumChatFormatting.BLACK + "_";
/*     */         }
/*     */         else
/*     */         {
/* 417 */           s5 = s5 + "" + EnumChatFormatting.GRAY + "_";
/*     */         }
/*     */       
/* 420 */       } else if (this.field_175387_B != this.currPage) {
/*     */         
/* 422 */         if (ItemEditableBook.validBookTagContents(this.bookObj.getTagCompound())) {
/*     */           
/*     */           try
/*     */           {
/* 426 */             IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s5);
/* 427 */             this.field_175386_A = (ichatcomponent != null) ? GuiUtilRenderComponents.splitText(ichatcomponent, 116, this.fontRendererObj, true, true) : null;
/*     */           }
/* 429 */           catch (JsonParseException var13)
/*     */           {
/* 431 */             this.field_175386_A = null;
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 436 */           ChatComponentText chatcomponenttext = new ChatComponentText(EnumChatFormatting.DARK_RED.toString() + "* Invalid book tag *");
/* 437 */           this.field_175386_A = Lists.newArrayList((Iterable)chatcomponenttext);
/*     */         } 
/*     */         
/* 440 */         this.field_175387_B = this.currPage;
/*     */       } 
/*     */       
/* 443 */       int j1 = this.fontRendererObj.getStringWidth(s4);
/* 444 */       this.fontRendererObj.drawString(s4, (i - j1 + this.bookImageWidth - 44), (j + 16), 0);
/*     */       
/* 446 */       if (this.field_175386_A == null) {
/*     */         
/* 448 */         this.fontRendererObj.drawSplitString(s5, i + 36, j + 16 + 16, 116, 0);
/*     */       }
/*     */       else {
/*     */         
/* 452 */         int k1 = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.field_175386_A.size());
/*     */         
/* 454 */         for (int l1 = 0; l1 < k1; l1++) {
/*     */           
/* 456 */           IChatComponent ichatcomponent2 = this.field_175386_A.get(l1);
/* 457 */           this.fontRendererObj.drawString(ichatcomponent2.getUnformattedText(), (i + 36), (j + 16 + 16 + l1 * this.fontRendererObj.FONT_HEIGHT), 0);
/*     */         } 
/*     */         
/* 460 */         IChatComponent ichatcomponent1 = func_175385_b(mouseX, mouseY);
/*     */         
/* 462 */         if (ichatcomponent1 != null)
/*     */         {
/* 464 */           handleComponentHover(ichatcomponent1, mouseX, mouseY);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 469 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 474 */     if (mouseButton == 0) {
/*     */       
/* 476 */       IChatComponent ichatcomponent = func_175385_b(mouseX, mouseY);
/*     */       
/* 478 */       if (handleComponentClick(ichatcomponent)) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 484 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean handleComponentClick(IChatComponent component) {
/* 489 */     ClickEvent clickevent = (component == null) ? null : component.getChatStyle().getChatClickEvent();
/*     */     
/* 491 */     if (clickevent == null)
/*     */     {
/* 493 */       return false;
/*     */     }
/* 495 */     if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
/*     */       
/* 497 */       String s = clickevent.getValue();
/*     */ 
/*     */       
/*     */       try {
/* 501 */         int i = Integer.parseInt(s) - 1;
/*     */         
/* 503 */         if (i >= 0 && i < this.bookTotalPages && i != this.currPage)
/*     */         {
/* 505 */           this.currPage = i;
/* 506 */           updateButtons();
/* 507 */           return true;
/*     */         }
/*     */       
/* 510 */       } catch (Throwable throwable) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 515 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 519 */     boolean flag = super.handleComponentClick(component);
/*     */     
/* 521 */     if (flag && clickevent.getAction() == ClickEvent.Action.RUN_COMMAND)
/*     */     {
/* 523 */       this.mc.displayGuiScreen((GuiScreen)null);
/*     */     }
/*     */     
/* 526 */     return flag;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IChatComponent func_175385_b(int p_175385_1_, int p_175385_2_) {
/* 532 */     if (this.field_175386_A == null)
/*     */     {
/* 534 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 538 */     int i = p_175385_1_ - (this.width - this.bookImageWidth) / 2 - 36;
/* 539 */     int j = p_175385_2_ - 2 - 16 - 16;
/*     */     
/* 541 */     if (i >= 0 && j >= 0) {
/*     */       
/* 543 */       int k = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.field_175386_A.size());
/*     */       
/* 545 */       if (i <= 116 && j < this.mc.fontRendererObj.FONT_HEIGHT * k + k) {
/*     */         
/* 547 */         int l = j / this.mc.fontRendererObj.FONT_HEIGHT;
/*     */         
/* 549 */         if (l >= 0 && l < this.field_175386_A.size()) {
/*     */           
/* 551 */           IChatComponent ichatcomponent = this.field_175386_A.get(l);
/* 552 */           int i1 = 0;
/*     */           
/* 554 */           for (IChatComponent ichatcomponent1 : ichatcomponent) {
/*     */             
/* 556 */             if (ichatcomponent1 instanceof ChatComponentText) {
/*     */               
/* 558 */               i1 += this.mc.fontRendererObj.getStringWidth(((ChatComponentText)ichatcomponent1).getChatComponentText_TextValue());
/*     */               
/* 560 */               if (i1 > i)
/*     */               {
/* 562 */                 return ichatcomponent1;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 568 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 572 */       return null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 577 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   static class NextPageButton
/*     */     extends GuiButton
/*     */   {
/*     */     private final boolean field_146151_o;
/*     */ 
/*     */     
/*     */     public NextPageButton(int p_i46316_1_, int p_i46316_2_, int p_i46316_3_, boolean p_i46316_4_) {
/* 588 */       super(p_i46316_1_, p_i46316_2_, p_i46316_3_, 23, 13, "");
/* 589 */       this.field_146151_o = p_i46316_4_;
/*     */     }
/*     */ 
/*     */     
/*     */     public void drawButton(Minecraft mc, int mouseX, int mouseY) {
/* 594 */       if (this.visible) {
/*     */         
/* 596 */         boolean flag = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
/* 597 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 598 */         mc.getTextureManager().bindTexture(GuiScreenBook.bookGuiTextures);
/* 599 */         int i = 0;
/* 600 */         int j = 192;
/*     */         
/* 602 */         if (flag)
/*     */         {
/* 604 */           i += 23;
/*     */         }
/*     */         
/* 607 */         if (!this.field_146151_o)
/*     */         {
/* 609 */           j += 13;
/*     */         }
/*     */         
/* 612 */         drawTexturedModalRect(this.xPosition, this.yPosition, i, j, 23, 13);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiScreenBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
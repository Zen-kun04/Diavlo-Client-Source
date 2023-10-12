/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.util.ChatAllowedCharacters;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class GuiTextField
/*     */   extends Gui {
/*     */   private final int id;
/*     */   private final FontRenderer fontRendererInstance;
/*     */   public int xPosition;
/*     */   public int yPosition;
/*     */   private final int width;
/*     */   private final int height;
/*  20 */   private String text = "";
/*  21 */   private int maxStringLength = 32;
/*     */   private int cursorCounter;
/*     */   private boolean enableBackgroundDrawing = true;
/*     */   private boolean canLoseFocus = true;
/*     */   private boolean isFocused;
/*     */   private boolean isEnabled = true;
/*     */   private int lineScrollOffset;
/*     */   private int cursorPosition;
/*     */   private int selectionEnd;
/*  30 */   private int enabledColor = 14737632;
/*  31 */   private int disabledColor = 7368816;
/*     */   private boolean visible = true;
/*     */   private GuiPageButtonList.GuiResponder field_175210_x;
/*  34 */   private Predicate<String> validator = Predicates.alwaysTrue();
/*     */ 
/*     */   
/*     */   public GuiTextField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
/*  38 */     this.id = componentId;
/*  39 */     this.fontRendererInstance = fontrendererObj;
/*  40 */     this.xPosition = x;
/*  41 */     this.yPosition = y;
/*  42 */     this.width = par5Width;
/*  43 */     this.height = par6Height;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_175207_a(GuiPageButtonList.GuiResponder p_175207_1_) {
/*  48 */     this.field_175210_x = p_175207_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateCursorCounter() {
/*  53 */     this.cursorCounter++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setText(String p_146180_1_) {
/*  58 */     if (this.validator.apply(p_146180_1_)) {
/*     */       
/*  60 */       if (p_146180_1_.length() > this.maxStringLength) {
/*     */         
/*  62 */         this.text = p_146180_1_.substring(0, this.maxStringLength);
/*     */       }
/*     */       else {
/*     */         
/*  66 */         this.text = p_146180_1_;
/*     */       } 
/*     */       
/*  69 */       setCursorPositionEnd();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getText() {
/*  75 */     return this.text;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSelectedText() {
/*  80 */     int i = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
/*  81 */     int j = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
/*  82 */     return this.text.substring(i, j);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValidator(Predicate<String> theValidator) {
/*  87 */     this.validator = theValidator;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeText(String p_146191_1_) {
/*  92 */     String s = "";
/*  93 */     String s1 = ChatAllowedCharacters.filterAllowedCharacters(p_146191_1_);
/*  94 */     int i = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
/*  95 */     int j = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
/*  96 */     int k = this.maxStringLength - this.text.length() - i - j;
/*  97 */     int l = 0;
/*     */     
/*  99 */     if (this.text.length() > 0)
/*     */     {
/* 101 */       s = s + this.text.substring(0, i);
/*     */     }
/*     */     
/* 104 */     if (k < s1.length()) {
/*     */       
/* 106 */       s = s + s1.substring(0, k);
/* 107 */       l = k;
/*     */     }
/*     */     else {
/*     */       
/* 111 */       s = s + s1;
/* 112 */       l = s1.length();
/*     */     } 
/*     */     
/* 115 */     if (this.text.length() > 0 && j < this.text.length())
/*     */     {
/* 117 */       s = s + this.text.substring(j);
/*     */     }
/*     */     
/* 120 */     if (this.validator.apply(s)) {
/*     */       
/* 122 */       this.text = s;
/* 123 */       moveCursorBy(i - this.selectionEnd + l);
/*     */       
/* 125 */       if (this.field_175210_x != null)
/*     */       {
/* 127 */         this.field_175210_x.func_175319_a(this.id, this.text);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteWords(int p_146177_1_) {
/* 134 */     if (this.text.length() != 0)
/*     */     {
/* 136 */       if (this.selectionEnd != this.cursorPosition) {
/*     */         
/* 138 */         writeText("");
/*     */       }
/*     */       else {
/*     */         
/* 142 */         deleteFromCursor(getNthWordFromCursor(p_146177_1_) - this.cursorPosition);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteFromCursor(int p_146175_1_) {
/* 149 */     if (this.text.length() != 0)
/*     */     {
/* 151 */       if (this.selectionEnd != this.cursorPosition) {
/*     */         
/* 153 */         writeText("");
/*     */       }
/*     */       else {
/*     */         
/* 157 */         boolean flag = (p_146175_1_ < 0);
/* 158 */         int i = flag ? (this.cursorPosition + p_146175_1_) : this.cursorPosition;
/* 159 */         int j = flag ? this.cursorPosition : (this.cursorPosition + p_146175_1_);
/* 160 */         String s = "";
/*     */         
/* 162 */         if (i >= 0)
/*     */         {
/* 164 */           s = this.text.substring(0, i);
/*     */         }
/*     */         
/* 167 */         if (j < this.text.length())
/*     */         {
/* 169 */           s = s + this.text.substring(j);
/*     */         }
/*     */         
/* 172 */         if (this.validator.apply(s)) {
/*     */           
/* 174 */           this.text = s;
/*     */           
/* 176 */           if (flag)
/*     */           {
/* 178 */             moveCursorBy(p_146175_1_);
/*     */           }
/*     */           
/* 181 */           if (this.field_175210_x != null)
/*     */           {
/* 183 */             this.field_175210_x.func_175319_a(this.id, this.text);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getId() {
/* 192 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNthWordFromCursor(int p_146187_1_) {
/* 197 */     return getNthWordFromPos(p_146187_1_, getCursorPosition());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNthWordFromPos(int p_146183_1_, int p_146183_2_) {
/* 202 */     return func_146197_a(p_146183_1_, p_146183_2_, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public int func_146197_a(int p_146197_1_, int p_146197_2_, boolean p_146197_3_) {
/* 207 */     int i = p_146197_2_;
/* 208 */     boolean flag = (p_146197_1_ < 0);
/* 209 */     int j = Math.abs(p_146197_1_);
/*     */     
/* 211 */     for (int k = 0; k < j; k++) {
/*     */       
/* 213 */       if (!flag) {
/*     */         
/* 215 */         int l = this.text.length();
/* 216 */         i = this.text.indexOf(' ', i);
/*     */         
/* 218 */         if (i == -1)
/*     */         {
/* 220 */           i = l;
/*     */         }
/*     */         else
/*     */         {
/* 224 */           while (p_146197_3_ && i < l && this.text.charAt(i) == ' ')
/*     */           {
/* 226 */             i++;
/*     */           }
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 232 */         while (p_146197_3_ && i > 0 && this.text.charAt(i - 1) == ' ')
/*     */         {
/* 234 */           i--;
/*     */         }
/*     */         
/* 237 */         while (i > 0 && this.text.charAt(i - 1) != ' ')
/*     */         {
/* 239 */           i--;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 244 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public void moveCursorBy(int p_146182_1_) {
/* 249 */     setCursorPosition(this.selectionEnd + p_146182_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCursorPosition(int p_146190_1_) {
/* 254 */     this.cursorPosition = p_146190_1_;
/* 255 */     int i = this.text.length();
/* 256 */     this.cursorPosition = MathHelper.clamp_int(this.cursorPosition, 0, i);
/* 257 */     setSelectionPos(this.cursorPosition);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCursorPositionZero() {
/* 262 */     setCursorPosition(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCursorPositionEnd() {
/* 267 */     setCursorPosition(this.text.length());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean textboxKeyTyped(char p_146201_1_, int p_146201_2_) {
/* 272 */     if (!this.isFocused)
/*     */     {
/* 274 */       return false;
/*     */     }
/* 276 */     if (GuiScreen.isKeyComboCtrlA(p_146201_2_)) {
/*     */       
/* 278 */       setCursorPositionEnd();
/* 279 */       setSelectionPos(0);
/* 280 */       return true;
/*     */     } 
/* 282 */     if (GuiScreen.isKeyComboCtrlC(p_146201_2_)) {
/*     */       
/* 284 */       GuiScreen.setClipboardString(getSelectedText());
/* 285 */       return true;
/*     */     } 
/* 287 */     if (GuiScreen.isKeyComboCtrlV(p_146201_2_)) {
/*     */       
/* 289 */       if (this.isEnabled)
/*     */       {
/* 291 */         writeText(GuiScreen.getClipboardString());
/*     */       }
/*     */       
/* 294 */       return true;
/*     */     } 
/* 296 */     if (GuiScreen.isKeyComboCtrlX(p_146201_2_)) {
/*     */       
/* 298 */       GuiScreen.setClipboardString(getSelectedText());
/*     */       
/* 300 */       if (this.isEnabled)
/*     */       {
/* 302 */         writeText("");
/*     */       }
/*     */       
/* 305 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 309 */     switch (p_146201_2_) {
/*     */       
/*     */       case 14:
/* 312 */         if (GuiScreen.isCtrlKeyDown()) {
/*     */           
/* 314 */           if (this.isEnabled)
/*     */           {
/* 316 */             deleteWords(-1);
/*     */           }
/*     */         }
/* 319 */         else if (this.isEnabled) {
/*     */           
/* 321 */           deleteFromCursor(-1);
/*     */         } 
/*     */         
/* 324 */         return true;
/*     */       
/*     */       case 199:
/* 327 */         if (GuiScreen.isShiftKeyDown()) {
/*     */           
/* 329 */           setSelectionPos(0);
/*     */         }
/*     */         else {
/*     */           
/* 333 */           setCursorPositionZero();
/*     */         } 
/*     */         
/* 336 */         return true;
/*     */       
/*     */       case 203:
/* 339 */         if (GuiScreen.isShiftKeyDown()) {
/*     */           
/* 341 */           if (GuiScreen.isCtrlKeyDown())
/*     */           {
/* 343 */             setSelectionPos(getNthWordFromPos(-1, getSelectionEnd()));
/*     */           }
/*     */           else
/*     */           {
/* 347 */             setSelectionPos(getSelectionEnd() - 1);
/*     */           }
/*     */         
/* 350 */         } else if (GuiScreen.isCtrlKeyDown()) {
/*     */           
/* 352 */           setCursorPosition(getNthWordFromCursor(-1));
/*     */         }
/*     */         else {
/*     */           
/* 356 */           moveCursorBy(-1);
/*     */         } 
/*     */         
/* 359 */         return true;
/*     */       
/*     */       case 205:
/* 362 */         if (GuiScreen.isShiftKeyDown()) {
/*     */           
/* 364 */           if (GuiScreen.isCtrlKeyDown())
/*     */           {
/* 366 */             setSelectionPos(getNthWordFromPos(1, getSelectionEnd()));
/*     */           }
/*     */           else
/*     */           {
/* 370 */             setSelectionPos(getSelectionEnd() + 1);
/*     */           }
/*     */         
/* 373 */         } else if (GuiScreen.isCtrlKeyDown()) {
/*     */           
/* 375 */           setCursorPosition(getNthWordFromCursor(1));
/*     */         }
/*     */         else {
/*     */           
/* 379 */           moveCursorBy(1);
/*     */         } 
/*     */         
/* 382 */         return true;
/*     */       
/*     */       case 207:
/* 385 */         if (GuiScreen.isShiftKeyDown()) {
/*     */           
/* 387 */           setSelectionPos(this.text.length());
/*     */         }
/*     */         else {
/*     */           
/* 391 */           setCursorPositionEnd();
/*     */         } 
/*     */         
/* 394 */         return true;
/*     */       
/*     */       case 211:
/* 397 */         if (GuiScreen.isCtrlKeyDown()) {
/*     */           
/* 399 */           if (this.isEnabled)
/*     */           {
/* 401 */             deleteWords(1);
/*     */           }
/*     */         }
/* 404 */         else if (this.isEnabled) {
/*     */           
/* 406 */           deleteFromCursor(1);
/*     */         } 
/*     */         
/* 409 */         return true;
/*     */     } 
/*     */     
/* 412 */     if (ChatAllowedCharacters.isAllowedCharacter(p_146201_1_)) {
/*     */       
/* 414 */       if (this.isEnabled)
/*     */       {
/* 416 */         writeText(Character.toString(p_146201_1_));
/*     */       }
/*     */       
/* 419 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 423 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseClicked(int p_146192_1_, int p_146192_2_, int p_146192_3_) {
/* 431 */     boolean flag = (p_146192_1_ >= this.xPosition && p_146192_1_ < this.xPosition + this.width && p_146192_2_ >= this.yPosition && p_146192_2_ < this.yPosition + this.height);
/*     */     
/* 433 */     if (this.canLoseFocus)
/*     */     {
/* 435 */       setFocused(flag);
/*     */     }
/*     */     
/* 438 */     if (this.isFocused && flag && p_146192_3_ == 0) {
/*     */       
/* 440 */       int i = p_146192_1_ - this.xPosition;
/*     */       
/* 442 */       if (this.enableBackgroundDrawing)
/*     */       {
/* 444 */         i -= 4;
/*     */       }
/*     */       
/* 447 */       String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), getWidth());
/* 448 */       setCursorPosition(this.fontRendererInstance.trimStringToWidth(s, i).length() + this.lineScrollOffset);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawTextBox() {
/* 454 */     if (getVisible()) {
/*     */       
/* 456 */       if (getEnableBackgroundDrawing()) {
/*     */         
/* 458 */         drawRect((this.xPosition - 1), (this.yPosition - 1), (this.xPosition + this.width + 1), (this.yPosition + this.height + 1), -6250336);
/* 459 */         drawRect(this.xPosition, this.yPosition, (this.xPosition + this.width), (this.yPosition + this.height), -16777216);
/*     */       } 
/*     */       
/* 462 */       int i = this.isEnabled ? this.enabledColor : this.disabledColor;
/* 463 */       int j = this.cursorPosition - this.lineScrollOffset;
/* 464 */       int k = this.selectionEnd - this.lineScrollOffset;
/* 465 */       String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), getWidth());
/* 466 */       boolean flag = (j >= 0 && j <= s.length());
/* 467 */       boolean flag1 = (this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag);
/* 468 */       int l = this.enableBackgroundDrawing ? (this.xPosition + 4) : this.xPosition;
/* 469 */       int i1 = this.enableBackgroundDrawing ? (this.yPosition + (this.height - 8) / 2) : this.yPosition;
/* 470 */       int j1 = l;
/*     */       
/* 472 */       if (k > s.length())
/*     */       {
/* 474 */         k = s.length();
/*     */       }
/*     */       
/* 477 */       if (s.length() > 0) {
/*     */         
/* 479 */         String s1 = flag ? s.substring(0, j) : s;
/* 480 */         j1 = this.fontRendererInstance.drawStringWithShadow(s1, l, i1, i);
/*     */       } 
/*     */       
/* 483 */       boolean flag2 = (this.cursorPosition < this.text.length() || this.text.length() >= getMaxStringLength());
/* 484 */       int k1 = j1;
/*     */       
/* 486 */       if (!flag) {
/*     */         
/* 488 */         k1 = (j > 0) ? (l + this.width) : l;
/*     */       }
/* 490 */       else if (flag2) {
/*     */         
/* 492 */         k1 = j1 - 1;
/* 493 */         j1--;
/*     */       } 
/*     */       
/* 496 */       if (s.length() > 0 && flag && j < s.length())
/*     */       {
/* 498 */         j1 = this.fontRendererInstance.drawStringWithShadow(s.substring(j), j1, i1, i);
/*     */       }
/*     */       
/* 501 */       if (flag1)
/*     */       {
/* 503 */         if (flag2) {
/*     */           
/* 505 */           Gui.drawRect(k1, (i1 - 1), (k1 + 1), (i1 + 1 + this.fontRendererInstance.FONT_HEIGHT), -3092272);
/*     */         }
/*     */         else {
/*     */           
/* 509 */           this.fontRendererInstance.drawStringWithShadow("_", k1, i1, i);
/*     */         } 
/*     */       }
/*     */       
/* 513 */       if (k != j) {
/*     */         
/* 515 */         int l1 = l + this.fontRendererInstance.getStringWidth(s.substring(0, k));
/* 516 */         drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + this.fontRendererInstance.FONT_HEIGHT);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_) {
/* 523 */     if (p_146188_1_ < p_146188_3_) {
/*     */       
/* 525 */       int i = p_146188_1_;
/* 526 */       p_146188_1_ = p_146188_3_;
/* 527 */       p_146188_3_ = i;
/*     */     } 
/*     */     
/* 530 */     if (p_146188_2_ < p_146188_4_) {
/*     */       
/* 532 */       int j = p_146188_2_;
/* 533 */       p_146188_2_ = p_146188_4_;
/* 534 */       p_146188_4_ = j;
/*     */     } 
/*     */     
/* 537 */     if (p_146188_3_ > this.xPosition + this.width)
/*     */     {
/* 539 */       p_146188_3_ = this.xPosition + this.width;
/*     */     }
/*     */     
/* 542 */     if (p_146188_1_ > this.xPosition + this.width)
/*     */     {
/* 544 */       p_146188_1_ = this.xPosition + this.width;
/*     */     }
/*     */     
/* 547 */     Tessellator tessellator = Tessellator.getInstance();
/* 548 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 549 */     GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
/* 550 */     GlStateManager.disableTexture2D();
/* 551 */     GlStateManager.enableColorLogic();
/* 552 */     GlStateManager.colorLogicOp(5387);
/* 553 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION);
/* 554 */     worldrenderer.pos(p_146188_1_, p_146188_4_, 0.0D).endVertex();
/* 555 */     worldrenderer.pos(p_146188_3_, p_146188_4_, 0.0D).endVertex();
/* 556 */     worldrenderer.pos(p_146188_3_, p_146188_2_, 0.0D).endVertex();
/* 557 */     worldrenderer.pos(p_146188_1_, p_146188_2_, 0.0D).endVertex();
/* 558 */     tessellator.draw();
/* 559 */     GlStateManager.disableColorLogic();
/* 560 */     GlStateManager.enableTexture2D();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxStringLength(int p_146203_1_) {
/* 565 */     this.maxStringLength = p_146203_1_;
/*     */     
/* 567 */     if (this.text.length() > p_146203_1_)
/*     */     {
/* 569 */       this.text = this.text.substring(0, p_146203_1_);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxStringLength() {
/* 575 */     return this.maxStringLength;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCursorPosition() {
/* 580 */     return this.cursorPosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getEnableBackgroundDrawing() {
/* 585 */     return this.enableBackgroundDrawing;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEnableBackgroundDrawing(boolean p_146185_1_) {
/* 590 */     this.enableBackgroundDrawing = p_146185_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTextColor(int p_146193_1_) {
/* 595 */     this.enabledColor = p_146193_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDisabledTextColour(int p_146204_1_) {
/* 600 */     this.disabledColor = p_146204_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFocused(boolean p_146195_1_) {
/* 605 */     if (p_146195_1_ && !this.isFocused)
/*     */     {
/* 607 */       this.cursorCounter = 0;
/*     */     }
/*     */     
/* 610 */     this.isFocused = p_146195_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFocused() {
/* 615 */     return this.isFocused;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEnabled(boolean p_146184_1_) {
/* 620 */     this.isEnabled = p_146184_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSelectionEnd() {
/* 625 */     return this.selectionEnd;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 630 */     return getEnableBackgroundDrawing() ? (this.width - 8) : this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelectionPos(int p_146199_1_) {
/* 635 */     int i = this.text.length();
/*     */     
/* 637 */     if (p_146199_1_ > i)
/*     */     {
/* 639 */       p_146199_1_ = i;
/*     */     }
/*     */     
/* 642 */     if (p_146199_1_ < 0)
/*     */     {
/* 644 */       p_146199_1_ = 0;
/*     */     }
/*     */     
/* 647 */     this.selectionEnd = p_146199_1_;
/*     */     
/* 649 */     if (this.fontRendererInstance != null) {
/*     */       
/* 651 */       if (this.lineScrollOffset > i)
/*     */       {
/* 653 */         this.lineScrollOffset = i;
/*     */       }
/*     */       
/* 656 */       int j = getWidth();
/* 657 */       String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), j);
/* 658 */       int k = s.length() + this.lineScrollOffset;
/*     */       
/* 660 */       if (p_146199_1_ == this.lineScrollOffset)
/*     */       {
/* 662 */         this.lineScrollOffset -= this.fontRendererInstance.trimStringToWidth(this.text, j, true).length();
/*     */       }
/*     */       
/* 665 */       if (p_146199_1_ > k) {
/*     */         
/* 667 */         this.lineScrollOffset += p_146199_1_ - k;
/*     */       }
/* 669 */       else if (p_146199_1_ <= this.lineScrollOffset) {
/*     */         
/* 671 */         this.lineScrollOffset -= this.lineScrollOffset - p_146199_1_;
/*     */       } 
/*     */       
/* 674 */       this.lineScrollOffset = MathHelper.clamp_int(this.lineScrollOffset, 0, i);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCanLoseFocus(boolean p_146205_1_) {
/* 680 */     this.canLoseFocus = p_146205_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getVisible() {
/* 685 */     return this.visible;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVisible(boolean p_146189_1_) {
/* 690 */     this.visible = p_146189_1_;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiTextField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package net.minecraft.client.gui;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.C14PacketTabComplete;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class GuiChat extends GuiScreen {
/*  18 */   private static final Logger logger = LogManager.getLogger();
/*  19 */   private String historyBuffer = "";
/*  20 */   private int sentHistoryCursor = -1;
/*     */   private boolean playerNamesFound;
/*     */   private boolean waitingOnAutocomplete;
/*     */   private int autocompleteIndex;
/*  24 */   private List<String> foundPlayerNames = Lists.newArrayList();
/*     */   protected GuiTextField inputField;
/*  26 */   private String defaultInputFieldText = "";
/*     */ 
/*     */   
/*     */   public static boolean chatEnabled = false;
/*     */ 
/*     */   
/*     */   public GuiChat() {}
/*     */ 
/*     */   
/*     */   public GuiChat(String defaultText) {
/*  36 */     this.defaultInputFieldText = defaultText;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  41 */     chatEnabled = true;
/*  42 */     Keyboard.enableRepeatEvents(true);
/*  43 */     this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
/*  44 */     this.inputField = new GuiTextField(0, this.fontRendererObj, 4, this.height - 12, this.width - 4, 12);
/*  45 */     this.inputField.setMaxStringLength(100);
/*  46 */     this.inputField.setEnableBackgroundDrawing(false);
/*  47 */     this.inputField.setTextColor(16733525);
/*  48 */     this.inputField.setFocused(true);
/*  49 */     this.inputField.setText(this.defaultInputFieldText);
/*  50 */     this.inputField.setCanLoseFocus(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/*  55 */     chatEnabled = false;
/*  56 */     Keyboard.enableRepeatEvents(false);
/*  57 */     this.mc.ingameGUI.getChatGUI().resetScroll();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/*  62 */     this.inputField.updateCursorCounter();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/*  67 */     this.waitingOnAutocomplete = false;
/*     */     
/*  69 */     if (keyCode == 15) {
/*     */       
/*  71 */       autocompletePlayerNames();
/*     */     }
/*     */     else {
/*     */       
/*  75 */       this.playerNamesFound = false;
/*     */     } 
/*     */     
/*  78 */     if (keyCode == 1) {
/*     */       
/*  80 */       this.mc.displayGuiScreen((GuiScreen)null);
/*     */     }
/*  82 */     else if (keyCode != 28 && keyCode != 156) {
/*     */       
/*  84 */       if (keyCode == 200)
/*     */       {
/*  86 */         getSentHistory(-1);
/*     */       }
/*  88 */       else if (keyCode == 208)
/*     */       {
/*  90 */         getSentHistory(1);
/*     */       }
/*  92 */       else if (keyCode == 201)
/*     */       {
/*  94 */         this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
/*     */       }
/*  96 */       else if (keyCode == 209)
/*     */       {
/*  98 */         this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
/*     */       }
/*     */       else
/*     */       {
/* 102 */         this.inputField.textboxKeyTyped(typedChar, keyCode);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 107 */       String s = this.inputField.getText().trim();
/*     */       
/* 109 */       if (s.length() > 0)
/*     */       {
/* 111 */         sendChatMessage(s);
/*     */       }
/*     */       
/* 114 */       this.mc.displayGuiScreen((GuiScreen)null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/* 120 */     super.handleMouseInput();
/* 121 */     int i = Mouse.getEventDWheel();
/*     */     
/* 123 */     if (i != 0) {
/*     */       
/* 125 */       if (i > 1)
/*     */       {
/* 127 */         i = 1;
/*     */       }
/*     */       
/* 130 */       if (i < -1)
/*     */       {
/* 132 */         i = -1;
/*     */       }
/*     */       
/* 135 */       if (!isShiftKeyDown())
/*     */       {
/* 137 */         i *= 7;
/*     */       }
/*     */       
/* 140 */       this.mc.ingameGUI.getChatGUI().scroll(i);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 146 */     if (mouseButton == 0) {
/*     */       
/* 148 */       IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
/*     */       
/* 150 */       if (handleComponentClick(ichatcomponent)) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 156 */     this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
/* 157 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setText(String newChatText, boolean shouldOverwrite) {
/* 162 */     if (shouldOverwrite) {
/*     */       
/* 164 */       this.inputField.setText(newChatText);
/*     */     }
/*     */     else {
/*     */       
/* 168 */       this.inputField.writeText(newChatText);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void autocompletePlayerNames() {
/* 174 */     if (this.playerNamesFound) {
/*     */       
/* 176 */       this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
/*     */       
/* 178 */       if (this.autocompleteIndex >= this.foundPlayerNames.size())
/*     */       {
/* 180 */         this.autocompleteIndex = 0;
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 185 */       int i = this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false);
/* 186 */       this.foundPlayerNames.clear();
/* 187 */       this.autocompleteIndex = 0;
/* 188 */       String s = this.inputField.getText().substring(i).toLowerCase();
/* 189 */       String s1 = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
/* 190 */       sendAutocompleteRequest(s1, s);
/*     */       
/* 192 */       if (this.foundPlayerNames.isEmpty()) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 197 */       this.playerNamesFound = true;
/* 198 */       this.inputField.deleteFromCursor(i - this.inputField.getCursorPosition());
/*     */     } 
/*     */     
/* 201 */     if (this.foundPlayerNames.size() > 1) {
/*     */       
/* 203 */       StringBuilder stringbuilder = new StringBuilder();
/*     */       
/* 205 */       for (String s2 : this.foundPlayerNames) {
/*     */         
/* 207 */         if (stringbuilder.length() > 0)
/*     */         {
/* 209 */           stringbuilder.append(", ");
/*     */         }
/*     */         
/* 212 */         stringbuilder.append(s2);
/*     */       } 
/*     */       
/* 215 */       this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((IChatComponent)new ChatComponentText(stringbuilder.toString()), 1);
/*     */     } 
/*     */     
/* 218 */     this.inputField.writeText(this.foundPlayerNames.get(this.autocompleteIndex++));
/*     */   }
/*     */ 
/*     */   
/*     */   private void sendAutocompleteRequest(String p_146405_1_, String p_146405_2_) {
/* 223 */     if (p_146405_1_.length() >= 1) {
/*     */       
/* 225 */       BlockPos blockpos = null;
/*     */       
/* 227 */       if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
/*     */       {
/* 229 */         blockpos = this.mc.objectMouseOver.getBlockPos();
/*     */       }
/*     */       
/* 232 */       this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C14PacketTabComplete(p_146405_1_, blockpos));
/* 233 */       this.waitingOnAutocomplete = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSentHistory(int msgPos) {
/* 239 */     int i = this.sentHistoryCursor + msgPos;
/* 240 */     int j = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
/* 241 */     i = MathHelper.clamp_int(i, 0, j);
/*     */     
/* 243 */     if (i != this.sentHistoryCursor)
/*     */     {
/* 245 */       if (i == j) {
/*     */         
/* 247 */         this.sentHistoryCursor = j;
/* 248 */         this.inputField.setText(this.historyBuffer);
/*     */       }
/*     */       else {
/*     */         
/* 252 */         if (this.sentHistoryCursor == j)
/*     */         {
/* 254 */           this.historyBuffer = this.inputField.getText();
/*     */         }
/*     */         
/* 257 */         this.inputField.setText(this.mc.ingameGUI.getChatGUI().getSentMessages().get(i));
/* 258 */         this.sentHistoryCursor = i;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 265 */     drawRect(2.0D, (this.height - 14), Math.max(GuiNewChat.calculateChatboxWidth(this.mc.gameSettings.chatWidth) + 5, this.fontRendererObj.getStringWidth(this.inputField.getText()) + 7), (this.height - 2), -2147483648);
/* 266 */     this.inputField.drawTextBox();
/* 267 */     IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
/*     */     
/* 269 */     if (ichatcomponent != null && ichatcomponent.getChatStyle().getChatHoverEvent() != null)
/*     */     {
/* 271 */       handleComponentHover(ichatcomponent, mouseX, mouseY);
/*     */     }
/*     */     
/* 274 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAutocompleteResponse(String[] p_146406_1_) {
/* 279 */     if (this.waitingOnAutocomplete) {
/*     */       
/* 281 */       this.playerNamesFound = false;
/* 282 */       this.foundPlayerNames.clear();
/*     */       
/* 284 */       for (String s : p_146406_1_) {
/*     */         
/* 286 */         if (s.length() > 0)
/*     */         {
/* 288 */           this.foundPlayerNames.add(s);
/*     */         }
/*     */       } 
/*     */       
/* 292 */       String s1 = this.inputField.getText().substring(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false));
/* 293 */       String s2 = StringUtils.getCommonPrefix(p_146406_1_);
/*     */       
/* 295 */       if (s2.length() > 0 && !s1.equalsIgnoreCase(s2)) {
/*     */         
/* 297 */         this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
/* 298 */         this.inputField.writeText(s2);
/*     */       }
/* 300 */       else if (this.foundPlayerNames.size() > 0) {
/*     */         
/* 302 */         this.playerNamesFound = true;
/* 303 */         autocompletePlayerNames();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doesGuiPauseGame() {
/* 310 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiChat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package rip.diavlo.base.ui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.api.font.CustomFontRenderer;
/*     */ import rip.diavlo.base.api.module.Category;
/*     */ import rip.diavlo.base.api.module.Module;
/*     */ import rip.diavlo.base.api.value.Value;
/*     */ import rip.diavlo.base.api.value.impl.ModeValue;
/*     */ import rip.diavlo.base.api.value.impl.NumberValue;
/*     */ import rip.diavlo.base.api.value.impl.StringValue;
/*     */ import rip.diavlo.base.managers.ModuleManager;
/*     */ import rip.diavlo.base.modules.render.ClickGUIModule;
/*     */ import rip.diavlo.base.utils.ChatUtil;
/*     */ import rip.diavlo.base.utils.render.RenderUtil;
/*     */ 
/*     */ public abstract class GUIUtils
/*     */   extends GuiScreen
/*     */   implements GUIColors
/*     */ {
/*  29 */   public final ModuleManager manager = Client.getInstance().getModuleManager();
/*     */   
/*     */   public boolean dragging;
/*     */   
/*     */   public boolean listening;
/*     */   public boolean stringListening;
/*     */   public StringValue selectedStringValue;
/*     */   public Module selectedModule;
/*  37 */   public final double booleanHeight = 11.0D;
/*  38 */   public final double numHeight = 24.0D;
/*  39 */   public final double modeHeight = 12.0D;
/*  40 */   public int white = -3355444;
/*     */   
/*     */   public Category selectedCategory;
/*  43 */   public HashSet<Module> extendedModule = new HashSet<>();
/*     */ 
/*     */   
/*  46 */   public HashSet<NumberValue<Number>> draggingFirst = new HashSet<>();
/*  47 */   public HashSet<NumberValue<Number>> draggingSecond = new HashSet<>();
/*     */   
/*  49 */   public HashSet<NumberValue<Number>> dragSlider = new HashSet<>();
/*     */ 
/*     */ 
/*     */   
/*  53 */   public final CustomFontRenderer icon3_16 = Client.getInstance().getFontManager().getFontBy("Icon3").size(16);
/*  54 */   public final CustomFontRenderer icon3_18 = Client.getInstance().getFontManager().getFontBy("Icon3").size(18);
/*     */   
/*  56 */   public final CustomFontRenderer widthComparator = Client.getInstance().getFontManager().getFontBy("ProductSansRegular").size(28);
/*     */   public double diff;
/*     */   
/*     */   public boolean fullScreen() {
/*  60 */     return (this.mc.isFullScreen() || (!this.mc.isFullScreen() && this.mc.currentScreen.width >= 558 && this.mc.currentScreen.height >= 302));
/*     */   }
/*     */   
/*     */   public CustomFontRenderer big() {
/*  64 */     return Client.getInstance().getFontManager().getFontBy("Nunito").size(fullScreen() ? 20 : 18);
/*     */   }
/*     */   
/*     */   public CustomFontRenderer medium() {
/*  68 */     return Client.getInstance().getFontManager().getFontBy("Nunito").size(fullScreen() ? 18 : 14);
/*     */   }
/*     */   
/*     */   public CustomFontRenderer small() {
/*  72 */     return Client.getInstance().getFontManager().getFontBy("Nunito").size(fullScreen() ? 16 : 14);
/*     */   }
/*     */   
/*     */   public CustomFontRenderer mini() {
/*  76 */     return Client.getInstance().getFontManager().getFontBy("Nunito").size(fullScreen() ? 14 : 12);
/*     */   }
/*     */   
/*     */   public boolean isInsideX(int mouseX, double x, double width) {
/*  80 */     return (mouseX > x && mouseX < x + width);
/*     */   }
/*     */   
/*     */   public boolean isInside(int mouseX, int mouseY, double x, double y, String string, CustomFontRenderer font) {
/*  84 */     return isInside(mouseX, mouseY, x, y, font.width(string), font.height());
/*     */   }
/*     */   
/*     */   public boolean isInside(int mouseX, int mouseY, double x, double y, double width, double height) {
/*  88 */     return (isInsideX(mouseX, x, width) && isInsideY(mouseY, y, height));
/*     */   }
/*     */   
/*     */   public boolean isInsideY(int mouseY, double y, double height) {
/*  92 */     return (mouseY > y && mouseY < y + height);
/*     */   }
/*     */   
/*     */   public ScaledResolution getScaledRes() {
/*  96 */     return new ScaledResolution(this.mc);
/*     */   }
/*     */   
/*     */   public double comp(double min, double max, double val) {
/* 100 */     return Math.min(max, Math.max(min, val));
/*     */   }
/*     */   
/*     */   public static double roundToPlace(double value, int scale) {
/* 104 */     return (new BigDecimal(value)).setScale(scale, RoundingMode.HALF_UP).doubleValue();
/*     */   }
/*     */   
/*     */   public void inside(int mouseX, int mouseY, Module module, boolean inside) {
/* 108 */     if (inside) {
/* 109 */       String description = module.getDescription();
/* 110 */       if (description != null && !description.equals("") && !description.equals("\n")) {
/* 111 */         RenderUtil.roundedRectangle((mouseX + 5), mouseY, mini().width(description) + 7.0D, mini().height() + 2.0D, 2.0D, Color.WHITE);
/* 112 */         RenderUtil.roundedRectangle(mouseX + 5.2D, mouseY + 0.2D, mini().width(description) + 6.6D, mini().height() + 1.6D, 2.0D, BACKGROUND.darker());
/* 113 */         mini().drawString(description, mouseX + 8.2D, mouseY + 2.2D, -1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void numberDraw(int mouseX, double posX, double maxWidth, double offset, Value<?> value, NumberValue<Number> setting) {
/* 120 */     double min = setting.getMin().doubleValue();
/* 121 */     double max = setting.getMax().doubleValue();
/* 122 */     double l = maxWidth - 20.0D;
/* 123 */     double renderWidth = Math.abs(l * (setting.get().doubleValue() - min) / (max - min));
/* 124 */     double maxRenderWidth2 = l * (setting.getMax().doubleValue() - min) / (max - min);
/* 125 */     double diff = Math.min(l, Math.max(0.0D, mouseX - posX + 6.0D));
/* 126 */     double d = Math.abs(maxRenderWidth2 - renderWidth);
/* 127 */     if (this.dragSlider.contains(setting)) {
/* 128 */       if (diff == 0.0D) {
/* 129 */         setting.setValue(setting.getMin());
/*     */       } else {
/* 131 */         double newValue = roundToPlace(diff / l * (max - min) + min, setting.getIncrement().intValue());
/* 132 */         setting.setValue(Double.valueOf(newValue));
/*     */       } 
/*     */     }
/*     */     
/* 136 */     RenderUtil.rectangle(posX + 9.0D, offset + 16.0D, renderWidth, 1.0D, textColor().darker());
/* 137 */     RenderUtil.rectangle(posX + 9.0D + renderWidth, offset + 16.0D, d, 1.0D, textColor());
/* 138 */     RenderUtil.roundedRectangle(posX + 5.0D + renderWidth, offset + 13.0D, 8.0D, 8.0D, 5.0D, Color.WHITE);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     small().drawString(value.getName() + ": ", posX + 6.0D, offset + 3.45D, this.white);
/* 144 */     small().drawString(String.valueOf(value.getValue()), posX + 6.0D + small().width(value.getName() + ": "), offset + 3.45D, this.white);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void moduleClick(int mouseButton, boolean left, boolean right, List<Module> modules, Module module) {
/* 200 */     if (left) {
/* 201 */       module.toggle();
/*     */       
/*     */       return;
/*     */     } 
/* 205 */     if (right) {
/* 206 */       modules.forEach(m -> this.extendedModule.remove(m));
/* 207 */       this.extendedModule.add(module);
/*     */       
/*     */       return;
/*     */     } 
/* 211 */     if (mouseButton == 2) {
/* 212 */       this.selectedModule = module;
/* 213 */       this.listening = true;
/* 214 */       ChatUtil.display("Waiting keybind for " + module.getName());
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean numberClick(int mouseX, int mouseY, double maxWidth, double posX, boolean left, double offset, NumberValue<Number> setting) {
/* 219 */     double min = setting.getMin().doubleValue();
/* 220 */     double max = setting.getMax().doubleValue();
/* 221 */     double renderWidth2 = (maxWidth - 20.0D) * (setting.getMax().doubleValue() - min) / (max - min);
/* 222 */     if (isInside(mouseX, mouseY, posX + 6.0D, offset + 13.0D, renderWidth2, 10.0D) && left) {
/* 223 */       this.dragSlider.add(setting);
/* 224 */       return true;
/*     */     } 
/* 226 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean modeClick(int mouseX, int mouseY, boolean left, boolean right, double offset, Value<?> value, double posX, ModeValue setting) {
/* 264 */     String mode = null;
/* 265 */     String s_mode = (String)setting.getValue();
/* 266 */     String name = value.getName() + ": ";
/* 267 */     if (isInside(mouseX, mouseY, posX + 1.0D, offset, 36.0D + small().width(s_mode) + small().width(name), small().height())) {
/* 268 */       int index = setting.getChoices().indexOf(setting.get());
/* 269 */       int maxIndex = setting.getChoices().size();
/* 270 */       if (left) {
/* 271 */         if (maxIndex <= index + 1) {
/* 272 */           mode = setting.getChoices().get(0);
/*     */         } else {
/* 274 */           mode = setting.getChoices().get(index + 1);
/*     */         } 
/* 276 */       } else if (right) {
/* 277 */         if (0 > index - 1) {
/* 278 */           mode = setting.getChoices().get(maxIndex - 1);
/*     */         } else {
/* 280 */           mode = setting.getChoices().get(index - 1);
/*     */         } 
/*     */       } 
/*     */       
/* 284 */       setting.setValue(mode);
/* 285 */       return true;
/*     */     } 
/* 287 */     return false;
/*     */   }
/*     */   
/*     */   public boolean stringClick(int mouseX, int mouseY, boolean left, boolean right, double offset, double posX, StringValue setting) {
/* 291 */     String name = setting.getName();
/* 292 */     String mode = (String)setting.get();
/* 293 */     if (isInside(mouseX, mouseY, posX + 6.0D, offset, 36.0D + small().width(mode) + small().width(name), small().height())) {
/* 294 */       if (!this.stringListening) {
/* 295 */         this.selectedStringValue = setting;
/* 296 */         this.stringListening = true;
/* 297 */       } else if (left) {
/* 298 */         this.selectedStringValue = null;
/* 299 */         this.stringListening = false;
/*     */       } 
/* 301 */       if (right) {
/* 302 */         setting.setValue("");
/*     */       }
/* 304 */       return true;
/*     */     } 
/* 306 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double drawList(double posX, double offset, Value<?> value, String mode, Color color, CustomFontRenderer font) {
/* 333 */     String name = value.getName() + ": ";
/* 334 */     font.drawString(name, posX + 6.0D, offset + 3.0D, this.white);
/* 335 */     font.drawString(mode, posX + 6.0D + font.width(name), offset + 3.0D, color.getRGB());
/* 336 */     return offset + 12.0D;
/*     */   }
/*     */   
/*     */   public double drawList(double posX, double offset, Value<?> value, String mode) {
/* 340 */     return drawList(posX, offset, value, mode, Color.WHITE, small());
/*     */   }
/*     */   
/*     */   public Color textColor() {
/* 344 */     return GUIColors.TEXT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doesGuiPauseGame() {
/* 350 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 355 */     super.keyTyped(typedChar, keyCode);
/* 356 */     if (this.stringListening) {
/* 357 */       String value = (String)this.selectedStringValue.get();
/* 358 */       if (keyCode == 28) {
/* 359 */         ChatUtil.display("New value for " + this.selectedStringValue.getName() + ": " + value);
/* 360 */         this.stringListening = false;
/* 361 */         this.selectedStringValue = null;
/*     */         return;
/*     */       } 
/* 364 */       if (keyCode == 14) {
/* 365 */         if (value.length() == 0)
/* 366 */           return;  String newValue = value.substring(0, value.length() - 1);
/* 367 */         if (newValue.length() == 0) {
/* 368 */           this.selectedStringValue.setValue("");
/*     */           return;
/*     */         } 
/* 371 */         this.selectedStringValue.setValue(newValue);
/*     */         return;
/*     */       } 
/* 374 */       if (Character.isLetterOrDigit(typedChar)) {
/* 375 */         this.selectedStringValue.setValue(value + typedChar);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleKeyboardInput() {
/*     */     try {
/* 383 */       super.handleKeyboardInput();
/* 384 */       int key = Keyboard.getEventKey();
/* 385 */       if (this.listening)
/* 386 */       { this.listening = false;
/* 387 */         if (key == 1) {
/* 388 */           ChatUtil.display("Exit from key binding"); return;
/*     */         } 
/* 390 */         if (key == 211) {
/* 391 */           ChatUtil.display("Unbound '" + this.selectedModule.getName() + "'");
/* 392 */           key = 0;
/*     */         } else {
/* 394 */           ChatUtil.display("Bound '" + this.selectedModule.getName() + "' to '" + Keyboard.getKeyName(key) + "'");
/* 395 */         }  this.selectedModule.setKey(key); }
/*     */       
/* 397 */       else if (key == ((ClickGUIModule)this.manager.get(ClickGUIModule.class)).getKey() && this.mc.currentScreen != null) { this.mc.currentScreen.onGuiClosed(); }
/*     */     
/*     */     } catch (Throwable $ex) {
/*     */       throw $ex;
/*     */     }  } public void onGuiClosed() {
/* 402 */     Client.getInstance().getEventBus().unregister(this);
/* 403 */     super.onGuiClosed();
/* 404 */     ((ClickGUIModule)this.manager.get(ClickGUIModule.class)).setToggled(false);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\ui\GUIUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
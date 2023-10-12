/*     */ package rip.diavlo.base.ui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.api.module.Category;
/*     */ import rip.diavlo.base.api.module.Module;
/*     */ import rip.diavlo.base.api.value.Value;
/*     */ import rip.diavlo.base.api.value.impl.BooleanValue;
/*     */ import rip.diavlo.base.api.value.impl.ModeValue;
/*     */ import rip.diavlo.base.api.value.impl.NumberValue;
/*     */ import rip.diavlo.base.api.value.impl.StringValue;
/*     */ import rip.diavlo.base.utils.ChatUtil;
/*     */ import rip.diavlo.base.utils.render.RenderUtil;
/*     */ 
/*     */ public class NewUI
/*     */   extends GUIUtils {
/*  24 */   private final HashMap<Category, Double> posX = new HashMap<>();
/*  25 */   private final HashMap<Category, Double> posY = new HashMap<>();
/*  26 */   private final HashMap<Category, Double> dragX = new HashMap<>();
/*  27 */   private final HashMap<Category, Double> dragY = new HashMap<>();
/*     */   
/*  29 */   private final double width = 104.0D;
/*  30 */   private final double moduleHeight = 18.0D;
/*     */   
/*  32 */   private final double booleanHeight = 18.0D;
/*     */   
/*     */   private Category s;
/*  35 */   private final ArrayList<Category> extendedCategory = new ArrayList<>();
/*     */   
/*     */   public NewUI() {
/*  38 */     this.dragging = false;
/*  39 */     this.draggingFirst.clear();
/*  40 */     this.draggingSecond.clear();
/*  41 */     this.listening = false;
/*  42 */     this.stringListening = false;
/*  43 */     this.selectedCategory = Category.COMBAT;
/*  44 */     int i = 0;
/*  45 */     for (Category category : Category.categories()) {
/*  46 */       this.posX.put(category, Double.valueOf(10.4D + 110.9D * i));
/*  47 */       this.posY.put(category, Double.valueOf(69.0D));
/*  48 */       i++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  54 */     Client.getInstance().getEventBus().register(this);
/*  55 */     super.initGui();
/*  56 */     this.listening = false;
/*  57 */     this.stringListening = false;
/*  58 */     this.dragging = false;
/*  59 */     this.selectedModule = null;
/*  60 */     this.dragSlider.clear();
/*  61 */     this.draggingFirst.clear();
/*  62 */     this.draggingSecond.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  67 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*  68 */     if (this.dragging && 
/*  69 */       this.selectedCategory != null && this.dragX.containsKey(this.selectedCategory) && this.dragY.containsKey(this.selectedCategory)) {
/*  70 */       this.posX.put(this.selectedCategory, Double.valueOf(mouseX - ((Double)this.dragX.get(this.selectedCategory)).doubleValue()));
/*  71 */       this.posY.put(this.selectedCategory, Double.valueOf(mouseY - ((Double)this.dragY.get(this.selectedCategory)).doubleValue()));
/*     */     } 
/*     */     
/*  74 */     Module desc = null;
/*  75 */     for (Category category : Category.categories()) {
/*  76 */       List<Module> modules = (List<Module>)this.manager.get(category).stream().sorted(Comparator.comparingDouble(module -> -this.widthComparator.width(module.getName()))).collect(Collectors.toCollection(ArrayList::new));
/*  77 */       double posX = ((Double)this.posX.get(category)).doubleValue();
/*  78 */       double posY = ((Double)this.posY.get(category)).doubleValue();
/*     */       
/*  80 */       double mainHeight = 18.0D;
/*  81 */       for (Module module : modules) {
/*  82 */         if (!this.extendedCategory.contains(category))
/*  83 */           continue;  mainHeight = getHeight(mainHeight, module);
/*     */       } 
/*     */ 
/*     */       
/*  87 */       RenderUtil.roundedRectangle(posX, posY, 104.0D, mainHeight + 5.0D, 3.0D, BACKGROUND);
/*  88 */       RenderUtil.roundedRectangle(posX + 1.0D, posY + 1.0D, 102.0D, mainHeight + 3.0D, 3.0D, BACKGROUND.brighter());
/*     */ 
/*     */       
/*  91 */       big().drawCenteredString(category.getLabel(), posX + 52.0D, posY + 9.0D - 2.0D, TEXT.getRGB());
/*     */ 
/*     */ 
/*     */       
/*  95 */       if (!this.extendedCategory.contains(category))
/*     */         continue; 
/*  97 */       double mainOffSet = posY + 18.0D;
/*     */       
/*  99 */       for (Module module : modules) {
/* 100 */         boolean inside = isInside(mouseX, mouseY, posX, mainOffSet, 104.0D, 18.0D);
/* 101 */         mainOffSet = getOffSet(module, posX, mainOffSet, inside);
/* 102 */         if (inside) desc = module; 
/* 103 */         if (this.extendedModule.contains(module)) {
/* 104 */           mainOffSet = drawSettings(mouseX, posX, posY, module, mainOffSet);
/*     */         }
/*     */       } 
/* 107 */       if (desc != null) {
/* 108 */         String description = desc.getDescription();
/* 109 */         if (description != null && !description.equals("") && !description.equals("\n")) {
/* 110 */           RenderUtil.roundedRectangle((mouseX + 5), mouseY, mini().width(description) + 7.0D, mini().height() + 2.0D, 2.0D, Color.WHITE);
/* 111 */           RenderUtil.roundedRectangle(mouseX + 5.2D, mouseY + 0.2D, mini().width(description) + 6.6D, mini().height() + 1.6D, 2.0D, BACKGROUND.darker());
/* 112 */           mini().drawString(description, mouseX + 8.2D, mouseY + 2.2D, -1);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T extends Number> double drawSettings(int mouseX, double posX, double posY, Module module, double offset) {
/* 119 */     for (Value<?> value : (Iterable<Value<?>>)module.getAllValues()) {
/* 120 */       if (Value.hideSetting(value))
/* 121 */         continue;  if (value instanceof BooleanValue) {
/* 122 */         BooleanValue setting = (BooleanValue)value;
/*     */         
/* 124 */         RenderUtil.roundedRectangle(posX + 1.25D, offset + 1.0D, 101.5D, 18.0D, 4.0D, MODULEBACK);
/* 125 */         RenderUtil.roundedCenteredRectangle(posX + 104.0D - 2.0D, offset + 6.5D, 20.0D, 10.0D, 5.0D, TEXT);
/* 126 */         RenderUtil.roundedCenteredRectangle(posX + 104.0D - 2.0D + 0.125D, offset + 6.875D, 19.635D, 9.635D, 5.0D, MODULEBACK.brighter());
/*     */ 
/*     */ 
/*     */         
/* 130 */         RenderUtil.roundedRectangle(posX + 104.0D + 5.0D + (!((Boolean)setting.getValue()).booleanValue() ? -25 : -15), offset + 8.0D, 6.0D, 6.0D, 4.0D, !((Boolean)setting.get()).booleanValue() ? TEXT : textColor());
/* 131 */         small().drawString(value.getName(), posX + 10.0D, offset + 1.0D + small().height() / 2.0D, TEXT.getRGB());
/* 132 */         offset += 18.0D; continue;
/* 133 */       }  if (value instanceof NumberValue) {
/* 134 */         NumberValue<Number> setting = (NumberValue)value;
/*     */         
/* 136 */         RenderUtil.roundedRectangle(posX + 1.25D, offset, 101.5D, 24.0D, 1.0D, MODULEBACK);
/* 137 */         numberDraw(mouseX, posX, 104.0D, offset, value, setting);
/* 138 */         offset += 24.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 150 */       if (value instanceof ModeValue) {
/*     */         
/* 152 */         RenderUtil.roundedRectangle(posX + 1.25D, offset, 101.5D, 12.0D, 1.0D, MODULEBACK);
/* 153 */         offset = drawList(posX, offset, value, (String)((ModeValue)value).get()); continue;
/* 154 */       }  if (value instanceof StringValue) {
/* 155 */         StringValue setting = (StringValue)value;
/*     */         
/* 157 */         RenderUtil.roundedRectangle(posX + 1.25D, offset, 101.5D, 12.0D, 1.0D, MODULEBACK);
/* 158 */         offset = (setting == this.selectedStringValue) ? drawList(posX, offset, value, (String)setting.get(), textColor(), small()) : drawList(posX, offset, value, (String)setting.get());
/*     */       } 
/*     */     } 
/* 161 */     return offset;
/*     */   }
/*     */   
/*     */   private double getOffSet(Module module, double posX, double offSet, boolean inside) {
/* 165 */     if (inside && !module.isToggled()) {
/* 166 */       RenderUtil.roundedRectangle(posX, offSet, 104.0D, 18.0D, 1.0D, BACKGROUND.darker());
/*     */     }
/* 168 */     if (module.isToggled()) {
/* 169 */       RenderUtil.roundedRectangle(posX + 3.0D, offSet, 98.0D, 18.0D, 4.0D, ACTIVE);
/*     */     }
/* 171 */     small().drawString(module.getName(), posX + 5.0D, offSet + 9.0D - 5.0D, module.isToggled() ? Color.WHITE.getRGB() : TEXT.getRGB());
/* 172 */     String keyName = Keyboard.getKeyName(module.getKey());
/* 173 */     if (this.listening && module == this.selectedModule) keyName = "[   ]"; 
/* 174 */     if (!keyName.equalsIgnoreCase("none")) {
/*     */       
/* 176 */       small().drawCenteredString(keyName, posX + 104.0D - keyWidth(keyName) / 2.0D - 7.0D, offSet + 9.0D, TEXT.getRGB());
/* 177 */       if (this.listening && module == this.selectedModule) RenderUtil.rectangle(posX + 104.0D - keyWidth(keyName) - 1.0D, offSet + 7.0D, fullScreen() ? 4.0D : 3.0D, fullScreen() ? 4.0D : 3.0D, TEXT); 
/*     */     } 
/* 179 */     return offSet + 18.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   private double keyWidth(String key) {
/* 184 */     return small().width(key) + 4.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state) {
/* 189 */     super.mouseReleased(mouseX, mouseY, state);
/* 190 */     this.dragging = false;
/* 191 */     this.draggingFirst.clear();
/* 192 */     this.draggingSecond.clear();
/* 193 */     this.dragSlider.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/*     */     try {
/* 199 */       super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */       
/* 201 */       for (Category category : Category.categories()) {
/* 202 */         List<Module> modules = (List<Module>)this.manager.get(category).stream().sorted(Comparator.comparingDouble(module -> -this.widthComparator.width(module.getName()))).collect(Collectors.toCollection(ArrayList::new));
/* 203 */         double posX = ((Double)this.posX.get(category)).doubleValue();
/* 204 */         double posY = ((Double)this.posY.get(category)).doubleValue();
/* 205 */         boolean left = (mouseButton == 0);
/* 206 */         boolean right = (mouseButton == 1);
/* 207 */         if (isInside(mouseX, mouseY, posX, posY, 104.0D, 18.0D)) {
/* 208 */           this.selectedCategory = category;
/*     */           
/* 210 */           if (left) {
/* 211 */             this.dragging = true;
/* 212 */             this.dragX.put(category, Double.valueOf(mouseX - posX));
/* 213 */             this.dragY.put(category, Double.valueOf(mouseY - posY));
/*     */             
/*     */             return;
/*     */           } 
/* 217 */           if (right) {
/* 218 */             if (!this.extendedCategory.contains(category)) {
/* 219 */               this.extendedCategory.add(category);
/*     */               return;
/*     */             } 
/* 222 */             this.extendedCategory.remove(category);
/*     */           } 
/*     */           return;
/*     */         } 
/* 226 */         if (!this.extendedCategory.contains(category))
/* 227 */           continue;  double mainOffSet = posY + 18.0D;
/* 228 */         for (Module module : modules) {
/* 229 */           if (moduleClick(mouseX, mouseY, mouseButton, posX, left, right, mainOffSet, module))
/* 230 */             return;  mainOffSet += 18.0D;
/* 231 */           if (this.extendedModule.contains(module)) {
/* 232 */             ClickResult result = clickSetting(mouseX, mouseY, posX, left, right, mainOffSet, module);
/* 233 */             if (result.finish())
/* 234 */               return;  mainOffSet = result.offset();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } catch (Throwable $ex) {
/*     */       throw $ex;
/*     */     } 
/*     */   } private ClickResult clickSetting(int mouseX, int mouseY, double posX, boolean left, boolean right, double offset, Module module) {
/* 242 */     for (Value<?> value : (Iterable<Value<?>>)module.getAllValues()) {
/* 243 */       if (Value.hideSetting(value))
/* 244 */         continue;  if (value instanceof BooleanValue) {
/* 245 */         if (isInside(mouseX, mouseY, posX + 1.25D, offset + 1.0D, 101.5D, 18.0D)) {
/* 246 */           ((BooleanValue)value).toggle();
/* 247 */           return new ClickResult(offset, true);
/*     */         } 
/* 249 */         offset += 18.0D; continue;
/* 250 */       }  if (value instanceof ModeValue) {
/* 251 */         if (modeClick(mouseX, mouseY, left, right, offset, value, posX, (ModeValue)value))
/* 252 */           return new ClickResult(offset, true); 
/* 253 */         offset += 12.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 264 */       if (value instanceof NumberValue) {
/* 265 */         if (numberClick(mouseX, mouseY, 104.0D, posX, left, offset, (NumberValue)value))
/* 266 */           return new ClickResult(offset, true); 
/* 267 */         offset += 24.0D; continue;
/* 268 */       }  if (value instanceof StringValue) {
/* 269 */         if (stringClick(mouseX, mouseY, left, right, offset, posX, (StringValue)value))
/* 270 */           return new ClickResult(offset, true); 
/* 271 */         offset += 12.0D;
/*     */       } 
/*     */     } 
/* 274 */     return new ClickResult(offset, false);
/*     */   }
/*     */   
/*     */   private boolean moduleClick(int mouseX, int mouseY, int mouseButton, double posX, boolean left, boolean right, double offSet, Module module) {
/* 278 */     if (isInside(mouseX, mouseY, posX + 1.0D, offSet + 1.0D, 102.0D, 16.0D)) {
/*     */       
/* 280 */       if (left) {
/* 281 */         module.toggle();
/* 282 */         return true;
/*     */       } 
/*     */       
/* 285 */       if (right) {
/* 286 */         if (!this.extendedModule.contains(module) && module.getAllValues().size() > 0) {
/* 287 */           this.extendedModule.add(module);
/* 288 */           return true;
/*     */         } 
/* 290 */         this.extendedModule.remove(module);
/* 291 */         return true;
/*     */       } 
/*     */       
/* 294 */       if (mouseButton == 2) {
/* 295 */         this.selectedModule = module;
/* 296 */         this.listening = true;
/* 297 */         ChatUtil.display("Waiting keybind for " + module.getName());
/* 298 */         return true;
/*     */       } 
/* 300 */       return true;
/*     */     } 
/* 302 */     return false;
/*     */   }
/*     */   
/*     */   public double getHeight(double height, Module module) {
/* 306 */     height += 18.0D;
/* 307 */     if (!this.extendedModule.contains(module)) return height; 
/* 308 */     if (module.getAllValues() == null) return height; 
/* 309 */     for (Value<?> value : (Iterable<Value<?>>)module.getAllValues()) {
/* 310 */       if (Value.hideSetting(value))
/* 311 */         continue;  if (value instanceof BooleanValue) {
/* 312 */         height += 18.0D; continue;
/* 313 */       }  if (value instanceof StringValue || value instanceof ModeValue) {
/* 314 */         height += 12.0D; continue;
/* 315 */       }  if (value instanceof NumberValue) {
/* 316 */         height += 24.0D;
/*     */       }
/*     */     } 
/* 319 */     return height;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\ui\NewUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
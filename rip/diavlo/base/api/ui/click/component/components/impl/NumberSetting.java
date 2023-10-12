/*     */ package rip.diavlo.base.api.ui.click.component.components.impl;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.RoundingMode;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.api.ui.click.component.Component;
/*     */ import rip.diavlo.base.api.ui.click.component.components.Button;
/*     */ import rip.diavlo.base.api.value.impl.NumberValue;
/*     */ import rip.diavlo.base.modules.render.ClickGUI;
/*     */ 
/*     */ public class NumberSetting
/*     */   extends Component
/*     */ {
/*  19 */   private final FontRenderer fontRenderer = (Minecraft.getMinecraft()).fontRendererObj;
/*     */   
/*     */   private boolean hovered;
/*     */   
/*     */   private NumberValue set;
/*     */   
/*     */   private Button parent;
/*     */   private int offset;
/*     */   private int x;
/*     */   private int y;
/*     */   private boolean dragging = false;
/*     */   private double renderWidth;
/*     */   
/*     */   public NumberSetting(NumberValue value, Button button, int offset) {
/*  33 */     this.set = value;
/*  34 */     this.parent = button;
/*  35 */     this.x = button.parent.getX() + button.parent.getWidth();
/*  36 */     this.y = button.parent.getY() + button.offset;
/*  37 */     this.offset = offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderComponent() {
/*  42 */     switch ((String)((ClickGUI)Client.getInstance().getModuleManager().get(ClickGUI.class)).mode.getValue()) {
/*     */       case "Normal":
/*  44 */         Gui.drawRect((this.parent.parent.getX() + 2), (this.parent.parent.getY() + this.offset), (this.parent.parent.getX() + this.parent.parent.getWidth()), (this.parent.parent.getY() + this.offset + 12), this.hovered ? -14540254 : -15658735);
/*     */         
/*  46 */         Gui.drawRect((this.parent.parent.getX() + 2), (this.parent.parent.getY() + this.offset), (this.parent.parent.getX() + (int)this.renderWidth), (this.parent.parent.getY() + this.offset + 12), this.hovered ? -11184811 : -12303292);
/*  47 */         Gui.drawRect(this.parent.parent.getX(), (this.parent.parent.getY() + this.offset), (this.parent.parent.getX() + 2), (this.parent.parent.getY() + this.offset + 12), -15658735);
/*  48 */         GL11.glPushMatrix();
/*  49 */         GL11.glScalef(0.5F, 0.5F, 0.5F);
/*  50 */         this.fontRenderer.drawStringWithShadow("§7" + this.set.getName() + ": §f" + this.set.getValue(), (this.parent.parent.getX() * 2 + 15), ((this.parent.parent.getY() + this.offset + 2) * 2 + 5), -1);
/*     */         
/*  52 */         GL11.glPopMatrix();
/*     */         break;
/*     */       case "MetaClient":
/*  55 */         Gui.drawRect((this.parent.parent.getX() + 2), (this.parent.parent.getY() + this.offset), (this.parent.parent.getX() + this.parent.parent.getWidth()), (this.parent.parent.getY() + this.offset + 12), this.hovered ? -14540254 : 1073741824);
/*  56 */         Gui.drawRect((this.parent.parent.getX() + 2), (this.parent.parent.getY() + this.offset), (this.parent.parent.getX() + (int)this.renderWidth), (this.parent.parent.getY() + this.offset + 12), this.hovered ? -11184811 : 1073741824);
/*  57 */         Gui.drawRect(this.parent.parent.getX(), (this.parent.parent.getY() + this.offset), (this.parent.parent.getX() + 2), (this.parent.parent.getY() + this.offset + 12), 1073741824);
/*  58 */         GL11.glPushMatrix();
/*  59 */         GL11.glScalef(0.5F, 0.5F, 0.5F);
/*  60 */         this.fontRenderer.drawStringWithShadow(this.set.getName() + ": " + this.set.getValue(), (this.parent.parent.getX() * 2 + 15), ((this.parent.parent.getY() + this.offset + 2) * 2 + 5), -1);
/*  61 */         GL11.glPopMatrix();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOff(int newOff) {
/*  69 */     this.offset = newOff;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateComponent(int mouseX, int mouseY) {
/*  74 */     this.hovered = (isMouseOnButtonD(mouseX, mouseY) || isMouseOnButtonI(mouseX, mouseY));
/*  75 */     this.y = this.parent.parent.getY() + this.offset;
/*  76 */     this.x = this.parent.parent.getX();
/*     */     
/*  78 */     double diff = Math.min(88, Math.max(0, mouseX - this.x));
/*     */     
/*  80 */     double min = this.set.getMin().doubleValue();
/*  81 */     double max = this.set.getMax().doubleValue();
/*     */     
/*  83 */     this.renderWidth = 88.0D * (this.set.getValue().doubleValue() - min) / (max - min);
/*     */     
/*  85 */     if (this.dragging)
/*  86 */       if (diff == 0.0D) {
/*  87 */         this.set.setValue(this.set.getMin());
/*     */       } else {
/*     */         
/*  90 */         double newValue = roundToPlace(diff / 88.0D * (max - min) + min, 2);
/*  91 */         if (newValue <= max) {
/*  92 */           setValue(Double.valueOf(newValue));
/*     */         }
/*     */       }  
/*     */   }
/*     */   
/*     */   private static double roundToPlace(double value, int places) {
/*  98 */     if (places < 0) {
/*  99 */       throw new IllegalArgumentException();
/*     */     }
/* 101 */     BigDecimal bd = new BigDecimal(value);
/* 102 */     bd = bd.setScale(places, RoundingMode.HALF_UP);
/* 103 */     return bd.doubleValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int button) {
/* 108 */     if (isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open) {
/* 109 */       this.dragging = true;
/*     */     }
/* 111 */     if (isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open) {
/* 112 */       this.dragging = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
/* 117 */     this.dragging = false;
/*     */   }
/*     */   
/*     */   private double snapToStep(double value, double valueStep) {
/* 121 */     if (valueStep > 0.0D) {
/* 122 */       value = valueStep * Math.round(value / valueStep);
/*     */     }
/* 124 */     return value;
/*     */   }
/*     */   
/*     */   private void setValue(Number value) {
/* 128 */     if (this.set.getIncrement() instanceof Double) {
/* 129 */       this.set.setValue(Double.valueOf(MathHelper.clamp_double(snapToStep(value.doubleValue(), this.set.getIncrement().doubleValue()), this.set.getMin().doubleValue(), this.set.getMax().doubleValue())));
/*     */     }
/* 131 */     if (this.set.getIncrement() instanceof Integer) {
/* 132 */       this.set.setValue(Integer.valueOf(MathHelper.clamp_int((int)snapToStep(value.doubleValue(), this.set.getIncrement().doubleValue()), this.set.getMin().intValue(), this.set.getMax().intValue())));
/*     */     }
/* 134 */     if (this.set.getIncrement() instanceof Float) {
/* 135 */       this.set.setValue(Float.valueOf(MathHelper.clamp_float((float)snapToStep(value.doubleValue(), this.set.getIncrement().doubleValue()), this.set.getMin().floatValue(), this.set.getMax().floatValue())));
/*     */     }
/* 137 */     if (this.set.getIncrement() instanceof Long) {
/* 138 */       this.set.setValue(Integer.valueOf(MathHelper.clamp_int((int)snapToStep(value.doubleValue(), this.set.getIncrement().doubleValue()), this.set.getMin().intValue(), this.set.getMax().intValue())));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isMouseOnButtonD(int x, int y) {
/* 143 */     if (x > this.x && x < this.x + this.parent.parent.getWidth() / 2 + 1 && y > this.y && y < this.y + 12) {
/* 144 */       return true;
/*     */     }
/* 146 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isMouseOnButtonI(int x, int y) {
/* 150 */     if (x > this.x + this.parent.parent.getWidth() / 2 && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + 12) {
/* 151 */       return true;
/*     */     }
/* 153 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\ap\\ui\click\component\components\impl\NumberSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package rip.diavlo.base.api.ui.click.component.components;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.api.module.Module;
/*     */ import rip.diavlo.base.api.ui.click.component.Component;
/*     */ import rip.diavlo.base.api.ui.click.component.Frame;
/*     */ import rip.diavlo.base.api.ui.click.component.components.impl.BooleanSetting;
/*     */ import rip.diavlo.base.api.ui.click.component.components.impl.ModeSetting;
/*     */ import rip.diavlo.base.api.ui.click.component.components.impl.NumberSetting;
/*     */ import rip.diavlo.base.api.ui.click.component.components.impl.StringSetting;
/*     */ import rip.diavlo.base.api.value.Value;
/*     */ import rip.diavlo.base.api.value.impl.BooleanValue;
/*     */ import rip.diavlo.base.api.value.impl.ModeValue;
/*     */ import rip.diavlo.base.api.value.impl.NumberValue;
/*     */ import rip.diavlo.base.api.value.impl.StringValue;
/*     */ import rip.diavlo.base.modules.render.ClickGUI;
/*     */ 
/*     */ public class Button extends Component {
/*  23 */   private final FontRenderer fontRenderer = (Minecraft.getMinecraft()).fontRendererObj;
/*     */   
/*     */   public Module mod;
/*     */   public Frame parent;
/*     */   public int offset;
/*     */   private boolean isHovered;
/*     */   private ArrayList<Component> subcomponents;
/*     */   public boolean open;
/*     */   private int height;
/*     */   
/*     */   public Button(Module mod, Frame parent, int offset) {
/*  34 */     this.mod = mod;
/*  35 */     this.parent = parent;
/*  36 */     this.offset = offset;
/*  37 */     this.subcomponents = new ArrayList<>();
/*  38 */     this.open = false;
/*  39 */     this.height = 12;
/*  40 */     int opY = offset + 12;
/*     */     
/*  42 */     int finalOpY = opY;
/*     */     
/*  44 */     Client.getInstance().getValueManager().getValuesFromOwner(mod).forEach(setting -> {
/*     */           if (setting instanceof BooleanValue) {
/*     */             this.subcomponents.add(new BooleanSetting((BooleanValue)setting, this, finalOpY));
/*     */           }
/*     */           
/*     */           if (setting instanceof ModeValue) {
/*     */             this.subcomponents.add(new ModeSetting((ModeValue)setting, this, mod, finalOpY));
/*     */           }
/*     */           if (setting instanceof NumberValue) {
/*     */             this.subcomponents.add(new NumberSetting((NumberValue)setting, this, finalOpY));
/*     */           }
/*     */           if (setting instanceof StringValue) {
/*     */             this.subcomponents.add(new StringSetting((StringValue)setting, this, finalOpY));
/*     */           }
/*     */         });
/*  59 */     this.subcomponents.add(new KeybindSetting(this, opY));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOff(int newOff) {
/*  64 */     this.offset = newOff;
/*  65 */     int opY = this.offset + 12;
/*  66 */     for (Component comp : this.subcomponents) {
/*  67 */       comp.setOff(opY);
/*  68 */       opY += 12;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderComponent() {
/*  74 */     switch ((String)((ClickGUI)Client.getInstance().getModuleManager().get(ClickGUI.class)).mode.getValue()) {
/*     */       case "Normal":
/*  76 */         Gui.drawRect(this.parent.getX(), (this.parent.getY() + this.offset), (this.parent.getX() + this.parent.getWidth()), (this.parent.getY() + 12 + this.offset), this.isHovered ? (this.mod.isToggled() ? (new Color(570425344, true)).darker().getRGB() : 570425344) : (this.mod.isToggled() ? (new Color(335544320, true)).getRGB() : 285212672));
/*  77 */         GL11.glPushMatrix();
/*  78 */         GL11.glScalef(0.5F, 0.5F, 0.5F);
/*  79 */         this.fontRenderer.drawStringWithShadow(this.mod.getName(), ((this.parent.getX() + 2) * 2), ((this.parent.getY() + this.offset + 2) * 2 + 4), this.mod.isToggled() ? 10066329 : -1);
/*  80 */         if (this.subcomponents.size() > 2)
/*  81 */           this.fontRenderer.drawStringWithShadow(this.open ? "-" : "+", ((this.parent.getX() + this.parent.getWidth() - 10) * 2), ((this.parent.getY() + this.offset + 2) * 2 + 4), -1); 
/*  82 */         GL11.glPopMatrix();
/*  83 */         if (this.open && 
/*  84 */           !this.subcomponents.isEmpty()) {
/*  85 */           for (Component comp : this.subcomponents) {
/*  86 */             comp.renderComponent();
/*     */           }
/*     */         }
/*     */         break;
/*     */       case "MetaClient":
/*  91 */         Gui.drawRect(this.parent.getX(), (this.parent.getY() + this.offset), (this.parent.getX() + this.parent.getWidth()), (this.parent.getY() + 12 + this.offset), this.isHovered ? (this.mod.isToggled() ? (new Color(570425344, true)).darker().getRGB() : 570425344) : (this.mod.isToggled() ? (new Color(335544320, true)).getRGB() : 285212672));
/*  92 */         GL11.glPushMatrix();
/*  93 */         GL11.glScalef(0.5F, 0.5F, 0.5F);
/*  94 */         this.fontRenderer.drawStringWithShadow(this.mod.getName(), ((this.parent.getX() + 2) * 2), ((this.parent.getY() + this.offset + 2) * 2 + 4), this.mod.isToggled() ? 10215248 : -1);
/*  95 */         this.fontRenderer.drawStringWithShadow(this.open ? "-" : "+", ((this.parent.getX() + this.parent.getWidth() - 10) * 2), ((this.parent.getY() + this.offset + 2) * 2 + 4), -1);
/*  96 */         GL11.glPopMatrix();
/*  97 */         if (this.open && !this.subcomponents.isEmpty()) {
/*  98 */           for (Component component : this.subcomponents) {
/*  99 */             component.renderComponent();
/*     */           }
/*     */         }
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 109 */     if (this.open) {
/* 110 */       return 12 * (this.subcomponents.size() + 1);
/*     */     }
/* 112 */     return 12;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateComponent(int mouseX, int mouseY) {
/* 117 */     this.isHovered = isMouseOnButton(mouseX, mouseY);
/* 118 */     if (!this.subcomponents.isEmpty()) {
/* 119 */       for (Component comp : this.subcomponents) {
/* 120 */         comp.updateComponent(mouseX, mouseY);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int button) {
/* 126 */     if (isMouseOnButton(mouseX, mouseY) && button == 0) {
/* 127 */       this.mod.toggle();
/*     */     }
/* 129 */     if (isMouseOnButton(mouseX, mouseY) && button == 1) {
/* 130 */       this.open = !this.open;
/* 131 */       this.parent.refresh();
/*     */     } 
/* 133 */     for (Component comp : this.subcomponents) {
/* 134 */       comp.mouseClicked(mouseX, mouseY, button);
/*     */     }
/*     */   }
/*     */   
/*     */   public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
/* 139 */     for (Component comp : this.subcomponents) {
/* 140 */       comp.mouseReleased(mouseX, mouseY, mouseButton);
/*     */     }
/*     */   }
/*     */   
/*     */   public void keyTyped(char typedChar, int key) {
/* 145 */     for (Component comp : this.subcomponents)
/* 146 */       comp.keyTyped(typedChar, key); 
/*     */   }
/*     */   
/*     */   public boolean isMouseOnButton(int x, int y) {
/* 150 */     if (x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset)
/* 151 */       return true; 
/* 152 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\ap\\ui\click\component\components\Button.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
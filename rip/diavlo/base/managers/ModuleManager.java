/*     */ package rip.diavlo.base.managers;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.api.font.CustomFontRenderer;
/*     */ import rip.diavlo.base.api.module.Category;
/*     */ import rip.diavlo.base.api.module.Module;
/*     */ import rip.diavlo.base.api.value.Value;
/*     */ import rip.diavlo.base.modules.combat.BowAimBot;
/*     */ import rip.diavlo.base.modules.exploit.ResourcePackSpoofer;
/*     */ import rip.diavlo.base.modules.movement.InvMove;
/*     */ import rip.diavlo.base.modules.movement.Sprint;
/*     */ import rip.diavlo.base.modules.player.FastBreak;
/*     */ import rip.diavlo.base.modules.player.GodMode;
/*     */ import rip.diavlo.base.modules.render.BigItems;
/*     */ import rip.diavlo.base.modules.render.HUD;
/*     */ import rip.diavlo.base.modules.render.Notifications;
/*     */ 
/*     */ public class ModuleManager {
/*  24 */   private final List<Module> modules = new ArrayList<>(); public List<Module> getModules() { return this.modules; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/*  29 */     add(new Module[] { (Module)new VelocityExploit() });
/*  30 */     add(new Module[] { (Module)new SinglePacketCrash() });
/*  31 */     add(new Module[] { (Module)new ResourcePackSpoofer() });
/*  32 */     add(new Module[] { (Module)new Crasher() });
/*  33 */     add(new Module[] { (Module)new TextureDIsabler() });
/*     */ 
/*     */     
/*  36 */     add(new Module[] { (Module)new HUD() });
/*  37 */     add(new Module[] { (Module)new Notifications() });
/*  38 */     add(new Module[] { (Module)new ClickGUIModule() });
/*  39 */     add(new Module[] { (Module)new FullBright() });
/*  40 */     add(new Module[] { (Module)new BigItems() });
/*  41 */     add(new Module[] { (Module)new OjoDeShinigami() });
/*  42 */     add(new Module[] { (Module)new GamemodeOnTab() });
/*     */ 
/*     */ 
/*     */     
/*  46 */     add(new Module[] { (Module)new Fly() });
/*  47 */     add(new Module[] { (Module)new Sprint() });
/*  48 */     add(new Module[] { (Module)new TimerHack() });
/*  49 */     add(new Module[] { (Module)new Step() });
/*  50 */     add(new Module[] { (Module)new Speed() });
/*  51 */     add(new Module[] { (Module)new InvMove() });
/*  52 */     add(new Module[] { (Module)new NoFall() });
/*     */ 
/*     */     
/*  55 */     add(new Module[] { (Module)new GodMode() });
/*  56 */     add(new Module[] { (Module)new FastBreak() });
/*  57 */     add(new Module[] { (Module)new FastPlace() });
/*  58 */     add(new Module[] { (Module)new Nuker() });
/*     */ 
/*     */     
/*  61 */     add(new Module[] { (Module)new Killaura() });
/*  62 */     add(new Module[] { (Module)new TargetStrafe() });
/*  63 */     add(new Module[] { (Module)new AimBot() });
/*  64 */     add(new Module[] { (Module)new FastBow() });
/*  65 */     add(new Module[] { (Module)new BowAimBot() });
/*     */     
/*  67 */     ((HUD)get(HUD.class)).setToggled(true);
/*  68 */     ((Notifications)get(Notifications.class)).setToggled(true);
/*     */   }
/*     */   
/*     */   private void add(Module... modules) {
/*  72 */     Arrays.<Module>stream(modules).forEach(module -> {
/*     */           getModules().add(module);
/*     */           
/*     */           for (Field field : module.getClass().getDeclaredFields()) {
/*     */             try {
/*     */               field.setAccessible(true);
/*     */               Object obj = field.get(module);
/*     */               if (obj instanceof Value) {
/*     */                 Collections.addAll(Client.getInstance().getValueManager().getValues(), new Value[] { (Value)obj });
/*     */               }
/*  82 */             } catch (IllegalAccessException e) {
/*     */               e.printStackTrace();
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public List<Module> getModulesSorted(CustomFontRenderer fontRenderer) {
/*  90 */     List<Module> moduleList = new ArrayList<>(this.modules);
/*  91 */     moduleList.sort((a, b) -> {
/*     */           String nameA = a.getName();
/*     */ 
/*     */           
/*     */           String nameB = b.getName();
/*     */ 
/*     */           
/*     */           String dataA = (a.getSuffix() == null) ? "" : a.getSuffix();
/*     */           
/*     */           String dataB = (b.getSuffix() == null) ? "" : b.getSuffix();
/*     */           
/*     */           int first = (int)fontRenderer.getWidth(nameA + dataA);
/*     */           
/*     */           int second = (int)fontRenderer.getWidth(nameB + dataB);
/*     */           
/*     */           return second - first;
/*     */         });
/*     */     
/* 109 */     return moduleList;
/*     */   }
/*     */   
/*     */   public List<Module> getModulesSorted(FontRenderer fontRenderer) {
/* 113 */     List<Module> moduleList = new ArrayList<>(this.modules);
/* 114 */     moduleList.sort((a, b) -> {
/*     */           String nameA = a.getName();
/*     */ 
/*     */           
/*     */           String nameB = b.getName();
/*     */ 
/*     */           
/*     */           String dataA = (a.getSuffix() == null) ? "" : a.getSuffix();
/*     */           
/*     */           String dataB = (b.getSuffix() == null) ? "" : b.getSuffix();
/*     */           
/*     */           int first = fontRenderer.getStringWidth(nameA + dataA);
/*     */           
/*     */           int second = fontRenderer.getStringWidth(nameB + dataB);
/*     */           
/*     */           return second - first;
/*     */         });
/*     */     
/* 132 */     return moduleList;
/*     */   }
/*     */   
/*     */   public <T extends Module> T get(Class<? extends T> clazz) {
/* 136 */     return (T)this.modules.stream().filter(element -> element.getClass().equals(clazz)).findFirst().orElseThrow(() -> new NoSuchElementException("Element belonging to class '" + clazz.getName() + "' not found"));
/*     */   }
/*     */   
/*     */   public ArrayList<Module> get(Category category) {
/* 140 */     ArrayList<Module> mods = new ArrayList<>();
/* 141 */     for (Module m : this.modules) {
/* 142 */       if (m.getCategory() == category)
/* 143 */         mods.add(m); 
/*     */     } 
/* 145 */     return mods;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\managers\ModuleManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
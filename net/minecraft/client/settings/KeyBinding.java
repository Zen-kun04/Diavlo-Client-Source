/*     */ package net.minecraft.client.settings;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.util.IntHashMap;
/*     */ 
/*     */ public class KeyBinding
/*     */   implements Comparable<KeyBinding> {
/*  12 */   private static final List<KeyBinding> keybindArray = Lists.newArrayList();
/*  13 */   private static final IntHashMap<KeyBinding> hash = new IntHashMap();
/*  14 */   private static final Set<String> keybindSet = Sets.newHashSet();
/*     */   
/*     */   private final String keyDescription;
/*     */   private final int keyCodeDefault;
/*     */   private final String keyCategory;
/*     */   private int keyCode;
/*     */   public boolean pressed;
/*     */   private int pressTime;
/*     */   
/*     */   public static void onTick(int keyCode) {
/*  24 */     if (keyCode != 0) {
/*     */       
/*  26 */       KeyBinding keybinding = (KeyBinding)hash.lookup(keyCode);
/*     */       
/*  28 */       if (keybinding != null)
/*     */       {
/*  30 */         keybinding.pressTime++;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setKeyBindState(int keyCode, boolean pressed) {
/*  37 */     if (keyCode != 0) {
/*     */       
/*  39 */       KeyBinding keybinding = (KeyBinding)hash.lookup(keyCode);
/*     */       
/*  41 */       if (keybinding != null)
/*     */       {
/*  43 */         keybinding.pressed = pressed;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void unPressAllKeys() {
/*  50 */     for (KeyBinding keybinding : keybindArray)
/*     */     {
/*  52 */       keybinding.unpressKey();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void resetKeyBindingArrayAndHash() {
/*  58 */     hash.clearMap();
/*     */     
/*  60 */     for (KeyBinding keybinding : keybindArray)
/*     */     {
/*  62 */       hash.addKey(keybinding.keyCode, keybinding);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<String> getKeybinds() {
/*  68 */     return keybindSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public KeyBinding(String description, int keyCode, String category) {
/*  73 */     this.keyDescription = description;
/*  74 */     this.keyCode = keyCode;
/*  75 */     this.keyCodeDefault = keyCode;
/*  76 */     this.keyCategory = category;
/*  77 */     keybindArray.add(this);
/*  78 */     hash.addKey(keyCode, this);
/*  79 */     keybindSet.add(category);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isKeyDown() {
/*  84 */     return this.pressed;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getKeyCategory() {
/*  89 */     return this.keyCategory;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPressed() {
/*  94 */     if (this.pressTime == 0)
/*     */     {
/*  96 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 100 */     this.pressTime--;
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void unpressKey() {
/* 107 */     this.pressTime = 0;
/* 108 */     this.pressed = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getKeyDescription() {
/* 113 */     return this.keyDescription;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getKeyCodeDefault() {
/* 118 */     return this.keyCodeDefault;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getKeyCode() {
/* 123 */     return this.keyCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setKeyCode(int keyCode) {
/* 128 */     this.keyCode = keyCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(KeyBinding p_compareTo_1_) {
/* 133 */     int i = I18n.format(this.keyCategory, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyCategory, new Object[0]));
/*     */     
/* 135 */     if (i == 0)
/*     */     {
/* 137 */       i = I18n.format(this.keyDescription, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyDescription, new Object[0]));
/*     */     }
/*     */     
/* 140 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\settings\KeyBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package net.minecraft.potion;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.IntegerCache;
/*     */ import net.optifine.CustomColors;
/*     */ 
/*     */ public class PotionHelper
/*     */ {
/*  14 */   public static final String unusedString = null;
/*     */   public static final String sugarEffect = "-0+1-2-3&4-4+13";
/*     */   public static final String ghastTearEffect = "+0-1-2-3&4-4+13";
/*     */   public static final String spiderEyeEffect = "-0-1+2-3&4-4+13";
/*     */   public static final String fermentedSpiderEyeEffect = "-0+3-4+13";
/*     */   public static final String speckledMelonEffect = "+0-1+2-3&4-4+13";
/*     */   public static final String blazePowderEffect = "+0-1-2+3&4-4+13";
/*     */   public static final String magmaCreamEffect = "+0+1-2-3&4-4+13";
/*     */   public static final String redstoneEffect = "-5+6-7";
/*     */   public static final String glowstoneEffect = "+5-6-7";
/*     */   public static final String gunpowderEffect = "+14&13-13";
/*     */   public static final String goldenCarrotEffect = "-0+1+2-3+13&4-4";
/*     */   public static final String pufferfishEffect = "+0-1+2+3+13&4-4";
/*     */   public static final String rabbitFootEffect = "+0+1-2+3&4-4+13";
/*  28 */   private static final Map<Integer, String> potionRequirements = Maps.newHashMap();
/*  29 */   private static final Map<Integer, String> potionAmplifiers = Maps.newHashMap();
/*  30 */   private static final Map<Integer, Integer> DATAVALUE_COLORS = Maps.newHashMap();
/*  31 */   private static final String[] potionPrefixes = new String[] { "potion.prefix.mundane", "potion.prefix.uninteresting", "potion.prefix.bland", "potion.prefix.clear", "potion.prefix.milky", "potion.prefix.diffuse", "potion.prefix.artless", "potion.prefix.thin", "potion.prefix.awkward", "potion.prefix.flat", "potion.prefix.bulky", "potion.prefix.bungling", "potion.prefix.buttered", "potion.prefix.smooth", "potion.prefix.suave", "potion.prefix.debonair", "potion.prefix.thick", "potion.prefix.elegant", "potion.prefix.fancy", "potion.prefix.charming", "potion.prefix.dashing", "potion.prefix.refined", "potion.prefix.cordial", "potion.prefix.sparkling", "potion.prefix.potent", "potion.prefix.foul", "potion.prefix.odorless", "potion.prefix.rank", "potion.prefix.harsh", "potion.prefix.acrid", "potion.prefix.gross", "potion.prefix.stinky" };
/*     */ 
/*     */   
/*     */   public static boolean checkFlag(int p_77914_0_, int p_77914_1_) {
/*  35 */     return ((p_77914_0_ & 1 << p_77914_1_) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int isFlagSet(int p_77910_0_, int p_77910_1_) {
/*  40 */     return checkFlag(p_77910_0_, p_77910_1_) ? 1 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int isFlagUnset(int p_77916_0_, int p_77916_1_) {
/*  45 */     return checkFlag(p_77916_0_, p_77916_1_) ? 0 : 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getPotionPrefixIndex(int dataValue) {
/*  50 */     return getPotionPrefixIndexFlags(dataValue, 5, 4, 3, 2, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int calcPotionLiquidColor(Collection<PotionEffect> p_77911_0_) {
/*  55 */     int i = 3694022;
/*     */     
/*  57 */     if (p_77911_0_ != null && !p_77911_0_.isEmpty()) {
/*     */       
/*  59 */       float f = 0.0F;
/*  60 */       float f1 = 0.0F;
/*  61 */       float f2 = 0.0F;
/*  62 */       float f3 = 0.0F;
/*     */       
/*  64 */       for (PotionEffect potioneffect : p_77911_0_) {
/*     */         
/*  66 */         if (potioneffect.getIsShowParticles()) {
/*     */           
/*  68 */           int j = Potion.potionTypes[potioneffect.getPotionID()].getLiquidColor();
/*     */           
/*  70 */           if (Config.isCustomColors())
/*     */           {
/*  72 */             j = CustomColors.getPotionColor(potioneffect.getPotionID(), j);
/*     */           }
/*     */           
/*  75 */           for (int k = 0; k <= potioneffect.getAmplifier(); k++) {
/*     */             
/*  77 */             f += (j >> 16 & 0xFF) / 255.0F;
/*  78 */             f1 += (j >> 8 & 0xFF) / 255.0F;
/*  79 */             f2 += (j >> 0 & 0xFF) / 255.0F;
/*  80 */             f3++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/*  85 */       if (f3 == 0.0F)
/*     */       {
/*  87 */         return 0;
/*     */       }
/*     */ 
/*     */       
/*  91 */       f = f / f3 * 255.0F;
/*  92 */       f1 = f1 / f3 * 255.0F;
/*  93 */       f2 = f2 / f3 * 255.0F;
/*  94 */       return (int)f << 16 | (int)f1 << 8 | (int)f2;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  99 */     return Config.isCustomColors() ? CustomColors.getPotionColor(0, i) : i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getAreAmbient(Collection<PotionEffect> potionEffects) {
/* 105 */     for (PotionEffect potioneffect : potionEffects) {
/*     */       
/* 107 */       if (!potioneffect.getIsAmbient())
/*     */       {
/* 109 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getLiquidColor(int dataValue, boolean bypassCache) {
/* 118 */     Integer integer = IntegerCache.getInteger(dataValue);
/*     */     
/* 120 */     if (!bypassCache) {
/*     */       
/* 122 */       if (DATAVALUE_COLORS.containsKey(integer))
/*     */       {
/* 124 */         return ((Integer)DATAVALUE_COLORS.get(integer)).intValue();
/*     */       }
/*     */ 
/*     */       
/* 128 */       int i = calcPotionLiquidColor(getPotionEffects(integer.intValue(), false));
/* 129 */       DATAVALUE_COLORS.put(integer, Integer.valueOf(i));
/* 130 */       return i;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 135 */     return calcPotionLiquidColor(getPotionEffects(integer.intValue(), true));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getPotionPrefix(int dataValue) {
/* 141 */     int i = getPotionPrefixIndex(dataValue);
/* 142 */     return potionPrefixes[i];
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getPotionEffect(boolean p_77904_0_, boolean p_77904_1_, boolean p_77904_2_, int p_77904_3_, int p_77904_4_, int p_77904_5_, int p_77904_6_) {
/* 147 */     int i = 0;
/*     */     
/* 149 */     if (p_77904_0_) {
/*     */       
/* 151 */       i = isFlagUnset(p_77904_6_, p_77904_4_);
/*     */     }
/* 153 */     else if (p_77904_3_ != -1) {
/*     */       
/* 155 */       if (p_77904_3_ == 0 && countSetFlags(p_77904_6_) == p_77904_4_)
/*     */       {
/* 157 */         i = 1;
/*     */       }
/* 159 */       else if (p_77904_3_ == 1 && countSetFlags(p_77904_6_) > p_77904_4_)
/*     */       {
/* 161 */         i = 1;
/*     */       }
/* 163 */       else if (p_77904_3_ == 2 && countSetFlags(p_77904_6_) < p_77904_4_)
/*     */       {
/* 165 */         i = 1;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 170 */       i = isFlagSet(p_77904_6_, p_77904_4_);
/*     */     } 
/*     */     
/* 173 */     if (p_77904_1_)
/*     */     {
/* 175 */       i *= p_77904_5_;
/*     */     }
/*     */     
/* 178 */     if (p_77904_2_)
/*     */     {
/* 180 */       i *= -1;
/*     */     }
/*     */     
/* 183 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int countSetFlags(int p_77907_0_) {
/*     */     int i;
/* 190 */     for (i = 0; p_77907_0_ > 0; i++)
/*     */     {
/* 192 */       p_77907_0_ &= p_77907_0_ - 1;
/*     */     }
/*     */     
/* 195 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int parsePotionEffects(String p_77912_0_, int p_77912_1_, int p_77912_2_, int p_77912_3_) {
/* 200 */     if (p_77912_1_ < p_77912_0_.length() && p_77912_2_ >= 0 && p_77912_1_ < p_77912_2_) {
/*     */       
/* 202 */       int i = p_77912_0_.indexOf('|', p_77912_1_);
/*     */       
/* 204 */       if (i >= 0 && i < p_77912_2_) {
/*     */         
/* 206 */         int l1 = parsePotionEffects(p_77912_0_, p_77912_1_, i - 1, p_77912_3_);
/*     */         
/* 208 */         if (l1 > 0)
/*     */         {
/* 210 */           return l1;
/*     */         }
/*     */ 
/*     */         
/* 214 */         int j2 = parsePotionEffects(p_77912_0_, i + 1, p_77912_2_, p_77912_3_);
/* 215 */         return (j2 > 0) ? j2 : 0;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 220 */       int j = p_77912_0_.indexOf('&', p_77912_1_);
/*     */       
/* 222 */       if (j >= 0 && j < p_77912_2_) {
/*     */         
/* 224 */         int i2 = parsePotionEffects(p_77912_0_, p_77912_1_, j - 1, p_77912_3_);
/*     */         
/* 226 */         if (i2 <= 0)
/*     */         {
/* 228 */           return 0;
/*     */         }
/*     */ 
/*     */         
/* 232 */         int k2 = parsePotionEffects(p_77912_0_, j + 1, p_77912_2_, p_77912_3_);
/* 233 */         return (k2 <= 0) ? 0 : ((i2 > k2) ? i2 : k2);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 238 */       boolean flag = false;
/* 239 */       boolean flag1 = false;
/* 240 */       boolean flag2 = false;
/* 241 */       boolean flag3 = false;
/* 242 */       boolean flag4 = false;
/* 243 */       int k = -1;
/* 244 */       int l = 0;
/* 245 */       int i1 = 0;
/* 246 */       int j1 = 0;
/*     */       
/* 248 */       for (int k1 = p_77912_1_; k1 < p_77912_2_; k1++) {
/*     */         
/* 250 */         char c0 = p_77912_0_.charAt(k1);
/*     */         
/* 252 */         if (c0 >= '0' && c0 <= '9') {
/*     */           
/* 254 */           if (flag)
/*     */           {
/* 256 */             i1 = c0 - 48;
/* 257 */             flag1 = true;
/*     */           }
/*     */           else
/*     */           {
/* 261 */             l *= 10;
/* 262 */             l += c0 - 48;
/* 263 */             flag2 = true;
/*     */           }
/*     */         
/* 266 */         } else if (c0 == '*') {
/*     */           
/* 268 */           flag = true;
/*     */         }
/* 270 */         else if (c0 == '!') {
/*     */           
/* 272 */           if (flag2) {
/*     */             
/* 274 */             j1 += getPotionEffect(flag3, flag1, flag4, k, l, i1, p_77912_3_);
/* 275 */             flag3 = false;
/* 276 */             flag4 = false;
/* 277 */             flag = false;
/* 278 */             flag1 = false;
/* 279 */             flag2 = false;
/* 280 */             i1 = 0;
/* 281 */             l = 0;
/* 282 */             k = -1;
/*     */           } 
/*     */           
/* 285 */           flag3 = true;
/*     */         }
/* 287 */         else if (c0 == '-') {
/*     */           
/* 289 */           if (flag2) {
/*     */             
/* 291 */             j1 += getPotionEffect(flag3, flag1, flag4, k, l, i1, p_77912_3_);
/* 292 */             flag3 = false;
/* 293 */             flag4 = false;
/* 294 */             flag = false;
/* 295 */             flag1 = false;
/* 296 */             flag2 = false;
/* 297 */             i1 = 0;
/* 298 */             l = 0;
/* 299 */             k = -1;
/*     */           } 
/*     */           
/* 302 */           flag4 = true;
/*     */         }
/* 304 */         else if (c0 != '=' && c0 != '<' && c0 != '>') {
/*     */           
/* 306 */           if (c0 == '+' && flag2)
/*     */           {
/* 308 */             j1 += getPotionEffect(flag3, flag1, flag4, k, l, i1, p_77912_3_);
/* 309 */             flag3 = false;
/* 310 */             flag4 = false;
/* 311 */             flag = false;
/* 312 */             flag1 = false;
/* 313 */             flag2 = false;
/* 314 */             i1 = 0;
/* 315 */             l = 0;
/* 316 */             k = -1;
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 321 */           if (flag2) {
/*     */             
/* 323 */             j1 += getPotionEffect(flag3, flag1, flag4, k, l, i1, p_77912_3_);
/* 324 */             flag3 = false;
/* 325 */             flag4 = false;
/* 326 */             flag = false;
/* 327 */             flag1 = false;
/* 328 */             flag2 = false;
/* 329 */             i1 = 0;
/* 330 */             l = 0;
/* 331 */             k = -1;
/*     */           } 
/*     */           
/* 334 */           if (c0 == '=') {
/*     */             
/* 336 */             k = 0;
/*     */           }
/* 338 */           else if (c0 == '<') {
/*     */             
/* 340 */             k = 2;
/*     */           }
/* 342 */           else if (c0 == '>') {
/*     */             
/* 344 */             k = 1;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 349 */       if (flag2)
/*     */       {
/* 351 */         j1 += getPotionEffect(flag3, flag1, flag4, k, l, i1, p_77912_3_);
/*     */       }
/*     */       
/* 354 */       return j1;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 360 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<PotionEffect> getPotionEffects(int p_77917_0_, boolean p_77917_1_) {
/* 366 */     List<PotionEffect> list = null;
/*     */     
/* 368 */     for (Potion potion : Potion.potionTypes) {
/*     */       
/* 370 */       if (potion != null && (!potion.isUsable() || p_77917_1_)) {
/*     */         
/* 372 */         String s = potionRequirements.get(Integer.valueOf(potion.getId()));
/*     */         
/* 374 */         if (s != null) {
/*     */           
/* 376 */           int i = parsePotionEffects(s, 0, s.length(), p_77917_0_);
/*     */           
/* 378 */           if (i > 0) {
/*     */             
/* 380 */             int j = 0;
/* 381 */             String s1 = potionAmplifiers.get(Integer.valueOf(potion.getId()));
/*     */             
/* 383 */             if (s1 != null) {
/*     */               
/* 385 */               j = parsePotionEffects(s1, 0, s1.length(), p_77917_0_);
/*     */               
/* 387 */               if (j < 0)
/*     */               {
/* 389 */                 j = 0;
/*     */               }
/*     */             } 
/*     */             
/* 393 */             if (potion.isInstant()) {
/*     */               
/* 395 */               i = 1;
/*     */             }
/*     */             else {
/*     */               
/* 399 */               i = 1200 * (i * 3 + (i - 1) * 2);
/* 400 */               i >>= j;
/* 401 */               i = (int)Math.round(i * potion.getEffectiveness());
/*     */               
/* 403 */               if ((p_77917_0_ & 0x4000) != 0)
/*     */               {
/* 405 */                 i = (int)Math.round(i * 0.75D + 0.5D);
/*     */               }
/*     */             } 
/*     */             
/* 409 */             if (list == null)
/*     */             {
/* 411 */               list = Lists.newArrayList();
/*     */             }
/*     */             
/* 414 */             PotionEffect potioneffect = new PotionEffect(potion.getId(), i, j);
/*     */             
/* 416 */             if ((p_77917_0_ & 0x4000) != 0)
/*     */             {
/* 418 */               potioneffect.setSplashPotion(true);
/*     */             }
/*     */             
/* 421 */             list.add(potioneffect);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 427 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int brewBitOperations(int p_77906_0_, int p_77906_1_, boolean p_77906_2_, boolean p_77906_3_, boolean p_77906_4_) {
/* 432 */     if (p_77906_4_) {
/*     */       
/* 434 */       if (!checkFlag(p_77906_0_, p_77906_1_))
/*     */       {
/* 436 */         return 0;
/*     */       }
/*     */     }
/* 439 */     else if (p_77906_2_) {
/*     */       
/* 441 */       p_77906_0_ &= 1 << p_77906_1_ ^ 0xFFFFFFFF;
/*     */     }
/* 443 */     else if (p_77906_3_) {
/*     */       
/* 445 */       if ((p_77906_0_ & 1 << p_77906_1_) == 0)
/*     */       {
/* 447 */         p_77906_0_ |= 1 << p_77906_1_;
/*     */       }
/*     */       else
/*     */       {
/* 451 */         p_77906_0_ &= 1 << p_77906_1_ ^ 0xFFFFFFFF;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 456 */       p_77906_0_ |= 1 << p_77906_1_;
/*     */     } 
/*     */     
/* 459 */     return p_77906_0_;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int applyIngredient(int p_77913_0_, String p_77913_1_) {
/* 464 */     int i = 0;
/* 465 */     int j = p_77913_1_.length();
/* 466 */     boolean flag = false;
/* 467 */     boolean flag1 = false;
/* 468 */     boolean flag2 = false;
/* 469 */     boolean flag3 = false;
/* 470 */     int k = 0;
/*     */     
/* 472 */     for (int l = i; l < j; l++) {
/*     */       
/* 474 */       char c0 = p_77913_1_.charAt(l);
/*     */       
/* 476 */       if (c0 >= '0' && c0 <= '9') {
/*     */         
/* 478 */         k *= 10;
/* 479 */         k += c0 - 48;
/* 480 */         flag = true;
/*     */       }
/* 482 */       else if (c0 == '!') {
/*     */         
/* 484 */         if (flag) {
/*     */           
/* 486 */           p_77913_0_ = brewBitOperations(p_77913_0_, k, flag2, flag1, flag3);
/* 487 */           flag3 = false;
/* 488 */           flag1 = false;
/* 489 */           flag2 = false;
/* 490 */           flag = false;
/* 491 */           k = 0;
/*     */         } 
/*     */         
/* 494 */         flag1 = true;
/*     */       }
/* 496 */       else if (c0 == '-') {
/*     */         
/* 498 */         if (flag) {
/*     */           
/* 500 */           p_77913_0_ = brewBitOperations(p_77913_0_, k, flag2, flag1, flag3);
/* 501 */           flag3 = false;
/* 502 */           flag1 = false;
/* 503 */           flag2 = false;
/* 504 */           flag = false;
/* 505 */           k = 0;
/*     */         } 
/*     */         
/* 508 */         flag2 = true;
/*     */       }
/* 510 */       else if (c0 == '+') {
/*     */         
/* 512 */         if (flag)
/*     */         {
/* 514 */           p_77913_0_ = brewBitOperations(p_77913_0_, k, flag2, flag1, flag3);
/* 515 */           flag3 = false;
/* 516 */           flag1 = false;
/* 517 */           flag2 = false;
/* 518 */           flag = false;
/* 519 */           k = 0;
/*     */         }
/*     */       
/* 522 */       } else if (c0 == '&') {
/*     */         
/* 524 */         if (flag) {
/*     */           
/* 526 */           p_77913_0_ = brewBitOperations(p_77913_0_, k, flag2, flag1, flag3);
/* 527 */           flag3 = false;
/* 528 */           flag1 = false;
/* 529 */           flag2 = false;
/* 530 */           flag = false;
/* 531 */           k = 0;
/*     */         } 
/*     */         
/* 534 */         flag3 = true;
/*     */       } 
/*     */     } 
/*     */     
/* 538 */     if (flag)
/*     */     {
/* 540 */       p_77913_0_ = brewBitOperations(p_77913_0_, k, flag2, flag1, flag3);
/*     */     }
/*     */     
/* 543 */     return p_77913_0_ & 0x7FFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getPotionPrefixIndexFlags(int p_77908_0_, int p_77908_1_, int p_77908_2_, int p_77908_3_, int p_77908_4_, int p_77908_5_) {
/* 548 */     return (checkFlag(p_77908_0_, p_77908_1_) ? 16 : 0) | (checkFlag(p_77908_0_, p_77908_2_) ? 8 : 0) | (checkFlag(p_77908_0_, p_77908_3_) ? 4 : 0) | (checkFlag(p_77908_0_, p_77908_4_) ? 2 : 0) | (checkFlag(p_77908_0_, p_77908_5_) ? 1 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 553 */     potionRequirements.put(Integer.valueOf(Potion.regeneration.getId()), "0 & !1 & !2 & !3 & 0+6");
/* 554 */     potionRequirements.put(Integer.valueOf(Potion.moveSpeed.getId()), "!0 & 1 & !2 & !3 & 1+6");
/* 555 */     potionRequirements.put(Integer.valueOf(Potion.fireResistance.getId()), "0 & 1 & !2 & !3 & 0+6");
/* 556 */     potionRequirements.put(Integer.valueOf(Potion.heal.getId()), "0 & !1 & 2 & !3");
/* 557 */     potionRequirements.put(Integer.valueOf(Potion.poison.getId()), "!0 & !1 & 2 & !3 & 2+6");
/* 558 */     potionRequirements.put(Integer.valueOf(Potion.weakness.getId()), "!0 & !1 & !2 & 3 & 3+6");
/* 559 */     potionRequirements.put(Integer.valueOf(Potion.harm.getId()), "!0 & !1 & 2 & 3");
/* 560 */     potionRequirements.put(Integer.valueOf(Potion.moveSlowdown.getId()), "!0 & 1 & !2 & 3 & 3+6");
/* 561 */     potionRequirements.put(Integer.valueOf(Potion.damageBoost.getId()), "0 & !1 & !2 & 3 & 3+6");
/* 562 */     potionRequirements.put(Integer.valueOf(Potion.nightVision.getId()), "!0 & 1 & 2 & !3 & 2+6");
/* 563 */     potionRequirements.put(Integer.valueOf(Potion.invisibility.getId()), "!0 & 1 & 2 & 3 & 2+6");
/* 564 */     potionRequirements.put(Integer.valueOf(Potion.waterBreathing.getId()), "0 & !1 & 2 & 3 & 2+6");
/* 565 */     potionRequirements.put(Integer.valueOf(Potion.jump.getId()), "0 & 1 & !2 & 3 & 3+6");
/* 566 */     potionAmplifiers.put(Integer.valueOf(Potion.moveSpeed.getId()), "5");
/* 567 */     potionAmplifiers.put(Integer.valueOf(Potion.digSpeed.getId()), "5");
/* 568 */     potionAmplifiers.put(Integer.valueOf(Potion.damageBoost.getId()), "5");
/* 569 */     potionAmplifiers.put(Integer.valueOf(Potion.regeneration.getId()), "5");
/* 570 */     potionAmplifiers.put(Integer.valueOf(Potion.harm.getId()), "5");
/* 571 */     potionAmplifiers.put(Integer.valueOf(Potion.heal.getId()), "5");
/* 572 */     potionAmplifiers.put(Integer.valueOf(Potion.resistance.getId()), "5");
/* 573 */     potionAmplifiers.put(Integer.valueOf(Potion.poison.getId()), "5");
/* 574 */     potionAmplifiers.put(Integer.valueOf(Potion.jump.getId()), "5");
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\potion\PotionHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ 
/*     */ 
/*     */ public class CombatTracker
/*     */ {
/*  15 */   private final List<CombatEntry> combatEntries = Lists.newArrayList();
/*     */   
/*     */   private final EntityLivingBase fighter;
/*     */   private int field_94555_c;
/*     */   private int field_152775_d;
/*     */   private int field_152776_e;
/*     */   private boolean field_94552_d;
/*     */   private boolean field_94553_e;
/*     */   private String field_94551_f;
/*     */   
/*     */   public CombatTracker(EntityLivingBase fighterIn) {
/*  26 */     this.fighter = fighterIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_94545_a() {
/*  31 */     func_94542_g();
/*     */     
/*  33 */     if (this.fighter.isOnLadder()) {
/*     */       
/*  35 */       Block block = this.fighter.worldObj.getBlockState(new BlockPos(this.fighter.posX, (this.fighter.getEntityBoundingBox()).minY, this.fighter.posZ)).getBlock();
/*     */       
/*  37 */       if (block == Blocks.ladder)
/*     */       {
/*  39 */         this.field_94551_f = "ladder";
/*     */       }
/*  41 */       else if (block == Blocks.vine)
/*     */       {
/*  43 */         this.field_94551_f = "vines";
/*     */       }
/*     */     
/*  46 */     } else if (this.fighter.isInWater()) {
/*     */       
/*  48 */       this.field_94551_f = "water";
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void trackDamage(DamageSource damageSrc, float healthIn, float damageAmount) {
/*  54 */     reset();
/*  55 */     func_94545_a();
/*  56 */     CombatEntry combatentry = new CombatEntry(damageSrc, this.fighter.ticksExisted, healthIn, damageAmount, this.field_94551_f, this.fighter.fallDistance);
/*  57 */     this.combatEntries.add(combatentry);
/*  58 */     this.field_94555_c = this.fighter.ticksExisted;
/*  59 */     this.field_94553_e = true;
/*     */     
/*  61 */     if (combatentry.isLivingDamageSrc() && !this.field_94552_d && this.fighter.isEntityAlive()) {
/*     */       
/*  63 */       this.field_94552_d = true;
/*  64 */       this.field_152775_d = this.fighter.ticksExisted;
/*  65 */       this.field_152776_e = this.field_152775_d;
/*  66 */       this.fighter.sendEnterCombat();
/*     */     } 
/*     */   }
/*     */   
/*     */   public IChatComponent getDeathMessage() {
/*     */     IChatComponent ichatcomponent;
/*  72 */     if (this.combatEntries.size() == 0)
/*     */     {
/*  74 */       return new ChatComponentTranslation("death.attack.generic", new Object[] { this.fighter.getDisplayName() });
/*     */     }
/*     */ 
/*     */     
/*  78 */     CombatEntry combatentry = func_94544_f();
/*  79 */     CombatEntry combatentry1 = this.combatEntries.get(this.combatEntries.size() - 1);
/*  80 */     IChatComponent ichatcomponent1 = combatentry1.getDamageSrcDisplayName();
/*  81 */     Entity entity = combatentry1.getDamageSrc().getEntity();
/*     */ 
/*     */     
/*  84 */     if (combatentry != null && combatentry1.getDamageSrc() == DamageSource.fall) {
/*     */       
/*  86 */       IChatComponent ichatcomponent2 = combatentry.getDamageSrcDisplayName();
/*     */       
/*  88 */       if (combatentry.getDamageSrc() != DamageSource.fall && combatentry.getDamageSrc() != DamageSource.outOfWorld) {
/*     */         
/*  90 */         if (ichatcomponent2 != null && (ichatcomponent1 == null || !ichatcomponent2.equals(ichatcomponent1))) {
/*     */           
/*  92 */           Entity entity1 = combatentry.getDamageSrc().getEntity();
/*  93 */           ItemStack itemstack1 = (entity1 instanceof EntityLivingBase) ? ((EntityLivingBase)entity1).getHeldItem() : null;
/*     */           
/*  95 */           if (itemstack1 != null && itemstack1.hasDisplayName())
/*     */           {
/*  97 */             ichatcomponent = new ChatComponentTranslation("death.fell.assist.item", new Object[] { this.fighter.getDisplayName(), ichatcomponent2, itemstack1.getChatComponent() });
/*     */           }
/*     */           else
/*     */           {
/* 101 */             ichatcomponent = new ChatComponentTranslation("death.fell.assist", new Object[] { this.fighter.getDisplayName(), ichatcomponent2 });
/*     */           }
/*     */         
/* 104 */         } else if (ichatcomponent1 != null) {
/*     */           
/* 106 */           ItemStack itemstack = (entity instanceof EntityLivingBase) ? ((EntityLivingBase)entity).getHeldItem() : null;
/*     */           
/* 108 */           if (itemstack != null && itemstack.hasDisplayName())
/*     */           {
/* 110 */             ichatcomponent = new ChatComponentTranslation("death.fell.finish.item", new Object[] { this.fighter.getDisplayName(), ichatcomponent1, itemstack.getChatComponent() });
/*     */           }
/*     */           else
/*     */           {
/* 114 */             ichatcomponent = new ChatComponentTranslation("death.fell.finish", new Object[] { this.fighter.getDisplayName(), ichatcomponent1 });
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 119 */           ichatcomponent = new ChatComponentTranslation("death.fell.killer", new Object[] { this.fighter.getDisplayName() });
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 124 */         ichatcomponent = new ChatComponentTranslation("death.fell.accident." + func_94548_b(combatentry), new Object[] { this.fighter.getDisplayName() });
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 129 */       ichatcomponent = combatentry1.getDamageSrc().getDeathMessage(this.fighter);
/*     */     } 
/*     */     
/* 132 */     return ichatcomponent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityLivingBase func_94550_c() {
/* 138 */     EntityLivingBase entitylivingbase = null;
/* 139 */     EntityPlayer entityplayer = null;
/* 140 */     float f = 0.0F;
/* 141 */     float f1 = 0.0F;
/*     */     
/* 143 */     for (CombatEntry combatentry : this.combatEntries) {
/*     */       
/* 145 */       if (combatentry.getDamageSrc().getEntity() instanceof EntityPlayer && (entityplayer == null || combatentry.func_94563_c() > f1)) {
/*     */         
/* 147 */         f1 = combatentry.func_94563_c();
/* 148 */         entityplayer = (EntityPlayer)combatentry.getDamageSrc().getEntity();
/*     */       } 
/*     */       
/* 151 */       if (combatentry.getDamageSrc().getEntity() instanceof EntityLivingBase && (entitylivingbase == null || combatentry.func_94563_c() > f)) {
/*     */         
/* 153 */         f = combatentry.func_94563_c();
/* 154 */         entitylivingbase = (EntityLivingBase)combatentry.getDamageSrc().getEntity();
/*     */       } 
/*     */     } 
/*     */     
/* 158 */     if (entityplayer != null && f1 >= f / 3.0F)
/*     */     {
/* 160 */       return (EntityLivingBase)entityplayer;
/*     */     }
/*     */ 
/*     */     
/* 164 */     return entitylivingbase;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private CombatEntry func_94544_f() {
/* 170 */     CombatEntry combatentry = null;
/* 171 */     CombatEntry combatentry1 = null;
/* 172 */     int i = 0;
/* 173 */     float f = 0.0F;
/*     */     
/* 175 */     for (int j = 0; j < this.combatEntries.size(); j++) {
/*     */       
/* 177 */       CombatEntry combatentry2 = this.combatEntries.get(j);
/* 178 */       CombatEntry combatentry3 = (j > 0) ? this.combatEntries.get(j - 1) : null;
/*     */       
/* 180 */       if ((combatentry2.getDamageSrc() == DamageSource.fall || combatentry2.getDamageSrc() == DamageSource.outOfWorld) && combatentry2.getDamageAmount() > 0.0F && (combatentry == null || combatentry2.getDamageAmount() > f)) {
/*     */         
/* 182 */         if (j > 0) {
/*     */           
/* 184 */           combatentry = combatentry3;
/*     */         }
/*     */         else {
/*     */           
/* 188 */           combatentry = combatentry2;
/*     */         } 
/*     */         
/* 191 */         f = combatentry2.getDamageAmount();
/*     */       } 
/*     */       
/* 194 */       if (combatentry2.func_94562_g() != null && (combatentry1 == null || combatentry2.func_94563_c() > i))
/*     */       {
/* 196 */         combatentry1 = combatentry2;
/*     */       }
/*     */     } 
/*     */     
/* 200 */     if (f > 5.0F && combatentry != null)
/*     */     {
/* 202 */       return combatentry;
/*     */     }
/* 204 */     if (i > 5 && combatentry1 != null)
/*     */     {
/* 206 */       return combatentry1;
/*     */     }
/*     */ 
/*     */     
/* 210 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String func_94548_b(CombatEntry p_94548_1_) {
/* 216 */     return (p_94548_1_.func_94562_g() == null) ? "generic" : p_94548_1_.func_94562_g();
/*     */   }
/*     */ 
/*     */   
/*     */   public int func_180134_f() {
/* 221 */     return this.field_94552_d ? (this.fighter.ticksExisted - this.field_152775_d) : (this.field_152776_e - this.field_152775_d);
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_94542_g() {
/* 226 */     this.field_94551_f = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 231 */     int i = this.field_94552_d ? 300 : 100;
/*     */     
/* 233 */     if (this.field_94553_e && (!this.fighter.isEntityAlive() || this.fighter.ticksExisted - this.field_94555_c > i)) {
/*     */       
/* 235 */       boolean flag = this.field_94552_d;
/* 236 */       this.field_94553_e = false;
/* 237 */       this.field_94552_d = false;
/* 238 */       this.field_152776_e = this.fighter.ticksExisted;
/*     */       
/* 240 */       if (flag)
/*     */       {
/* 242 */         this.fighter.sendEndCombat();
/*     */       }
/*     */       
/* 245 */       this.combatEntries.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityLivingBase getFighter() {
/* 251 */     return this.fighter;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\CombatTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
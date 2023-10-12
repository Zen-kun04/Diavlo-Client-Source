/*    */ package net.minecraft.entity;
/*    */ 
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.entity.monster.IMob;
/*    */ import net.minecraft.entity.passive.EntityAmbientCreature;
/*    */ import net.minecraft.entity.passive.EntityAnimal;
/*    */ import net.minecraft.entity.passive.EntityWaterMob;
/*    */ import net.minecraft.entity.passive.IAnimals;
/*    */ 
/*    */ public enum EnumCreatureType
/*    */ {
/* 12 */   MONSTER((Class)IMob.class, 70, Material.air, false, false),
/* 13 */   CREATURE((Class)EntityAnimal.class, 10, Material.air, true, true),
/* 14 */   AMBIENT((Class)EntityAmbientCreature.class, 15, Material.air, true, false),
/* 15 */   WATER_CREATURE((Class)EntityWaterMob.class, 5, Material.water, true, false);
/*    */   
/*    */   private final Class<? extends IAnimals> creatureClass;
/*    */   
/*    */   private final int maxNumberOfCreature;
/*    */   private final Material creatureMaterial;
/*    */   private final boolean isPeacefulCreature;
/*    */   private final boolean isAnimal;
/*    */   
/*    */   EnumCreatureType(Class<? extends IAnimals> creatureClassIn, int maxNumberOfCreatureIn, Material creatureMaterialIn, boolean isPeacefulCreatureIn, boolean isAnimalIn) {
/* 25 */     this.creatureClass = creatureClassIn;
/* 26 */     this.maxNumberOfCreature = maxNumberOfCreatureIn;
/* 27 */     this.creatureMaterial = creatureMaterialIn;
/* 28 */     this.isPeacefulCreature = isPeacefulCreatureIn;
/* 29 */     this.isAnimal = isAnimalIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends IAnimals> getCreatureClass() {
/* 34 */     return this.creatureClass;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxNumberOfCreature() {
/* 39 */     return this.maxNumberOfCreature;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean getPeacefulCreature() {
/* 44 */     return this.isPeacefulCreature;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean getAnimal() {
/* 49 */     return this.isAnimal;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\EnumCreatureType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
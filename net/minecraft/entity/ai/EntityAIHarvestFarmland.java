/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockCrops;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.InventoryBasic;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityAIHarvestFarmland extends EntityAIMoveToBlock {
/*     */   private final EntityVillager theVillager;
/*     */   private boolean hasFarmItem;
/*     */   private boolean field_179503_e;
/*     */   private int field_179501_f;
/*     */   
/*     */   public EntityAIHarvestFarmland(EntityVillager theVillagerIn, double speedIn) {
/*  23 */     super((EntityCreature)theVillagerIn, speedIn, 16);
/*  24 */     this.theVillager = theVillagerIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  29 */     if (this.runDelay <= 0) {
/*     */       
/*  31 */       if (!this.theVillager.worldObj.getGameRules().getBoolean("mobGriefing"))
/*     */       {
/*  33 */         return false;
/*     */       }
/*     */       
/*  36 */       this.field_179501_f = -1;
/*  37 */       this.hasFarmItem = this.theVillager.isFarmItemInInventory();
/*  38 */       this.field_179503_e = this.theVillager.func_175557_cr();
/*     */     } 
/*     */     
/*  41 */     return super.shouldExecute();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  46 */     return (this.field_179501_f >= 0 && super.continueExecuting());
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  51 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/*  56 */     super.resetTask();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  61 */     super.updateTask();
/*  62 */     this.theVillager.getLookHelper().setLookPosition(this.destinationBlock.getX() + 0.5D, (this.destinationBlock.getY() + 1), this.destinationBlock.getZ() + 0.5D, 10.0F, this.theVillager.getVerticalFaceSpeed());
/*     */     
/*  64 */     if (getIsAboveDestination()) {
/*     */       
/*  66 */       World world = this.theVillager.worldObj;
/*  67 */       BlockPos blockpos = this.destinationBlock.up();
/*  68 */       IBlockState iblockstate = world.getBlockState(blockpos);
/*  69 */       Block block = iblockstate.getBlock();
/*     */       
/*  71 */       if (this.field_179501_f == 0 && block instanceof BlockCrops && ((Integer)iblockstate.getValue((IProperty)BlockCrops.AGE)).intValue() == 7) {
/*     */         
/*  73 */         world.destroyBlock(blockpos, true);
/*     */       }
/*  75 */       else if (this.field_179501_f == 1 && block == Blocks.air) {
/*     */         
/*  77 */         InventoryBasic inventorybasic = this.theVillager.getVillagerInventory();
/*     */         
/*  79 */         for (int i = 0; i < inventorybasic.getSizeInventory(); i++) {
/*     */           
/*  81 */           ItemStack itemstack = inventorybasic.getStackInSlot(i);
/*  82 */           boolean flag = false;
/*     */           
/*  84 */           if (itemstack != null)
/*     */           {
/*  86 */             if (itemstack.getItem() == Items.wheat_seeds) {
/*     */               
/*  88 */               world.setBlockState(blockpos, Blocks.wheat.getDefaultState(), 3);
/*  89 */               flag = true;
/*     */             }
/*  91 */             else if (itemstack.getItem() == Items.potato) {
/*     */               
/*  93 */               world.setBlockState(blockpos, Blocks.potatoes.getDefaultState(), 3);
/*  94 */               flag = true;
/*     */             }
/*  96 */             else if (itemstack.getItem() == Items.carrot) {
/*     */               
/*  98 */               world.setBlockState(blockpos, Blocks.carrots.getDefaultState(), 3);
/*  99 */               flag = true;
/*     */             } 
/*     */           }
/*     */           
/* 103 */           if (flag) {
/*     */             
/* 105 */             itemstack.stackSize--;
/*     */             
/* 107 */             if (itemstack.stackSize <= 0)
/*     */             {
/* 109 */               inventorybasic.setInventorySlotContents(i, (ItemStack)null);
/*     */             }
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 117 */       this.field_179501_f = -1;
/* 118 */       this.runDelay = 10;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
/* 124 */     Block block = worldIn.getBlockState(pos).getBlock();
/*     */     
/* 126 */     if (block == Blocks.farmland) {
/*     */       
/* 128 */       pos = pos.up();
/* 129 */       IBlockState iblockstate = worldIn.getBlockState(pos);
/* 130 */       block = iblockstate.getBlock();
/*     */       
/* 132 */       if (block instanceof BlockCrops && ((Integer)iblockstate.getValue((IProperty)BlockCrops.AGE)).intValue() == 7 && this.field_179503_e && (this.field_179501_f == 0 || this.field_179501_f < 0)) {
/*     */         
/* 134 */         this.field_179501_f = 0;
/* 135 */         return true;
/*     */       } 
/*     */       
/* 138 */       if (block == Blocks.air && this.hasFarmItem && (this.field_179501_f == 1 || this.field_179501_f < 0)) {
/*     */         
/* 140 */         this.field_179501_f = 1;
/* 141 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIHarvestFarmland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
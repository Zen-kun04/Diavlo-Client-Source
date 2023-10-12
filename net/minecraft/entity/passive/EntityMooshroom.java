/*    */ package net.minecraft.entity.passive;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityAgeable;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.item.EntityItem;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.EnumParticleTypes;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityMooshroom extends EntityCow {
/*    */   public EntityMooshroom(World worldIn) {
/* 16 */     super(worldIn);
/* 17 */     setSize(0.9F, 1.3F);
/* 18 */     this.spawnableBlock = (Block)Blocks.mycelium;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean interact(EntityPlayer player) {
/* 23 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*    */     
/* 25 */     if (itemstack != null && itemstack.getItem() == Items.bowl && getGrowingAge() >= 0) {
/*    */       
/* 27 */       if (itemstack.stackSize == 1) {
/*    */         
/* 29 */         player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.mushroom_stew));
/* 30 */         return true;
/*    */       } 
/*    */       
/* 33 */       if (player.inventory.addItemStackToInventory(new ItemStack(Items.mushroom_stew)) && !player.capabilities.isCreativeMode) {
/*    */         
/* 35 */         player.inventory.decrStackSize(player.inventory.currentItem, 1);
/* 36 */         return true;
/*    */       } 
/*    */     } 
/*    */     
/* 40 */     if (itemstack != null && itemstack.getItem() == Items.shears && getGrowingAge() >= 0) {
/*    */       
/* 42 */       setDead();
/* 43 */       this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY + (this.height / 2.0F), this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/*    */       
/* 45 */       if (!this.worldObj.isRemote) {
/*    */         
/* 47 */         EntityCow entitycow = new EntityCow(this.worldObj);
/* 48 */         entitycow.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/* 49 */         entitycow.setHealth(getHealth());
/* 50 */         entitycow.renderYawOffset = this.renderYawOffset;
/*    */         
/* 52 */         if (hasCustomName())
/*    */         {
/* 54 */           entitycow.setCustomNameTag(getCustomNameTag());
/*    */         }
/*    */         
/* 57 */         this.worldObj.spawnEntityInWorld((Entity)entitycow);
/*    */         
/* 59 */         for (int i = 0; i < 5; i++)
/*    */         {
/* 61 */           this.worldObj.spawnEntityInWorld((Entity)new EntityItem(this.worldObj, this.posX, this.posY + this.height, this.posZ, new ItemStack((Block)Blocks.red_mushroom)));
/*    */         }
/*    */         
/* 64 */         itemstack.damageItem(1, (EntityLivingBase)player);
/* 65 */         playSound("mob.sheep.shear", 1.0F, 1.0F);
/*    */       } 
/*    */       
/* 68 */       return true;
/*    */     } 
/*    */ 
/*    */     
/* 72 */     return super.interact(player);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public EntityMooshroom createChild(EntityAgeable ageable) {
/* 78 */     return new EntityMooshroom(this.worldObj);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\passive\EntityMooshroom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
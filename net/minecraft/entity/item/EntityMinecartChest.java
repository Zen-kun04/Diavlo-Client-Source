/*    */ package net.minecraft.entity.item;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockChest;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.ContainerChest;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.util.DamageSource;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityMinecartChest extends EntityMinecartContainer {
/*    */   public EntityMinecartChest(World worldIn) {
/* 19 */     super(worldIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityMinecartChest(World worldIn, double x, double y, double z) {
/* 24 */     super(worldIn, x, y, z);
/*    */   }
/*    */ 
/*    */   
/*    */   public void killMinecart(DamageSource source) {
/* 29 */     super.killMinecart(source);
/*    */     
/* 31 */     if (this.worldObj.getGameRules().getBoolean("doEntityDrops"))
/*    */     {
/* 33 */       dropItemWithOffset(Item.getItemFromBlock((Block)Blocks.chest), 1, 0.0F);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSizeInventory() {
/* 39 */     return 27;
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityMinecart.EnumMinecartType getMinecartType() {
/* 44 */     return EntityMinecart.EnumMinecartType.CHEST;
/*    */   }
/*    */ 
/*    */   
/*    */   public IBlockState getDefaultDisplayTile() {
/* 49 */     return Blocks.chest.getDefaultState().withProperty((IProperty)BlockChest.FACING, (Comparable)EnumFacing.NORTH);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDefaultDisplayTileOffset() {
/* 54 */     return 8;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getGuiID() {
/* 59 */     return "minecraft:chest";
/*    */   }
/*    */ 
/*    */   
/*    */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
/* 64 */     return (Container)new ContainerChest((IInventory)playerInventory, (IInventory)this, playerIn);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityMinecartChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
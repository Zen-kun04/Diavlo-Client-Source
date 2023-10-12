/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.ITickable;
/*     */ 
/*     */ public class TileEntityEnderChest
/*     */   extends TileEntity
/*     */   implements ITickable {
/*     */   public float lidAngle;
/*     */   public float prevLidAngle;
/*     */   public int numPlayersUsing;
/*     */   private int ticksSinceSync;
/*     */   
/*     */   public void update() {
/*  16 */     if (++this.ticksSinceSync % 20 * 4 == 0)
/*     */     {
/*  18 */       this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.numPlayersUsing);
/*     */     }
/*     */     
/*  21 */     this.prevLidAngle = this.lidAngle;
/*  22 */     int i = this.pos.getX();
/*  23 */     int j = this.pos.getY();
/*  24 */     int k = this.pos.getZ();
/*  25 */     float f = 0.1F;
/*     */     
/*  27 */     if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
/*     */       
/*  29 */       double d0 = i + 0.5D;
/*  30 */       double d1 = k + 0.5D;
/*  31 */       this.worldObj.playSoundEffect(d0, j + 0.5D, d1, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
/*     */     } 
/*     */     
/*  34 */     if ((this.numPlayersUsing == 0 && this.lidAngle > 0.0F) || (this.numPlayersUsing > 0 && this.lidAngle < 1.0F)) {
/*     */       
/*  36 */       float f2 = this.lidAngle;
/*     */       
/*  38 */       if (this.numPlayersUsing > 0) {
/*     */         
/*  40 */         this.lidAngle += f;
/*     */       }
/*     */       else {
/*     */         
/*  44 */         this.lidAngle -= f;
/*     */       } 
/*     */       
/*  47 */       if (this.lidAngle > 1.0F)
/*     */       {
/*  49 */         this.lidAngle = 1.0F;
/*     */       }
/*     */       
/*  52 */       float f1 = 0.5F;
/*     */       
/*  54 */       if (this.lidAngle < f1 && f2 >= f1) {
/*     */         
/*  56 */         double d3 = i + 0.5D;
/*  57 */         double d2 = k + 0.5D;
/*  58 */         this.worldObj.playSoundEffect(d3, j + 0.5D, d2, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
/*     */       } 
/*     */       
/*  61 */       if (this.lidAngle < 0.0F)
/*     */       {
/*  63 */         this.lidAngle = 0.0F;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean receiveClientEvent(int id, int type) {
/*  70 */     if (id == 1) {
/*     */       
/*  72 */       this.numPlayersUsing = type;
/*  73 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  77 */     return super.receiveClientEvent(id, type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void invalidate() {
/*  83 */     updateContainingBlockInfo();
/*  84 */     super.invalidate();
/*     */   }
/*     */ 
/*     */   
/*     */   public void openChest() {
/*  89 */     this.numPlayersUsing++;
/*  90 */     this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.numPlayersUsing);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeChest() {
/*  95 */     this.numPlayersUsing--;
/*  96 */     this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.numPlayersUsing);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeUsed(EntityPlayer p_145971_1_) {
/* 101 */     return (this.worldObj.getTileEntity(this.pos) != this) ? false : ((p_145971_1_.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D));
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\tileentity\TileEntityEnderChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
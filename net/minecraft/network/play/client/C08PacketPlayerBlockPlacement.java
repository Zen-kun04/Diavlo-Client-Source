/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ public class C08PacketPlayerBlockPlacement implements Packet<INetHandlerPlayServer> {
/* 12 */   private static final BlockPos field_179726_a = new BlockPos(-1, -1, -1);
/*    */   
/*    */   private BlockPos position;
/*    */   
/*    */   private int placedBlockDirection;
/*    */   
/*    */   private ItemStack stack;
/*    */   private float facingX;
/*    */   private float facingY;
/*    */   private float facingZ;
/*    */   
/*    */   public C08PacketPlayerBlockPlacement() {}
/*    */   
/*    */   public C08PacketPlayerBlockPlacement(ItemStack stackIn) {
/* 26 */     this(field_179726_a, 255, stackIn, 0.0F, 0.0F, 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public C08PacketPlayerBlockPlacement(BlockPos positionIn, int placedBlockDirectionIn, ItemStack stackIn, float facingXIn, float facingYIn, float facingZIn) {
/* 31 */     this.position = positionIn;
/* 32 */     this.placedBlockDirection = placedBlockDirectionIn;
/* 33 */     this.stack = (stackIn != null) ? stackIn.copy() : null;
/* 34 */     this.facingX = facingXIn;
/* 35 */     this.facingY = facingYIn;
/* 36 */     this.facingZ = facingZIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 41 */     this.position = buf.readBlockPos();
/* 42 */     this.placedBlockDirection = buf.readUnsignedByte();
/* 43 */     this.stack = buf.readItemStackFromBuffer();
/* 44 */     this.facingX = buf.readUnsignedByte() / 16.0F;
/* 45 */     this.facingY = buf.readUnsignedByte() / 16.0F;
/* 46 */     this.facingZ = buf.readUnsignedByte() / 16.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 51 */     buf.writeBlockPos(this.position);
/* 52 */     buf.writeByte(this.placedBlockDirection);
/* 53 */     buf.writeItemStackToBuffer(this.stack);
/* 54 */     buf.writeByte((int)(this.facingX * 16.0F));
/* 55 */     buf.writeByte((int)(this.facingY * 16.0F));
/* 56 */     buf.writeByte((int)(this.facingZ * 16.0F));
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayServer handler) {
/* 61 */     handler.processPlayerBlockPlacement(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getPosition() {
/* 66 */     return this.position;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getPlacedBlockDirection() {
/* 71 */     return this.placedBlockDirection;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getStack() {
/* 76 */     return this.stack;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getPlacedBlockOffsetX() {
/* 81 */     return this.facingX;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getPlacedBlockOffsetY() {
/* 86 */     return this.facingY;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getPlacedBlockOffsetZ() {
/* 91 */     return this.facingZ;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\client\C08PacketPlayerBlockPlacement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.network.rcon;
/*    */ 
/*    */ import net.minecraft.command.CommandResultStats;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.util.Vec3;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class RConConsoleSource
/*    */   implements ICommandSender {
/* 15 */   private static final RConConsoleSource instance = new RConConsoleSource();
/* 16 */   private StringBuffer buffer = new StringBuffer();
/*    */ 
/*    */   
/*    */   public String getName() {
/* 20 */     return "Rcon";
/*    */   }
/*    */ 
/*    */   
/*    */   public IChatComponent getDisplayName() {
/* 25 */     return (IChatComponent)new ChatComponentText(getName());
/*    */   }
/*    */ 
/*    */   
/*    */   public void addChatMessage(IChatComponent component) {
/* 30 */     this.buffer.append(component.getUnformattedText());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
/* 35 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getPosition() {
/* 40 */     return new BlockPos(0, 0, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 getPositionVector() {
/* 45 */     return new Vec3(0.0D, 0.0D, 0.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   public World getEntityWorld() {
/* 50 */     return MinecraftServer.getServer().getEntityWorld();
/*    */   }
/*    */ 
/*    */   
/*    */   public Entity getCommandSenderEntity() {
/* 55 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean sendCommandFeedback() {
/* 60 */     return true;
/*    */   }
/*    */   
/*    */   public void setCommandStat(CommandResultStats.Type type, int amount) {}
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\rcon\RConConsoleSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
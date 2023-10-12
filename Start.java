/*    */ import java.util.Arrays;
/*    */ import net.minecraft.client.main.Main;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Start
/*    */ {
/*    */   public static void main(String[] args) {
/* 10 */     Main.main(concat(new String[] { "--version", "mcp", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}" }, args));
/*    */   }
/*    */   
/*    */   public static <T> T[] concat(T[] first, T[] second) {
/* 14 */     T[] result = Arrays.copyOf(first, first.length + second.length);
/* 15 */     System.arraycopy(second, 0, result, first.length, second.length);
/* 16 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\Start.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
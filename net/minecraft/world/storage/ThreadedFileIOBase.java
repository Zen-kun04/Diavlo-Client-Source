/*    */ package net.minecraft.world.storage;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ThreadedFileIOBase
/*    */   implements Runnable {
/*  9 */   private static final ThreadedFileIOBase threadedIOInstance = new ThreadedFileIOBase();
/* 10 */   private List<IThreadedFileIO> threadedIOQueue = Collections.synchronizedList(Lists.newArrayList());
/*    */   
/*    */   private volatile long writeQueuedCounter;
/*    */   private volatile long savedIOCounter;
/*    */   private volatile boolean isThreadWaiting;
/*    */   
/*    */   private ThreadedFileIOBase() {
/* 17 */     Thread thread = new Thread(this, "File IO Thread");
/* 18 */     thread.setPriority(1);
/* 19 */     thread.start();
/*    */   }
/*    */ 
/*    */   
/*    */   public static ThreadedFileIOBase getThreadedIOInstance() {
/* 24 */     return threadedIOInstance;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/*    */     while (true) {
/* 31 */       processQueue();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private void processQueue() {
/* 37 */     for (int i = 0; i < this.threadedIOQueue.size(); i++) {
/*    */       
/* 39 */       IThreadedFileIO ithreadedfileio = this.threadedIOQueue.get(i);
/* 40 */       boolean flag = ithreadedfileio.writeNextIO();
/*    */       
/* 42 */       if (!flag) {
/*    */         
/* 44 */         this.threadedIOQueue.remove(i--);
/* 45 */         this.savedIOCounter++;
/*    */       } 
/*    */ 
/*    */       
/*    */       try {
/* 50 */         Thread.sleep(this.isThreadWaiting ? 0L : 10L);
/*    */       }
/* 52 */       catch (InterruptedException interruptedexception1) {
/*    */         
/* 54 */         interruptedexception1.printStackTrace();
/*    */       } 
/*    */     } 
/*    */     
/* 58 */     if (this.threadedIOQueue.isEmpty()) {
/*    */       
/*    */       try {
/*    */         
/* 62 */         Thread.sleep(25L);
/*    */       }
/* 64 */       catch (InterruptedException interruptedexception) {
/*    */         
/* 66 */         interruptedexception.printStackTrace();
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void queueIO(IThreadedFileIO p_75735_1_) {
/* 73 */     if (!this.threadedIOQueue.contains(p_75735_1_)) {
/*    */       
/* 75 */       this.writeQueuedCounter++;
/* 76 */       this.threadedIOQueue.add(p_75735_1_);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void waitForFinish() throws InterruptedException {
/* 82 */     this.isThreadWaiting = true;
/*    */     
/* 84 */     while (this.writeQueuedCounter != this.savedIOCounter)
/*    */     {
/* 86 */       Thread.sleep(10L);
/*    */     }
/*    */     
/* 89 */     this.isThreadWaiting = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\storage\ThreadedFileIOBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
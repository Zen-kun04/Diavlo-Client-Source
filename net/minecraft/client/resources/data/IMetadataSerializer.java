/*    */ package net.minecraft.client.resources.data;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.TypeAdapterFactory;
/*    */ import net.minecraft.util.ChatStyle;
/*    */ import net.minecraft.util.EnumTypeAdapterFactory;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.util.IRegistry;
/*    */ import net.minecraft.util.RegistrySimple;
/*    */ 
/*    */ public class IMetadataSerializer {
/* 15 */   private final IRegistry<String, Registration<? extends IMetadataSection>> metadataSectionSerializerRegistry = (IRegistry<String, Registration<? extends IMetadataSection>>)new RegistrySimple();
/* 16 */   private final GsonBuilder gsonBuilder = new GsonBuilder();
/*    */   
/*    */   private Gson gson;
/*    */   
/*    */   public IMetadataSerializer() {
/* 21 */     this.gsonBuilder.registerTypeHierarchyAdapter(IChatComponent.class, new IChatComponent.Serializer());
/* 22 */     this.gsonBuilder.registerTypeHierarchyAdapter(ChatStyle.class, new ChatStyle.Serializer());
/* 23 */     this.gsonBuilder.registerTypeAdapterFactory((TypeAdapterFactory)new EnumTypeAdapterFactory());
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends IMetadataSection> void registerMetadataSectionType(IMetadataSectionSerializer<T> metadataSectionSerializer, Class<T> clazz) {
/* 28 */     this.metadataSectionSerializerRegistry.putObject(metadataSectionSerializer.getSectionName(), new Registration<>(metadataSectionSerializer, clazz));
/* 29 */     this.gsonBuilder.registerTypeAdapter(clazz, metadataSectionSerializer);
/* 30 */     this.gson = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends IMetadataSection> T parseMetadataSection(String sectionName, JsonObject json) {
/* 35 */     if (sectionName == null)
/*    */     {
/* 37 */       throw new IllegalArgumentException("Metadata section name cannot be null");
/*    */     }
/* 39 */     if (!json.has(sectionName))
/*    */     {
/* 41 */       return (T)null;
/*    */     }
/* 43 */     if (!json.get(sectionName).isJsonObject())
/*    */     {
/* 45 */       throw new IllegalArgumentException("Invalid metadata for '" + sectionName + "' - expected object, found " + json.get(sectionName));
/*    */     }
/*    */ 
/*    */     
/* 49 */     Registration<?> registration = (Registration)this.metadataSectionSerializerRegistry.getObject(sectionName);
/*    */     
/* 51 */     if (registration == null)
/*    */     {
/* 53 */       throw new IllegalArgumentException("Don't know how to handle metadata section '" + sectionName + "'");
/*    */     }
/*    */ 
/*    */     
/* 57 */     return (T)getGson().fromJson((JsonElement)json.getAsJsonObject(sectionName), registration.clazz);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Gson getGson() {
/* 64 */     if (this.gson == null)
/*    */     {
/* 66 */       this.gson = this.gsonBuilder.create();
/*    */     }
/*    */     
/* 69 */     return this.gson;
/*    */   }
/*    */ 
/*    */   
/*    */   class Registration<T extends IMetadataSection>
/*    */   {
/*    */     final IMetadataSectionSerializer<T> section;
/*    */     final Class<T> clazz;
/*    */     
/*    */     private Registration(IMetadataSectionSerializer<T> metadataSectionSerializer, Class<T> clazzToRegister) {
/* 79 */       this.section = metadataSectionSerializer;
/* 80 */       this.clazz = clazzToRegister;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\resources\data\IMetadataSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
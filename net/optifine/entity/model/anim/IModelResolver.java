package net.optifine.entity.model.anim;

import net.minecraft.client.model.ModelRenderer;
import net.optifine.expr.IExpressionResolver;

public interface IModelResolver extends IExpressionResolver {
  ModelRenderer getModelRenderer(String paramString);
  
  ModelVariableFloat getModelVariable(String paramString);
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\entity\model\anim\IModelResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
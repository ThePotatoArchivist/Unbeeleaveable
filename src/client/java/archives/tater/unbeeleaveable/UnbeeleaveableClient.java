package archives.tater.unbeeleaveable;

import archives.tater.unbeeleaveable.render.entity.BeeBombEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class UnbeeleaveableClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		EntityRendererRegistry.register(Unbeeleaveable.BEE_BOMB_ENTITY, BeeBombEntityRenderer::new);
	}
}

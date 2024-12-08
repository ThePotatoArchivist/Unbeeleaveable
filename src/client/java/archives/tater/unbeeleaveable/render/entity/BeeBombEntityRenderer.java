package archives.tater.unbeeleaveable.render.entity;

import archives.tater.unbeeleaveable.Unbeeleaveable;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntEntityRenderer;

public class BeeBombEntityRenderer extends TntEntityRenderer implements TntEntityRendererBlockStateProvider {
    public BeeBombEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public BlockState provideCustomBlockState() {
        return Unbeeleaveable.BEE_BOMB.getDefaultState();
    }
}

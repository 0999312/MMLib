package cn.mcmod_mmf.mmlib.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

public abstract class TrailParticle extends TextureSheetParticle {

    public TrailParticle(ClientLevel p_i232351_1_, double p_i232351_2_, double p_i232351_4_, double p_i232351_6_,
            double p_i232351_8_, double p_i232351_10_, double p_i232351_12_) {
        super(p_i232351_1_, p_i232351_2_, p_i232351_4_ - 0.5D, p_i232351_6_);
        this.setSize(0.02F, 0.02F);
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.2F;
        this.xd = p_i232351_8_ * (double) 0.2F + (Math.random() * 2.0D - 1.0D) * (double) 0.02F;
        this.yd = p_i232351_10_ * (double) 0.2F + (Math.random() * 2.0D - 1.0D) * (double) 0.02F;
        this.zd = p_i232351_12_ * (double) 0.2F + (Math.random() * 2.0D - 1.0D) * (double) 0.02F;
        this.lifetime = (int) (4.0D / (Math.random() * 0.8D + 0.2D));
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        } else {
            this.yd += 0.002D;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= (double) 0.85F;
            this.yd *= (double) 0.85F;
            this.zd *= (double) 0.85F;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

}

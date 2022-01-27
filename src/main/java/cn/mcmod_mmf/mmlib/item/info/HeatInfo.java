package cn.mcmod_mmf.mmlib.item.info;

public class HeatInfo {
    private final float heatCapacity;
    private final float meltTemp;
    private final boolean isForgeable;

    private HeatInfo(HeatInfo.Builder builder) {
        heatCapacity = builder.heatCapacity;
        meltTemp = builder.meltTemp;
        isForgeable = builder.isForgeable;
    }

    public static HeatInfo.Builder builder() {
        return new HeatInfo.Builder();
    }

    public static class Builder {
        private float heatCapacity;
        private float meltTemp;
        private boolean isForgeable;

        public Builder heatCapacity(float heatCapacity) {
            this.heatCapacity = heatCapacity;
            return this;
        }

        public Builder meltTemp(float meltTemp) {
            this.meltTemp = meltTemp;
            return this;
        }

        public Builder forgeable() {
            this.isForgeable = true;
            return this;
        }

        public HeatInfo build() {
            return new HeatInfo(this);
        }
    }

    public boolean isForgeable() {
        return isForgeable;
    }

    public float getMeltTemp() {
        return meltTemp;
    }

    public float getHeatCapacity() {
        return heatCapacity;
    }
}

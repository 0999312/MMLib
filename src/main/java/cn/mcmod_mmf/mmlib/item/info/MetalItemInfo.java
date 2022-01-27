package cn.mcmod_mmf.mmlib.item.info;

public class MetalItemInfo {
    private final String metalStr;
    private final int Amount;
    private final boolean canMelt;

    private MetalItemInfo(MetalItemInfo.Builder builder) {
        metalStr = builder.metalStr;
        Amount = builder.amount;
        canMelt = builder.canMelt;
    }
    
    public static MetalItemInfo.Builder builder() {
        return new MetalItemInfo.Builder();
    }
    
    public static class Builder {
        private String metalStr;
        private int amount;
        private boolean canMelt;
        
        public Builder metal(String metal) {
            this.metalStr = metal;
            return this;
        }
        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }
        public Builder canMelt() {
            this.canMelt = true;
            return this;
        }
        
        public MetalItemInfo build() {
            return new MetalItemInfo(this);
        }
    }

    public String getMetal() {
        return metalStr;
    }

    public int getAmount() {
        return Amount;
    }

    public boolean isCanMelt() {
        return canMelt;
    }

}

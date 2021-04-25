package cn.mcmod_mmf.mmlib.item.info;

public class MetalItemInfo {
	private final String metalStr;
	private final int Amount;
	private final boolean canMelt;
	public MetalItemInfo(String metal, int amount, boolean melt) {
		metalStr = metal;
		Amount = amount;
		canMelt = melt;
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

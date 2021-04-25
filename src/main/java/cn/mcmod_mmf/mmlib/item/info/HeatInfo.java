package cn.mcmod_mmf.mmlib.item.info;

public class HeatInfo {
	private final float heatCapacity;
	private final float meltTemp;
	private final boolean isForgeable;
	public HeatInfo(float heat,float temp,boolean forgeable) {
		heatCapacity = heat;
		meltTemp = temp;
		isForgeable = forgeable;
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

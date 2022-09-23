package dev.sxmurxy.customhitboxes.ui.animations;

public class DoubleAnimation {

	private boolean animating, starting, ending, auto;
	private double min, max, s, now;

	public DoubleAnimation(double now) {
		this.now = now;
	}

	public DoubleAnimation() {
		this(0);
	}

	public void begin(double min, double max, double start, double speed, boolean auto) {
		if (!ending)
			this.now = start;
		this.animating = true;
		this.starting = true;
		this.ending = false;
		this.auto = auto;
		this.min = min;
		this.max = max;
		this.s = (max - min) / (20 / speed);
	}

	public void begin(double min, double max, double speed, boolean auto) {
		begin(min, max, min, speed, auto);
	}

	public void beginEnding() {
		starting = false;
		ending = true;
	}

	public double get() {
		if (starting) {
			if (now + s < max) {
				now = max;
				starting = false;
				if (auto) {
					ending = true;
				}
			} else
				now += s;
		} else if (ending) {
			if (now - s > min) {
				now = min;
				ending = false;
				animating = false;
			} else
				now -= s;
		}
		return now;
	}

	public void stop() {
		starting = false;
		ending = false;
		animating = false;
	}

	public boolean isAnimating() {
		return animating;
	}

	public boolean isStarting() {
		return starting;
	}

	public boolean isEnding() {
		return ending;
	}

}

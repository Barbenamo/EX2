package gameClient;

public class timeListener {
	private MyPanel panel;
	
	public timeListener(MyPanel panel) {
		this.panel = panel;
	}

	public void update(long time) {
		panel.updateTime(time);
	}
}

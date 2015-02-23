package freundTech.minecraft.motecraft;

import motej.Mote;
import motej.StatusInformationReport;
import motej.event.StatusInformationListener;

public class WiimoteBatteryUpdater extends Thread {

	public void run() {
		while (true) {
			if (MoteCraft.mote != null) {
				StatusInformationReport report = MoteCraft.mote
						.getStatusInformationReport();

				for (int i = 0; i < 10; i++) { // Keep Java busy until report
												// arrives
					if (report != null) {
						break;
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
					}
				}
				if (report != null) {
					byte batteryLevel = (byte) (report.getBatteryLevel());
					byte numleds = (byte) (batteryLevel / 31.75 + 1);
					boolean[] leds = new boolean[4];
					for (int i = 0; i < numleds; i++) {
						leds[i] = true;
					}
					MoteCraft.mote.setPlayerLeds(leds);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}
				}
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}

		}
	}
}

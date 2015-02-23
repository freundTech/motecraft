package freundTech.minecraft.motecraft;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import motej.Extension;
import motej.IrPoint;
import motej.Mote;
import motej.event.AccelerometerEvent;
import motej.event.AccelerometerListener;
import motej.event.CoreButtonEvent;
import motej.event.CoreButtonListener;
import motej.event.ExtensionEvent;
import motej.event.ExtensionListener;
import motej.event.IrCameraEvent;
import motej.event.IrCameraListener;
import motej.request.ReportModeRequest;
import motejx.extensions.nunchuk.AnalogStickEvent;
import motejx.extensions.nunchuk.AnalogStickListener;
import motejx.extensions.nunchuk.Nunchuk;
import motejx.extensions.nunchuk.NunchukButtonEvent;
import motejx.extensions.nunchuk.NunchukButtonListener;

public class WiiMoteListener implements ExtensionListener, AnalogStickListener,
		NunchukButtonListener, CoreButtonListener, AccelerometerListener,
		IrCameraListener {

	public Object lock = new Object();

	public Point analogCenter = new Point(0, 0);
	public Point analogPoint = new Point(0, 0);
	public double analogDistance;
	public double analogAngle;

	public int slotoffset = 0;
	private boolean minuspressed = false;
	private boolean pluspressed = false;

	public boolean homepressed = false;
	private boolean keephomepressed = false;

	public boolean zpressed = false;
	public boolean cpressed = false;

	public boolean apressed = false;
	public boolean bpressed = false;
	private boolean keepbpressed = false;

	public double accelX = 122;
	public double accelY = 122;
	public double accelZ = 139; // Gravitation

	public double accelNunX = 122;
	public double accelNunY = 122;
	public double accelNunZ = 139; // Gravitation
	public float yaw = 0;
	public float pitch = 0;

	private List<IrPoint> irPoints = new ArrayList<IrPoint>();
	public int mousePosX = 0;
	public int mousePosY = 0;

	public double accelZmoved = 0;

	public List<Float> yawList = new ArrayList<Float>();
	public List<Float> pitchList = new ArrayList<Float>();

	@Override
	public void buttonPressed(NunchukButtonEvent event) {
		synchronized (lock) {

			zpressed = event.isButtonZPressed();
			cpressed = event.isButtonCPressed();
		}
	}

	@Override
	public void buttonPressed(CoreButtonEvent event) {
		synchronized (lock) {
			this.apressed = event.isButtonAPressed();
			if (event.isButtonBPressed()) {
				if (!keepbpressed) {
					bpressed = true;
					keepbpressed = true;
				}
			} else {
				bpressed = false;
				keepbpressed = false;
			}

			if (event.isButtonMinusPressed() && !this.minuspressed) {
				--this.slotoffset;
			} else if (event.isButtonPlusPressed() && !this.pluspressed) {
				++this.slotoffset;
			}

			this.minuspressed = event.isButtonMinusPressed();
			this.pluspressed = event.isButtonPlusPressed();

			if (event.isButtonHomePressed()) {
				if (!keephomepressed) {
					homepressed = true;
					keephomepressed = true;
				}
			} else {
				homepressed = false;
				keephomepressed = false;
			}
		}
	}

	@Override
	public void analogStickChanged(AnalogStickEvent event) {
		synchronized (lock) {
			if (analogCenter.x == 0 && analogCenter.y == 0) {
				analogCenter = new Point(event.getPoint());
			}
			analogPoint = event.getPoint();
			analogDistance = analogPoint.distance(analogCenter);
			analogAngle = Math.atan2(analogPoint.x - analogCenter.x,
					analogPoint.y - analogCenter.y) * 180 / Math.PI;
		}
	}

	@Override
	public void extensionConnected(ExtensionEvent event) {
		synchronized (lock) {
			final Extension ext = event.getExtension();

			if (ext instanceof Nunchuk) {
				this.analogCenter = new Point(0, 0);
				MoteCraft.nunchuk = (Nunchuk) ext;
				MoteCraft.nunchuk.addAnalogStickListener(this);
				MoteCraft.nunchuk.addNunchukButtonListener(this);
				MoteCraft.nunchuk.addAccelerometerListener(this);

				MoteCraft.mote
						.setReportMode(ReportModeRequest.DATA_REPORT_0x37);
			}
		}
	}

	@Override
	public void extensionDisconnected(ExtensionEvent arg0) {
		synchronized (lock) {
			MoteCraft.mote.setReportMode(ReportModeRequest.DATA_REPORT_0x37);
		}
	}

	@Override
	public void accelerometerChanged(AccelerometerEvent event) {
		synchronized (lock) {
			if (event.getSource() instanceof Nunchuk) {
				this.accelNunX = event.getX();
				this.accelNunY = event.getY();
				this.accelNunZ = event.getZ();
				double x = (this.accelNunX - 128) / 60;
				double y = (this.accelNunY - 128) / 60;
				double z = (this.accelNunZ - 128) / 60;
				this.yawList.add((float) (Math.atan2(y,
						Math.sqrt(x * x + z * z)) * 180 / Math.PI));
				this.pitchList.add((float) (Math.atan2(-x,
						Math.sqrt(y * y + z * z)) * 180 / Math.PI));
			} else {
				this.accelX = event.getX();
				this.accelY = event.getY();
				this.accelZ = event.getZ();
				this.accelZmoved = event.getZ() - 139;
			}
		}
	}

	@Override
	public void irImageChanged(IrCameraEvent event) {
		synchronized (this) {
			mousePosX = 0;
			mousePosY = 0;
			int size = 0;
			for (int i = 0; i < 4; i++) {
				if (event.getIrPoint(i).x != 1023 && event.getIrPoint(i).y != 1023) {
					mousePosX += event.getIrPoint(i).x;
					mousePosY += event.getIrPoint(i).y;
					size++;
				}
			}
			if (size != 0) {
				mousePosX /= size;
				mousePosY /= size;
			}
		}
	}
}

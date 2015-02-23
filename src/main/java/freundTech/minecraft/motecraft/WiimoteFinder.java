package freundTech.minecraft.motecraft;

import motej.Mote;
import motej.MoteFinder;
import motej.MoteFinderListener;
import motej.event.MoteDisconnectedEvent;
import motej.event.MoteDisconnectedListener;
import motej.request.ReportModeRequest;

public class WiimoteFinder implements MoteFinderListener, MoteDisconnectedListener{	
	
	private Mote mote;
	private MoteFinder finder;
	private WiiMoteListener listener;
	
	public WiimoteFinder(WiiMoteListener listener) {
		finder = MoteFinder.getMoteFinder();
		finder.addMoteFinderListener(this);
		
		finder.startDiscovery();
		this.listener = listener;
	}
	
	@Override
	public void moteFound(Mote mote) {
		finder.stopDiscovery();
		MoteCraft.mote = mote;
		mote.addMoteDisconnectedListener(this);
		mote.addExtensionListener(listener);
		mote.addCoreButtonListener(listener);
		mote.addAccelerometerListener(listener);
		mote.addIrCameraListener(listener);
		mote.enableIrCamera();
		mote.setReportMode(ReportModeRequest.DATA_REPORT_0x37);
	}

	@Override
	public void moteDisconnected(MoteDisconnectedEvent arg0) {
		MoteCraft.mote = null;
		System.out.println("DISCONNECTED");
		finder.startDiscovery();
		
	}	
}

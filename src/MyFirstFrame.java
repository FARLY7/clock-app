import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MyFirstFrame extends Frame implements WindowListener{
	
	public static void main(String[] args) {
		new MyFirstFrame();
	}

	public MyFirstFrame(){
		this.add(new MyFirstCanvas());
		this.addWindowListener(this);
		this.setSize(906,928);
		this.setVisible(true);
	}


	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
	public void windowClosing(WindowEvent arg0) { System.exit(0); }
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}



}

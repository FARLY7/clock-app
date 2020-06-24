import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MyFirstCanvas extends Canvas implements MouseListener, MouseMotionListener, KeyListener{

	/* Used for checking the state of the Stop Clock */
	private static int ON = 0;
	private static int PAUSE = 1;
	private static int OFF = 2;
	/* Useful statics for when setting the clock hands initial time */
	private static long MILLIS_IN_MINUTE = 60 * 1000;
	private static long MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60;
	private static long MILLIS_IN_HALF_DAY = MILLIS_IN_HOUR * 12;	
	private static double SECS_IN_MINUTE_TO_ANGLE = (2 * Math.PI / 60);
	private static double MILLIS_IN_HOUR_TO_ANGLE = (2 * Math.PI / MILLIS_IN_HOUR);
	private static double MILLIS_IN_HALF_DAY_TO_ANGLE = (2 * Math.PI / MILLIS_IN_HALF_DAY);
	
	/* List of Cities for World Clock. In respective array slots according to GMT */
	private int GMTpos = 11;
	private String[] GMT = {
			"Midway Island", 		// -11
			"    Hawaii", 			// -10
			"    Alaska",			// -9
			"    Seattle",			// -8
			"    Denver",			// -7
			"   Chicago",			// -6
			"   New York",			// -5
			"   Santiago",			// -4
			"Buenos Aires",			// -3
			"Mid-Atlantic",			// -2
			"   Azores",			// -1
			"   London",			// 0
			" Amsterdam",			// +1
			"   Athens",			// +2
			"  Baghdad",			// +3
			"    Kabul",			// +4
			" New Delhi",			// +5
			" Sri Lanka",			// +6
			"  Bangkok",			// +7
			"    Beijing",			// +8
			"     Tokyo",			// +9
			"    Sydney",			// +10
			"  Magadan",			// +11
			"      Fiji" };			// +12	
	
	private G2DAbstractCanvas absCanvas = new G2DAbstractCanvas(900,900);
	private Image bufferImage;
	
	Calendar rightNow = Calendar.getInstance();

	private ArrayList<G2DObject> drawings = new ArrayList<G2DObject>();
	private G2DGroup thickMarkings = new G2DGroup();
	private G2DGroup thinMarkings = new G2DGroup();
	private G2DGroup stopClockThickMarkings = new G2DGroup();
	private G2DGroup stopClockThinMarkings = new G2DGroup();
	private G2DGroup worldClockThinMarkings = new G2DGroup();
	private G2DGroup worldClockThickMarkings = new G2DGroup();
	private G2DGroup numbers = new G2DGroup();
		
	/* ----------------- COLOURS ----------------- */
	private Color grey = new Color(91,92,96);
	private Color orange = new Color(253,151,50);
	private Color darkOrange = new Color(238,120,2);
	private Color darkGrey = new Color(80, 81, 84);
	private Color lightGrey = new Color(119,120,125);
	/* ------------------------------------------- */
		
	/* Clock Centres */
	private G2DPoint centre = new G2DPoint(450, 450);
	private G2DPoint stopClockCentre = new G2DPoint(300, 590);
	private G2DPoint worldClockCentre = new G2DPoint(600, 590);
	
	/* Clock 'Scene' Graphics */
	private G2DCircle border = new G2DCircle(centre, 430, orange);
	private G2DCircle border2 = new G2DCircle(centre, 440, darkOrange);
	private G2DCircle border3 = new G2DCircle(centre, 410, darkOrange);
	private G2DCircle clockFace = new G2DCircle(centre, 400, grey);
	private G2DCircle centreC = new G2DCircle(centre, 35, Color.BLACK);
	private G2DCircle centreC2 = new G2DCircle(centre, 20, grey);
	private G2DCircle stopClockFace = new G2DCircle(stopClockCentre, 75, lightGrey);
	private G2DCircle worldClockFace = new G2DCircle(worldClockCentre, 75, lightGrey);
	
	/* Clock Hands */
	private G2DPolygon secondHand = new G2DPolygon(Color.RED);
	private G2DPolygon minuteHand = new G2DPolygon(Color.BLACK);
	private G2DPolygon hourHand = new G2DPolygon(Color.BLACK);
	private G2DPolygon stopClockSecHand = new G2DPolygon(Color.RED);
	private G2DPolygon stopClockMinHand = new G2DPolygon(Color.BLACK);
	private G2DPolygon worldClockHourHand = new G2DPolygon(Color.BLACK);

	private G2DString stopClockTime = new G2DString("00.00.000", 260, 630, 21, Color.BLACK);
	private G2DString worldClockType = new G2DString(GMT[GMTpos], 552, 630, 19, Color.BLACK);
	/* Stop Clock Lap Logs */
	private G2DString lap1 = new G2DString("", 20, 800, 19, Color.WHITE);
	private G2DString lap2 = new G2DString("", 20, 817, 19, Color.WHITE);
	private G2DString lap3 = new G2DString("", 20, 834, 19, Color.WHITE);
	private G2DString lap4 = new G2DString("", 20, 851, 19, Color.WHITE);
	
	/* Day of the Month */
	private G2DRectangle dayBox = new G2DRectangle(centre.getX()-25, centre.getY()+160, centre.getX()+25, centre.getY()+210, Color.DARK_GRAY);
	private G2DRectangle dayBox1 = new G2DRectangle(centre.getX()-27, centre.getY()+158, centre.getX()+27, centre.getY()+212, darkOrange);
	private G2DString day = new G2DString("", centre.getX()-21, centre.getY()+200, 45, Color.WHITE);
	
	/* Variables storing the previous (x,y) of the mouse, and the 'dragged-too' (x,y) */ 
	private double x1, y1, x2, y2;
	/* Storing the time for the Stop Clock */
	private int milli, sec, min, lapCount;
	/* Variables used to determing which Clock Hand is being clicked and dragged */
	private boolean draggingMin = false;
	private boolean draggingSec = false;
	private boolean draggingHour = false;
	
	private Thread stopClock;
	private Thread keepTime;
	private int stopClockState = OFF;
	
	public MyFirstCanvas()
	{	
		this.setBackground(Color.BLACK);
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.addMouseMotionListener(this);
		this.setFocusable(true);
		createMarkings();
		createHands();
		createStopClockHands();
		createNumbers();
		
		/* Main Clock Drawings */
		drawings.add(border2);
		drawings.add(border);
		drawings.add(border3);
		drawings.add(clockFace);
		drawings.add(thickMarkings);
		drawings.add(thinMarkings);
		/* Stop Clock Drawings */
		drawings.add(stopClockFace);
		drawings.add(stopClockMinHand);
		drawings.add(stopClockTime);
		drawings.add(stopClockSecHand);
		drawings.add(stopClockThinMarkings);
		drawings.add(stopClockThickMarkings);
		/* World Clock Drawings */
		drawings.add(worldClockFace);
		drawings.add(worldClockThinMarkings);
		drawings.add(worldClockThickMarkings);
		drawings.add(worldClockType);
		drawings.add(worldClockHourHand);
		/* Roman Numeral Numbers */
		drawings.add(numbers);
		/* Date of the Month */
		drawings.add(dayBox1);
		drawings.add(dayBox);
		drawings.add(day);
		/* Main Clock Hands */
		drawings.add(minuteHand);
		drawings.add(hourHand);
		drawings.add(secondHand);
		/* Graphics */
		drawings.add(centreC);
		drawings.add(centreC2);
		/* Lap Log Strings */
		drawings.add(lap1);
		drawings.add(lap2);
		drawings.add(lap3);
		drawings.add(lap4);
		setHands();
		
		keepTime = new Thread(timeKeeping);	
		stopClock = new Thread(stopClockTimer);
		keepTime.start();
	}
	
	
	private boolean painting = false;
	public void paint(Graphics g)
	{		
		if(!painting){
			painting = true;
			bufferImage = createImage(getWidth(), getHeight());
			Graphics buffer = bufferImage.getGraphics();
			absCanvas.setPhysicalDisplay(getWidth(), getHeight(), buffer); 
			buffer.clearRect(0, 0, getWidth(), getHeight());
			for (G2DObject drawing : drawings )
				drawing.draw(absCanvas);	
			g.drawImage(bufferImage, 0, 0, null);
			painting = false; 
		}
		
	}
	
	public void createMarkings()
	{	
		for( int i = 0 ; i < 12 ; i++){
			thickMarkings.add(new G2DRectangle( new G2DPoint(centre.getX()-5, centre.getY()-380), new G2DPoint(centre.getX()+5, centre.getY()-330)));
			stopClockThickMarkings.add( new G2DCircle(stopClockCentre.getX(), stopClockCentre.getY()-75, 4) );
			worldClockThickMarkings.add( new G2DCircle(worldClockCentre.getX(), worldClockCentre.getY()-75, 4) );
		}
		for( int i = 0 ; i < 60 ; i++){
			thinMarkings.add(new G2DRectangle( new G2DPoint(centre.getX()-1, centre.getY()-380), new G2DPoint(centre.getX()+1, centre.getY()-350)));
			stopClockThinMarkings.add( new G2DCircle(stopClockCentre.getX(), stopClockCentre.getY()-75, 1) );
			worldClockThinMarkings.add( new G2DCircle(worldClockCentre.getX(), worldClockCentre.getY()-75, 1));
		}	
		
		/* ------------------- THICK MAIN MARKINGS ------------------- */
		int degrees = 0;
		for( G2DObject mark : thickMarkings.objects)
		{
			mark.transform(MatrixTransform.rotate(centre.getX(), centre.getY(), degrees));
			degrees += 30;
		}
		/* ------------------- THIN MAIN MARKINGS ------------------- */
		degrees = 0;
		for( G2DObject mark : thinMarkings.objects)
		{
			mark.transform(MatrixTransform.rotate(centre.getX(), centre.getY(), degrees));
			degrees += 6;
		}				
		/* ----------------- STOP CLOCK THICK MARKINGS --------------- */
		degrees = 0;
		for( G2DObject mark : stopClockThickMarkings.objects)
		{
			mark.transform(MatrixTransform.rotate(stopClockCentre.getX(), stopClockCentre.getY(), degrees));
			degrees += 30;
		}		
		/* ----------------- STOP CLOCK THIN MARKINGS --------------- */
		degrees = 0;
		for( G2DObject mark : stopClockThinMarkings.objects)
		{
			mark.transform(MatrixTransform.rotate(stopClockCentre.getX(), stopClockCentre.getY(), degrees));
			degrees += 6;
		}
		/* ----------------- WORLD CLOCK THICK MARKINGS --------------- */
		degrees = 0;
		for( G2DObject mark : worldClockThickMarkings.objects)
		{
			mark.transform(MatrixTransform.rotate(worldClockCentre.getX(), worldClockCentre.getY(), degrees));
			degrees += 30;
		}		
		/* ----------------- WORLD CLOCK THIN MARKINGS --------------- */
		degrees = 0;
		for( G2DObject mark : worldClockThinMarkings.objects)
		{
			mark.transform(MatrixTransform.rotate(worldClockCentre.getX(), worldClockCentre.getY(), degrees));
			degrees += 6;
		}
	}
	
	public void createHands()
	{
		secondHand.addPoint(centre.getX()-5, centre.getY()+30);
		secondHand.addPoint(centre.getX(), centre.getY()-310);
		secondHand.addPoint(centre.getX()+5, centre.getY()+30);

		minuteHand.addPoint(centre.getX()-5, centre.getY()+30);
		minuteHand.addPoint(centre.getX()-5, centre.getY()-150);		
		minuteHand.addPoint(centre.getX()-12, centre.getY()-200);
		minuteHand.addPoint(centre.getX(), centre.getY()-310);
		minuteHand.addPoint(centre.getX()+12, centre.getY()-200);
		minuteHand.addPoint(centre.getX()+5, centre.getY()-150);
		minuteHand.addPoint(centre.getX()+5, centre.getY()+30);
		
		hourHand.addPoint(centre.getX()-6, centre.getY()+30);
		hourHand.addPoint(centre.getX()-6, centre.getY()-150);
		hourHand.addPoint(centre.getX()-15, centre.getY()-200);
		hourHand.addPoint(centre.getX(), centre.getY()-270);
		hourHand.addPoint(centre.getX()+15, centre.getY()-200);
		hourHand.addPoint(centre.getX()+6, centre.getY()-150);
		hourHand.addPoint(centre.getX()+6, centre.getY()+30);
		
		worldClockHourHand.addPoint(worldClockCentre.getX()-3, worldClockCentre.getY()+5);
		worldClockHourHand.addPoint(worldClockCentre.getX(), worldClockCentre.getY()-60);
		worldClockHourHand.addPoint(worldClockCentre.getX()+3, worldClockCentre.getY()+5);
	}

	public void setHands()
	{
		long t = rightNow.getInstance().getTimeInMillis();
		// milliseconds in the current minute
		long minute = t % MILLIS_IN_MINUTE;
		// seconds in the current minute
		long secs_min = minute / 1000L;
		// milliseconds in the current hour
		long hour = t % MILLIS_IN_HOUR;
		// milliseconds in the current half day
		long hday = t % MILLIS_IN_HALF_DAY;
		/* Calculate the clock hand angles */
		double sec_angle = -Math.PI / 2 + secs_min * SECS_IN_MINUTE_TO_ANGLE;
		double min_angle = -Math.PI / 2 + hour * MILLIS_IN_HOUR_TO_ANGLE;
		double hour_angle = -Math.PI / 2 + hday * MILLIS_IN_HALF_DAY_TO_ANGLE;
		
		secondHand.transform(MatrixTransform.rotate(centre.getX(), centre.getY(),  Math.toDegrees(sec_angle)+90));
		minuteHand.transform(MatrixTransform.rotate(centre.getX(), centre.getY(),  Math.toDegrees(min_angle)+90));
		hourHand.transform(MatrixTransform.rotate(centre.getX(), centre.getY(),  Math.toDegrees(hour_angle)+90));
		worldClockHourHand.transform(MatrixTransform.rotate(worldClockCentre.getX(), worldClockCentre.getY(),  Math.toDegrees(hour_angle)+90));
	
	}
	
	public void createStopClockHands()
	{
		/* Stop Clock Second Hand */
		stopClockSecHand.addPoint(stopClockCentre.getX()-3, stopClockCentre.getY()+5);
		stopClockSecHand.addPoint(stopClockCentre.getX(), stopClockCentre.getY()-60);
		stopClockSecHand.addPoint(stopClockCentre.getX()+3, stopClockCentre.getY()+5);
		/* Stop Clock Minute Hand */
		stopClockMinHand.addPoint(stopClockCentre.getX()-3, stopClockCentre.getY()+5);
		stopClockMinHand.addPoint(stopClockCentre.getX(), stopClockCentre.getY()-60);
		stopClockMinHand.addPoint(stopClockCentre.getX()+3, stopClockCentre.getY()+5);	
	}

	public void createNumbers()
	{	
		G2DString no12 = new G2DString("XII", centre.getX()-50, centre.getY()-260, 75, Color.BLACK);	numbers.add(no12);
		G2DString no3 = new G2DString("III", centre.getX()+225, centre.getY()+25, 75, Color.BLACK);		numbers.add(no3);
		G2DString no6 = new G2DString("VI", centre.getX()-42, centre.getY()+300, 75, Color.BLACK);		numbers.add(no6);
		G2DString no9 = new G2DString("IX", centre.getX()-310, centre.getY()+25, 75, Color.BLACK);		numbers.add(no9);	
		day.setText(""+rightNow.getInstance().get(Calendar.DAY_OF_MONTH));
	}


	Runnable stopClockTimer = new Runnable(){
		public void run(){
			
			milli = 0; sec = 0; min = 0;		
			try {		
				while(true)
				{
					Thread.sleep(100);
					stopClockSecHand.transform(MatrixTransform.rotate(stopClockCentre.getX(), stopClockCentre.getY(), 0.6));
					stopClockMinHand.transform(MatrixTransform.rotate(stopClockCentre.getX(), stopClockCentre.getY(), 0.01));
					
					milli += 1;
					if(milli == 10) { sec += 1; milli = 0; }
					if(sec == 60)   { min += 1; sec = 0;   }
						
					if(sec < 10) stopClockTime.setText("0"+ min +".0"+ sec +"."+ milli +"00");
					else stopClockTime.setText("0"+ min +"."+ sec +"."+ milli +"00");
					paint(getGraphics());
				}
			} catch (InterruptedException e) {e.printStackTrace(); }
		}
	};
	
	Runnable timeKeeping = new Runnable()
	{
		public void run()
		{
			try {
				while(true)
				{					
					Thread.sleep(100);	
					secondHand.transform(MatrixTransform.rotate(centre.getX(), centre.getY(), 0.6));
					minuteHand.transform(MatrixTransform.rotate(centre.getX(), centre.getY(), 0.01));
					hourHand.transform(MatrixTransform.rotate(centre.getX(), centre.getY(), 0.001667));
					
					if(stopClockState == OFF || stopClockState == PAUSE){
						paint(getGraphics()); /* Paint only called in one thread at a time */
					}
				}
			} catch (InterruptedException e) { e.printStackTrace(); }
		}
	};
	
	
	public double getAngle(double x, double y)
	{	
		double angle = Math.atan( y / x );
		
		if (x < 0 && y >= 0) angle += Math.PI;
		else if (x < 0 && y < 0) angle += Math.PI;
		else if (x >= 0 && y < 0) angle += 2*Math.PI;

		return Math.toDegrees(angle);
	}
	
	
	public void mouseClicked(MouseEvent e)	{}
	public void mouseDragged(MouseEvent e)
	{
		x2 = absCanvas.abstractX(e.getX()) - centre.getX();
		y2 = absCanvas.abstractY(e.getY()) - centre.getY();

		double angle = getAngle(x2, y2) - getAngle(x1, y1);

		if(draggingMin == true){
			minuteHand.transform(MatrixTransform.rotate(centre.getX(), centre.getY(), angle));
			paint(getGraphics());
		}
		else if(draggingHour == true){
			hourHand.transform(MatrixTransform.rotate(centre.getX(), centre.getY(), angle));
			worldClockHourHand.transform(MatrixTransform.rotate(worldClockCentre.getX(), worldClockCentre.getY(), angle));
			paint(getGraphics());
		}
		else if(draggingSec == true){
			secondHand.transform(MatrixTransform.rotate(centre.getX(), centre.getY(), angle));
			paint(getGraphics());
		}
		
		x1 = x2;
		y1 = y2;		
	}
	public void mouseMoved(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent e) {
		
		x1 = absCanvas.abstractX(e.getX()) - centre.getX();
		y1 = absCanvas.abstractY(e.getY()) - centre.getY();
		
		if( minuteHand.isInside(e.getX(), e.getY()) ){
			draggingMin = true;
			keepTime.suspend();
			painting = false;
		}
		else if( hourHand.isInside(e.getX(), e.getY()) ){
			draggingHour = true;
			keepTime.suspend();
			painting = false;
		}
		else if( secondHand.isInside(e.getX(), e.getY()) ){
			draggingSec = true;
			keepTime.suspend();
			painting = false;
		}

	}
	public void mouseReleased(MouseEvent arg0) {
		if(draggingMin == true || draggingSec == true || draggingHour == true){
			draggingMin = false;
			draggingHour = false;
			draggingSec = false;
			keepTime.resume();
		}
	}

	public void keyPressed(KeyEvent e) {
	
		if( e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			if(stopClockState == OFF){
				if(!stopClock.isAlive()) stopClock.start();
				else stopClock.resume();
				stopClockState = ON;
			}
			else if (stopClockState == ON) {
				stopClock.suspend();
				stopClockState = PAUSE;
			}
			else if( stopClockState == PAUSE) {
				stopClockSecHand = new G2DPolygon(Color.RED);
				stopClockMinHand = new G2DPolygon(Color.BLACK);
				createStopClockHands();
				drawings.set(9, stopClockSecHand);
				drawings.set(7, stopClockMinHand);
				lapCount = 0; milli = 0; sec = 0; min = 0;
				stopClockTime.setText("00.00.000");
				lap1.setText(""); lap2.setText(""); lap3.setText(""); lap4.setText("");
				stopClockState = OFF;
			}
		}
		else if( e.getKeyCode() == KeyEvent.VK_L)
		{
			if(stopClockState == ON)
			{										
				switch (lapCount){	
				case 0: if( sec < 10 ) lap1.setText("Lap 1: 0"+min+".0"+sec+"."+milli+"00");
						else lap1.setText("Lap 1: 0"+min+"."+sec+"."+milli+"00"); break;
				
				case 1: if( sec < 10 ) lap2.setText("Lap 2: 0"+min+".0"+sec+"."+milli+"00");
						else lap2.setText("Lap 2: 0"+min+"."+sec+"."+milli+"00"); break;
						
				case 2: if( sec < 10 ) lap3.setText("Lap 3: 0"+min+".0"+sec+"."+milli+"00");
						else lap3.setText("Lap 1: 0"+min+".0"+sec+"."+milli+"00"); break;
				
				case 3: if( sec < 10 ) lap4.setText("Lap 4: 0"+min+".0"+sec+"."+milli+"00");
						else lap4.setText("Lap 4: 0"+min+".0"+sec+"."+milli+"00"); break; }
				lapCount += 1;			
			}
		}
		else if( e.getKeyCode() == KeyEvent.VK_UP)
		{			
			if(GMTpos == 23) GMTpos = 0; 
			else GMTpos += 1;
			worldClockType.setText(GMT[GMTpos]);	
			worldClockHourHand.transform(MatrixTransform.rotate(worldClockCentre.getX(), worldClockCentre.getY(), 30));
		}
		else if( e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			if(GMTpos == 0) GMTpos = 23; 
			else GMTpos -= 1;
			worldClockType.setText(GMT[GMTpos]);
			worldClockHourHand.transform(MatrixTransform.rotate(worldClockCentre.getX(), worldClockCentre.getY(), -30));
		}
		
	}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}

}


public class Bus implements Runnable{
	
	public Bus(){
		Thread t = new Thread(this);
		t.start();
	}
	
	final double gear1 = .006;
	final double gear2 = .01;
	final double gear3 = .016;
	final double gear4 = .023;
	
	int speed = 0;
	int engineRPM = 500;
	double totalMiles = 0;
	double fuelGallons = 25;
	boolean on = true;
	boolean doorOpen = false;;
//	final int MAX_SPEED = 80; //a little redundant
	final int MAX_RPM = 4000;
	final int NUM_GEARS = 4;
	int currentGear = 1;

	double busHealth = 100;
	
	public void damageBus(){
		busHealth--;
	}
	
	public void accelerate(){
		
		if(Window.u && ((engineRPM) <= MAX_RPM)){
			engineRPM += 40/currentGear;
			
		} else if(Window.d){
			if(engineRPM < 500){
				engineRPM = 0;
			} else {
				engineRPM -= 75;
			}
		} else {
			
			if(engineRPM < 500){
				engineRPM = 0;
			} else {
				engineRPM -= 25;
			}
			
		}
		
			switch(currentGear){
			
			case 1:				
				if(engineRPM >= 3500){
					shiftUp();
				}
				
				break;
			case 2:
				if(engineRPM <= 700){
					shiftDown();
				}
				
				if(engineRPM >= 3700){
					shiftUp();
				}
				break;
			case 3:
				if(engineRPM <= 700){
					shiftDown();
				}
				
				if(engineRPM >= 4000){
					shiftUp();
				}
				break;
			case 4:
				if(engineRPM <= 700){
					shiftDown();
				}
				
				if(speed >= 80){
					engineRPM = engineRPM-25;
				}
				break;
			
			}
			
			calculateSpeed();
	}
	
	public int calculateSpeed(){
		if(currentGear == 1){
			return (int) (engineRPM * gear1);
		} else if(currentGear == 2){
			return (int) (engineRPM * gear2);
		} else if(currentGear == 3){
			return (int) (engineRPM * gear3);
		} else if(currentGear == 4){
			return (int) (engineRPM * gear4);
		} else {
			return 0;
		}
	}
	
	public void shiftUp(){
		if(currentGear == 1){
			engineRPM = (int) (speed / gear2);
			currentGear = 2;
		} else if(currentGear == 2){
			engineRPM = (int) (speed/ gear3);
			currentGear = 3;
		} else if(currentGear == 3){
			engineRPM = (int) (speed / gear4);
			currentGear = 4;
		}
	}
	
	public void shiftDown(){
		if(currentGear == 4){
			engineRPM = (int) (speed / gear3);
			currentGear = 3;
		} else if(currentGear == 3){
			engineRPM = (int) (speed / gear2);
			currentGear = 2;
		} else if(currentGear == 2){
			engineRPM = (int) (speed / gear3);
			currentGear = 1;
		}
	}

	@Override
	public void run() {
		while(true){			
			double add = speed / 1200.0;
			
			totalMiles += add;
//			System.out.println("TOTAL MILES" + totalMiles);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}

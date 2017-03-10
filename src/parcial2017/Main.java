package parcial2017;

import processing.core.PApplet;

public class Main extends PApplet{
	
	private Logica app;
	
	public static void main(String[] args){
		PApplet.main("parcial2017.Main");
	}
	
	@Override
	public void settings(){
		size(300,300);
	}
	
	@Override
	public void setup(){
		app = new Logica(this);
	}
	
	@Override
	public void draw(){
		background(255);
		app.pintar();
	}

}

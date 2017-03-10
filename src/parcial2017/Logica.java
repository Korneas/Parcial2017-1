package parcial2017;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;

public class Logica implements Observer {

	private PApplet app;
	private Comunicacion c;
	private final String GROUP_ADDRESS = "226.24.6.8";

	private int id;
	private boolean start, iniciar;
	private float r, g, b;
	private float[] col;

	private ArrayList<Bola> bolas;
	private ArrayList<Cuadrado> quads;

	public Logica(PApplet app) {
		this.app = app;

		c = new Comunicacion();
		Thread nt = new Thread(c);
		nt.start();

		c.addObserver(this);

		col = new float[3];

		if (c.getId() != 0) {
			id = c.getId();
			r = app.random(200);
			g = app.random(200);
			b = app.random(200);

			col[0] = r;
			col[1] = g;
			col[2] = b;
		}

		bolas = new ArrayList<Bola>();
		quads = new ArrayList<Cuadrado>();

		if (id == 3) {
			try {
				c.enviar(new Movement(id, "comenzar", col), GROUP_ADDRESS);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void pintar() {

		if (app.frameCount % 12 == 0) {
			start = true;
		}

		app.fill(col[0], col[1], col[2]);
		app.stroke(0);
		app.strokeWeight(1);
		app.rectMode(PApplet.CENTER);
		app.rect(app.width / 2, 50, 30, 30);
		app.fill(255);
		app.textAlign(PApplet.CENTER);
		app.text(id, app.width / 2, 55);

		if (id >= 4) {
			app.fill(0);
			app.textAlign(PApplet.CENTER);
			app.text("Jugadores completos :(\nYa no puedes jugar", app.width / 2, app.height / 2);
		}

		for (int i = 0; i < bolas.size(); i++) {
			bolas.get(i).pintar();
		}

		for (int i = 0; i < quads.size(); i++) {
			quads.get(i).pintar();
		}

		if (iniciar) {
			if (app.mousePressed) {
				if (id == 1) {
					try {
						c.enviar(new Movement(id, app.mouseX + ":" + app.mouseY, col), GROUP_ADDRESS);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (id == 2) {
					try {
						c.enviar(new Movement(id, "eliminar", col), GROUP_ADDRESS);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (id == 3) {
					try {
						c.enviar(new Movement(id, app.mouseX + ":" + app.mouseY, col), GROUP_ADDRESS);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				start = false;
			}
		} else if(!iniciar && id<4){
			app.fill(0);
			app.textAlign(PApplet.CENTER);
			app.text("Esperando jugadores", app.width / 2, app.height / 2);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Movement) {
			Movement mov = (Movement) arg;
			if (id < 4) {
				if (mov.getEmisor() == 1) {
					if (mov.getContenido().contains(":")) {
						String[] pos = mov.getContenido().split(":");
						int xE = Integer.parseInt(pos[0]);
						int yE = Integer.parseInt(pos[1]);
						bolas.add(new Bola(app, xE, yE, mov.getColor()));
					}
				}

				if (mov.getEmisor() == 2) {
					if (mov.getContenido().contains("eliminar")) {
						bolas.clear();
						quads.clear();
					}
				}

				if (mov.getEmisor() == 3) {
					if (mov.getContenido().contains(":")) {
						String[] pos = mov.getContenido().split(":");
						int xE = Integer.parseInt(pos[0]);
						int yE = Integer.parseInt(pos[1]);
						quads.add(new Cuadrado(app, xE, yE, mov.getColor()));
					}

					if (mov.getContenido().contains("comenzar")) {
						iniciar = true;
					}
				}
			}
		}
	}
}

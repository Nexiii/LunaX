package io.github.nx.exampleGame;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import io.github.nx.LunaX.engine.AbstractGame;
import io.github.nx.LunaX.engine.GameContainer;
import io.github.nx.LunaX.engine.Renderer;
import io.github.nx.LunaX.engine.audio.SoundClip;
import io.github.nx.LunaX.engine.gfx.Font;
import io.github.nx.LunaX.engine.gfx.Image;
import io.github.nx.LunaX.engine.serialization.Storage;

public class SpaceGame extends AbstractGame {

	private static String TITLE = "LunaX | Space Defender";

	private boolean inMenu = true; 

	private Player player;
	private ArrayList<Bullet> bullets = new ArrayList<>();
	private ArrayList<Enemy> enemies = new ArrayList<>();
	private ArrayList<Asteroid> asteroids = new ArrayList<>();
	private ArrayList<Particle> particles = new ArrayList<>();
	private ArrayList<EnemyBullet> enemyBullets = new ArrayList<>();

	private Image crosshair;
	private Image enemyImg;
	private Image enemyShootingImg;
	private Image asteroidImg;

	private Console console;

	private SoundClip shootSound;
	private SoundClip explodeSound;

	private float enemySpawnTimer = 0;
	private float asteroidSpawnTimer = 0;

	private float soundCooldown = 0;
	private int score = 0;
	private int highscore = 0;
	private boolean gameOver = false;

	private float[] starX = new float[100];
	private float[] starY = new float[100];
	private float[] starSpeed = new float[100];

	@Override
	public void init(GameContainer gc) {
		Storage.init("save.bin");
		highscore = Storage.read("highscore", 0);

		enemyImg = new Image("/sprites/enemy.png");
		enemyShootingImg = new Image("/sprites/enemyShooting.png");
		asteroidImg = new Image("/sprites/asteroid.png");
		crosshair = new Image("/sprites/crosshair.png");
		
		player = new Player(gc.getWidth() / 2, gc.getHeight() - 50, this);
		player.x = gc.getWidth() / 2 - (player.width / 2);
		
		console = new Console(gc);
		console.log("Engine loaded.");

		shootSound = new SoundClip("/sounds/shoot.wav");
		shootSound.setVolume(0.5f);

		explodeSound = new SoundClip("/sounds/death.wav");
		explodeSound.setVolume(0.5f);

		for (int i = 0; i < starX.length; i++) {
			starX[i] = (float) (Math.random() * gc.getWidth());
			starY[i] = (float) (Math.random() * gc.getHeight());
			starSpeed[i] = (float) (Math.random() * 150 + 20);
		}
	}

	@Override
	public void update(GameContainer gc, float dt) {
		console.update(gc, dt);
		if (gc.getInput().isKeyDown(KeyEvent.VK_F1)) {
			console.toggle();
			try { Thread.sleep(200); } catch (Exception e) {}
		}
		if (gc.getInput().isKey(KeyEvent.VK_ALT) && gc.getInput().isKeyDown(KeyEvent.VK_ENTER)) {
			gc.toggleFullscreen();
			try { Thread.sleep(200); } catch (Exception e) {}
		}
		if (console.isOpen()) return;

		for (int i = 0; i < starX.length; i++) {
			starY[i] += starSpeed[i] * dt;
			if (starY[i] > gc.getHeight()) {
				starY[i] = 0;
				starX[i] = (float) (Math.random() * gc.getWidth());
			}
		}
		if (inMenu) {
			if (gc.getInput().isKeyDown(KeyEvent.VK_ENTER)) {
				inMenu = false;
				console.log("Game Start!");
			}
			return;
		}
		soundCooldown -= dt;
		if (gameOver) {
			if (score > highscore) {
				if (highscore != score) console.log("New Highscore! " + score);
				highscore = score;
				Storage.add("highscore", highscore);
				Storage.save();
			}

			if (gc.getInput().isKey(KeyEvent.VK_ENTER)) {
				console.log("Restart...");
				resetGame(gc);
			}
			return;
		}
		player.update(gc, this, dt);

		asteroidSpawnTimer += dt;
		if (asteroidSpawnTimer > 4.0f) {
			float randX = (float) (Math.random() * (gc.getWidth() - 40));
			asteroids.add(new Asteroid(randX, -50, asteroidImg));
			asteroidSpawnTimer = 0;
		}

		for (int i = asteroids.size() - 1; i >= 0; i--) {
			Asteroid a = asteroids.get(i);
			a.update(dt);

			if (checkCollision(player.x, player.y, player.getImage(), a.x, a.y, asteroidImg)) {
				gameOver = true;
				console.log("Crashed into Asteroid!");
			}

			for (int j = bullets.size() - 1; j >= 0; j--) {
				Bullet b = bullets.get(j);
				if (b.x < a.x + a.width && b.x + b.width > a.x && b.y < a.y + a.height && b.y + b.height > a.y) {
					if (checkPixelHit(b.x, b.y, asteroidImg, a.x, a.y)) {
						b.remove = true;
						spawnExplosion(b.x, b.y);
						break;
					}
				}
			}

			for (Enemy e : enemies) {
				if (!e.remove) {
					if (e.x < a.x + a.width && e.x + e.width > a.x && e.y < a.y + a.height && e.y + e.height > a.y) {
						e.remove = true;
						if (soundCooldown <= 0.05f) explodeSound.play();
						spawnExplosion(e.x + e.width / 2, e.y + e.height / 2);
					}
				}
			}

			if (a.remove) asteroids.remove(i);
		}

		enemySpawnTimer += dt;
		float spawnRate = 1.5f - (score * 0.01f);
		if (spawnRate < 0.4f) spawnRate = 0.4f;

		if (enemySpawnTimer > spawnRate) {
			float randX = (float) (Math.random() * (gc.getWidth() - 40)) + 20;
			int enemyType = 0;
			Image imgToUse = enemyImg;

			if (score > 50 && Math.random() < 0.3) enemyType = 1;
			if (score >= 250 && Math.random() < 0.2) {
				enemyType = 2;
				imgToUse = enemyShootingImg;
			}
			enemies.add(new Enemy(randX, -50, imgToUse, enemyType));
			enemySpawnTimer = 0;
		}

		for (int i = enemyBullets.size() - 1; i >= 0; i--) {
			EnemyBullet eb = enemyBullets.get(i);
			eb.update(dt);

			if (player.x < eb.x + eb.width && player.x + player.width > eb.x && 
				player.y < eb.y + eb.height && player.y + player.height > eb.y) {
				
				if (checkPixelHit(eb.x, eb.y, player.getImage(), player.x, player.y)) {
					eb.remove = true;
					if (soundCooldown <= 0.05f) explodeSound.play();
					gameOver = true;
				}
			}
			if (eb.remove) enemyBullets.remove(i);
		}

		for (int i = bullets.size() - 1; i >= 0; i--) {
			Bullet b = bullets.get(i);
			b.update(dt);
			if (b.remove) bullets.remove(i);
		}
		for (int i = particles.size() - 1; i >= 0; i--) {
			Particle p = particles.get(i);
			if (!p.update(dt)) particles.remove(i);
		}

		for (int i = enemies.size() - 1; i >= 0; i--) {
			Enemy e = enemies.get(i);
			e.update(dt, this);

			if (e.y + e.height >= gc.getHeight()) {
				console.log("Game Over (Enemy Limit)!");
				gameOver = true;
			}
			
			Image currentEnemyImg = (e.getType() == 2) ? enemyShootingImg : enemyImg; 

			for (int j = bullets.size() - 1; j >= 0; j--) {
				Bullet b = bullets.get(j);
				
				if (b.x < e.x + e.width && b.x + b.width > e.x && b.y < e.y + e.height && b.y + b.height > e.y) {
					boolean hit = false;
					if (checkPixelHit(b.x + b.width/2, b.y + b.height/2, currentEnemyImg, e.x, e.y)) hit = true;
					else if (checkPixelHit(b.x, b.y, currentEnemyImg, e.x, e.y)) hit = true;
					else if (checkPixelHit(b.x + b.width, b.y + b.height, currentEnemyImg, e.x, e.y)) hit = true;

					if (hit) {
						if (soundCooldown <= 0.05f) explodeSound.play();
						spawnExplosion(e.x + e.width / 2, e.y + e.height / 2);
						e.remove = true;
						b.remove = true;
						score += 5;
						break;
					}
				}
			}
			if (e.remove) enemies.remove(i);
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.clear();

		for (int i = 0; i < starX.length; i++) {
			int col = (starSpeed[i] > 100) ? 0xffffffff : 0xff666666;
			r.setPixel((int) starX[i], (int) starY[i], col);
		}

		for (Particle p : particles) p.render(r);

		r.drawImage(crosshair, gc.getInput().getMouseX() - (crosshair.getWidth() / 2),
				gc.getInput().getMouseY() - (crosshair.getHeight() / 2), false);

		if (inMenu) {
			int cx = gc.getWidth() / 2;
			int cy = gc.getHeight() / 2;

			r.drawString("SPACE DEFENDER", cx - 60, cy - 30, 0xff00ffff, Font.ARIAL, 1.0f);

			if ((System.currentTimeMillis() / 500) % 2 == 0) {
				r.drawString("PRESS ENTER", cx - 45, cy + 30, 0xffffffff, Font.ARIAL, 1.0f);
			}

			r.drawImage(enemyImg, cx - 100, cy, false);
			r.drawImage(enemyShootingImg, cx + 80, cy, false);

			if (console.isOpen()) console.render(r, gc);
			return;
		}

		for (Asteroid a : asteroids) a.render(r);
		for (EnemyBullet eb : enemyBullets) eb.render(r);

		if (gameOver) {
			int cx = gc.getWidth() / 2;
			int cy = gc.getHeight() / 2;
			r.drawString("GAME OVER", cx - 40, cy - 24, 0xffff0000, Font.ARIAL, 1.0f);
			r.drawString("Score: " + score, cx - 40, cy, 0xffffff00, Font.ARIAL, 1.0f);
			r.drawString("Highscore: " + highscore, cx - 40, cy + 12, 0xff00ff00, Font.ARIAL, 1.0f);
			r.drawString("Press ENTER", cx - 40, cy + 24, 0xffffffff, Font.ARIAL, 1.0f);
		} else {
			player.render(r, this);
			for (Bullet b : bullets) b.render(r);
			for (Enemy e : enemies) e.render(r);

			r.drawString("Score: " + score, 0, 0, 0xffffffff, Font.ARIAL, 1.0f);
			if (score > highscore) {
				r.drawString("Highscore: " + score, 0, 16, 0xff00ff00, Font.ARIAL, 1.0f);
				Storage.save();
			} else {
				r.drawString("Highscore: " + highscore, 0, 16, 0xffaaaaaa, Font.ARIAL, 1.0f);
			}
			r.drawString("FPS:" + gc.getFps(), 0, 32, 0xffffffff, Font.ARIAL, 1.0f);
		}
		if (console.isOpen()) console.render(r, gc);
	}
	
	private void resetGame(GameContainer gc) {
		enemies.clear();
		bullets.clear();
		particles.clear();
		asteroids.clear();
		enemyBullets.clear();
		score = 0;
		player.x = gc.getWidth() / 2 - (player.width / 2);
		enemySpawnTimer = 0;
		gameOver = false;
		highscore = Storage.read("highscore", 0);
	}

	public boolean checkCollision(float x1, float y1, Image img1, float x2, float y2, Image img2) {
		if (img1 == null || img2 == null) return false;
		if (x1 >= x2 + img2.getWidth() || x1 + img1.getWidth() <= x2 ||
			y1 >= y2 + img2.getHeight() || y1 + img1.getHeight() <= y2) {
			return false;
		}

		int left = (int) Math.max(x1, x2);
		int right = (int) Math.min(x1 + img1.getWidth(), x2 + img2.getWidth());
		int top = (int) Math.max(y1, y2);
		int bottom = (int) Math.min(y1 + img1.getHeight(), y2 + img2.getHeight());

		int[] pixels1 = img1.getPixels();
		int[] pixels2 = img2.getPixels();
		int w1 = img1.getWidth();
		int w2 = img2.getWidth();

		for (int y = top; y < bottom; y++) {
			for (int x = left; x < right; x++) {
				int p1x = x - (int)x1;
				int p1y = y - (int)y1;
				int p2x = x - (int)x2;
				int p2y = y - (int)y2;
				int color1 = pixels1[p1x + p1y * w1];
				int color2 = pixels2[p2x + p2y * w2];
				if ((color1 & 0xff000000) != 0 && (color2 & 0xff000000) != 0) return true;
			}
		}
		return false;
	}
	
	public boolean checkPixelHit(float bx, float by, Image targetImg, float targetX, float targetY) {
		int relX = (int)bx - (int)targetX;
		int relY = (int)by - (int)targetY;
		if (relX >= 0 && relX < targetImg.getWidth() && relY >= 0 && relY < targetImg.getHeight()) {
			int[] pixels = targetImg.getPixels();
			int col = pixels[relX + relY * targetImg.getWidth()];
			if ((col & 0xff000000) != 0) return true;
		}
		return false;
	}

	private void spawnExplosion(float x, float y) {
		if (particles.size() > 500) return;
		for (int i = 0; i < 20; i++) {
			int col = Math.random() > 0.5 ? 0xffffaa00 : 0xffff0000;
			particles.add(new Particle(x, y, col));
		}
	}

	public void spawnBullet(float x, float y) {
		bullets.add(new Bullet(x, y));
		if (soundCooldown <= 0) {
			shootSound.play();
			soundCooldown = 0.08f;
		}
	}

	public void spawnEnemyBullet(float x, float y) {
		enemyBullets.add(new EnemyBullet(x, y));
		shootSound.play();
	}

	public static void main(String[] args) {
		GameContainer gc = new GameContainer(new SpaceGame());
		gc.setWidth(360);
		gc.setHeight(240);
		gc.setScale(3f);
		gc.setTitle(TITLE);
		gc.setIcoPath("/sprites/enemy.png");
		gc.start();
	}

	public int getScore() { return score; }
	public int getHighscore() { return highscore; }
	public Player getPlayer() { return player; }
}
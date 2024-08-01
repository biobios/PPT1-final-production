package last_kadai;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

enum GameState{
	Playing,
	Result,
}

public class SnakeGame extends Canvas implements KeyListener{
	
	Snake snake;
	SnakeGameMap map;
	int width;
	int height;
	Object monitor;
	boolean stop;
	Font font = new Font(Font.MONOSPACED, Font.PLAIN, 32);
	GameState state;
	
	static final long frame_millis = 100L;
	static final int first_snake_node_counts = 5;
	static final int cell_size = 25;
	
	SnakeGame(int width, int height){
		this.width = width;
		this.height = height;
		monitor = new Object();
		stop = false;
		init();
		
		screenBuffer = new BufferedImage(width * cell_size, height * cell_size, BufferedImage.TYPE_3BYTE_BGR);
		
		addKeyListener(this);
		setSize(width * cell_size, height * cell_size);
	}
	
	void init() {
		map = new SnakeGameMap(width, height);
		snake = new Snake(map, new Point(0, 0), Direction.right, first_snake_node_counts);
		map.setNewFeed();
		state = GameState.Playing;
	}
	
	void run(){
		long previusTime = System.currentTimeMillis();
		while(true) {
			long dt = System.currentTimeMillis() - previusTime;//前回のフレームからの時間を計算する
			if( dt < frame_millis ) {//前回のフレームからの時間が1フレーム当たりの時間より小さいなら
				try {
					Thread.sleep(frame_millis - dt);//前回のフレームからの時間が1フレーム当たりの時間と同じになるように待機する
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			previusTime = System.currentTimeMillis();
			
			
			updateGame();//ゲーム状態を更新
			
			paintGame(screenBuffer.getGraphics());//ゲームをscreenBufferに描画
			
			repaint();//screenBufferをウィンドウに描画
			
			if(state == GameState.Result) {//ゲーム状態がリザルト画面なら
				synchronized(monitor){
					stop = true;
					try {
						monitor.wait();//ゲームループを停止する
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				}
				init();//ゲームループが再始動したら初期化
			}
			
		}
	}
	
	void updateGame() {

		snake.move();//蛇を１マス動かす
		
		if(snake.collided()) {//蛇が障害物にぶつかっていたら
			state = GameState.Result;//ゲーム状態をリザルトに変更する
		}
	}
	
	void paintGame(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());//黒で塗りつぶす
		
		map.draw(g, new Point(0, 0), new Point(cell_size,cell_size), new Point(1, 1));//マップを描画
		snake.draw(g, new Point(0, 0), new Point(cell_size,cell_size), new Point(1, 1));//蛇を描画
		
		if(state == GameState.Result)drawResult(g);//ゲーム状態がリザルトならリザルト画面を描画
	}
	
	BufferedImage screenBuffer;
	
	public void drawResult(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, getHeight() / 3, getWidth() - 1, getHeight() / 3);//黒色で矩形を描画
		g.setColor(Color.WHITE);
		g.drawRect(0, getHeight() / 3, getWidth() - 1, getHeight() / 3);//矩形を白で縁取る
		g.setFont(font);
		g.drawString("result", 0, (getHeight() / 3) + font.getSize());//１行目
		g.drawString("length : " + snake.getLength(), 0, (getHeight() / 3) + font.getSize() * 2);//2行目
		g.drawString("push Enter to restart.", 0, (getHeight() / 3) + font.getSize() * 3);//3行目
	}
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(screenBuffer, 0, 0, this);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		int pressedKeyCode = e.getKeyCode();

		if(stop && pressedKeyCode == KeyEvent.VK_ENTER) {//ゲームループが止まっていて、エンターが押されたら
			synchronized(monitor) {
				monitor.notifyAll();//ゲームループを始動する
				stop = false;
			}
		}
		
		switch(pressedKeyCode) {//WASDによって方向を変える
		case KeyEvent.VK_W:
			snake.setDirection(Direction.up);
			break;
		case KeyEvent.VK_D:
			snake.setDirection(Direction.right);
			break; 
		case KeyEvent.VK_S:
			snake.setDirection(Direction.down);
			break;
		case KeyEvent.VK_A:
			snake.setDirection(Direction.left);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}

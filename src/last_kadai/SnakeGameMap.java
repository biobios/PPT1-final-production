package last_kadai; 

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList; 
import java.util.BitSet;
import java.util.Random;

public class SnakeGameMap {
	BitSet filled;
	
	int width;
	int height;

	Random rand = new Random();
	Point feedPosition;
	ArrayList<Point> obstacles = new ArrayList<Point>();
	
	SnakeGameMap(int width, int height){
		filled = new BitSet(width * height);//全体の当たり判定を保持する
		this.width = width;
		this.height = height;
	}
	
	void fill(int x, int y) {
		if(!inRange(x,y))return;//範囲外なら何もしない
		filled.set(x + y * width);
	}
	
	void remove(int x, int y) {
		if(!inRange(x,y))return;//範囲外なら何もしない
		filled.clear(x + y * width);
	}
	
	boolean inRange(int x, int y) {//範囲内ならtrue,範囲外ならfalse
		if(x >= width || y >= height || x < 0 || y < 0)return false;
		return true;
	}
	
	boolean isFilled(int x, int y) {
		if(!inRange(x,y))return true;//範囲外はtrue扱い
		return filled.get(x + y * width);
	}
	
	void setNewFeed() {
		
		feedPosition = getRandomPositionExcludeFilled();//新しい餌の位置を設定する
		
	}
	
	void addObstacle() {
		Point obstacle = getRandomPositionExcludeFilled();//新しい障害物の位置を導出して
		fill(obstacle.x,obstacle.y);//障害物の位置に当たり判定を設定する
		obstacles.add(obstacle);
	}
	
	void draw(Graphics g, Point origin, Point gridSize, Point margin) {
		g.setColor(Color.GREEN);//緑色で
		g.fillRect(origin.x + feedPosition.x * gridSize.x,
				   origin.y + feedPosition.y * gridSize.y,
				   gridSize.x,
				   gridSize.y);//餌を描画
		
		g.setColor(Color.RED);//赤色で
		for(Point obstacle : obstacles) {//拡張for 第11回　１３ページ目のスライドを参考にした
			g.fillRect(origin.x + obstacle.x * gridSize.x + margin.x,
					   origin.y + obstacle.y * gridSize.y + margin.y,
					   gridSize.x - 2 * margin.x,
					   gridSize.y - 2 * margin.y);//障害物を描画
		}
	}
	
	Point getRandomPositionExcludeFilled() {
		int pos = rand.nextInt(width * height - filled.cardinality());
		int from = 0;
		int filled_counts = filled.get(from, pos + 1).cardinality();
		
		while(filled_counts != 0) {
			from = pos + 1;
			pos += filled_counts;
			filled_counts = filled.get(from, pos + 1).cardinality();
		}
		
		return new Point(pos % width, pos / width);
	}
	
	Point getFeedPosition() {
		return feedPosition;
	}
	
}

package last_kadai;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayDeque;

enum Direction{
	up(new Point(0, -1)),
	right(new Point(1, 0)),
	down(new Point(0, 1)),
	left(new Point(-1, 0));
	

	Point displacement;
	Direction(Point t){
		displacement = t; 
	}
	
	Point nextHeadPosition(Point prev) {//前回の頭の位置を入力すると次の頭の位置を返す関数
		Point ret = new Point(prev);
		ret.translate(displacement.x, displacement.y);
		return ret;
	}
	
	Direction reverse() {//反対を返す関数
		switch(this) {
		case down:
			return up;
		case left:
			return right;
		case right:
			return left;
		case up:
			return down;
		}
		return this;
	}
}

public class Snake {

	ArrayDeque<Point> snakeNodes = new ArrayDeque<Point>();
	Direction prevDirection;
	Direction currentDirection;
	SnakeGameMap map;
	boolean collided;
	
	Snake(SnakeGameMap map, Point initPoint, Direction initDirection, int length){
		this.map = map;
		prevDirection = initDirection;
		currentDirection = initDirection;
		
		collided = false;
		
		addSnakeNode(new Point(initPoint));//基準のノードを追加して
		for(int i = 1; i < length; i++) {
			addSnakeNode(currentDirection.nextHeadPosition(snakeNodes.getLast()));//そこからinitDirectionの方向に成長させる
		}
	}
	
	void move() {
		prevDirection = currentDirection;
		collided = false;
		addSnakeNode(prevDirection.nextHeadPosition(snakeNodes.getLast()));//頭を追加する
		if(snakeNodes.getLast().equals(map.getFeedPosition())) {//餌を取ったなら
			map.addObstacle();//障害物を追加
			map.setNewFeed();//餌を新しい位置に変更
		}else {//餌を取ってないなら
			cutSnakeTail();//しっぽを一つなくす
		}
	}
	
	void addSnakeNode(Point node) {
		snakeNodes.add(node);//ノードを追加
		if(map.isFilled(node.x, node.y)) {//もしすでに当たり判定がある場所なら
			collided = true;//衝突フラグを立てる
		}else {//そうじゃないなら
			map.fill(node.x, node.y);//当たり判定を設定する
		}
	}
	
	int getLength() {
		return snakeNodes.size();
	}
	
	boolean collided() {
		return collided;
	}
	
	Point cutSnakeTail() {
		Point beCut = snakeNodes.pop();//しっぽを取り出す
		map.remove(beCut.x, beCut.y);//取り出したノードの位置の当たり判定を無くす
		return beCut;
	}
	
	void draw(Graphics g, Point origin, Point gridSize, Point margin) {
		g.setColor(Color.WHITE);//白色で
		for(Point node : snakeNodes) {//それぞれのノードについて//拡張for 第11回　１３ページ目のスライドを参考にした
			g.fillRect(origin.x + node.x * gridSize.x + margin.x,
					   origin.y + node.y * gridSize.y + margin.y,
					   gridSize.x - 2 * margin.x,
					   gridSize.y - 2 * margin.y);//矩形を描画する
		}
	}
	
	void setDirection(Direction d) {//前回の進行方向の逆以外のときに、進行方向を変更する
		if(prevDirection.reverse() == d) return;
		currentDirection = d;
	}

}

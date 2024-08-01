package last_kadai;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		JFrame frame = new JFrame("最終課題");//ウィンドウを生成
		SnakeGame game = new SnakeGame(20, 20);//ゲームを生成
		frame.add(game);//ウィンドウにゲームを追加
		frame.pack();//ウィンドウのサイズをゲームに合わせる
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウが閉じられたらプログラムが終了するようにする
		frame.setResizable(false);//ウィンドウのサイズを変更不可にする
		frame.setVisible(true);//ウィンドウを表示する
		game.run();//ゲーム実行
		

	}

}

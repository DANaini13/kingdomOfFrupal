package com.company.Game;

public class MainViewController {

    private GameMainView gameMainView;
    private GameView gameView;

    private MainViewController() {
        initSubviews();
    }

    private void initSubviews(){
        this.gameMainView = GameMainView.createAndShowView(700);
        this.gameView = GameView.createWithBound(200, 100, 500, 500);
        this.gameMainView.add(this.gameView);
        this.gameView.setFocusable(true);
        this.gameView.requestFocus();
    }

    static public MainViewController create() {
        MainViewController mainViewController = new MainViewController();
        return mainViewController;
    }

    public void start() {
        GameViewController gameViewController = GameViewController.createWithGameView(this.gameView);
        gameViewController.start();
    }
}

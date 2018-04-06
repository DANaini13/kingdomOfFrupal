package com.company.Game;

public class MainViewController {

    private GameMainView gameMainView;
    private GameView gameView;
    private GameStatusView gameStatusView;

    private MainViewController() {
        initSubviews();
    }

    private void initSubviews(){
        this.gameMainView = GameMainView.createAndShowView(700);
        this.gameView = GameView.createWithBound(210, 75, 500, 500, 20, 20);
        this.gameMainView.add(this.gameView);
        this.gameView.setFocusable(true);
        this.gameView.requestFocus();
        this.gameStatusView = GameStatusView.create(210, 0, 200, 20);
        this.gameMainView.add(this.gameStatusView);
    }

    static public MainViewController create() {
        MainViewController mainViewController = new MainViewController();
        return mainViewController;
    }

    public void start() {
        GameViewController gameViewController = GameViewController.createWithGameView(this.gameView);
        gameViewController.setEnergyChangeHandler(newEnergy -> {
            this.gameStatusView.setEnergy(newEnergy);
        });
        gameViewController.start();
    }
}

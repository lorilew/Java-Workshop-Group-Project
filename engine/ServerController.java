package engine;

import sockets.GameListener;

public class ServerController implements GameListener{

    private ObservableBoardData data;

    public ServerController(ObservableBoardData data) {
        this.data = data;
    }

    
    @Override
    public void makeMove(String fen) {
        data.setFen(fen);
        data.notifyObservers(data); // Must update this usage
    }

    @Override
    public void setWin() {
        data.setWin();
        data.notifyObservers(data); // Must update this usage
    }

    @Override
    public void setLoss() {
        data.setLoss();
        data.notifyObservers(data); // Must update this usage
    }

    @Override
    public void setDraw() {
        data.setDraw();
        data.notifyObservers(data); // Must update this usage
    }
    
}

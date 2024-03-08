package refereesite;

public interface IRefereeSite_Referee {
    void announceNewGame();

    void callTrial();

    void declareGameWinner(int team);

    void declareMatchWinner(int team);
}
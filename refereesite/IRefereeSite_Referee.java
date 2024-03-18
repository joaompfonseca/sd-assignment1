package refereesite;

public interface IRefereeSite_Referee {
    void announceNewGame();

    void callTrial();

    void declareGameWinner(int team, boolean knockout);

    void declareMatchWinner(int team);
}
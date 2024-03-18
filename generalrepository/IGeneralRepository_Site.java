package generalrepository;

public interface IGeneralRepository_Site {

    void reviewNotes(int team);

    void announceNewGame();

    void callTrial();

    void declareGameWinner(int team, boolean knockout);

    void declareMatchWinner(int team);
}

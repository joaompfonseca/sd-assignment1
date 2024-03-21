# SD Assignment 1

## Description

The assignment goal was to develop a multithreaded implementation of the "Game of the Rope", as described in the
[description of the assignment](description.pdf) provided by the course's professor. The solution was implemented in
Java 21 and uses reentrant locks to ensure mutual exclusion and condition variables to ensure synchronization between
threads.

The game is played by two teams, each with 5 players and 1 coach. A referee is responsible for the flow of the game.
An [interaction diagram](interaction-diagram.png) was provided to help understand the game's flow.

The following entities were defined:

- `Referee`: organizes the game and ensures that the rules are followed;
- `Coach`: responsible for preparing the team for the game;
- `Contestant`: responsible for playing the game.

To allow synchronization between the threads, the following information sharing regions were defined:

- `RefereeSite`: where the referee shares the game state with the coaches;
- `Playground`: where the contestants play the game;
- `ContestantsBench`: where the contestants wait for their turn to play;
- `GeneralRepository`: where the state of the game is logged by the referee site, playground and contestants bench.

**Course:** Distributed Systems (2023/2024).

## Running the program

- Run `./run.sh` in root to compile and run the program.
- Run `./test.sh <N_RUNS>` in root to run the program multiple times to check for deadlocks.

## Logging

Logs of the runs are saved in the `logs` directory. The logs are named `<timestamp>.log`.

## Authors

- Diogo Paiva, 103183
- João Fonseca, 103154

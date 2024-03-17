# Usage: ./run.sh
# Description: Compiles the source code and runs the GameOfTheRope program once.
# Example: ./run.sh
mkdir -p "bin"
javac -d "bin" *.java
java -cp "bin" GameOfTheRope
rm -rf "bin"

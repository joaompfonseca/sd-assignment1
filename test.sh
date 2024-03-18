# Usage: ./test.sh <N_RUNS>
# Description: Compiles the source code and runs the GameOfTheRope program N_RUNS times.
# Example: ./test.sh 10

N_RUNS=$1 # Number of runs

if [ -z "$N_RUNS" ] || [ $N_RUNS -lt 1 ]; then
  echo "Usage: ./benchmark.sh <N_RUNS>"
  exit 1
fi

# Compile the source code
mkdir -p "bin"
javac -d "bin" *.java

# Run the GameOfTheRope program N_RUNS times
for i in $(seq 1 $N_RUNS); do
  echo -n "Run $i/$N_RUNS"

  start=$(date +%s)
  java -cp "bin" GameOfTheRope > /dev/null
  EXIT_CODE=$?
  finished=$(date +%s)
  time_taken=$((finished - start))

  if [ $EXIT_CODE -eq 0 ]; then
      echo " --- OK in $time_taken seconds"
  else
      echo " --- ERROR in $time_taken seconds (exit code $EXIT_CODE)"
  fi
done

# Clean up
rm -rf "bin"
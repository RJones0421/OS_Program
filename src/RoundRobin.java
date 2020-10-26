public class RoundRobin extends Scheduler {
    //Constructs the Round Robin algorithm
    public RoundRobin(Process[] processes, int roundTime) {
        super(processes);
        this.roundTime = roundTime;
        this.currentRound = roundTime;
    }

    //Currently unused, could be used to run a RR algorithm
    //Created to understand algorithm, confirm functionality, and that stats would be accurate
    public void Run() {
        Start();

        while (!isFinished) {
            //Loops for the current round
            while (currentRound > 0) {
                time++;
                currentRound--;

                AddWaitingTime();
                RunIO();
                //Run the current process if there is one
                if (current != null) {
                    RunReady();
                }
                //Else add unused time and try to get a new process
                else {
                    nullTime++;
                    Dequeue();
                }
                CheckIfFinished();
            }
            //If the round ends and the process was not removed,
            //add it back to the ready queue
            if (current != null) {
                Enqueue(current);
            }
            //If there is no process but the program is still running,
            //increment the time, add to unused time, and run the IO.
            //There will be nothing in the ready queue if there is no process.
            else if (!isFinished){
                time++;
                nullTime++;
                RunIO();
            }
            //Get the next process
            Dequeue();
        }
        GetResults();

        System.out.println("Results for Round Robin");
        PrintResults();
    }

    //Moves process from ready queue to current
    @Override
    protected void Dequeue() {
        if (readyQueue.size() > 0) {
            current = readyQueue.remove();
            currentRound = roundTime;
            //Context switch
            PrintCurrent();

            //Sets the response time
            if (current.firstRun) {
                current.responseTime = time;
                current.firstRun = false;
            }
        }
    }

    //Updated algorithm to run the current process
    @Override
    protected void RunReady() {
        current.Run(time);

        //If the process has finished
        if (current.currentTime == 0) {
            //End the round
            currentRound = 0;
            if (current.isFinished) {
                CheckIfFinished();
            }
            else {
                ioQueue.add(current);
                current.SetTime();
            }
            current = null;
            Dequeue();
        }
    }

    //Run for one time unit (used in MLFQ)
    public void RunOnce() {
        currentRound--;
        current.Run(time);

        if (current.currentTime == 0) {
            currentRound = 0;
        }
    }

    //Synchronizes time with MLFQ
    public void UpdateTime() {
        time++;
    }

    private final int roundTime;
    public int currentRound;
}

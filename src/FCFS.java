public class FCFS extends Scheduler {
    //Constructs the FCFS algorithm
    public FCFS(Process[] processes) {
        super(processes);
    }

    //Runs the processes for this algorithm
    public void Run() {
        Start();

        while (!isFinished) {
            time++;

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
            //Check for termination condition
            CheckIfFinished();
        }
        GetResults();

        System.out.println("Results for FCFS");
        PrintResults();
    }

    //Moves process from ready queue to current
    @Override
    protected void Dequeue() {
        if (readyQueue.size() > 0) {
            current = readyQueue.remove();
            //Context switch
            PrintCurrent();

            //Set the response time
            if (current.firstRun) {
                current.responseTime = time;
                current.firstRun = false;
            }
        }
    }

    //Run for one time unit (used in MLFQ)
    public void RunOnce() {
        current.Run(time);
    }

    //Synchronizes time with MLFQ
    public void UpdateTime() {
        time++;
    }
}

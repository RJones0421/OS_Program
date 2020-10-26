public class SJF extends Scheduler {
    //Constructs the SJF algorithm
    public SJF(Process[] processes) {
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

        System.out.println("Results for SJF");
        PrintResults();
    }

    //Moves process from ready queue to current
    @Override
    protected void Dequeue() {
        if (readyQueue.size() > 0) {
            int shortest = 0;
            //Find shortest burst
            for (int i = 0; i < readyQueue.size(); i++) {
                if (readyQueue.get(i).currentTime < readyQueue.get(shortest).currentTime)
                    shortest = i;
            }
            current = readyQueue.remove(shortest);
            //Conext switch
            PrintCurrent();

            //Sets the response time
            if (current.firstRun) {
                current.responseTime = time;
                current.firstRun = false;
            }
        }
    }
}

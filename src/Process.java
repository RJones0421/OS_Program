public class Process {
    //Constructs the process
    public Process(int[] timeArray, int i) {
        this.timeArray = timeArray;
        processNum = i + 1;

        currentTime = this.timeArray[0];
        currentIndex = 0;

        waitingTime = 0;
        turnaroundTime = 0;
        responseTime = 0;
    }

    //Runs the process for one unit of time
    public void Run(int time) {
        currentTime--;
        if (currentTime == 0) {
            currentIndex++;
            //Only checks if finished when the burst finishes
            if (currentIndex == timeArray.length) {
                isFinished = true;
                turnaroundTime = time;
            }
        }
    }

    //Set the new burst time
    public void SetTime() {
        currentTime = timeArray[currentIndex];
    }

    //Increments the waiting time
    public void AddWaitingTime() {
        waitingTime++;
    }

    //Gets the waiting time
    public int GetWaitingTime() {
        return waitingTime;
    }

    //Unchanging info about this process
    private final int[] timeArray;
    public final int processNum;

    //Info related to the burst
    public int currentTime;
    private int currentIndex;

    //Statistic info related to the process
    private int waitingTime;
    public int turnaroundTime;
    public int responseTime;

    //Flags for the process
    public boolean firstRun = true;
    public boolean isFinished = false;
}

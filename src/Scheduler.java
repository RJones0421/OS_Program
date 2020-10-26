import java.util.Arrays;
import java.util.LinkedList;

public class Scheduler {
    //Constructs the Scheduler algorithm
    public Scheduler(Process[] processes) {
        this.processes = processes;
    }

    //Initializes all the processes and makes one ready
    public void Start() {
        readyQueue.addAll(Arrays.asList(processes));
        Dequeue();
    }

    //Add the item to the ready queue
    protected void Enqueue(Process process) {
        readyQueue.add(process);
    }

    //Remove an item from the ready queue to run
    //Will be implemented by all the algorithms that need it
    protected void Dequeue() {

    }

    //Add waiting time to all the processes in the ready queue
    protected void AddWaitingTime() {
        for (var p : readyQueue) {
            p.AddWaitingTime();
        }
    }

    //Run all the processes in the IO queue
    protected void RunIO() {
        int length = ioQueue.size();
        int i;

        for(i = 0; i < length; i++) {
            Process p = ioQueue.get(i);

            p.Run(time);
            if (p.currentTime <= 0) {
                //Adjust the length and index if the process finishes IO
                i--;
                length--;
                //Remove the process from the IO queue, set the new burst time, and add it to ready
                ioQueue.remove(p);
                p.SetTime();
                Enqueue(p);
            }
        }
    }

    //Default method for how the current process will run
    protected void RunReady() {
        current.Run(time);

        if (current.currentTime == 0) {
            if (current.isFinished) {
                CheckIfFinished();
            }
            else {
                //Move to the IO queue and set the new burst
                ioQueue.add(current);
                current.SetTime();
            }
            current = null;
            Dequeue();
        }
    }

    //Checks if all the processes are finished
    protected void CheckIfFinished() {
        for (var process : processes) {
            if (!process.isFinished) {
                return;
            }
        }
        isFinished = true;
    }

    //Prints the current state of the algorithm/queue
    protected void PrintCurrent() {
        System.out.println("Current program status");
        System.out.println("==========================");
        System.out.println("Current time = " + time);
        //Catch for if current happens to be null
        if (current == null) {
            System.out.println("Current process: None");
        }
        else {
            System.out.println("Current process: #" + current.processNum);
        }

        //Shows the ready queue
        System.out.println("Ready queue (next CPU burst):");
        for (var p : readyQueue) {
            System.out.println(" - Process #" + p.processNum + ": " + p.currentTime);
        }

        //Shows the IO queue
        System.out.println("IO queue (IO burst remaining):");
        for (var p : ioQueue) {
            System.out.println(" - Process #" + p.processNum + ": " + p.currentTime);
        }

        //Shows the finished processes
        System.out.println("Completed:");
        for (var p : processes) {
            if (p.isFinished) {
                System.out.println(" - Process #" + p.processNum);
            }
        }
        System.out.println("==========================\n");

    }

    //Print the final results of the algorithm
    public void PrintResults() {
        System.out.println("==========================");
        System.out.println("Process # | Tw\t| Ttr | Rt");
        for (int i = 0; i < processes.length; i++) {
            System.out.print("#" + (i + 1) + "        | ");
            System.out.print(resultsArray[i][0] + "\t| ");
            System.out.print(resultsArray[i][1] + " | ");
            System.out.println(resultsArray[i][2]);
        }
        System.out.println("==========================");
        System.out.println("Time taken = " + time);
        System.out.println("CPU Utilization = " + (cpuUtil * 100) + "%");
        System.out.println("Avg Waiting Time = " + avgWait);
        System.out.println("Avg Turnaround Time = " + avgRun);
        System.out.println("Avg Response Time = " + avgRes);
        System.out.println("==========================\n");
    }

    //Get the times for each process and calculate the averages
    //Stores the data in an array for easier repeated access
    protected void GetResults() {
        for (int i = 0; i < processes.length; i++) {
            int x;

            x =  processes[i].GetWaitingTime();
            resultsArray[i][0] =  x;
            avgWait += x;

            x =  processes[i].turnaroundTime;
            resultsArray[i][1] =  x;
            avgRun += x;

            x =  processes[i].responseTime;
            resultsArray[i][2] =  x;
            avgRes += x;
        }
        cpuUtil = (float) (time - nullTime) / time;
        avgWait /= 8;
        avgRun /= 8;
        avgRes /= 8;
    }

    //The lists for the queues
    protected final Process[] processes;
    protected final LinkedList<Process> readyQueue = new LinkedList<>();
    protected final LinkedList<Process> ioQueue = new LinkedList<>();

    //The current process
    protected Process current;

    //Time trackers
    protected int time = 0;
    protected int nullTime = 0;

    //Finished flag and loop terminator
    protected boolean isFinished = false;

    //Results variables
    private final int[][] resultsArray = new int[8][3];
    private float cpuUtil = 0, avgWait = 0, avgRun = 0, avgRes = 0;
}

public class OS_Start {
    public static void main(String[] args){

        //First Come First Serve
        CreateProcesses();
        FCFS fcfs = new FCFS(processes);
        fcfs.Run();

        //Shortest Job First
        CreateProcesses();
        SJF sjf = new SJF(processes);
        sjf.Run();

        //Multi-Level Feedback Queues
        CreateProcesses();
        MLFQ mlfq = new MLFQ(processes);
        mlfq.Run();

        //Final results display
        System.out.println("\n==========================================\n");
        System.out.println("Final Results\n");
        System.out.println("Results for FCFS");
        fcfs.PrintResults();
        System.out.println("Results for SJF");
        sjf.PrintResults();
        System.out.println("Results for MLFQ");
        mlfq.PrintResults();
    }

    //create new array of processes
    private static void CreateProcesses() {
        for (int i = 0; i < 8; i++){
            processes[i] = new Process(times[i], i);
        }
    }

    //Burst times
    private static final int[][] times = new int[][] {
            {5, 27, 3, 31, 5, 43, 4, 18, 6, 22, 4, 26, 3, 24, 4},
            {4, 48, 5, 44, 7, 42, 12, 37, 9, 76, 4, 41, 9, 31, 7, 43, 8},
            {8, 33, 12, 41, 18, 65, 14, 21, 4, 61, 15, 18, 14, 26, 5, 31, 6},
            {3, 35, 4, 41, 5, 45, 3, 51, 4, 61, 5, 54, 6, 82, 5, 77, 3},
            {16, 24, 17, 21, 5, 36, 16, 26, 7, 31, 13, 28, 11, 21, 6, 13, 3, 11, 4},
            {11, 22, 4, 8, 5, 10, 6, 12, 7, 14, 9, 18, 12, 24, 15, 30, 8},
            {14, 46, 17, 41, 11, 42, 15, 21, 4, 32, 7, 19, 16, 33, 10},
            {4, 14, 5, 33, 6, 51, 14, 73, 16, 87, 6}
    };

    private final static Process[] processes = new Process[8];
}

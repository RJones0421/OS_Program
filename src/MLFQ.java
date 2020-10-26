public class MLFQ extends Scheduler {
    //Construct the MLFQ
    public MLFQ(Process[] processes) {
        super(processes);
        this.q1 = new RoundRobin(processes, 5);
        this.q2 = new RoundRobin(processes, 10);
        this.q3 = new FCFS(processes);
    }

    //Run the algorithm
    public void Run() {
        q1.Start();
        previousRun = 1;

        while (!isFinished) {
            //Synchronize the time across the algorithms
            UpdateTime();

            AddWaitingTime();
            RunIO();

            //If q1 has a process ready
            if (q1.current != null) {
                //Run the queue and set the flag for what ran
                RunQueue(q1, q2);
                thisRun = 1;
            }
            //Else try to get a process
            else {
                q1.Dequeue();
            }

            //If q2 has a process ready
            if (q2.current != null) {
                //Run the queue if q1 did not run and set the flag
                if (!hasRun) {
                    RunQueue(q2, q3);
                    thisRun = 2;
                }
                //If q1 ran, increase the waiting time for the ready item in q2 too
                else {
                    q2.current.AddWaitingTime();
                }
            }
            //Else try to get a process
            else {
                q2.Dequeue();
            }

            //if q3 has a process ready
            if (q3.current != null) {
                //Run the queue if q1 and q2 did not run and set the flag
                if (!hasRun) {
                    RunQueue(q3);
                    thisRun = 3;
                }
                //If q1 or q2 ran, increase the waiting time for the ready item in q3
                else {
                    q3.current.AddWaitingTime();
                }
            }
            //Else try to get a process
            else {
                q3.Dequeue();
            }

            //If nothing ran, add to wasted time
            if(!hasRun) {
                nullTime++;
            }

            //Printing context switch
            if (previousRun != thisRun) {
                switch(thisRun) {
                    case 1:
                        System.out.println("Current for Round Robin 1");
                        q1.PrintCurrent();
                        break;
                    case 2:
                        System.out.println("Current for Round Robin 2");
                        q2.PrintCurrent();
                        break;
                    case 3:
                        System.out.println("Current for FCFS");
                        q3.PrintCurrent();
                        break;
                }
                previousRun = thisRun;
            }

            hasRun = false;
            CheckIfFinished();
        }
        GetResults();

        System.out.println("Results for MLFQ");
        PrintResults();
    }

    //Synchronize the time for all the algorithms
    private void UpdateTime() {
        time++;
        q1.UpdateTime();
        q2.UpdateTime();
        q3.UpdateTime();
    }

    //Run for the queue provided
    //Moves the process to the next queue if the round timer expires
    private void RunQueue(RoundRobin q, Scheduler next) {
        q.RunOnce();
        hasRun = true;

        //If the burst finishes
        if (q.current.currentTime == 0) {
            if (q.current.isFinished) {
                CheckIfFinished();
            } else {
                q.ioQueue.add(q.current);
                q.current.SetTime();
            }

            q.current = null;
            q.Dequeue();
        }
        //Else if the round ends, move the process, remove it from the original queue
        //then try to get the next process
        else if (q.currentRound == 0) {
            next.Enqueue(q.current);
            q.current = null;
            q.currentRound = -1;
            q.Dequeue();
        }
    }

    //Runs the queue for FCFS
    //does not need to know next as it is the final queue
    private void RunQueue(FCFS q) {
        q.RunOnce();
        hasRun = true;

        //Check is the burst finished
        if (q.current.currentTime == 0) {
            if (q.current.isFinished) {
                CheckIfFinished();
            } else {
                q.ioQueue.add(q.current);
                q.current.SetTime();
            }

            q.current = null;
            q.Dequeue();
        }
    }

    //Add waiting time for all of the queues
    @Override
    protected void AddWaitingTime() {
        q1.AddWaitingTime();
        q2.AddWaitingTime();
        q3.AddWaitingTime();
    }

    //Run IO for all of the queues
    @Override
    protected void RunIO() {
        q1.RunIO();
        q2.RunIO();
        q3.RunIO();
    }

    //The queues in the MLFQ
    RoundRobin q1, q2;
    FCFS q3;

    //The flags used to track the state
    private int previousRun;
    private int thisRun;
    private boolean hasRun = false;
}

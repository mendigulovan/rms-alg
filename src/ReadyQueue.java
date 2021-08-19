import java.util.LinkedList;

public class ReadyQueue {
    Task lastExecutedTask;
    LinkedList<Task> TheQueue;
    int timeLapsed;

    ReadyQueue() {
        TheQueue = new LinkedList<>();
        timeLapsed = 0;
        lastExecutedTask = null;
    }

    /**
     * 0 -> Nothing to do
     * 1 -> Everything went normally
     * -1 -> Deadline Missed!
     * 2 -> Process completely executed without missing the deadline
     *
     */

    int executeOneUnit() throws Exception {
        if (TheQueue.isEmpty()) {
            timeLapsed++;
            return 0;
        }
        timeLapsed++;
        Task T = TheQueue.getFirst();
        //Somehow remaining time became negative, or is 0 from first
        if (T.remainTime <= 0) {
            throw new Exception();
        }
        T.remainTime--;
        if ((T.remainTime + 1 == T.compTime) || (T != lastExecutedTask && lastExecutedTask != null)) {
            System.out.println("At time " + (timeLapsed - 1) + ", task " + T.id + " has started execution");
            //return 1;
        }

        lastExecutedTask = T;
        //After running if remaining time becomes zero, i.e. process is completely executed
        if (T.remainTime == 0) {
            if (T.entryTime + T.period >= timeLapsed) {
                System.out.println("At time " + (timeLapsed) + ", task " + T.id + " has been completely executed.");
                TheQueue.pollFirst();
                return 2;
            } else {
                System.out.println("Task " + TheQueue.getFirst().id + " finished at time " + timeLapsed + " thus, missing it's deadline of time " + (TheQueue.getFirst().entryTime + TheQueue.getFirst().period) + ".");
                TheQueue.pollFirst();
                return -1;
            }
        }
        return 99;

    }

    /**
     * Added task in empty queue -> 0
     * Added identical task to the first task -> 1
     * Added the most prioritized task
     * Added the second task with less priority
     * Impossible -> 7
     */

    int addNewTask(Task T) {
        if (TheQueue.isEmpty()) {
            TheQueue.addFirst(T);
            T.entryTime = timeLapsed;
            return 0;
        }

        if (T.period == TheQueue.getFirst().period) {
            TheQueue.add(1, T);
            T.entryTime = timeLapsed;
            return 1;
        }

        if (T.period < TheQueue.getFirst().period) {
            preemptTask(T);
        }
        else {
            addNextTask(T);
        }
        return 7;
    }

    /**
     *  Added the most prioritized task
     *  Previous Process Not Preempted -> 2
     *  Previous Process Preempted -> 3
     */
    int preemptTask(Task T) {

        boolean tFlag = TheQueue.getFirst().compTime == TheQueue.getFirst().remainTime;
        TheQueue.addFirst(T);
        T.entryTime = timeLapsed;
        if (tFlag) return 2;
        else {
            System.out.println("At time " + timeLapsed + ", task " + TheQueue.get(1).id + " has been preempted.");
            return 3;
        }
    }

    /**
     * Added the second task with less priority -> 4
     * Added task somewhere in the middle -> 5
     * Added the least prioritized task in a list with size more than 2 -> 6
     */
    int addNextTask(Task T) {

        if (TheQueue.size() == 1) {
            TheQueue.add(T);
            T.entryTime = timeLapsed;
            return 4;
        }
        for (int i = 1; i < TheQueue.size(); i++) {
            if (T.period < TheQueue.get(i).period) {
                TheQueue.add(i, T);
                T.entryTime = timeLapsed;
                return 5;
            }

            if (i == TheQueue.size() - 1) {
                TheQueue.add(T);
                T.entryTime = timeLapsed;
                return 6;
            }

        }
        return 0;
    }
}



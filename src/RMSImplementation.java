import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class RMSImplementation {

    private static int gcd(int a, int b) {
        while (b > 0) {
            int temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    public static int lcm(int a, int b) {
        return a * (b / gcd(a, b));
    }

    public static int lcm(int[] input) {
        int result = input[0];
        for (int i = 1; i < input.length; i++) result = lcm(result, input[i]);
        return result;
    }

    private static double sigma(LinkedList<Task> taskList) {
        double returnValue = 0.00;
        for (Task eachTask : taskList) {
            returnValue = returnValue + (((double) eachTask.compTime) / ((double) eachTask.period));
        }
        return returnValue;
    }

    private static double muSigma(int n) {
        return ((double) n) * ((Math.pow(2, ((1 / ((double) n)))) - (double) 1));
    }

    public static void main(String[] args) {
        LinkedList<Task> PeriodicTaskList = new LinkedList<>();
        ReadyQueue ReadyQueue = new ReadyQueue();
        try {
            Scanner scan = new Scanner(System.in);
            int totalTask = scan.nextInt();
            ArrayList<Integer> periodList = new ArrayList<>();

            System.out.println("This task set consists of " + totalTask + " tasks. They are:");
            for (int i = 0; i < totalTask; i++) {
                int period = scan.nextInt();
                periodList.add(period);
                int cTime = scan.nextInt();
                Task tempTask = new Task(period, cTime, i);
                PeriodicTaskList.add(tempTask);
                System.out.println("Task " + tempTask.id + ":\n\tPeriod: " + tempTask.period + "\n\tComputation Time: " + tempTask.compTime);
            }

            boolean isFailure;
            isFailure = !(sigma(PeriodicTaskList) <= muSigma(PeriodicTaskList.size()));

            if (isFailure) {
                System.out.println("\n" +
                        "\n" +
                        "##Note that this task set does not satisfy the schedulability check,\n" +
                        "therefore there are chances of deadline misses.");
            } else {
                System.out.println("\n" +
                        "\n" +
                        "##Note that this task set satisfies the schedulability check,\n" +
                        "therefore it is definitely schedulable by RMS");
            }

            int[] periodArray = periodList.stream().mapToInt(i -> i).toArray();
            int periodLCM = lcm(periodArray);


            System.out.println("=========================================================\n" +
                    "The Execution Begins:\n" +
                    "=========================================================");
            for (int i = 0; ; i++) {
                //System.out.println("Current Time = "+i);
                //Add tasks to queue
                //for (Task individualTask : PeriodicTaskList){
                for (Task individualTask : PeriodicTaskList) {
                    if (i % individualTask.period == 0) {
                        ReadyQueue.addNewTask(new Task(individualTask.period, individualTask.compTime, individualTask.id));
                    }
                }

                //Check cycle complete or not
                if (i != 0 && i % periodLCM == 0) {
                    int tempVar = 0;
                    if (ReadyQueue.TheQueue.size() == PeriodicTaskList.size()) {
                        for (int k = 0; k < ReadyQueue.TheQueue.size(); k++) {
                            //System.out.println((ReadyQueue.TheQueue.get(k).remainTime)+" AND "+(ReadyQueue.TheQueue.get(k).compTime));
                            if (ReadyQueue.TheQueue.get(k).remainTime != ReadyQueue.TheQueue.get(k).compTime) {
                                tempVar = 1;
                                //System.out.println("TWO");
                                break;
                            }
                        }
                    }
                    if (tempVar == 0) {
                        System.out.println("End of one complete cycle");
                        return;
                    }
                }

                //Execute Once!
                int output = ReadyQueue.executeOneUnit();

                if (output == -1) {
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("Some error occurred. Program will now terminate: " + e);
        }
    }
}


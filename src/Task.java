public class Task  {
    int period;  //period
    int compTime;  //computational time
    int remainTime;  //remaining computational time after preemption
    int entryTime;
    int id;

    Task(int p, int cTime, int identification) {
        period = p;
        compTime = cTime;
        remainTime = cTime;
        id = identification;
    }
}
import java.util.concurrent.CyclicBarrier;

public class Main {
    //static char[4] ch = new char[];

    //{"a","b","c","d"};
    static String first  = "BDCAA";
    static String second = "CABDA";
    static String third  = "BADCA";
    static String fourth = "BACDA";

    static boolean mainAlive = true;
    public static void setNotMainAlice(){
        mainAlive = false;
    }

    static void setNewFirstStr(String newStr){
        first = newStr;
    }
    static void setNewSecondStr(String newStr){
        second = newStr;
    }
    static void setNewThirdStr(String newStr){
        third = newStr;
    }
    static void setNewFourthStr(String newStr){
        fourth = newStr;
    }
    private static final CyclicBarrier BARRIER = new CyclicBarrier(4, new FerryBoat());

    public static void main(String[] arg) throws InterruptedException {
        Thread[] threads = new Thread[4];
       // for (int i = 0; i < 4; i++) {
            threads[0] = new Thread(new Mythread(first,0));
            threads[0].start();
            Thread.sleep(10);

        threads[1] = new Thread(new Mythread(second,1));
        threads[1].start();
        Thread.sleep(10);

        threads[2] = new Thread(new Mythread(third,2));
        threads[2].start();
        Thread.sleep(10);

        threads[3] = new Thread(new Mythread(fourth,3));
        threads[3].start();
        Thread.sleep(10);
       // }
    }

    public static class FerryBoat implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
               // System.out.println("Паром переправил автомобили!");
                int[] countA = new int[4];
                int[] countB = new int[4];
                System.out.println(first);
                System.out.println(second);
                System.out.println(third);
                System.out.println(fourth);
                for(int i = 0;  i<first.length();i++){
                    if(first.charAt(i) == 'A') countA[0]++;
                    else if (second.charAt(i) == 'A') {
                        countA[1]++;
                    }
                    else if (third.charAt(i) == 'A') {
                        countA[2]++;
                    }
                    else if (fourth.charAt(i) == 'A') {
                        countA[3]++;
                    }
                    else if (first.charAt(i) == 'B') {
                        countB[0]++;
                    }
                    else if (second.charAt(i) == 'B') {
                        countB[1]++;
                    }
                    else if (third.charAt(i) == 'B') {
                        countB[2]++;
                    }else if (fourth.charAt(i) == 'B') {
                        countB[3]++;
                    }
                }
                Thread.sleep(300);
                if(countA[0]==countA[1] && countA[1]==countA[2]){
                    if(countB[0]==countB[1] && countB[1]==countB[2]){
                        System.out.println(first);
                        System.out.println(second);
                        System.out.println(third);
                        setNotMainAlice();
                    }
                }
                else if(countA[0]==countA[2] && countA[2]==countA[3]){
                    if(countB[0]==countB[2] && countB[2]==countB[3]){
                        System.out.println(first);
                        System.out.println(third);
                        System.out.println(fourth);
                        setNotMainAlice();
                    }
                }
                else if(countA[0]==countA[1] && countA[1]==countA[3]){
                    if(countB[0]==countB[1] && countB[1]==countB[3]){
                        System.out.println(first);
                        System.out.println(second);
                        System.out.println(fourth);
                        setNotMainAlice();
                    }
                }
                else if(countA[1]==countA[2] && countA[2]==countA[3]){
                    if(countB[1]==countB[2] && countB[2]==countB[3]){
                        System.out.println(second);
                        System.out.println(third);
                        System.out.println(fourth);
                        setNotMainAlice();
                    }
                }

            } catch (InterruptedException e) {
            }
        }
    }

    public static class Mythread implements Runnable {
        String str;
        int id;
        Mythread(String s, int num){
            this.str = s;
            this.id = num;
        }

        @Override
        public void run() {
            try {
                System.out.printf("Автомобиль №%d подъехал к паромной переправе.\n", id);
                BARRIER.await();
                while(mainAlive){
                    int a = (int) (Math.random()*3);
                    String newStr ="";
                    for(int i = 0; i<str.length();i++){
                        if(i!=a){
                            newStr += str.charAt(i);
                        }
                        else if(i == a){
                            if('A' == str.charAt(i)){
                                newStr += 'C';
                            }
                            if('C' == str.charAt(i)){
                                newStr += 'A';
                            }
                            if('B' == str.charAt(i)){
                                newStr += 'D';
                            }
                            if('D' == str.charAt(i)){
                                newStr += 'B';
                            }
                        }
                    }
                    if(id == 0){
                        setNewFirstStr(newStr);
                    }
                    if(id == 1){
                        setNewSecondStr(newStr);
                    }
                    if(id == 2){
                        setNewThirdStr(newStr);
                    }
                    if(id == 3){
                        setNewFourthStr(newStr);
                    }
                    BARRIER.await();
                }
            } catch (Exception e) {
            }
        }

    }

}


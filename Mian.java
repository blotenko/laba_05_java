import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Mian {
    static int AMOUNTOFALL = 100;
    static ArrayList<Integer> arr = new ArrayList<Integer>();
    static boolean mainAlive = true;
    public static void setNotMainAlice(){
        mainAlive = false;
    }
   // private static final CyclicBarrier BARRIER = new CyclicBarrier(4, new FerryBoat());
    //Инициализируем барьер на три потока и таском, который будет выполняться, когда
    //у барьера соберется три потока. После этого, они будут освобождены.
   public static Thread[] threads;
    public static void main(String[] args) throws InterruptedException {

        Thread[] threads = new Thread[AMOUNTOFALL];
        for (int i = 0; i < AMOUNTOFALL; i++) {
            threads[i] = new Thread(new Car(i));
            threads[i].start();
         //   new Thread(new Car(i)).start();
            Thread.sleep(10);
        }
    }
    private static final CyclicBarrier BARRIER = new CyclicBarrier(AMOUNTOFALL, new FerryBoat(threads));
    //Таск, который будет выполняться при достижении сторонами барьера
    public static class FerryBoat implements Runnable {
        Thread[] threads;
        FerryBoat(Thread[] th){
            threads = th;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                System.out.println("Паром переправил автомобили!");
                int count = 0;
                for(int i = 0;  i<arr.size();i++){
                    System.out.println(arr.get(i));
                    if(i+1!=arr.size() && arr.get(i) == 1 && arr.get(i+1) ==0){
                        count++;
                    }
                }
                Thread.sleep(300);
                if(count == 0){
                    setNotMainAlice();
                }

            } catch (InterruptedException e) {
            }
        }
        boolean getSomething(){
            return true;
        }
    }

    //Стороны, которые будут достигать барьера
    public static class Car implements Runnable {
        boolean alive;

        private int carNumber;

        public Car(int carNumber) {
            this.carNumber = carNumber;
            alive = true;
        }
        public void setNotAlive(){
            alive = false;
        }
        @Override
        public void run() {
            try {
                System.out.printf("Автомобиль №%d подъехал к паромной переправе.\n", carNumber);
                //Для указания потоку о том что он достиг барьера, нужно вызвать метод await()
                //После этого данный поток блокируется, и ждет пока остальные стороны достигнут барьера
                int a = (int) (Math.random()*2);
                arr.add(a);
              //  System.out.println(BARRIER.getNumberWaiting());
                BARRIER.await();
                while(mainAlive){
                 //System.out.println("WAITING");
                    if(arr.get(carNumber)==1 && carNumber!=AMOUNTOFALL-1
                    ){
                        if(arr.get(carNumber+1)==0){
                            arr.set(carNumber,0);
                            arr.set(carNumber+1,1);
                        }
                    }
                    else if(arr.get(carNumber)==0){
                        if(carNumber != 0){
                            if(arr.get(carNumber-1)==1){
                                arr.set(carNumber-1,0);
                                arr.set(carNumber,1);
                            }
                        }
                    }
                    BARRIER.await();
                }
                System.out.printf("Автомобиль №%d продолжил движение.\n", carNumber);
            } catch (Exception e) {
            }
        }
    }
}
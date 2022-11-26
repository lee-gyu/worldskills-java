
public class ThreadingMain {

	static int counter = 0;
	
	public static void main(String[] args) throws InterruptedException {
		// static void main
		// Main Thread 할당
		
		Thread t1 = new Thread(() -> {
			
			synchronized ("COUNTER") {
				System.out.println("A Lock");
				for (int i = 0; i < 10000; i++) {
					++counter;
				}				
			}
			
			System.out.println("A unlock");
			
		});
		
		Thread t2 = new Thread(() -> {
			
			synchronized ("COUNTER") {
				System.out.println("B Lock");
				for (int i = 0; i < 10000; i++) {
					++counter;
				}			
			}
			
			System.out.println("B unlock");
		});
		
		t1.start(); // 0 ~ 1000
		t2.start(); // 1000 ~ 2000
		
		// thread의 흐름이 끝날 때까지 대기 (blocking)
		t1.join();
		t2.join();
		
		System.out.println(counter);
		
		// 비동기 프로그래밍 <-- 멀티 쓰레딩 
		
		
	}

}

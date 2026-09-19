package sample_tests;

import hw3.LiftElement;

public class LiftTest {
	public static void main(String[] args) {
		// top at x = 50, height 10, bottom at 60
		LiftElement p = new LiftElement(200, 50, 10, 10);
		p.setBounds(40, 70);
		p.setVelocity(0, 6);
		p.update();
		System.out.println("Lift vertical dimensions are between " + p.getYReal() + ", " + (p.getYReal() + 10)); // [56, 66]
		System.out.println("Lift vertical velocity is " + p.getDeltaY()); // 6.0
		p.update();
		System.out.println(p.getYReal() + ", " + (p.getYReal() + 10)); // [60, 70]
		System.out.println("Velocity " + p.getDeltaY()); // -6.0
		p.update();
		System.out.println(p.getYReal() + ", " + (p.getYReal() + 10)); // [54, 64]
		System.out.println("Velocity " + p.getDeltaY()); // -6.0
		p.update();
		System.out.println(p.getYReal() + ", " + (p.getYReal() + 10)); // [48, 58]
		System.out.println("Velocity " + p.getDeltaY()); // -6.0
		p.update();
		System.out.println(p.getYReal() + ", " + (p.getYReal() + 10)); // [42, 52]
		System.out.println("Velocity " + p.getDeltaY()); // -6.0
		p.update();
		System.out.println(p.getYReal() + ", " + (p.getYReal() + 10)); // [40, 50]
		System.out.println("Velocity " + p.getDeltaY()); // 6.0
	}
}
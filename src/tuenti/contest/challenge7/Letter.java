package tuenti.contest.challenge7;

import java.util.ArrayList;

public class Letter {
	private char car;
	private ArrayList<Letter> next = new ArrayList<Letter>();
	private ArrayList<Letter> prev = new ArrayList<Letter>();
	public Letter(char car) {
		this.car = car;
	}
	public char getCar() {
		return car;
	}
	public void setCar(char car) {
		this.car = car;
	}
	public ArrayList<Letter> getNext() {
		return next;
	}
	public void setNext(ArrayList<Letter> next) {
		this.next = next;
	}
	public boolean equals(Object object) {
		if (object instanceof Letter) {
			return this.car == ((Letter)object).getCar();
		} else if (object instanceof Character) {
			return ((Character)object).equals(this.car);
		}
		return false;
	}
	public ArrayList<Letter> getPrev() {
		return prev;
	}
	public void setPrev(ArrayList<Letter> prev) {
		this.prev = prev;
	}
}

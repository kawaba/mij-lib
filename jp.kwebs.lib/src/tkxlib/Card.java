package tkxlib;

public class Card {
	private String suit;
	private int number;
	
	public Card(String suit, int number) {
		this.suit = suit.toLowerCase();
		this.number = number;
	}
	
	public Card(int seqNumber) {
		int suitNumber = (seqNumber-1)/13; // 0, 1, 2, 3, 4
		this.suit=switch(suitNumber) {
			case	0	->	"spade";
			case	1	->	"club";
			case	2	->	"diamond";
			case	3	->	"heart";
			default		->	"joker";
		};
		this.number = (seqNumber % 13==0)? 13 : seqNumber % 13;
	}	
	
	public int seqNumber() {
		int suitNumber = switch(suit.charAt(0)) {
			case	's'		->	0;
			case	'c'		->	1;
			case	'd'		->	2;
			case	'h'		->	3;
			default			->	4;
		};
		return suitNumber * 13 + number;
	}
	
	public String getSuit() {
		return suit;
	}

	public void setSuit(String suit) {
		this.suit = suit;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	
    @Override
	public String toString() {
		return "Card [suit=" + suit + ", number=" + number + "]";
	}

    /**
     * seqNumber to cardNumber
     * @param seqNumber
     * @return
     */
    public static int toCardnumber(int seqNumber) {
		int cardNumber = (seqNumber % 13==0)? 13 : seqNumber % 13;
		return cardNumber;
    }
    /**
     * seqNumber to suitNumber
     * @param seqNumber
     * @return suitNumber
     */
    public static int toSuitnumber(int seqNumber) {
		int suitNumber = (seqNumber-1)/13; // 0, 1, 2, 3, 4
		return suitNumber;
    }
    /**
     * seqNumber to suit
     * @param seqNumber
     * @return 	suite
     */
    public static String toSuit(int seqNumber) {
		int suitNumber = (seqNumber-1)/13; // 0, 1, 2, 3, 4
		String suit=switch(suitNumber) {
			case	0	->	"spade";
			case	1	->	"club";
			case	2	->	"diamond";
			case	3	->	"heart";
			default		->	"joker";
		};
		return suit;
    }
    
    
	public static void main(String[] args) {
		
		System.out.println(new Card(1));
		System.out.println(new Card(13));
		System.out.println(new Card(26));
		System.out.println(new Card(39));
		System.out.println(new Card(52));
		System.out.println(new Card(2));
		System.out.println(new Card(14));
		System.out.println(new Card(53));

    }
}

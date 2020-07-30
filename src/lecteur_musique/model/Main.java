
package lecteur_musique.model;

public class Main {
    public static void main(String[] args) {
	Music m1 = new Music("blala/jdiej/m1.mp3", "booba", 120);
	Music m2 = new Music("blala/m2.mp3", "pnl", 160);
	Music m3 = new Music("blala/oeoer/m3.mp3", "plk", 140);
	DashBoard dashBoard = new DashBoard();
	dashBoard.addMusic(m1);
	dashBoard.addMusic(m2);
	dashBoard.addMusic(m3);
	System.out.println(dashBoard.toString());
	dashBoard.nextMusic();
	System.out.println(dashBoard.toString());
	dashBoard.nextMusic();
	System.out.println(dashBoard.toString());
	dashBoard.nextMusic();
	System.out.println(dashBoard.toString());
	dashBoard.precedentMusic();
	System.out.println(dashBoard.toString());
    }
}

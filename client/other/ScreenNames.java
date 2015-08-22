package client.other;

/**
 * The names of the screens in the UI, set as simple strings from 1 to 5 for simplicity.
 * @author Joaquin de la Sierra
 *
 */

public enum ScreenNames {
	
	LOGINSCREEN("1"),
	MAINMENUSCREEN("2"),
	CHESSBOARDSCREEN("3"),
	JOINGAMESCREEN("4"),
	MYGAMESINPROGRESSSCREN("5");

	private final String text;

    private ScreenNames(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
	
}
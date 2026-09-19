package hw1;

/**
 * Model of a Monopoly-like game. Two players take turns
 * rolling dice to move around a board. The game ends
 * when one of the players has at least MONEY_TO_WIN
 * money or one of the players goes bankrupt (they have
 * negative money).
 *
 * @author Simon Samuel
 */
public class CyGame {
	/**
	 * The endzone square type.
	 */
	public static final int ENDZONE = 0;
	/**
	 * The CyTown square type.
	 */
	public static final int CYTOWN = 1;
	/**
	 * The pay rent square type.
	 */
	public static final int PAY_RENT = 2;
	/**
	 * The fall behind square type.
	 */
	public static final int FALL_BEHIND = 3;
	/**
	 * The blizzard square type.
	 */
	public static final int BLIZZARD = 4;
	/**
	 * The pass class square type.
	 */
	public static final int PASS_CLASS = 5;
	/**
	 * Points awarded when landing on or passing over the endzone square.
	 */
	public static final int ENDZONE_PRIZE = 200;
	/**
	 * The standard rent payed to the other player when landing on a
	 * pay rent square.
	 */
	public static final int STANDARD_RENT_PAYMENT = 80;
	/**
	 * The cost to by CyTown.
	 */
	public static final int CYTOWN_COST = 200;
	/**
	 * The amount of money required to win.
	 */
	public static final int MONEY_TO_WIN = 400;

	private int player;
	private int playerOnePos;
	private int playerTwoPos;
	private final int PLAYER_ONE = 1;
	private final int PLAYER_TWO = 2;
	private int playerOneMoney;
	private int playerTwoMoney;
	private int boardSize;
	private boolean playerOneHasCyTown;
	private boolean playerTwoHasCyTown;

	/**
	 * Constructs a new game.
	 *
	 * @param numSquares number of squares on the board
	 * @param startingMoney starting amount of money for each player
	 */
	public CyGame(int numSquares, int startingMoney) {
		this.playerOnePos = 0;
		this.playerTwoPos = 0;
		this.boardSize = numSquares;
		this.playerOneMoney = startingMoney;
		this.playerTwoMoney = startingMoney;
		this.player = PLAYER_ONE;
		this.playerOneHasCyTown = false;
		this.playerTwoHasCyTown = false;
	}

	/**
	 * Returns a one-line string representation of the current game state.
	 *
	 * @return current game state as a string
	 */
	public String toString() {
		String fmt = "Player 1%s: (%d, %b, $%d) Player 2%s: (%d, %b, $%d)";
		String player1Turn = "";
		String player2Turn = "";
		if (getCurrentPlayer() == 1) {
			player1Turn = "*";
		} else {
			player2Turn = "*";
		}
		return String.format(fmt,
				player1Turn, getPlayerSquare(1), isPlayer1CyTownOwner(), getPlayerMoney(1),
				player2Turn, getPlayerSquare(2), isPlayer2CyTownOwner(), getPlayerMoney(2));
	}

	/**
	 * Attempts to buy CyTown for the current player.
	 */
	public void buyCyTown() {
		if (isGameEnded()) {
			return;
		}

		if (getSquareType(getPlayerSquare(player)) != CYTOWN) {
			return;
		}

		if (!playerOneHasCyTown && !playerTwoHasCyTown) {
			if (player == PLAYER_ONE && playerOneMoney >= CYTOWN_COST) {
				playerOneHasCyTown = true;
				playerOneMoney -= CYTOWN_COST;
			} else if (player == PLAYER_TWO && playerTwoMoney >= CYTOWN_COST) {
				playerTwoHasCyTown = true;
				playerTwoMoney -= CYTOWN_COST;
			}
		}

		endTurn();
	}

	/**
	 * Ends the current player's turn.
	 */
	public void endTurn() {
		if (player == PLAYER_ONE) {
			player = PLAYER_TWO;
		} else {
			player = PLAYER_ONE;
		}
	}

	/**
	 * Returns the current player.
	 *
	 * @return current player number
	 */
	public int getCurrentPlayer() {
		return player;
	}

	/**
	 * Returns the player who is not currently taking a turn.
	 *
	 * @return other player number
	 */
	public int getOtherPlayer() {
		if (player == PLAYER_ONE) {
			return PLAYER_TWO;
		}
		return PLAYER_ONE;
	}

	/**
	 * Returns the money of the given player.
	 *
	 * @param player the player number
	 * @return the player's money
	 */
	public int getPlayerMoney(int player) {
		if (player == PLAYER_ONE) {
			return playerOneMoney;
		}
		return playerTwoMoney;
	}

	/**
	 * Returns the square of the given player.
	 *
	 * @param player the player number
	 * @return the player's square position
	 */
	public int getPlayerSquare(int player) {
		if (player == PLAYER_ONE) {
			return playerOnePos;
		}
		return playerTwoPos;
	}

	/**
	 * Returns the square type for the given square.
	 *
	 * @param square the square index
	 * @return the square type constant
	 */
	public int getSquareType(int square) {
		if (square == 0) {
			return ENDZONE;
		} else if (square == boardSize - 1) {
			return CYTOWN;
		} else if (square % 5 == 0) {
			return PAY_RENT;
		} else if (square % 7 == 0 || square % 11 == 0) {
			return FALL_BEHIND;
		} else if (square % 3 == 0) {
			return BLIZZARD;
		} else {
			return PASS_CLASS;
		}
	}

	/**
	 * Returns whether the game has ended.
	 *
	 * @return true if the game is over, false otherwise
	 */
	public boolean isGameEnded() {
		return playerOneMoney >= MONEY_TO_WIN
				|| playerTwoMoney >= MONEY_TO_WIN
				|| playerOneMoney < 0
				|| playerTwoMoney < 0;
	}

	/**
	 * Returns whether player 1 owns CyTown.
	 *
	 * @return true if player 1 owns CyTown
	 */
	public boolean isPlayer1CyTownOwner() {
		return playerOneHasCyTown;
	}

	/**
	 * Returns whether player 2 owns CyTown.
	 *
	 * @return true if player 2 owns CyTown
	 */
	public boolean isPlayer2CyTownOwner() {
		return playerTwoHasCyTown;
	}

	/**
	 * Moves the current player based on the die roll and applies the
	 * effect of the square landed on.
	 *
	 * @param value die roll value
	 */
	public void roll(int value) {
		if (isGameEnded()) {
			return;
		}

		int currentPlayer = getCurrentPlayer();
		int otherPlayer = getOtherPlayer();
		int currentPos = getPlayerSquare(currentPlayer);
		int newPos = currentPos + value;
		int type;
		int rent;
		boolean gotEndzonePrize = false;

		if (getSquareType(currentPos) == BLIZZARD && value % 2 == 0) {
			endTurn();
			return;
		}

		if (newPos >= boardSize) {
			newPos %= boardSize;
			gotEndzonePrize = true;
			if (currentPlayer == PLAYER_ONE) {
				playerOneMoney += ENDZONE_PRIZE;
			} else {
				playerTwoMoney += ENDZONE_PRIZE;
			}
		}

		if (currentPlayer == PLAYER_ONE) {
			playerOnePos = newPos;
		} else {
			playerTwoPos = newPos;
		}

		type = getSquareType(newPos);

		if (type == ENDZONE) {
			if (!gotEndzonePrize) {
				if (currentPlayer == PLAYER_ONE) {
					playerOneMoney += ENDZONE_PRIZE;
				} else {
					playerTwoMoney += ENDZONE_PRIZE;
				}
			}
			endTurn();
			return;
		}

		if (type == CYTOWN) {
			return;
		}

		if (type == PAY_RENT) {
			rent = STANDARD_RENT_PAYMENT;
			if ((otherPlayer == PLAYER_ONE && playerOneHasCyTown)
					|| (otherPlayer == PLAYER_TWO && playerTwoHasCyTown)) {
				rent *= 2;
			}

			if (currentPlayer == PLAYER_ONE) {
				playerOneMoney -= rent;
				playerTwoMoney += rent;
			} else {
				playerTwoMoney -= rent;
				playerOneMoney += rent;
			}
			endTurn();
			return;
		}

		if (type == FALL_BEHIND) {
			newPos--;
			if (newPos < 0) {
				newPos = boardSize - 1;
			}

			if (currentPlayer == PLAYER_ONE) {
				playerOnePos = newPos;
			} else {
				playerTwoPos = newPos;
			}

			type = getSquareType(newPos);

			if (type == ENDZONE) {
				if (currentPlayer == PLAYER_ONE) {
					playerOneMoney += ENDZONE_PRIZE;
				} else {
					playerTwoMoney += ENDZONE_PRIZE;
				}
				endTurn();
				return;
			}

			if (type == CYTOWN) {
				return;
			}

			if (type == PAY_RENT) {
				rent = STANDARD_RENT_PAYMENT;
				if ((otherPlayer == PLAYER_ONE && playerOneHasCyTown)
						|| (otherPlayer == PLAYER_TWO && playerTwoHasCyTown)) {
					rent *= 2;
				}

				if (currentPlayer == PLAYER_ONE) {
					playerOneMoney -= rent;
					playerTwoMoney += rent;
				} else {
					playerTwoMoney -= rent;
					playerOneMoney += rent;
				}
			}

			endTurn();
			return;
		}

		if (type == BLIZZARD) {
			endTurn();
			return;
		}

		newPos += 4;
		if (newPos >= boardSize) {
			newPos %= boardSize;
			if (!gotEndzonePrize) {
				gotEndzonePrize = true;
				if (currentPlayer == PLAYER_ONE) {
					playerOneMoney += ENDZONE_PRIZE;
				} else {
					playerTwoMoney += ENDZONE_PRIZE;
				}
			}
		}

		if (currentPlayer == PLAYER_ONE) {
			playerOnePos = newPos;
		} else {
			playerTwoPos = newPos;
		}

		type = getSquareType(newPos);

		if (type == ENDZONE) {
			if (!gotEndzonePrize) {
				if (currentPlayer == PLAYER_ONE) {
					playerOneMoney += ENDZONE_PRIZE;
				} else {
					playerTwoMoney += ENDZONE_PRIZE;
				}
			}
			endTurn();
			return;
		}

		if (type == CYTOWN) {
			return;
		}

		if (type == PAY_RENT) {
			rent = STANDARD_RENT_PAYMENT;
			if ((otherPlayer == PLAYER_ONE && playerOneHasCyTown)
					|| (otherPlayer == PLAYER_TWO && playerTwoHasCyTown)) {
				rent *= 2;
			}

			if (currentPlayer == PLAYER_ONE) {
				playerOneMoney -= rent;
				playerTwoMoney += rent;
			} else {
				playerTwoMoney -= rent;
				playerOneMoney += rent;
			}
		}

		endTurn();
	}
}

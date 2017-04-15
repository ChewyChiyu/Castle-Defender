
public class Skelly extends Character {
	public Skelly(int x, int y){
		super("/imgs/Skelly.png",x,y,100,100,1,50,2,CharacterTypes.SKELLY);
		changeDirection(Direction.RIGHT);
		changeXVelocity(1);
		toggleAI();
	}

}

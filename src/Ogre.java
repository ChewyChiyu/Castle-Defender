
public class Ogre extends Character {
	public Ogre(int x, int y){
		super("/imgs/Ogre.png",x,y,100,100,1,50,2,CharacterTypes.OGRE);
		changeDirection(Direction.RIGHT);
		changeXVelocity(1);
		toggleAI();
	}

}

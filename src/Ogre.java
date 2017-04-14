
public class Ogre extends Character {
	public Ogre(int x, int y, Direction d){
		super("/imgs/Ogre.png",x,y,100,100,1,100,2);
		changeDirection(d);
		changeXVelocity(1);
		toggleAI();
	}

}

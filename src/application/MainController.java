package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class MainController {
	@FXML private Button btnStart;     	//button to start and restart the game
	@FXML private Button btnThrust;		//The thrust button
	@FXML private TextField tfName;		//TestField for user's name
	@FXML private TextField tfVelocity; //TextField to record the velocity of the lander
	@FXML private TextField tfHeight;	//TextField to record the height of the lander
	@FXML private ProgressBar pbFuel;	//ProgreeBar to show the remaining fuel
	@FXML private ProgressBar pbHeight; //ProgressBar to show the remaining height

	private PhysicsEngine physicsEngine = new PhysicsEngine();   // object of PhysicsEngine class

	@FXML public void initialize(){

		btnStart.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				onStartClicked();  //calls the method when start button is clicked
			}
		});

		Timeline time = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				onTimer(); // calls the onTimer method every 0.1 second
			}
		}));

		time.setCycleCount(Timeline.INDEFINITE);
		time.play();

	}

	public void onStartClicked(){
		physicsEngine.start(); // calls the start method from PhysicsEngine class
	}

	public void onTimer(){
		// field variable to check if nextStep method returns true
		boolean result = false;

		//condition to check if thrust button is pressed and then passes true to nextStep method
		if(btnThrust.isPressed()){
			result = physicsEngine.nextStep(true);
			tfVelocity.setText(physicsEngine.getVelocity()+"");   //setting velocity value by calling getVelocity method of PhysicsEngine class
			tfHeight.setText(physicsEngine.getHeight()+"");		 //setting height value by calling getHeight method of PhysicsEngine class

			double height = physicsEngine.getHeight();			//assigning height value from PhysicsEngine class to height variable
			pbHeight.setProgress(height/500.0);					//changing value of height progressbar

			double fuel = physicsEngine.getFuel();				//assigning fuel value from PhysicsEngine class to fuel variable
			pbFuel.setProgress(fuel/100.0);						//changing fuel progressbar

			//this block runs when the thrust button is not pressed
		}else{
			result = physicsEngine.nextStep(false);
			tfVelocity.setText(physicsEngine.getVelocity()+""); //setting velocity value by calling getVelocity method of PhysicsEngine class
			tfHeight.setText(physicsEngine.getHeight()+"");		//setting height value by calling getHeight method of PhysicsEngine class

			double height = physicsEngine.getHeight();			//assigning height value from PhysicsEngine class to height variable
			pbHeight.setProgress(height/500.0);					//changing value of height progressbar

		}
		if(result)  		 //checks if result is true
			decide();		//calls decide method
	}

	//this method decides whether the lander landed successfully or not
	public void decide(){

		//if the downward velocity of the lander is equal or greater than safe landing speed, the lander wins and alert box will popup
		if(PhysicsEngine.SAFE_LANDING_SPEED <= physicsEngine.getVelocity()){
			Alert alert = new Alert(AlertType.INFORMATION,"Congratulations "+tfName.getText()+ ", you landed successfully.");
			 alert.show();
		}

		//else the lander loses the game
		else{
			 Alert alert = new Alert(AlertType.INFORMATION, "Sorry "+tfName.getText()+ ", the game crashed.");
			 alert.show();
		}

	}
}

package rose.orphnoch.mhw.futuremanager

import scalafx.scene.Scene
import scalafx.application.JFXApp
import scalafx.scene.control.{Button, Label, TextField}
import scalafxml.core.{FXMLView, NoDependencyResolver}

object FutureManager extends JFXApp {
  val resource = getClass.getResource("main.fxml")
  val root = FXMLView(resource,NoDependencyResolver)
  stage = new scalafx.application.JFXApp.PrimaryStage(){
    title = "MHW 未来予知マネージャー"
    scene = new Scene(new javafx.scene.Scene(root))
  }

}
class FMController(
                  omoteFirst: TextField,
                  omoteSecond: TextField,
                  omoteThird: TextField,
                  uraFirst: TextField,
                  uraSecond: TextField,
                  uraThird: TextField,
                  registerButton: Button,
                  searchButton: Button,
                  searchTextField: TextField,
                  questCounter: Label,
                  questCountUp: Button,
                  questCountDown: Button
                  ){


}



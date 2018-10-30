package rose.orphnoch.mhw.futuremanager

import java.io.{File, FileReader, FileWriter}

import au.com.bytecode.opencsv.{CSVReader, CSVWriter}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.StringProperty
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, ScrollPane, TextField}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{HBox, VBox}
import scalafxml.core.macros.sfxml
import scalafxml.core.{FXMLView, NoDependencyResolver}

import scala.language.implicitConversions

object FutureManager extends JFXApp {
  println(getClass.getResource("/main.fxml"))
  private val resource = getClass.getResource("/main.fxml")
  private val root = FXMLView(resource,NoDependencyResolver)
  stage = new scalafx.application.JFXApp.PrimaryStage(){
    title = "MHW 未来予知マネージャー"
    scene = new Scene(new javafx.scene.Scene(root))
  }
}
@sfxml
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
                  questCountDown: Button,
                  questCountReset: Button,
                  exitButton: Button,
                  omoteSearchResult: ScrollPane,
                  uraSearchResult: ScrollPane
                  ){
  registerButton.onMouseClicked = (event: MouseEvent) => {
    val omote1: String = omoteFirst.text
    val omote2: String = omoteSecond.text
    val omote3: String = omoteThird.text
    val ura1 = uraFirst.text
    val ura2 = uraSecond.text
    val ura3 = uraThird.text
    val lines = OmoteCSV.readAll()
      val index = if(lines.nonEmpty)lines.last.apply(0).toInt + 1 else 1
      if(lines.isEmpty){
        OmoteCSV.writeNext(Array[String]("id","装飾品1","装飾品2","装飾品3"))
        UraCSV.writeNext(Array[String]("id","装飾品1","装飾品2","装飾品3"))
      }
       if(omote1!="-"||omote2!="-"||omote3!="-"){
        OmoteCSV.writeNext(Array[String](index.toString, omote1, omote2, omote3))
        UraCSV.writeNext(Array[String](index.toString, ura1, ura2, ura3))
        omoteFirst.setText("")
        omoteSecond.setText("")
        omoteThird.setText("")
        uraFirst.setText("")
        uraSecond.setText("")
        uraThird.setText("")
      }
  }
  searchButton.onMouseClicked = (event: MouseEvent) => {
    Option(searchTextField.text.value) match {
      case Some(s) =>
        val omoteData = OmoteCSV.readAll()
        val uraData = UraCSV.readAll()
        val omoteResult = recursion(s,omoteData,Nil)
        val uraResult = recursion(s,uraData,Nil)
        omoteSearchResult.content = new HBox {
          children = omoteResult map {
            line =>
            val label = new Label()
            label.setText(line.mkString(" "))
            label
          }
        }
        uraSearchResult.content = new VBox{
          children = uraResult map {
            line =>
              val label = new Label()
              label.setText(line.mkString(" "))
              label
          }
        }
        searchTextField.setPromptText("ここに入力して検索")
      case None =>
        searchTextField.setPromptText("入力してください")
    }
    def recursion(word: String,csvData: Seq[Array[String]],list: Seq[Array[String]]): Seq[Array[String]] = {
      if(csvData.isEmpty) return Nil
      if(csvData.head.contains(word)) return recursion(word,csvData.tail,list:+csvData.head)
      recursion(word,csvData.tail,list)
    }
  }
  questCountUp.onMouseClicked = (event: MouseEvent) => {
      val current = questCounter.text.value.toInt
      questCounter.setText((current + 1).toString)
  }
  questCountDown.onMouseClicked = (event: MouseEvent) => {
      val current = questCounter.text.value.toInt
      if(current >= 1)questCounter.setText((current - 1).toString)
  }
  questCountReset.onMouseClicked = (event: MouseEvent) => questCounter.setText("0")
  exitButton.onMouseClicked = (event: MouseEvent) => {
    FutureManager.stage.close()
  }
  private implicit def convertTextFieldProperty(sp: StringProperty): String = Option(sp.value) map {
    case "" => "-"
    case s@_ => s
  } getOrElse "-"
}
trait CSVFile {
  def writeNext(line: Array[String]): Unit
  def readAll(): Seq[Array[String]]
}
object OmoteCSV extends CSVFile {
  val file = new File("omote.csv")
  println(file.exists())
  override def writeNext(line: Array[String]): Unit = {
    val writer = new CSVWriter(new FileWriter(file,true))
    writer.writeNext(line)
    writer.flush()
    writer.close()
  }
  override def readAll(): Seq[Array[String]] = {
    val reader = new CSVReader(new FileReader(file))
    val ls = Iterator.continually(reader.readNext()).takeWhile(_ != null).toList
    reader.close()
    ls
  }
}
object UraCSV extends CSVFile {
  val file = new File("ura.csv")
  override def writeNext(line: Array[String]): Unit = {
    val writer = new CSVWriter(new FileWriter(file,true))
    writer.writeNext(line)
    writer.flush()
    writer.close()
  }
  override def readAll(): Seq[Array[String]] =  {
    val reader = new CSVReader(new FileReader(file))
    val ls = Iterator.continually(reader.readNext()).takeWhile(_ != null).toList
    reader.close()
    ls
  }
}
























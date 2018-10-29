package rose.orphnoch.mhw.futuremanager

import java.io.{File, FileReader, FileWriter}

import au.com.bytecode.opencsv.{CSVReader, CSVWriter}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.StringProperty
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, ScrollPane, TextField}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.HBox
import scalafxml.core.macros.sfxml
import scalafxml.core.{FXMLView, NoDependencyResolver}

import scala.language.implicitConversions
import scala.collection.JavaConverters._

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
    val omote1 = omoteFirst.text.getOrElse("")
    val omote2 = omoteSecond.text.getOrElse("")
    val omote3 = omoteThird.text.getOrElse("")
    val ura1 = uraFirst.text.getOrElse("")
    val ura2 = uraSecond.text.getOrElse("")
    val ura3 = uraThird.text.getOrElse("")
    val lines = OmoteCSV.readAll()
    if(lines.nonEmpty) {
      val index = lines.last.apply(0).toInt + 1
      OmoteCSV.writeNext(Array[String](index.toString, omote1, omote2, omote3))
      UraCSV.writeNext(Array[String](index.toString, ura1, ura2, ura3))
    }else{
      OmoteCSV.writeNext(Array[String]("0","","",""))
      UraCSV.writeNext(Array[String]("0","","",""))
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
        uraSearchResult.content = new HBox{
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
      questCounter.setText((current - 1).toString)
  }
  questCountReset.onMouseClicked = (event: MouseEvent) => questCounter.setText("0")
  exitButton.onMouseClicked = (event: MouseEvent) => {
    OmoteCSV.close()
    UraCSV.close()
  }
  private implicit def wrapTextFieldProperty(sp: StringProperty): Option[String] = Option(sp.value)
}
trait CSVFile {
  def writeNext(line: Array[String]): Unit
  def readAll(): Seq[Array[String]]
  def close(): Unit
}
object OmoteCSV extends CSVFile {
  val file = new File("../omote.csv")
  val writer = new CSVWriter(new FileWriter(file))
  val reader = new CSVReader(new FileReader(file))
  override def writeNext(line: Array[String]): Unit = {
    this.writer.writeNext(line)
    this.writer.flush()
  }
  override def readAll(): Seq[Array[String]] =  this.reader.readAll().asScala.toList
  override def close(): Unit = {
    this.writer.close()
    this.reader.close()
  }
}
object UraCSV extends CSVFile {
  val file = new File("../ura.csv")
  val writer = new CSVWriter(new FileWriter(file))
  val reader = new CSVReader(new FileReader(file))
  override def writeNext(line: Array[String]): Unit = {
    this.writer.writeNext(line)
    this.writer.flush()
  }
  override def readAll(): Seq[Array[String]] =  this.reader.readAll().asScala.toList
  override def close(): Unit = {
    this.writer.close()
    this.reader.close()
  }
}























